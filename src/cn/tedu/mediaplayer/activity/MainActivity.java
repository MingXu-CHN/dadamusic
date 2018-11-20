package cn.tedu.mediaplayer.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.aidl.MusicInterface;
import cn.tedu.mediaplayer.aidl.MusicInterface.Stub;
import cn.tedu.mediaplayer.app.DaDaApp;
import cn.tedu.mediaplayer.entity.Music;
import cn.tedu.mediaplayer.entity.SongInfo;
import cn.tedu.mediaplayer.entity.SongUrl;
import cn.tedu.mediaplayer.fragment.MusicFragment;
import cn.tedu.mediaplayer.fragment.MyFragment;
import cn.tedu.mediaplayer.fragment.PlayPanelFragment;
import cn.tedu.mediaplayer.fragment.SearchFragment;
import cn.tedu.mediaplayer.fragment.TestFragment;
import cn.tedu.mediaplayer.model.BitmapCallback;
import cn.tedu.mediaplayer.model.MusicModel;
import cn.tedu.mediaplayer.model.SongInfoCallback;
import cn.tedu.mediaplayer.service.PlayMusicService;
import cn.tedu.mediaplayer.util.BitmapUtils;
import cn.tedu.mediaplayer.util.GlobalConsts;
import cn.tedu.mediaplayer.util.URLFactory;

public class MainActivity extends FragmentActivity {
	private ImageButton btnSearch;
	private RadioGroup radioGroup;
	private RadioButton rbMine;
	private RadioButton rbMusic;
	private RadioButton rbDongtai;
	private RadioButton rbFind;
	private FrameLayout frameLayoutContainer;
	private FrameLayout frameLayoutBangdanDetail;
	private FrameLayout frameLayoutSearch;
	
	private Fragment mineFragment;
	private Fragment musicFragment;
	private Fragment dongtaiFragment;
	private Fragment findFragment;
	private ServiceConnection conn;
	private MusicInterface musicInterface;
	
