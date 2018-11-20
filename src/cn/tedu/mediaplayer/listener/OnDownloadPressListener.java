package cn.tedu.mediaplayer.listener;

import cn.tedu.mediaplayer.entity.SongUrl;

public interface OnDownloadPressListener {
	/**
	 * 当点击popwindow的下载按钮后 执行
	 * @param url
	 */
	public void onDownloadPressed(SongUrl url);
}
