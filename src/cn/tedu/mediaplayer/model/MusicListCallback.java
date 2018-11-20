package cn.tedu.mediaplayer.model;

import java.util.List;

import cn.tedu.mediaplayer.entity.Music;

public interface MusicListCallback {
	public void onMusicListLoaded(List<Music> musics);
}
