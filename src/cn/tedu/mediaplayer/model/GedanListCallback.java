package cn.tedu.mediaplayer.model;

import java.util.List;

import cn.tedu.mediaplayer.entity.Gedan;

public interface GedanListCallback {
	/***
	 * ���赥�б������Ϻ�  �����øûص�����
	 * @param gedans
	 */
	public void onGedanListLoaded(List<Gedan> gedans);
	
}
