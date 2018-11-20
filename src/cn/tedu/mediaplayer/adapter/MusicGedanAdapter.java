package cn.tedu.mediaplayer.adapter;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.app.DaDaApp;
import cn.tedu.mediaplayer.entity.Gedan;
import cn.tedu.mediaplayer.util.LruBitmapCache;

public class MusicGedanAdapter extends BaseAdapter{
	private Context context;
	private List<Gedan> gedans;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private boolean scrolling;
	
	public MusicGedanAdapter(Context context, List<Gedan> gedans) {
		this.context = context;
		this.gedans = gedans;
		this.inflater = LayoutInflater.from(context);
		imageLoader = new ImageLoader(DaDaApp.getQueue(), DaDaApp.getBitmapCache());
	}

	@Override
	public int getCount() {
		return gedans.size();
	}

	@Override
	public Gedan getItem(int position) {
		return gedans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_music_gedan, null);
			holder = new ViewHolder();
			holder.ivPic = (ImageView) convertView.findViewById(R.id.ivPic);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.radioListen = (RadioButton) convertView.findViewById(R.id.radioListen);
			convertView.setTag(holder);
		}
		holder=(ViewHolder) convertView.getTag();
		//控件赋值
		Gedan gedan = getItem(position);
		holder.radioListen.setText(gedan.getListenum());
		holder.tvTitle.setText(gedan.getTitle());
		//设置图片
		if(scrolling){
			//最好先去volley的内存缓存中找找 看看有没有这张bitmap
			//有的话就直接用了
			LruBitmapCache cache = DaDaApp.getBitmapCache();
			String url = gedan.getPic_300();
			String key = new StringBuilder(url.length() + 12).append("#W").append(0)
	                .append("#H").append(0).append(url).toString();
			Bitmap bitmap=cache.getMemoryCacheBitmap(key);
			if(bitmap!=null){
				holder.ivPic.setImageBitmap(bitmap);
			}else{
				holder.ivPic.setImageResource(R.drawable.user_playlist_default_cd);
			}
		}else{
			ImageListener listener = ImageLoader.getImageListener(holder.ivPic, R.drawable.user_playlist_default_cd, R.drawable.user_playlist_default_cd);
			imageLoader.get(gedan.getPic_300(), listener);
		}
		return convertView;
	}

	public void setScrolling(boolean scrolling){
		this.scrolling = scrolling;
	}
	
	class ViewHolder{
		ImageView ivPic;
		TextView tvTitle;
		RadioButton radioListen;
	}
	
}





