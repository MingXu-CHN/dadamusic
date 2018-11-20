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
		//���ظ赥�б�
		MusicModel model = new MusicModel();
		model.loadGedanList(new GedanListCallback() {
			public void onGedanListLoaded(List<Gedan> gedans) {
				//����GridView
				updateGridView(gedans);
			}
		});
		return view;
	}

	/** ���ü��� */
	private void setListeners() {
		gridView.setOnScrollListener(new OnScrollListener() {
			//������״̬�ı�ʱִ��
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					adapter.setScrolling(false);
					//�ѵ�ǰ���������е�item�����ݼ���һ��
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
			//������ʱִ��
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				
			}
		});
	}

	/** �ؼ���ʼ�� */
	private void setViews(View view) {
		gridView = (GridView) view.findViewById(R.id.gridView);
	}
	
	/** ����GridView */
	public void updateGridView(List<Gedan> gedans){
		adapter = new MusicGedanAdapter(getActivity(), gedans);
		gridView.setAdapter(adapter);
	}
	
}




