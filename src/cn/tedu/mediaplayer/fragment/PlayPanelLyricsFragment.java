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
		//�ؼ���ʼ��
		setViews(view);
		setListeners();
		//����ҵ�񷽷� ���ظ�ʶ���
		MusicModel model = new MusicModel();
		Music music=DaDaApp.getApp().getCurrentMusic();
		String url = music.getSongInfo().getLrclink();
		model.loadLyrics(url, new LyricsCallback() {
			public void onLyricsLoaded(Lyrics lrc) {
				lyrics = lrc;
				updateLyricsListView();
			}
		});
		//ע��㲥������
		registReceivers();
		return view;
	}

	/** ���ü��� */
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
	
	//ע��㲥������
	private void registReceivers() {
		receiver = new UpdateLrcReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalConsts.ACTION_UPDATE_MUSIC_PROGRESS);
		filter.addAction(GlobalConsts.ACTION_MUSIC_STARTED);
		getActivity().registerReceiver(receiver, filter);
	}

	/** �ؼ���ʼ�� */
	private void setViews(View view) {
		listView = (ListView) view.findViewById(R.id.listView);
		linearLayoutSearchLyricsAndPic = (LinearLayout) view.findViewById(R.id.linearLayoutSearchLyricsAndPic);
	}

	/** ���¸��ListView */
	private void updateLyricsListView() {
		if(lyrics == null){ //û�и��  ��ʾ���Ҵ�ͼ����
			linearLayoutSearchLyricsAndPic.setVisibility(View.VISIBLE);
			return;
		}else{//���ز��Ҵ�ͼ����
			linearLayoutSearchLyricsAndPic.setVisibility(View.INVISIBLE);
		}
		adapter = new LyricsAdapter(getActivity(), lyrics);
		//��listView���headerView  ����һ���־���
		//��listView���footerView  ����һ���־���
		View v = View.inflate(getActivity(), R.layout.view_lyrics_header_empty, null);
		listView.addHeaderView(v);
		listView.addFooterView(v);
		listView.setAdapter(adapter);
	}
	
	/** ���ո������ֽ��ȵĹ㲥   ���¸����ʾ */
	class UpdateLrcReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(GlobalConsts.ACTION_MUSIC_STARTED)){
				//���¼��ظ��
				MusicModel model = new MusicModel();
				Music music=DaDaApp.getApp().getCurrentMusic();
				String url = music.getSongInfo().getLrclink();
				model.loadLyrics(url, new LyricsCallback() {
					public void onLyricsLoaded(Lyrics lrc) {
						lyrics = lrc;
						//����ListView
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
				if(position!=-1){ //�ҵ��˶�Ӧʱ���item
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







