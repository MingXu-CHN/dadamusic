package cn.tedu.mediaplayer.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.activity.MainActivity;
import cn.tedu.mediaplayer.adapter.LocalMusicAdapter;
import cn.tedu.mediaplayer.aidl.MusicInterface;
import cn.tedu.mediaplayer.app.DaDaApp;
import cn.tedu.mediaplayer.entity.Music;
import cn.tedu.mediaplayer.entity.SongInfo;
import cn.tedu.mediaplayer.entity.SongUrl;
import cn.tedu.mediaplayer.model.MusicListCallback;
import cn.tedu.mediaplayer.model.MusicModel;
import cn.tedu.mediaplayer.model.SongInfoCallback;

public class LocalMusicFragment extends Fragment{
	private ImageView ivBack;
	private ImageView ivScan;
	protected List<Music> musicList;
	private LocalMusicAdapter adapter;
	private ListView listView;
	private MusicModel model;
	private ProgressDialog progressDialog;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_local_music, null);
		setViews(view);
		setListeners();
		//加载本地音乐列表
		model = new MusicModel();
		model.loadLocalMusicList(new MusicListCallback() {
			public void onMusicListLoaded(List<Music> musics) {
				musicList = musics;
				updateListView();
			}
		});
		return view;
	}
	
	/** 更新适配器 */
	public void updateListView(){
		adapter = new LocalMusicAdapter(getActivity(), musicList, listView);
		listView.setAdapter(adapter);
	}
	
	/** 添加监听器 */
	private void setListeners() {
		ivScan.setOnClickListener(new OnClickListener() {
			private List<String> filepaths = new ArrayList<String>();
			public void onClick(View v) {
				File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
				loadMp3FilenameInMusicDir(file);
				String[] filepathArray = new String[filepaths.size()];
				filepaths.toArray(filepathArray);
				mediaScan(filepathArray);
			}

			private void loadMp3FilenameInMusicDir(File file) {
				File[] fs = file.listFiles();
				for(File f : fs){
					if(f.isDirectory()){
						loadMp3FilenameInMusicDir(f);
					}else if(f.getName().endsWith(".mp3")){
						filepaths.add(f.getAbsolutePath());
					}
				}
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//开始播放音乐
				DaDaApp.getApp().setMusicList(musicList);
				DaDaApp.getApp().setPosition(position);
				final Music music=musicList.get(position);
				//播放音乐
				try {
					String filelink=music.getFilepath();
					MainActivity act = (MainActivity)getActivity();
					MusicInterface musicInterface = act.getMusicInterface();
					musicInterface.playMusic(filelink);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		
		ivBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getFragmentManager().popBackStackImmediate();
			}
		});
	}
	public void mediaScan(String[] filepaths) {
		progressDialog  = new ProgressDialog(getActivity(), ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
		progressDialog.setMessage("扫描中");
		progressDialog.show();
	    MediaScannerConnection.scanFile(getActivity(), filepaths, null, new OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
                Log.v("MediaScanWork", "file " + path + " was scanned seccessfully: " + uri);
                //扫描完毕更新适配器
                model.loadLocalMusicList(new MusicListCallback() {
        			public void onMusicListLoaded(List<Music> musics) {
        				musicList = musics;
        				updateListView();
        				progressDialog.dismiss();
        			}
        		});
            }
        });
	}
	
	private void setViews(View view) {
		ivBack = (ImageView) view.findViewById(R.id.ivBack);
		listView = (ListView) view.findViewById(R.id.listView);
		ivScan = (ImageView) view.findViewById(R.id.ivScan);
	}
	
}
