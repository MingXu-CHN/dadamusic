package cn.tedu.mediaplayer.service;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.os.RemoteException;
import cn.tedu.mediaplayer.aidl.MusicInterface.Stub;
import cn.tedu.mediaplayer.util.GlobalConsts;

/** 播放音乐的服务 */
public class PlayMusicService extends Service{
	private MediaPlayer player;
	protected boolean isLoop=true;
	
	/** 在Service实例创建时执行  因为Service实例时单例的，所以onCreate就执行一次*/
	public void onCreate() {
		super.onCreate();
		player = new MediaPlayer();
		player.setOnPreparedListener(new OnPreparedListener() {
			public void onPrepared(MediaPlayer mp) {
				//数据源准备完毕
				player.start();
				//发送自定义广播  音乐开始播放广播
				Intent intent = new Intent(GlobalConsts.ACTION_MUSIC_STARTED);
				sendBroadcast(intent);
			}
		});
		player.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				//发送自定义广播  音乐广播完成
				Intent intent = new Intent(GlobalConsts.ACTION_MUSIC_COMPLETED);
				sendBroadcast(intent);
			}
		});
		//启动一条子线程  每1s发送一次更新音乐进度的广播
		new Thread(){
			public void run() {
				while(isLoop){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(player.isPlaying()){
						//发送更新音乐进度的广播
						Intent intent = new Intent(GlobalConsts.ACTION_UPDATE_MUSIC_PROGRESS);
						intent.putExtra("current", player.getCurrentPosition());
						intent.putExtra("total", player.getDuration());
						sendBroadcast(intent);
					}
				}
			}
		}.start();
	}
	
	public IBinder onBind(Intent intent) {
		return new MyBinder();
	}
	
	class MyBinder extends Stub{
		public void playMusic(String url) throws RemoteException {
			//实现音乐的播放
			try {
				player.reset();
				player.setDataSource(url);
				player.prepareAsync();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public int playOrPause() throws RemoteException {
			if(player.isPlaying()){
				player.pause();
				Intent intent = new Intent(GlobalConsts.ACTION_MUSIC_PAUSED);
				sendBroadcast(intent);
				return GlobalConsts.STATE_PAUSED;
			}else{
				player.start();
				Intent intent = new Intent(GlobalConsts.ACTION_MUSIC_STARTED);
				sendBroadcast(intent);
				return GlobalConsts.STATE_PLAYING;
			}
		}

		@Override
		public int getPlayState() throws RemoteException {
			if(player.isPlaying()){
				return GlobalConsts.STATE_PLAYING;
			}else{
				return GlobalConsts.STATE_PAUSED;
			}
		}

		@Override
		public void seekTo(int position) throws RemoteException {
			player.seekTo(position);
		}
		
	}
}




