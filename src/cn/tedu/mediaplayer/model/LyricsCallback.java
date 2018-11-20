package cn.tedu.mediaplayer.model;

import cn.tedu.mediaplayer.entity.Lyrics;

public interface LyricsCallback {
	public void onLyricsLoaded(Lyrics lyrics);
}
