package cn.tedu.mediaplayer.adapter;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.app.DaDaApp;
import cn.tedu.mediaplayer.entity.Music;

/**  榜单详情界面中listView的适配器  */
public class BangdanDetailAdapter extends BaseAdapter{
	private Context context;
	private List<Music> musics;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	
	public BangdanDetailAdapter(Context context, List<Music> musics) {
		this.context = context;
		this.musics = musics;
		this.inflater = LayoutInflater.from(context);
		imageLoader = new ImageLoader(DaDaApp.getQueue(), DaDaApp.getBitmapCache());
	}

	@Override
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
			convertView = inflater.inflate(R.layout.item_music_bangdan_detail, null);
			holder = new ViewHolder();
			holder.ivPic = (ImageView) convertView.findViewById(R.id.ivPic);
			holder.ivMask = (ImageView) convertView.findViewById(R.id.ivMask);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvSinger = (TextView) convertView.findViewById(R.id.tvSinger);
			holder.tvRank = (TextView) convertView.findViewById(R.id.tvRank);
			convertView.setTag(holder);
		}
		holder=(ViewHolder) convertView.getTag();
		//更新view
		Music m = getItem(position);
		//更新标题  歌手名
		holder.tvTitle.setText(m.getTitle());
		holder.tvSinger.setText(m.getAuthor());
		//更新排名
		holder.tvRank.setText((position+1)<10 ? "0"+(position+1) : (position+1)+"");
		//更新遮罩层
		switch (position) {
		case 0:
			holder.ivMask.setImageResource(R.drawable.img_king_mask01);
			break;
		case 1:
			holder.ivMask.setImageResource(R.drawable.img_king_mask02);
			break;
		case 2:
			holder.ivMask.setImageResource(R.drawable.img_king_mask03);
			break;
		default:
			holder.ivMask.setImageResource(R.drawable.img_king_mask1);
			break;
		}
		//更新图片
		ImageListener listener = ImageLoader.getImageListener(holder.ivPic, R.drawable.music_item_default, R.drawable.music_item_default);
		imageLoader.get(m.getPic_small(), listener);
		return convertView;
	}
	
	class ViewHolder{
		ImageView ivMask;
		TextView tvRank;
		TextView tvTitle;
		TextView tvSinger;
		ImageView ivPic;
	}
	
}


