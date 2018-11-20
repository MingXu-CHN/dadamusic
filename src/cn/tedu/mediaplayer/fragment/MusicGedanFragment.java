package cn.tedu.mediaplayer.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.adapter.MusicGedanAdapter;
import cn.tedu.mediaplayer.entity.Gedan;
import cn.tedu.mediaplayer.model.GedanListCallback;
import cn.tedu.mediaplayer.model.MusicModel;

public class MusicGedanFragment extends Fragment{
	private GridView gridView;
	private MusicGedanAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_music_gedan, null);
		setViews(view);
		setListeners();
		//加载歌单列表
		MusicModel model = new MusicModel();
		model.loadGedanList(new GedanListCallback() {
			public void onGedanListLoaded(List<Gedan> gedans) {
				//更新GridView
				updateGridView(gedans);
			}
		});
		return view;
	}

	/** 设置监听 */
	private void setListeners() {
		gridView.setOnScrollListener(new OnScrollListener() {
			//当滚动状态改变时执行
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					adapter.setScrolling(false);
					//把当前界面中所有的item的数据加载一遍
					adapter.notifyDataSetChanged();
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					adapter.setScrolling(true);
					break;
				case OnScrollListener.SCROLL_STATE_FLING:
					adapter.setScrolling(true);
					break;
				}
			}
			//当滚动时执行
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				
			}
		});
	}

	/** 控件初始化 */
	private void setViews(View view) {
		gridView = (GridView) view.findViewById(R.id.gridView);
	}
	
	/** 更新GridView */
	public void updateGridView(List<Gedan> gedans){
		adapter = new MusicGedanAdapter(getActivity(), gedans);
		gridView.setAdapter(adapter);
	}
	
}




