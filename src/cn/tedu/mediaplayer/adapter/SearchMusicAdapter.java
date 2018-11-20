package cn.tedu.mediaplayer.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.entity.Music;
import cn.tedu.mediaplayer.entity.SongInfo;
import cn.tedu.mediaplayer.entity.SongUrl;
import cn.tedu.mediaplayer.listener.OnDownloadPressListener;
import cn.tedu.mediaplayer.model.MusicModel;
import cn.tedu.mediaplayer.model.SongInfoCallback;
import cn.tedu.mediaplayer.service.DownloadService;
import cn.tedu.mediaplayer.ui.DownloadPopWindow;

public class SearchMusicAdapter extends BaseAdapter{
	private Context context;
	private List<Music> musics;
	private LayoutInflater inflater;
	private ListView listView;
	private MusicModel model;
	
	public SearchMusicAdapter(Context context, List<Music> musics, ListView listView) {
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
			convertView = inflater.inflate(R.layout.item_search_music, null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvSinger = (TextView) convertView.findViewById(R.id.tvSinger);
			holder.btnDownload = (ImageButton) convertView.findViewById(R.id.btnDownload);
			convertView.setTag(holder);
		}
		holder=(ViewHolder) convertView.getTag();
		final Music music = getItem(position);
		holder.tvTitle.setText(Html.fromHtml(music.getTitle()));
		holder.tvSinger.setText(Html.fromHtml(music.getAuthor()+" - "+music.getAlbum_title()));
		holder.btnDownload.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//先通过歌曲的song_id  查询歌曲的url集合 然后执行后续popwindow操作
				model.loadSongInfoBySongId(music.getSong_id(), new SongInfoCallback() {
					public void onSongInfoLoaded(List<SongUrl> urls, SongInfo info) {
						music.setSongUrls(urls);
						music.setSongInfo(info);
						//弹出下载框  popwindow
						//为了不影响 原始url集合的顺序，需要重新创建一个list集合  进行操作
						List<SongUrl> copyUrls = new ArrayList<SongUrl>();
						copyUrls.addAll(urls);
						//执行排序操作
						Collections.sort(copyUrls, new Comparator<SongUrl>() {
							public int compare(SongUrl lhs, SongUrl rhs) {
								return -lhs.getFile_size()+rhs.getFile_size();
							}
						});
						//构建需要传递给popwindow的url集合
						List<SongUrl> songUrls = new ArrayList<SongUrl>();
						for(int i=0; i<copyUrls.size(); i++){
							songUrls.add(copyUrls.get(i));
						}
						DownloadPopWindow popWindow = new DownloadPopWindow((Activity)context, songUrls, new OnDownloadPressListener() {
							public void onDownloadPressed(SongUrl url) {
								//获取用户选中的url对象
								String filelink = url.getFile_link();
								//执行下载  启动Service
								Log.i("info", ""+filelink);
								Intent intent = new Intent(context, DownloadService.class);
								intent.putExtra("filelink", filelink);
								intent.putExtra("title", music.getTitle());
								intent.putExtra("total", url.getFile_size());
								intent.putExtra("music", music);
								context.startService(intent);
							}
						});
						popWindow.showAtLocation(listView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
					}
				});
			}
		});
		return convertView;
	}

	class ViewHolder{
		TextView tvTitle;
		TextView tvSinger;
		ImageButton btnDownload;
	}
	
}
