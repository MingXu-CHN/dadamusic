package cn.tedu.mediaplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.app.DaDaApp;

/**
 * Created by hanamingming on 2017/4/18.
 */
public class MatchPicsAdapter extends RecyclerView.Adapter {
    private List<String> picsPaths;
    private Context context;
    private ImageLoader imageLoader;

    public MatchPicsAdapter(Context context, List<String> picsPaths) {
        this.picsPaths = picsPaths;
        this.context = context;
        this.imageLoader = new ImageLoader(DaDaApp.getQueue(), DaDaApp.getBitmapCache());
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_match_pic, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder h = (ViewHolder) holder;
        String pic = picsPaths.get(position);
        ImageLoader.ImageListener l = ImageLoader.getImageListener(h.imageView, R.drawable.user_playlist_default_cd, R.drawable.user_playlist_default_cd);
        imageLoader.get(pic, l);
        //是否显示选中项
    }

    public int getItemCount() {
        return picsPaths.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
        }
    }
}
