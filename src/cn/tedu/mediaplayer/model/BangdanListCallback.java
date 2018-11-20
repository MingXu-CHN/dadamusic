package cn.tedu.mediaplayer.model;

import java.util.List;

import cn.tedu.mediaplayer.entity.Bangdan;

/** 榜单列表的回调接口 */
public interface BangdanListCallback {
	/**
	 * 当榜单列表数据加载完毕后执行
	 * @param bangdans
	 */
	public void onBangdanListLoaded(List<Bangdan> bangdans);
}



