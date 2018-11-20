package cn.tedu.mediaplayer.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.activity.SearchLyricsAndPicActivity;
import cn.tedu.mediaplayer.adapter.LyricsAdapter;
import cn.tedu.mediaplayer.app.DaDaApp;
import cn.tedu.mediaplayer.entity.Lyrics;
import cn.tedu.mediaplayer.entity.Music;
import cn.tedu.mediaplayer.model.LyricsCallback;
import cn.tedu.mediaplayer.model.MusicModel;
import cn.tedu.mediaplayer.util.DateUtil;
import cn.tedu.mediaplayer.util.GlobalConsts;

public class PlayPanelLyricsFragment extends Fragment{
	private ListView listView;
	private Lyrics lyrics;
	private LyricsAdapter adapter;
	private UpdateLrcReceiver receiver;
	private LinearLayout linearLayoutSearchLyricsAndPic;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_play_panel_lyrics, null);
		//控件初始化
		setViews(view);
		setListeners();
		//调用业务方法 加载歌词对象
		MusicModel model = new MusicModel();
		Music music=DaDaApp.getApp().getCurrentMusic();
		String url = music.getSongInfo().getLrclink();
		model.loadLyrics(url, new LyricsCallback() {
			public void onLyricsLoaded(Lyrics lrc) {
				lyrics = lrc;
				updateLyricsListView();
			}
		});
		//注册广播接收器
		registReceivers();
		return view;
	}

	/** 设置监听 */
	private void setListeners() {
		linearLayoutSearchLyricsAndPic.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(), SearchLyricsAndPicActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onDestroyView() {
		getActivity().unregisterReceiver(receiver);
		super.onDestroyView();
	}
	
	//注册广播接收器
	private void registReceivers() {
		receiver = new UpdateLrcReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalConsts.ACTION_UPDATE_MUSIC_PROGRESS);
		filter.addAction(GlobalConsts.ACTION_MUSIC_STARTED);
		getActivity().registerReceiver(receiver, filter);
	}

	/** 控件初始化 */
	private void setViews(View view) {
		listView = (ListView) view.findViewById(R.id.listView);
		linearLayoutSearchLyricsAndPic = (LinearLayout) view.findViewById(R.id.linearLayoutSearchLyricsAndPic);
	}

	/** 更新歌词ListView */
	private void updateLyricsListView() {
		if(lyrics == null){ //没有歌词  显示查找词图界面
			linearLayoutSearchLyricsAndPic.setVisibility(View.VISIBLE);
			return;
		}else{//隐藏查找词图界面
			linearLayoutSearchLyricsAndPic.setVisibility(View.INVISIBLE);
		}
		adapter = new LyricsAdapter(getActivity(), lyrics);
		//给listView添加headerView  顶开一部分距离
		//给listView添加footerView  顶开一部分距离
		View v = View.inflate(getActivity(), R.layout.view_lyrics_header_empty, null);
		listView.addHeaderView(v);
		listView.addFooterView(v);
		listView.setAdapter(adapter);
	}
	
	/** 接收更新音乐进度的广播   更新歌词显示 */
	class UpdateLrcReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(GlobalConsts.ACTION_MUSIC_STARTED)){
				//重新加载歌词
				MusicModel model = new MusicModel();
				Music music=DaDaApp.getApp().getCurrentMusic();
				String url = music.getSongInfo().getLrclink();
				model.loadLyrics(url, new LyricsCallback() {
					public void onLyricsLoaded(Lyrics lrc) {
						lyrics = lrc;
						//更新ListView
						//Log.i("info", ""+lyrics);
						updateLyricsListView();
					}
				});
			}else if(action.equals(GlobalConsts.ACTION_UPDATE_MUSIC_PROGRESS)){
				if(lyrics == null){
					return ;
				}
				int current=intent.getIntExtra("current", 0);
				String time = DateUtil.parseTime(current);
				int position=lyrics.getPosition(time);
				if(position!=-1){ //找到了对应时间的item
					adapter.setCurrentPosition(position);
					adapter.notifyDataSetChanged();
					listView.smoothScrollToPositionFromTop(position,300, 100);
				}
			}
		}
	}

	public void slide(float positionOffset) {
		if(positionOffset==0){
			positionOffset = 1;
		}
		if(positionOffset<0.8){
			positionOffset = 0.8f;
		}
		listView.setScaleX(positionOffset);
		listView.setScaleY(positionOffset);
	}
	
}







