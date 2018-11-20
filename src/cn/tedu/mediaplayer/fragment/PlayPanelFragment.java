package cn.tedu.mediaplayer.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.activity.MainActivity;
import cn.tedu.mediaplayer.adapter.CommonPagerAdapter;
import cn.tedu.mediaplayer.aidl.MusicInterface;
import cn.tedu.mediaplayer.app.DaDaApp;
import cn.tedu.mediaplayer.entity.Music;
import cn.tedu.mediaplayer.entity.SongInfo;
import cn.tedu.mediaplayer.entity.SongUrl;
import cn.tedu.mediaplayer.listener.OnDownloadPressListener;
import cn.tedu.mediaplayer.model.MusicModel;
import cn.tedu.mediaplayer.model.SongInfoCallback;
import cn.tedu.mediaplayer.service.DownloadService;
import cn.tedu.mediaplayer.ui.DownloadPopWindow;
import cn.tedu.mediaplayer.util.DateUtil;
import cn.tedu.mediaplayer.util.GlobalConsts;
/** �������fragment */
public class PlayPanelFragment extends Fragment {
	private ViewPager viewPager;
	private List<Fragment> fragments;
	private FragmentPagerAdapter adapter;
	private SeekBar seekbar;
	private ImageView ivPlay;
	private ImageView ivPreious;
	private ImageView ivNext;
	private ImageView ivDownload;
	private ImageView ivLike, ivShare, ivComment, ivMore;
	private ImageView ivLoopMode;
	private TextView tvCurrentTime, tvTotaltime;
	private UpdateProgressReceiver receiver;
	private MusicInterface musicInterface;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Music music=DaDaApp.getApp().getCurrentMusic();
		if(music==null){ //û����Ҫ���ŵĸ���
			View view = inflater.inflate(R.layout.fragment_play_panel_none, null);
			return view;
		}else{
			View view = inflater.inflate(R.layout.fragment_play_panel, null);
			view.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					return true;
				}
			});
			MainActivity act = (MainActivity)getActivity();
			musicInterface = act.getMusicInterface();
			//�ؼ���ʼ��
			setViews(view);
			//ע��㲥������
			registReceivers();
			//��Ӽ���
			setListener();
			//����viewpager������
			setPagerAdapter();
			return view;
		}
	}
	
	/** ����viewpager������ */
	private void setPagerAdapter() {
		fragments = new ArrayList<Fragment>();
		fragments.add(new PlayPanelCoverFragment());
		fragments.add(new PlayPanelLyricsFragment());
		adapter = new CommonPagerAdapter(getChildFragmentManager(), fragments);
		viewPager.setAdapter(adapter);
	}

	/*** ��Ӽ��� */
	private void setListener() {
		
		//���Ʋ���ģʽ
		ivLoopMode.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int loopMode=DaDaApp.getApp().nextLoopMode();
				switch (loopMode) {
				case DaDaApp.LOOP_MODE_ALL_LOOP:
					ivLoopMode.setImageResource(R.drawable.bt_playpage_loop_normal_new);
					break;
				case DaDaApp.LOOP_MODE_RANDOM_LOOP:
					ivLoopMode.setImageResource(R.drawable.bt_playpage_random_normal_new);
					break;
				case DaDaApp.LOOP_MODE_SINGLE_LOOP:
					ivLoopMode.setImageResource(R.drawable.bt_playpage_roundsingle_normal_new);
					break;
				}
			}
		});
		
		//�ж�ivDownloadͼƬ�� ���� ����  �������
		Music music=DaDaApp.getApp().getCurrentMusic();
		if(music.getFilepath()!=null){//�ļ��Ѵ���
			ivDownload.setImageResource(R.drawable.bt_playpage_button_download_activited_new);
			ivDownload.setOnClickListener(null);
		}else{
			ivDownload.setImageResource(R.drawable.bt_playpage_button_download_normal_new);
			//�ļ�������  ������������ؼ���
			setDownloadOnClickListener();
		}

		//��viewpager��Ӽ���
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int position) {
			}
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				PlayPanelCoverFragment f1 = (PlayPanelCoverFragment) fragments.get(0);
				f1.slide(1-positionOffset);
				PlayPanelLyricsFragment f2 = (PlayPanelLyricsFragment) fragments.get(1);
				f2.slide(positionOffset);  
			}
			public void onPageScrollStateChanged(int state) {
			}
		});
		
		//��ivPre�Ӽ���
		ivPreious.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//��position-1
				final Music music=DaDaApp.getApp().getPreviousMusic();
				if(music.getFilepath()!=null){//�Ǳ�������
					try {
						musicInterface.playMusic(music.getFilepath());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					return;
				}
				if(music.getSongUrls()!=null){ //��ǰ�Ѿ����ع�
					try {
						String filelink = music.getSongUrls().get(0).getFile_link();
						musicInterface.playMusic(filelink);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					return;
				}
				MusicModel model = new MusicModel();
				model.loadSongInfoBySongId(music.getSong_id(), new SongInfoCallback() {
					public void onSongInfoLoaded(List<SongUrl> urls, SongInfo info) {
						try {
							music.setSongUrls(urls);
							music.setSongInfo(info);
							String filelink = urls.get(0).getFile_link();
							musicInterface.playMusic(filelink);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		//��ivNext�Ӽ���
		ivNext.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//��position+1
				final Music music=DaDaApp.getApp().getNextMusic();
				if(music.getFilepath()!=null){//�Ǳ�������
					try {
						musicInterface.playMusic(music.getFilepath());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					return;
				}
				if(music.getSongUrls()!=null){ //��ǰ�Ѿ����ع�
					try {
						String filelink = music.getSongUrls().get(0).getFile_link();
						musicInterface.playMusic(filelink);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					return;
				}
				MusicModel model = new MusicModel();
				model.loadSongInfoBySongId(music.getSong_id(), new SongInfoCallback() {
					public void onSongInfoLoaded(List<SongUrl> urls, SongInfo info) {
						try {
							music.setSongUrls(urls);
							music.setSongInfo(info);
							String filelink = urls.get(0).getFile_link();
							musicInterface.playMusic(filelink);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		//��ivPlay��Ӽ���
		ivPlay.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					int state=musicInterface.playOrPause();
					if(state == GlobalConsts.STATE_PLAYING){
						ivPlay.setImageResource(R.drawable.bt_playpage_button_pause_normal_new);
					}else{
						ivPlay.setImageResource(R.drawable.bt_playpage_button_play_normal_new);
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		
		//
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(fromUser){
					try {
						musicInterface.seekTo(progress);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void setDownloadOnClickListener() {
		//�������
		ivDownload.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				List<SongUrl> copyUrls = new ArrayList<SongUrl>();
				final Music music=DaDaApp.getApp().getCurrentMusic();
				List<SongUrl> urls = music.getSongUrls();
				copyUrls.addAll(urls);
				//ִ���������
				Collections.sort(copyUrls, new Comparator<SongUrl>() {
					public int compare(SongUrl lhs, SongUrl rhs) {
						return -lhs.getFile_size()+rhs.getFile_size();
					}
				});
				//������Ҫ���ݸ�popwindow��url����
				List<SongUrl> songUrls = new ArrayList<SongUrl>();
				for(int i=0; i<copyUrls.size(); i++){
					songUrls.add(copyUrls.get(i));
				}
				DownloadPopWindow popWindow = new DownloadPopWindow(getActivity(), songUrls, new OnDownloadPressListener() {
					public void onDownloadPressed(SongUrl url) {
						//��ȡ�û�ѡ�е�url����
						String filelink = url.getFile_link();
						//ִ������  ����Service
						Log.i("info", ""+filelink);
						Intent intent = new Intent(getActivity(), DownloadService.class);
						intent.putExtra("filelink", filelink);
						intent.putExtra("title", music.getTitle());
						intent.putExtra("total", url.getFile_size());
						intent.putExtra("music", music);
						getActivity().startService(intent);
					}
				});
				popWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			}
		});
				
	}

	/** ע��㲥������ */
	private void registReceivers() {
		receiver = new UpdateProgressReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalConsts.ACTION_UPDATE_MUSIC_PROGRESS);
		getActivity().registerReceiver(receiver, filter);
	}

	@Override
	public void onDestroyView() {
		if(receiver!=null){
			getActivity().unregisterReceiver(receiver);
		}
		super.onDestroyView();
	}

	/** �ؼ���ʼ�� */
	private void setViews(View view) {
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		seekbar = (SeekBar) view.findViewById(R.id.seekBar);
		tvCurrentTime = (TextView) view.findViewById(R.id.tvCurrentTime);
		tvTotaltime = (TextView) view.findViewById(R.id.tvTotaltime);
		ivPlay = (ImageView) view.findViewById(R.id.ivPlay);
		ivPreious = (ImageView) view.findViewById(R.id.ivPrevious);
		ivNext = (ImageView) view.findViewById(R.id.ivNext);
		ivDownload = (ImageView) view.findViewById(R.id.ivDownload);
		ivLike = (ImageView) view.findViewById(R.id.ivLike);
		ivShare = (ImageView) view.findViewById(R.id.ivShare);
		ivComment = (ImageView) view.findViewById(R.id.ivComment);
		ivMore = (ImageView) view.findViewById(R.id.ivMoreactions);
		ivLoopMode = (ImageView) view.findViewById(R.id.ivLoop);
		
		//�ж�ivPlayӦ����ʾ��ͣ  ���� ���� ͼƬ��
		try {
			int state = musicInterface.getPlayState();
			if(state == GlobalConsts.STATE_PLAYING){
				ivPlay.setImageResource(R.drawable.bt_playpage_button_pause_normal_new);
			}else{
				ivPlay.setImageResource(R.drawable.bt_playpage_button_play_normal_new);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/** �������ֽ��� */
	class UpdateProgressReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent) {
			int current = intent.getIntExtra("current", 0);
			int total = intent.getIntExtra("total", 1);
			//����view
			seekbar.setMax(total);
			seekbar.setProgress(current);
			tvCurrentTime.setText(DateUtil.parseTime(current));
			tvTotaltime.setText(DateUtil.parseTime(total));
		}
	}
	
}





