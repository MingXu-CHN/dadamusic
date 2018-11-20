package cn.tedu.mediaplayer.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.app.DaDaApp;
import cn.tedu.mediaplayer.dao.DBHelper;
import cn.tedu.mediaplayer.entity.Music;

public class DownloadService extends IntentService {

	private static final int NOTIFICATION_ID = 100;

	public DownloadService() {
		super("download");
	}
	
	public DownloadService(String name) {
		super(name);
	}

	/**
	 * IntentService内部包含有一个工作线程Looper。
	 * 轮循消息队列，looper将会把消息队列中的消息
	 * 依次执行。
	 * 当我们调用startService启动IntentService时，
	 * 将会把onHandleIntent中的业务逻辑添加到消息
	 * 队列等待执行，Looper将会在子线程中轮循
	 * 消息队列，并且在合适的时机执行onHandleIntent方法
	 * 
	 * 所以onHandleIntent方法将会在子线程中执行
	 * 我们可以直接在该方法中访问网络。
	 */
	protected void onHandleIntent(Intent intent) {
		String filelink = intent.getStringExtra("filelink");
		String title = intent.getStringExtra("title");
		int total = intent.getIntExtra("total", 0);
		Music music = (Music) intent.getSerializableExtra("music");
		//准备一下 文件存储的位置：
		// /mnt/sdcard/Music/dada/往事随风.mp3
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "dada/"+title+".mp3");
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		//发送http请求
		try {
			URL url = new URL(filelink);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			InputStream is = conn.getInputStream();
			//执行下载业务  边读取  边保存到SD卡
			byte[] buffer = new byte[8*1024];
			int length = 0;
			FileOutputStream fos = new FileOutputStream(file);
			int currentLength=0;
			//音乐开始下载
			sendNotification("音乐下载", 100, 0, "音乐已经开始下载");
			//记录while循环的次数  60的倍数时发送通知
			int time = 0 ;
			while((length = is.read(buffer))!=-1){
				time ++;
				currentLength += length;
				//输出到SD卡的某个文件中
				fos.write(buffer, 0, length);
				fos.flush();
				//Log.d("info", "currentLength:"+currentLength);
				//已经把一部分数据保存到了sd卡  发送通知
				if(time%60 == 0){
					sendNotification("音乐下载", total, currentLength, "音乐已经开始下载");
				}
			}
			fos.close();
			clearNotification();
			sendNotification("音乐下载", 0, 0, "音乐下载完成");
			//把刚下载的音乐基本信息  也保存到本地数据库中  加载数据 时作为缓存使用
			music.setFilepath(file.getAbsolutePath());
			music.setFile_duration(total);
			updateMediaDatabase(music);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateMediaDatabase(Music music) {
		DBHelper helper = DBHelper.getInstance();
		helper.reSave(music);
		mediaScan(music.getFilepath());
	}
	
	public void mediaScan(String filepath) {
	    MediaScannerConnection.scanFile(this, new String[] { filepath }, null, new OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
                Log.v("MediaScanWork", "file " + path + " was scanned seccessfully: " + uri);
            }
        });
	}
	
	/*** 发通知 */
	public void sendNotification(String title, int max, int progress, String ticker){
		NotificationManager manager = (NotificationManager) 
				getSystemService(NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(this);
		builder.setContentTitle(title)
			.setProgress(max, progress, false)
			.setTicker(ticker)
			.setSmallIcon(R.drawable.bt_playpage_button_download_normal_new);
		if(max==0 && progress ==0){
			builder.setContentText("文件下载完成");
		}
		Notification n = builder.build();
		manager.notify(NOTIFICATION_ID, n);
	}
	
	/** 清除通知 */
	public void clearNotification(){
		NotificationManager manager = (NotificationManager) 
				getSystemService(NOTIFICATION_SERVICE);
		manager.cancel(NOTIFICATION_ID);
		
	}
	
}










