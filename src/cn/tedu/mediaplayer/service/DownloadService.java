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
	 * IntentService�ڲ�������һ�������߳�Looper��
	 * ��ѭ��Ϣ���У�looper�������Ϣ�����е���Ϣ
	 * ����ִ�С�
	 * �����ǵ���startService����IntentServiceʱ��
	 * �����onHandleIntent�е�ҵ���߼���ӵ���Ϣ
	 * ���еȴ�ִ�У�Looper���������߳�����ѭ
	 * ��Ϣ���У������ں��ʵ�ʱ��ִ��onHandleIntent����
	 * 
	 * ����onHandleIntent�������������߳���ִ��
	 * ���ǿ���ֱ���ڸ÷����з������硣
	 */
	protected void onHandleIntent(Intent intent) {
		String filelink = intent.getStringExtra("filelink");
		String title = intent.getStringExtra("title");
		int total = intent.getIntExtra("total", 0);
		Music music = (Music) intent.getSerializableExtra("music");
		//׼��һ�� �ļ��洢��λ�ã�
		// /mnt/sdcard/Music/dada/�������.mp3
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "dada/"+title+".mp3");
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		//����http����
		try {
			URL url = new URL(filelink);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			InputStream is = conn.getInputStream();
			//ִ������ҵ��  �߶�ȡ  �߱��浽SD��
			byte[] buffer = new byte[8*1024];
			int length = 0;
			FileOutputStream fos = new FileOutputStream(file);
			int currentLength=0;
			//���ֿ�ʼ����
			sendNotification("��������", 100, 0, "�����Ѿ���ʼ����");
			//��¼whileѭ���Ĵ���  60�ı���ʱ����֪ͨ
			int time = 0 ;
			while((length = is.read(buffer))!=-1){
				time ++;
				currentLength += length;
				//�����SD����ĳ���ļ���
				fos.write(buffer, 0, length);
				fos.flush();
				//Log.d("info", "currentLength:"+currentLength);
				//�Ѿ���һ�������ݱ��浽��sd��  ����֪ͨ
				if(time%60 == 0){
					sendNotification("��������", total, currentLength, "�����Ѿ���ʼ����");
				}
			}
			fos.close();
			clearNotification();
			sendNotification("��������", 0, 0, "�����������");
			//�Ѹ����ص����ֻ�����Ϣ  Ҳ���浽�������ݿ���  �������� ʱ��Ϊ����ʹ��
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
	
	/*** ��֪ͨ */
	public void sendNotification(String title, int max, int progress, String ticker){
		NotificationManager manager = (NotificationManager) 
				getSystemService(NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(this);
		builder.setContentTitle(title)
			.setProgress(max, progress, false)
			.setTicker(ticker)
			.setSmallIcon(R.drawable.bt_playpage_button_download_normal_new);
		if(max==0 && progress ==0){
			builder.setContentText("�ļ��������");
		}
		Notification n = builder.build();
		manager.notify(NOTIFICATION_ID, n);
	}
	
	/** ���֪ͨ */
	public void clearNotification(){
		NotificationManager manager = (NotificationManager) 
				getSystemService(NOTIFICATION_SERVICE);
		manager.cancel(NOTIFICATION_ID);
		
	}
	
}










