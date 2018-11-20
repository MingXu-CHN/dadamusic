package cn.tedu.mediaplayer.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.tedu.mediaplayer.R;

/** �������������б����� */
public class SuggestAdapter extends BaseAdapter{
	private Context context;
	private List<String> suggestData;
	private LayoutInflater inflater;
	
	public SuggestAdapter(Context context, List<String> suggestData) {
		this.context = context;
		this.suggestData = suggestData;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return suggestData.size();
	}

	@Override
	public String getItem(int position) {
		return suggestData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_search_suggest, null);
			holder = new ViewHolder();
			holder.textView = (TextView) convertView.findViewById(R.id.tvData);
			convertView.setTag(holder);
		}
		holder=(ViewHolder) convertView.getTag();
		//���Ը�ֵ
		holder.textView.setText(getItem(position));
		return convertView;
	}
	
	class ViewHolder{
		TextView textView;
	}
	
}






