package cn.tedu.mediaplayer.model;

import java.util.List;

import cn.tedu.mediaplayer.entity.Bangdan;

/** ���б�Ļص��ӿ� */
public interface BangdanListCallback {
	/**
	 * �����б����ݼ�����Ϻ�ִ��
	 * @param bangdans
	 */
	public void onBangdanListLoaded(List<Bangdan> bangdans);
}



