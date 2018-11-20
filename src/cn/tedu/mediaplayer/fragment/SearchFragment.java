package cn.tedu.mediaplayer.fragment;

import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.nex3z.flowlayout.FlowLayout;

import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.activity.MainActivity;
import cn.tedu.mediaplayer.adapter.SearchMusicAdapter;
import cn.tedu.mediaplayer.adapter.SuggestAdapter;
import cn.tedu.mediaplayer.aidl.MusicInterface;
import cn.tedu.mediaplayer.app.DaDaApp;
import cn.tedu.mediaplayer.entity.Music;
import cn.tedu.mediaplayer.entity.SongInfo;
import cn.tedu.mediaplayer.entity.SongUrl;
import cn.tedu.mediaplayer.model.MusicListCallback;
import cn.tedu.mediaplayer.model.MusicModel;
import cn.tedu.mediaplayer.model.SearchSuggestCallback;
import cn.tedu.mediaplayer.model.SongInfoCallback;

public class SearchFragment extends Fragment{
	private ListView listView;
	private EditText etKeyword;
	private MusicModel model;
	private List<String> suggestData;
	private SuggestAdapter suggestAdapter;
	private ImageButton btnSearch;
	private ImageButton btnBack; 
	private FlowLayout flowLayout;
	private SearchMusicAdapter searchAdapter;


	private int currentState = 0;
	protected List<Music> musics;
	
	private static final int STATE_SEARCH_SUGGEST = 1;
	private static final int STATE_SEARCH_RESULT = 2;
	private boolean clickTagToSearch = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search, null);
		model = new MusicModel();
		//控件初始化
		setViews(view);
		//设置监听
		setListeners();
		//加载搜索记录关键字列表
		loadSearchKeyList();
		return view;
	}

	/** 加载搜索记录关键字列表 */
	private void loadSearchKeyList() {
		final MusicModel model = new MusicModel();
		List<String> keys = model.loadSearchKeysList();
		//把查出来的所有的关键字都显示在flowLayout中
		flowLayout.removeAllViews();
		for(String key : keys) {
			Button button = (Button) View.inflate(getActivity(), R.layout.view_search_keyword, null);
			button.setText(key);
			flowLayout.addView(button);
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					flowLayout.setVisibility(View.INVISIBLE);
					Button btn = (Button) view;
					String keyword = btn.getText().toString();
					clickTagToSearch = true;
					etKeyword.setText(keyword);
					searchMusic();
				}
			});
		}
	}

	/** 设置监听 */
	private void setListeners() {
		btnBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getFragmentManager().popBackStackImmediate();
			}
		});
		
		//给listView添加监听
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Log.i("info", "currentState:"+currentState);
				if(currentState == STATE_SEARCH_SUGGEST){
					String suggest=suggestData.get(position);
					etKeyword.setText(suggest);
					//执行搜索音乐业务
					searchMusic();
				}else if(currentState == STATE_SEARCH_RESULT){
					//把当前position 与 搜索结果列表 存入app
					DaDaApp.getApp().setMusicList(musics);
					DaDaApp.getApp().setPosition(position);
					//获取当前音乐对象
					final Music music = DaDaApp.getApp().getCurrentMusic();
					model.loadSongInfoBySongId(music.getSong_id(), new SongInfoCallback() {
						public void onSongInfoLoaded(List<SongUrl> urls, SongInfo info) {
							try {
								music.setSongUrls(urls);
								music.setSongInfo(info);
								//播放音乐
								String filelink = urls.get(0).getFile_link();
								MusicInterface  musicInterface = ((MainActivity)getActivity()).getMusicInterface();
								musicInterface.playMusic(filelink);
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});
		
		
		//点击搜索  
		btnSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				searchMusic();
			}
		});
		
		//当文本框中输入文本后  执行的监听
		etKeyword.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void afterTextChanged(Editable s) {
				//获取文本框中的关键字  然后发送请求 获取推荐信息列表
				String keyword = etKeyword.getText().toString();
				if(keyword.isEmpty()){
					listView.setVisibility(View.INVISIBLE);
					flowLayout.setVisibility(View.VISIBLE);
					loadSearchKeyList();
				}else{
					listView.setVisibility(View.VISIBLE);
					//如果是点击标签的状态的话 则不加载建议列表直接返回
					if(clickTagToSearch){
						clickTagToSearch = false;
						return;
					}
					model.loadSuggestByKeyword(keyword, new SearchSuggestCallback() {
						public void onSuggestLoaded(List<String> data) {
							// 获取建议文本列表  更新界面
							suggestData = data;
							updateSuggestListView();
						}
					});
				}
			}
		});
	}

	/** 搜索音乐业务 */
	public void searchMusic(){
		String keyword = etKeyword.getText().toString();
		model.searchMusicListByKeyword(keyword, new MusicListCallback() {
			public void onMusicListLoaded(List<Music> musics) {
				SearchFragment.this.musics = musics;
				//更新ListView
				searchAdapter = new SearchMusicAdapter(getActivity(), musics, listView);
				listView.setAdapter(searchAdapter);
				currentState = STATE_SEARCH_RESULT;
			}
		});
	}
	
	/** 把建议列表数据 显示到ListView */
	public void updateSuggestListView(){
		suggestAdapter = new SuggestAdapter(getActivity(), suggestData);
		listView.setAdapter(suggestAdapter);
		currentState = STATE_SEARCH_SUGGEST;
	}
	
	/** 控件初始化 */
	private void setViews(View view) {
		btnBack = (ImageButton) view.findViewById(R.id.btnBack);
		etKeyword = (EditText) view.findViewById(R.id.etKeyword);
		listView = (ListView) view.findViewById(R.id.listView);
		btnSearch = (ImageButton) view.findViewById(R.id.btnSearch);
		flowLayout = (FlowLayout) view.findViewById(R.id.flowLayout);
	}
}










