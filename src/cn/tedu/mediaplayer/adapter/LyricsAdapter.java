package cn.tedu.mediaplayer.adapter;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.entity.LrcLine;
import cn.tedu.mediaplayer.entity.Lyrics;

/** ∏Ë¥   ≈‰∆˜ */
public class LyricsAdapter extends BaseAdapter{
	private Context context;
	private Lyrics lyrics;
	private LayoutInflater inflater;
	private int currentPosition;
	
	public LyricsAdapter(Context context, Lyrics lyrics) {
		this.context = context;
		this.lyrics = lyrics;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return lyrics.getLines().size();
	}

	@Override
	public LrcLine getItem(int position) {
		return lyrics.getLines().get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void setCurrentPosition(int position){
		this.currentPosition = position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_lyrics, null);
			holder = new ViewHolder();
			holder.textView = (TextView) convertView.findViewById(R.id.textView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		//øÿº˛∏≥÷µ
		LrcLine line = getItem(position);
		holder.textView.setText(line.getContent());
		if(currentPosition == position){ //∏ﬂ¡¡œ‘ æitem
			holder.textView.setTextColor(Color.BLACK);
			holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		}else{
			holder.textView.setTextColor(Color.DKGRAY);
			holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		}
		return convertView;
	}
	
	class ViewHolder{
		TextView textView;
	}
	
}





