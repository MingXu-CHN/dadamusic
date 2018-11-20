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
		//控件初始化
		setViews(view);
		setListeners();
		//获取传递过来的榜单type参数
		int type = getArguments().getInt("type");
		//加载这种类型的榜单集合数据
		MusicModel model = new MusicModel();
		model.loadBangdanDetail(type, new BangdanDetailCallback() {
			public void onBangdanDataLoaded(BangdanDetail bangdan) {
				BangdanDetailFragment.this.bangdan = bangdan;
				//更新listView
				updateListView();
			}
		});
		return view; 
	}

	/**监听*/
	private void setListeners() {
		ivBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getFragmentManager().popBackStackImmediate();
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//点击这首音乐   需要把当前播放列表与position
				//都存入application中  以后需要获取当前播放的
				//音乐信息时，直接去application中寻找即可。
				DaDaApp.getApp().setMusicList(bangdan.getMusics());
				DaDaApp.getApp().setPosition(position-1);
				
				final Music music=bangdan.getMusics().get(position-1);
				String songId = music.getSong_id();
				model.loadSongInfoBySongId(songId, new SongInfoCallback() {
					public void onSongInfoLoaded(List<SongUrl> urls, SongInfo info) {
						try {
							//把urls与info对象都存入music中  以后要用
							music.setSongUrls(urls);
							music.setSongInfo(info);
							//播放音乐
							String filelink=urls.get(0).getFile_link();
							//从MainActivity中直接获取musicInterface
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

	/** 更新列表  自定义adapter */
	public void updateListView() {
		adapter = new BangdanDetailAdapter(getActivity(), bangdan.getMusics());
		//设置顶部的ImageView
		View headerView = View.inflate(getActivity(), R.layout.view_bangdan_detail_list_header, null);
		listView.addHeaderView(headerView, null, false);
		listView.setHeaderView(headerView);
		listView.setHeaderActionbarImageView(ivActionbarBackground);
		listView.setAdapter(adapter);
		//更新headerView中的控件内容
		updateHeaderView(headerView);
	}

	//更新headerView中的控件内容
	private void updateHeaderView(final View headerView) {
		TextView tvUpdateDate = (TextView) headerView.findViewById(R.id.tvUpdateDate);
		tvUpdateDate.setText("更新日期："+bangdan.getBillboard().getUpdate_date().replace("-", "."));
		final ImageView imageView=(ImageView) headerView.findViewById(R.id.ivHeaderPic);
		//需要通过url路径去服务端下载一张图片
		String url = bangdan.getBillboard().getPic_s640();
		BitmapUtils.loadBitmap(url, new BitmapCallback() {
			public void onBitmapLoaded(final Bitmap bitmap) {
				//更改ImageView显示图片的方式  Matrix
				final Matrix matrix = new Matrix();
				// 图片的伸缩比例 = 屏幕宽度/图片宽度
				DisplayMetrics metric = new DisplayMetrics();  
				getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);  
				final float scale = 1.0f * metric.widthPixels / bitmap.getWidth();
				Log.i("info", "scale:"+scale);
				matrix.setScale(scale, scale, 0, 0);
				imageView.setImageMatrix(matrix);
				imageView.setImageBitmap(bitmap);
				//把图片也显示到背景ImageView中
				ivBackground.setImageMatrix(matrix);
				ivBackground.setImageBitmap(bitmap);
				//把图片显示到actionBar中
				//图片裁剪
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

	/*** 控件初始化 */
	private void setViews(View view) {
		listView = (PullbackListView) view.findViewById(R.id.listView);
		ivBackground = (ImageView) view.findViewById(R.id.ivBackground);
		relativeLayoutActionbar = (RelativeLayout) view.findViewById(R.id.relativeLayoutActionbar);
		ivActionbarBackground = (ImageView) view.findViewById(R.id.ivActionbarBackground);
		ivBack = (ImageView) view.findViewById(R.id.ivBack);
	}
}









