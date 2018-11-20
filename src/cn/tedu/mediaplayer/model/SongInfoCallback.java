package cn.tedu.mediaplayer.model;

import java.util.List;

import cn.tedu.mediaplayer.entity.SongInfo;
import cn.tedu.mediaplayer.entity.SongUrl;

public interface SongInfoCallback {
	/**
	 * 当音乐基本信息加载完毕后执行
	 */
	public void onSongInfoLoaded(List<SongUrl> urls, SongInfo info);
} 