	private LinearLayout linearLayoutBottombar;
	private ImageView ivBBPic;
	private TextView tvBBTitle;
	private TextView tvBBSinger;
	private ImageView ivBBPlay;
	private UpdateBottomBarReceiver receiver;
	protected String APK_URL;
	private BroadcastReceiver r2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//�ؼ���ʼ��
		setViews();
		//�������
		setListeners();
		//��Service
		bindService();
		//ע��㲥������
		registReceivers();
		//��ʼ��Fragment
		initFraments();
		//������
		checkUpdate();
	}
	
	// ������
	private void checkUpdate() {
		String url = URLFactory.getCheckUpdateUrl();
		StringRequest request = new StringRequest(url, new Listener<String>() {
			public void onResponse(String response) {
				try {
					JSONObject obj = new JSONObject(response);
					int code=obj.getInt("VERSION_CODE");
					APK_URL = obj.getString("APK_URL");
					if(code > DaDaApp.getApp().getCurrentVersionCode()){ //�ø��°汾��
						showDownloadApkDialog();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			public void onErrorResponse(VolleyError error) {
			}
		});
		DaDaApp.getQueue().add(request);
	}

	/** ��ʾ����apk�Ի��� */
	private void showDownloadApkDialog(){
		Builder builder = new Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		builder.setTitle("����")
			.setMessage("�����°汾���Ƿ���£�")
			.setPositiveButton("��������", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					downloadApk();
				}
			}).setNegativeButton("�Ժ���˵", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	/** ����apk */
	private void downloadApk(){
		DownloadManager dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
		Uri uri = Uri.parse(APK_URL);
		DownloadManager.Request request = new Request(uri);
		// ��������·�����ļ���
		request.setDestinationInExternalPublicDir("download", "��������.apk");
		request.setDescription("������������");
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setMimeType("application/vnd.android.package-archive");
		// ����Ϊ�ɱ�ý��ɨ�����ҵ�
		request.allowScanningByMediaScanner();
		// ����Ϊ�ɼ��Ϳɹ���
		request.setVisibleInDownloadsUi(true);
		long refernece = dManager.enqueue(request);
		// �ѵ�ǰ���ص�ID��������
		SharedPreferences sPreferences = getSharedPreferences("downloadcomplete", 0);
		sPreferences.edit().putLong("refernece", refernece).commit();
	}

	
	/** ע��㲥������ */
	private void registReceivers() {
		receiver = new UpdateBottomBarReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalConsts.ACTION_MUSIC_STARTED);
		filter.addAction(GlobalConsts.ACTION_MUSIC_COMPLETED);
		filter.addAction(GlobalConsts.ACTION_MUSIC_PAUSED);
		this.registerReceiver(receiver, filter);
		
		r2 = new ControllMusicReceiver();
		IntentFilter f2 = new IntentFilter();
		f2.addAction(GlobalConsts.ACTION_MUSIC_COMPLETED);
		this.registerReceiver(r2, f2);
	}

	/** ��Service */
	private void bindService() {
		Intent intent = new Intent(this, PlayMusicService.class);
		conn = new ServiceConnection() {
			//�쳣�Ͽ�ʱִ��
			public void onServiceDisconnected(ComponentName name) {
			}
			//���ӳɹ���ִ��  ���᷵��binder����
			public void onServiceConnected(ComponentName name, IBinder service) {
				//��ȡ����˷��ص�aidl�ӿ�ʵ��
				musicInterface = Stub.asInterface(service);
			}
		};
		this.bindService(intent, conn, Service.BIND_AUTO_CREATE);
	}

	@Override
	protected void onDestroy() {
		this.unbindService(conn);
		this.unregisterReceiver(receiver);
		this.unregisterReceiver(r2);
		super.onDestroy();
	}
	
	/** ��Ӽ����� */
	private void setListeners() {
		
		//�������  ������������
		btnSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FragmentTransaction tc = getSupportFragmentManager().beginTransaction();
				tc.setCustomAnimations(R.anim.bangdan_detail_fragment_enter, R.anim.bangdan_detail_fragment_exit,R.anim.bangdan_detail_fragment_enter, R.anim.bangdan_detail_fragment_exit)
					.add(R.id.frameLayoutSearch, new SearchFragment(), "search_panel")
					.addToBackStack("search_panel");
				tc.commit();
			}
		});
		
		//���ײ�����Ӽ���  ����󵯳����Ž���
		linearLayoutBottombar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FragmentTransaction tc = getSupportFragmentManager().beginTransaction();
				tc.setCustomAnimations(R.anim.play_panel_fragment_enter, R.anim.play_panel_fragment_exit,R.anim.play_panel_fragment_enter, R.anim.play_panel_fragment_exit)
					.add(R.id.frameLayoutPlaypanel, new PlayPanelFragment(), "play_panel")
					.addToBackStack("play_panel");
				tc.commit();
			}
		});
		
		//���ײ����Ĳ��Ű�ť��Ӽ���
		ivBBPlay.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(DaDaApp.getApp().getCurrentMusic()==null){
					return;
				}
				try {
					int state=musicInterface.playOrPause();
					if(state == GlobalConsts.STATE_PAUSED){
						ivBBPlay.setImageResource(R.drawable.bt_minibar_play_normal);
					}else{
						ivBBPlay.setImageResource(R.drawable.bt_minibar_pause_normal);
					}
					
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		
		//
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				FragmentTransaction tc = getSupportFragmentManager().beginTransaction();
				tc.hide(mineFragment);
				tc.hide(musicFragment);
				tc.hide(dongtaiFragment);
				tc.hide(findFragment);
				switch (checkedId) {
				case R.id.radioMine:
					tc.show(mineFragment);
					break;
				case R.id.radioMusic:
					tc.show(musicFragment);
					break;
				case R.id.radioDongtai:
					tc.show(dongtaiFragment);
					break;
				case R.id.radioFind:
					tc.show(findFragment);
					break;
				}
				tc.commit();
			}
		});
	}

	/**��ʼ��Fragment*/
	private void initFraments() {
		mineFragment = new MyFragment();
		musicFragment = new MusicFragment();
		dongtaiFragment = new TestFragment();
		findFragment = new TestFragment();
		//���ĸ�fragment��ӵ�fragmentTransaction��
		FragmentTransaction tc = getSupportFragmentManager().beginTransaction();
		tc.add(R.id.frameLayoutContainer, mineFragment);
		tc.add(R.id.frameLayoutContainer, musicFragment);
		tc.add(R.id.frameLayoutContainer, dongtaiFragment);
		tc.add(R.id.frameLayoutContainer, findFragment);
		tc.hide(mineFragment);
		tc.show(musicFragment);
		tc.hide(dongtaiFragment);
		tc.hide(findFragment);
		tc.commit();
	}

	/** �ؼ���ʼ�� */
	private void setViews() {
		btnSearch = (ImageButton) findViewById(R.id.btnSearch);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		rbMine = (RadioButton) findViewById(R.id.radioMine);
		rbMusic = (RadioButton) findViewById(R.id.radioMusic);
		rbDongtai = (RadioButton) findViewById(R.id.radioDongtai);
		rbFind = (RadioButton) findViewById(R.id.radioFind);
		frameLayoutContainer = (FrameLayout) findViewById(R.id.frameLayoutContainer);
		frameLayoutBangdanDetail = (FrameLayout) findViewById(R.id.frameLayoutBangdanDetail);
		frameLayoutSearch = (FrameLayout) findViewById(R.id.frameLayoutSearch);
		
		linearLayoutBottombar = (LinearLayout) findViewById(R.id.linearLayoutBottomBar);
		ivBBPic = (ImageView) findViewById(R.id.ivBBPic);
		ivBBPlay = (ImageView) findViewById(R.id.ivBBPlay);
		tvBBTitle = (TextView) findViewById(R.id.tvBBTitle);
		tvBBSinger = (TextView) findViewById(R.id.tvBBSinger);
	}
	
	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
        	Intent intent = new Intent(Intent.ACTION_MAIN,null);  
    		intent.addCategory(Intent.CATEGORY_HOME);  
    		startActivity(intent);
        }
	}
	
	public MusicInterface getMusicInterface(){
		return this.musicInterface;
	}

	/**
	 * ���ڸ��µײ�����UI
	 */
	class UpdateBottomBarReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(GlobalConsts.ACTION_MUSIC_PAUSED)){
				ivBBPlay.setImageResource(R.drawable.bt_minibar_play_normal);
			}else if(action.equals(GlobalConsts.ACTION_MUSIC_COMPLETED)){
				//���ܵ������ֲ�����ϵĹ㲥
				ivBBPlay.setImageResource(R.drawable.bt_minibar_play_normal);
			}else if(action.equals(GlobalConsts.ACTION_MUSIC_STARTED)){
				//���յ����ֿ�ʼ���Ź㲥�� ��
				Music music = DaDaApp.getApp().getCurrentMusic();
				//���µײ�����UI
				tvBBTitle.setText(music.getSongInfo().getTitle());
				tvBBSinger.setText(music.getSongInfo().getAuthor());
				//���²��Ű�ť
				ivBBPlay.setImageResource(R.drawable.bt_minibar_pause_normal);
				//���µײ�����pic
				BitmapUtils.loadBitmap(music.getSongInfo().getPic_small(), new BitmapCallback() {
					public void onBitmapLoaded(Bitmap bitmap) {
						ivBBPic.setImageBitmap(bitmap);
					}
				});
			}
		}
	}
	
	/** �������ֲ���ģʽ�Ĺ㲥������  �������ֲ�����ϵĹ㲥 */
	public class ControllMusicReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent) {
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
	}

	
}