package cn.tedu.mediaplayer.model;

import java.util.List;

import cn.tedu.mediaplayer.entity.SongInfo;
import cn.tedu.mediaplayer.entity.SongUrl;

public interface SongInfoCallback {
	/**
	 * �����ֻ�����Ϣ������Ϻ�ִ��
	 */
	public void onSongInfoLoaded(List<SongUrl> urls, SongInfo info);
} 





