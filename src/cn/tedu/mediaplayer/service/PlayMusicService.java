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

/** �������ֵķ��� */
public class PlayMusicService extends Service{
	private MediaPlayer player;
	protected boolean isLoop=true;
	
	/** ��Serviceʵ������ʱִ��  ��ΪServiceʵ��ʱ�����ģ�����onCreate��ִ��һ��*/
	public void onCreate() {
		super.onCreate();
		player = new MediaPlayer();
		player.setOnPreparedListener(new OnPreparedListener() {
			public void onPrepared(MediaPlayer mp) {
				//����Դ׼�����
				player.start();
				//�����Զ���㲥  ���ֿ�ʼ���Ź㲥
				Intent intent = new Intent(GlobalConsts.ACTION_MUSIC_STARTED);
				sendBroadcast(intent);
			}
		});
		player.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				//�����Զ���㲥  ���ֹ㲥���
				Intent intent = new Intent(GlobalConsts.ACTION_MUSIC_COMPLETED);
				sendBroadcast(intent);
			}
		});
		//����һ�����߳�  ÿ1s����һ�θ������ֽ��ȵĹ㲥
		new Thread(){
			public void run() {
				while(isLoop){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(player.isPlaying()){
						//���͸������ֽ��ȵĹ㲥
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
			//ʵ�����ֵĲ���
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




