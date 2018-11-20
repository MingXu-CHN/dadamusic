package cn.tedu.mediaplayer.model;

import java.util.List;

import cn.tedu.mediaplayer.entity.Gedan;

public interface GedanListCallback {
	/***
	 * 当歌单列表加载完毕后  将调用该回调方法
	 * @param gedans
	 */
	public void onGedanListLoaded(List<Gedan> gedans);
	
}
