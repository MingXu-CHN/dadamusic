package cn.tedu.mediaplayer.app;

import java.util.List;
import java.util.Random;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import cn.tedu.mediaplayer.entity.Music;
import cn.tedu.mediaplayer.util.LruBitmapCache;

public class DaDaApp extends Application {
	private static RequestQueue queue;
	private static LruBitmapCache cache;
	private static DaDaApp app;
	private List<Music> musics;
	private int position;
	private int LOOPMODE = 0;
	
	public static final int LOOP_MODE_ALL_LOOP = 0;
	public static final int LOOP_MODE_SINGLE_LOOP = 1;
	public static final int LOOP_MODE_RANDOM_LOOP = 2;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		queue = Volley.newRequestQueue(this);
		cache = new LruBitmapCache();
		app = this;
	}

	public static RequestQueue getQueue() {
		return queue;
	}

	public static LruBitmapCache getBitmapCache() {
		return cache;
	}

	public static DaDaApp getApp() {
		return app;
	}

	public void setMusicList(List<Music> musics) {
		this.musics = musics;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Music getCurrentMusic() {
		if (musics == null) {
			return null;
		}
		return musics.get(position);
	}

	public Music getPreviousMusic() {
		position = position == 0 ? 0 : position - 1;
		return musics.get(position);
	}

	public Music getNextMusic() {
		switch (LOOPMODE) {
		case LOOP_MODE_ALL_LOOP:
			position = position == musics.size() - 1 ? 0 : position + 1;
			break;
		case LOOP_MODE_SINGLE_LOOP:
			//position²»±ä
			break;
		case LOOP_MODE_RANDOM_LOOP:
			position = new Random().nextInt(musics.size());
			break;
		}
		return musics.get(position);
	}

	public int getCurrentVersionCode() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			return info.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void setLoopMode(int loopMode){
		this.LOOPMODE = loopMode;
	}

	public int nextLoopMode() {
		LOOPMODE = (LOOPMODE+1)%3;
		return LOOPMODE;
	}
	
}
