package cn.tedu.mediaplayer.model;

import java.util.List;

/** 搜索建议列表的回调接口 */
public interface SearchSuggestCallback {
	public void onSuggestLoaded(List<String> data);
}
