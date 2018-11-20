package cn.tedu.mediaplayer.adapter;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.app.DaDaApp;
import cn.tedu.mediaplayer.entity.Bangdan;
import cn.tedu.mediaplayer.util.LruBitmapCache;

public class MusicBangdanAdapter extends BaseAdapter{
	private Context context;
	private List<Bangdan> bangdans;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	
	public MusicBangdanAdapter(Context context, List<Bangdan> bangdans) {
		this.context = context;
		this.bangdans = bangdans;
		this.inflater = LayoutInflater.from(context);
		//构造方法仅执行一次   创建ImageLoader图片加载器
		imageLoader = new ImageLoader(DaDaApp.getQueue(), DaDaApp.getBitmapCache());
	}

	@Override
	public int getCount() {
		return bangdans.size();
	}

	@Override
	public Bangdan getItem(int position) {
		return bangdans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_music_bangdan, null);
			holder = new ViewHolder();
			holder.ivPic = (ImageView) convertView.findViewById(R.id.ivPic);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvFirst = (TextView) convertView.findViewById(R.id.tvFirst);
			holder.tvSecond = (TextView) convertView.findViewById(R.id.tvSecond);
			holder.tvThird = (TextView) convertView.findViewById(R.id.tvThird);
			convertView.setTag(holder);
		}
		holder=(ViewHolder) convertView.getTag();
		//给控件赋值
		Bangdan bangdan = getItem(position);
		holder.tvName.setText(bangdan.getName());
		holder.tvFirst.setText(bangdan.getContent().get(0).getTitle()+"-"+bangdan.getContent().get(0).getAuthor());
		holder.tvSecond.setText(bangdan.getContent().get(1).getTitle()+"-"+bangdan.getContent().get(1).getAuthor());
		holder.tvThird.setText(bangdan.getContent().get(2).getTitle()+"-"+bangdan.getContent().get(2).getAuthor());
		//设置图片内容  图片需要从服务端下载
		ImageListener listener = ImageLoader.getImageListener(holder.ivPic, R.drawable.music_item_default, R.drawable.music_item_default);
		imageLoader.get(bangdan.getPic_s192(), listener);
		return convertView;
	}
	
	class ViewHolder{
		ImageView ivPic;
		TextView tvName;
		TextView tvFirst;
		TextView tvSecond;
		TextView tvThird;
	}
	
}





