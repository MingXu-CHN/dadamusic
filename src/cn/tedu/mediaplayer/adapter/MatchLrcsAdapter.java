package cn.tedu.mediaplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.app.DaDaApp;
import cn.tedu.mediaplayer.entity.Lyrics;
import cn.tedu.mediaplayer.model.LyricsCallback;
import cn.tedu.mediaplayer.model.MusicModel;

import static cn.tedu.mediaplayer.R.id.imageView;

/**
 * Created by hanamingming on 2017/4/18.
 */
public class MatchLrcsAdapter extends RecyclerView.Adapter {
    private List<String> lrcsPaths;
    private Context context;
    private ImageLoader imageLoader;
    private MusicModel model;

    public MatchLrcsAdapter(Context context, List<String> lrcsPaths) {
        this.lrcsPaths = lrcsPaths;
        this.context = context;
        this.model = new MusicModel();
        this.imageLoader = new ImageLoader(DaDaApp.getQueue(), DaDaApp.getBitmapCache());
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_match_lrc, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder h = (ViewHolder) holder;
        String url = lrcsPaths.get(position);
        //通过路径加载歌词内容显示到ListView控件中
        model.loadLyrics(url, new LyricsCallback() {
            public void onLyricsLoaded(Lyrics lyrics) {
                MatchLrcItemAdapter adapter = new MatchLrcItemAdapter(context, lyrics);
                h.listView.setAdapter(adapter);
            }
        });

    }

    public int getItemCount() {
        return lrcsPaths.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ListView listView;
        public ViewHolder(View itemView) {
            super(itemView);
            listView = (ListView)itemView.findViewById(R.id.listView);
        }
    }
}
