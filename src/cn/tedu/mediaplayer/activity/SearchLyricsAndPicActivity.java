package cn.tedu.mediaplayer.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.adapter.MatchLrcsAdapter;
import cn.tedu.mediaplayer.adapter.MatchPicsAdapter;
import cn.tedu.mediaplayer.entity.Music;
import cn.tedu.mediaplayer.entity.SongInfo;
import cn.tedu.mediaplayer.entity.SongUrl;
import cn.tedu.mediaplayer.model.MusicListCallback;
import cn.tedu.mediaplayer.model.MusicModel;
import cn.tedu.mediaplayer.model.SongInfoCallback;

/**
 * Created by hanamingming on 2017/4/18.
 *
 */

public class SearchLyricsAndPicActivity extends Activity {

    private EditText etSongName;
    private EditText etAuthor;
    private RecyclerView recyclerViewPics;
    private RecyclerView recyclerViewLyrics;
    private ImageButton btnSearch;
    private MusicModel model;
    private List<String> lyricsPaths = new ArrayList<String>();
    private List<String> picsPaths = new ArrayList<String>();
    private MatchPicsAdapter picAdapter;
    private MatchLrcsAdapter lrcAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lyrics_pics);
        model = new MusicModel();
        setViews();
        setListeners();
    }

    /** 设置监听 */
    private void setListeners() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                searchLyricsAndPics();
            }
        });
    }

    /*** 查找词图 */
    private void searchLyricsAndPics() {
        String name = etSongName.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        //开始搜索词图
        model.searchMusicListByKeyword(name + (author.equals("") ? "" : " - " + author), new MusicListCallback() {
            public void onMusicListLoaded(List<Music> musics) {
                //清空列表

                progressDialog  = new ProgressDialog(SearchLyricsAndPicActivity.this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
                progressDialog.setMessage("扫描中");
                progressDialog.show();

                picsPaths.clear();
                lyricsPaths.clear();

                loadMusicInfo(musics, 0);
            }
        });
    }


    public void loadMusicInfo(final List<Music> musics, final int position){
        Music firstMusic = musics.get(position);
        //加载这首歌的基本信息 获取图片与歌词路径列表
        model.loadSongInfoBySongId(firstMusic.getSong_id(), new SongInfoCallback() {
            public void onSongInfoLoaded(List<SongUrl> urls, SongInfo info) {
                String picPath = info.getAlbum_500_500();
                if(!picPath.equals("")){
                    picsPaths.add(picPath);
                }
                String lrcPath = info.getLrclink();
                if(!lrcPath.equals("")){
                    lyricsPaths.add(lrcPath);
                }
                if(position==9){
                    updateMatchPicList();
                    updateMatchLrcList();
                    progressDialog.dismiss();
                    return;
                }
                loadMusicInfo(musics, position+1);
            }
        });
    }

    /** 更新匹配图片列表 */
    private void updateMatchPicList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewPics.setLayoutManager(layoutManager);
        picAdapter = new MatchPicsAdapter(this, picsPaths);
        recyclerViewPics.setAdapter(picAdapter);
    }

    /** 更新匹配歌词列表 */
    private void updateMatchLrcList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewLyrics.setLayoutManager(layoutManager);
        lrcAdapter = new MatchLrcsAdapter(this, lyricsPaths);
        recyclerViewLyrics.setAdapter(lrcAdapter);
    }

    /** 控件初始化 */
    private void setViews() {
        etSongName = (EditText) findViewById(R.id.etSongName);
        etAuthor = (EditText) findViewById(R.id.etAuthor);
        recyclerViewPics = (RecyclerView) findViewById(R.id.recyclerViewPics);
        recyclerViewLyrics = (RecyclerView) findViewById(R.id.recyclerViewLyrics);
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
    }

}
