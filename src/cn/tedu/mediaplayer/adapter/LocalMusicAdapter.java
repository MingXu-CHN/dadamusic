package cn.tedu.mediaplayer.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.entity.Music;
import cn.tedu.mediaplayer.model.MusicModel;
import cn.tedu.mediaplayer.util.DensityUtil;

public class LocalMusicAdapter extends BaseAdapter{
	private Context context;
	private List<Music> musics;
	private LayoutInflater inflater;
	private ListView listView;
	private MusicModel model;
	
	public LocalMusicAdapter(Context context, List<Music> musics, ListView listView) {
		this.context = context;
		this.musics = musics;
		this.listView = listView;
		this.model = new MusicModel();
		this.inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return musics.size();
	}

	@Override
	public Music getItem(int position) {
		return musics.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_local_music, null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvSinger = (TextView) convertView.findViewById(R.id.tvSinger);
			holder.btnMore = (ImageButton) convertView.findViewById(R.id.btnMore);
			convertView.setTag(holder);
		}
		holder=(ViewHolder) convertView.getTag();
		final Music music = getItem(position);
		holder.tvTitle.setText(Html.fromHtml(music.getTitle()));
		holder.tvSinger.setText(Html.fromHtml(music.getAuthor()+" - "+music.getAlbum_title()));
		if(position==musics.size()-1){
			convertView.setPadding(DensityUtil.dip2px(context, 16), 0, DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 60));
		}else{
			convertView.setPadding(DensityUtil.dip2px(context, 16), 0, DensityUtil.dip2px(context, 16), 0);
		}
		return convertView;
	}

	class ViewHolder{
		TextView tvTitle;
		TextView tvSinger;
		ImageButton btnMore;
	}
	
}
