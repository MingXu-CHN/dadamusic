package cn.tedu.mediaplayer.listener;

import cn.tedu.mediaplayer.entity.SongUrl;

public interface OnDownloadPressListener {
	/**
	 * �����popwindow�����ذ�ť�� ִ��
	 * @param url
	 */
	public void onDownloadPressed(SongUrl url);
}
