package cn.tedu.mediaplayer.model;

import android.graphics.Bitmap;

public interface BitmapCallback {
	/**
	 * 当图片加载完毕后自动调用该回调方法
	 * @param bitmap
	 */
	public void onBitmapLoaded(Bitmap bitmap);
}	
