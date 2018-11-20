package cn.tedu.mediaplayer.fragment;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.activity.MainActivity;
import cn.tedu.mediaplayer.adapter.BangdanDetailAdapter;
import cn.tedu.mediaplayer.aidl.MusicInterface;
import cn.tedu.mediaplayer.app.DaDaApp;
import cn.tedu.mediaplayer.entity.BangdanDetail;
import cn.tedu.mediaplayer.entity.Music;
import cn.tedu.mediaplayer.entity.SongInfo;
import cn.tedu.mediaplayer.entity.SongUrl;
import cn.tedu.mediaplayer.model.BangdanDetailCallback;
import cn.tedu.mediaplayer.model.BitmapCallback;
import cn.tedu.mediaplayer.model.MusicModel;
import cn.tedu.mediaplayer.model.SongInfoCallback;
import cn.tedu.mediaplayer.ui.PullbackListView;
import cn.tedu.mediaplayer.util.BitmapUtils;

public class BangdanDetailFragment extends Fragment{
	private PullbackListView listView;
	private BangdanDetail bangdan;
	private BangdanDetailAdapter adapter;
	private ImageView ivBackground;
	private RelativeLayout relativeLayoutActionbar;
	private ImageView ivActionbarBackground;
	private ImageView ivBack;
	private MusicModel model = new MusicModel();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_music_bangdan_detail, null);
		//�ؼ���ʼ��
		setViews(view);
		setListeners();
		//��ȡ���ݹ����İ�type����
		int type = getArguments().getInt("type");
		//�����������͵İ񵥼�������
		MusicModel model = new MusicModel();
		model.loadBangdanDetail(type, new BangdanDetailCallback() {
			public void onBangdanDataLoaded(BangdanDetail bangdan) {
				BangdanDetailFragment.this.bangdan = bangdan;
				//����listView
				updateListView();
			}
		});
		return view; 
	}

	/**����*/
	private void setListeners() {
		ivBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getFragmentManager().popBackStackImmediate();
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//�����������   ��Ҫ�ѵ�ǰ�����б���position
				//������application��  �Ժ���Ҫ��ȡ��ǰ���ŵ�
				//������Ϣʱ��ֱ��ȥapplication��Ѱ�Ҽ��ɡ�
				DaDaApp.getApp().setMusicList(bangdan.getMusics());
				DaDaApp.getApp().setPosition(position-1);
				
				final Music music=bangdan.getMusics().get(position-1);
				String songId = music.getSong_id();
				model.loadSongInfoBySongId(songId, new SongInfoCallback() {
					public void onSongInfoLoaded(List<SongUrl> urls, SongInfo info) {
						try {
							//��urls��info���󶼴���music��  �Ժ�Ҫ��
							music.setSongUrls(urls);
							music.setSongInfo(info);
							//��������
							String filelink=urls.get(0).getFile_link();
							//��MainActivity��ֱ�ӻ�ȡmusicInterface
							MainActivity act = (MainActivity)getActivity();
							MusicInterface musicInterface = act.getMusicInterface();
							musicInterface.playMusic(filelink);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}

	/** �����б�  �Զ���adapter */
	public void updateListView() {
		adapter = new BangdanDetailAdapter(getActivity(), bangdan.getMusics());
		//���ö�����ImageView
		View headerView = View.inflate(getActivity(), R.layout.view_bangdan_detail_list_header, null);
		listView.addHeaderView(headerView, null, false);
		listView.setHeaderView(headerView);
		listView.setHeaderActionbarImageView(ivActionbarBackground);
		listView.setAdapter(adapter);
		//����headerView�еĿؼ�����
		updateHeaderView(headerView);
	}

	//����headerView�еĿؼ�����
	private void updateHeaderView(final View headerView) {
		TextView tvUpdateDate = (TextView) headerView.findViewById(R.id.tvUpdateDate);
		tvUpdateDate.setText("�������ڣ�"+bangdan.getBillboard().getUpdate_date().replace("-", "."));
		final ImageView imageView=(ImageView) headerView.findViewById(R.id.ivHeaderPic);
		//��Ҫͨ��url·��ȥ���������һ��ͼƬ
		String url = bangdan.getBillboard().getPic_s640();
		BitmapUtils.loadBitmap(url, new BitmapCallback() {
			public void onBitmapLoaded(final Bitmap bitmap) {
				//����ImageView��ʾͼƬ�ķ�ʽ  Matrix
				final Matrix matrix = new Matrix();
				// ͼƬ���������� = ��Ļ���/ͼƬ���
				DisplayMetrics metric = new DisplayMetrics();  
				getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);  
				final float scale = 1.0f * metric.widthPixels / bitmap.getWidth();
				Log.i("info", "scale:"+scale);
				matrix.setScale(scale, scale, 0, 0);
				imageView.setImageMatrix(matrix);
				imageView.setImageBitmap(bitmap);
				//��ͼƬҲ��ʾ������ImageView��
				ivBackground.setImageMatrix(matrix);
				ivBackground.setImageBitmap(bitmap);
				//��ͼƬ��ʾ��actionBar��
				//ͼƬ�ü�
				ViewTreeObserver vto2 = headerView.getViewTreeObserver();    
			    vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
		            public void onGlobalLayout() {  
		            	headerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);    
		            	Bitmap bm = Bitmap.createBitmap(
								bitmap, 
								0, 
								(int)((headerView.getHeight()-relativeLayoutActionbar.getHeight())/scale), 
								bitmap.getWidth(), 
								(int)(relativeLayoutActionbar.getHeight()/scale));
						ivActionbarBackground.setImageMatrix(matrix);
						ivActionbarBackground.setImageBitmap(bm);
		            }
		        });
			}
		});
	}

	/*** �ؼ���ʼ�� */
	private void setViews(View view) {
		listView = (PullbackListView) view.findViewById(R.id.listView);
		ivBackground = (ImageView) view.findViewById(R.id.ivBackground);
		relativeLayoutActionbar = (RelativeLayout) view.findViewById(R.id.relativeLayoutActionbar);
		ivActionbarBackground = (ImageView) view.findViewById(R.id.ivActionbarBackground);
		ivBack = (ImageView) view.findViewById(R.id.ivBack);
	}
}









