package cn.tedu.mediaplayer.model;

import cn.tedu.mediaplayer.entity.BangdanDetail;

public interface BangdanDetailCallback {
	
	/**
	 * 当榜单详情解析封装完毕后 执行 
	 * @param bangdan
	 */
	public void onBangdanDataLoaded(BangdanDetail bangdan);

}






