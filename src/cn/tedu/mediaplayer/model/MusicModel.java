package cn.tedu.mediaplayer.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore.Audio.Media;

import cn.tedu.mediaplayer.app.DaDaApp;
import cn.tedu.mediaplayer.dao.DBHelper;
import cn.tedu.mediaplayer.entity.Bangdan;
import cn.tedu.mediaplayer.entity.BangdanDetail;
import cn.tedu.mediaplayer.entity.Gedan;
import cn.tedu.mediaplayer.entity.Lyrics;
import cn.tedu.mediaplayer.entity.Music;
import cn.tedu.mediaplayer.entity.SongInfo;
import cn.tedu.mediaplayer.entity.SongUrl;
import cn.tedu.mediaplayer.util.JSONParser;
import cn.tedu.mediaplayer.util.URLFactory;

/** ������ص�ҵ���� */
public class MusicModel {

	/** �������ݿ��еĹؼ����б�  ��ʱ�䵹������ */
	public List<String> loadSearchKeysList() {
		DBHelper db = DBHelper.getInstance();
		return db.findAllKeywords();
	}

	/**
	 * ͨ���ؼ��� ��ѯ�����б�
	 * @param keyword
	 * @param callback
	 */
	public void searchMusicListByKeyword(final String keyword, final MusicListCallback callback){
		RequestQueue queue = DaDaApp.getQueue();
		String url = URLFactory.getSearchMusicUrl(keyword);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			public void onResponse(String response) {
				try {
					JSONObject obj = new JSONObject(response);
					JSONArray ary = obj.getJSONArray("song_list");
					List<Music> musics = JSONParser.parseMusicList(ary);
					callback.onMusicListLoaded(musics);
					//�ж��Ƿ��ܲ������  ������� ��ѹؼ��ֱ��浽���ݿ��С�
					if(!musics.isEmpty()){
						DBHelper helper = DBHelper.getInstance();
						helper.saveKeyword(keyword);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			public void onErrorResponse(VolleyError error) {
			}
		});
		queue.add(request);
	}
	
	/** �ѹؼ�����ӵ���ʷ��ѯ��¼�� */
	private void addQueryHistory(String keyword){
		SharedPreferences pref = DaDaApp.getApp().getSharedPreferences("QUERY_HISTORY", 0);
		Editor editor = pref.edit();
		editor.putString(keyword, System.currentTimeMillis()+"");
		editor.commit();
	}
	
	/***
	 * ͨ���ؼ��ֲ�ѯ���������ı��б�
	 * @param keyword
	 * @param callback
	 */
	public void loadSuggestByKeyword(String keyword, final SearchSuggestCallback callback){
		RequestQueue queue = DaDaApp.getQueue();
		String url = URLFactory.getSearchSuggestUrl(keyword);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			public void onResponse(String response) {
				try {
					List<String> data = JSONParser.parseSearchSuggest(response);
					callback.onSuggestLoaded(data);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			public void onErrorResponse(VolleyError error) {
			}
		});
		queue.add(request);
	}
	
	/**
	 * ͨ��·�����ظ�ʶ���
	 * @param url
	 * @param callback
	 */
	public void loadLyrics(String url, final LyricsCallback callback){
		if(url==null || url.equals("")){ //û�и��
			callback.onLyricsLoaded(null);
			return;
		}
		//����ָ�򻺴����ļ���File����
		String filename = url.substring(url.lastIndexOf("/"));
		final File file = new File(DaDaApp.getApp().getCacheDir(), "lrc"+filename);
		if(file.exists()){ //�����ļ��Ѵ���  ���������ع���
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				StringBuffer sb = new StringBuffer();
				String line = null;
				while((line = reader.readLine())!=null){
					sb.append(line+"\n");
				}
				Lyrics lrc = parseLyrics(sb.toString());
				callback.onLyricsLoaded(lrc);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//���������û�и���ļ�  ��ȥ���ظ��
		RequestQueue queue = DaDaApp.getQueue();
		StringRequest request = new StringRequest(url, new Listener<String>() {
			public void onResponse(String response) {
				try {
					response = new String(response.getBytes("ISO-8859-1"), "utf-8");
					//��response���뻺���ļ���
					if(!file.getParentFile().exists()){
						file.getParentFile().mkdirs();
					}
					PrintWriter out = new PrintWriter(file);
					out.write(response);
					out.flush();
					out.close();
					Lyrics lyrics = parseLyrics(response);
					callback.onLyricsLoaded(lyrics);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			public void onErrorResponse(VolleyError error) {
			}
		});
		queue.add(request);
	}
	
	/**  �Ѹ���ı������ɸ�ʶ��� 
	 * @throws IOException */
	private Lyrics parseLyrics(String lrc) throws IOException{
		BufferedReader reader = new BufferedReader(new StringReader(lrc));
		String line = null;
		Lyrics lyrics = new Lyrics();
		while((line=reader.readLine())!=null){
			lyrics.addLine(line);
		}
		return lyrics;
	}
	
	/**
	 * ͨ��songId ���� ���׸�Ļ�����Ϣ
	 * @param songId
	 * @param callback
	 *   	��callback�ķ�ʽ  ��List<SongUrl>  SongInfo��
	 * 		���������ߣ��ص�������ִ�к���ҵ��
	 */
	public void loadSongInfoBySongId(String songId, final SongInfoCallback callback){
		RequestQueue queue = DaDaApp.getQueue();
		String url = URLFactory.getSongInfoUrl(songId);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			public void onResponse(String response) {
				try {
					JSONObject o = new JSONObject(response);
					JSONArray urlAry = o.getJSONObject("songurl").getJSONArray("url");
					JSONObject infoObj = o.getJSONObject("songinfo");
					List<SongUrl> urls = JSONParser.parseSongUrlArray(urlAry);
					SongInfo info = JSONParser.parseSongInfo(infoObj);
					callback.onSongInfoLoaded(urls, info);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			public void onErrorResponse(VolleyError error) {
			}
		});
		queue.add(request);
	}
	
	/**
	 * ������Ӧtype�İ�����
	 * @param type
	 * @param callback
	 */
	public void loadBangdanDetail(int type, final BangdanDetailCallback callback){
		RequestQueue queue = DaDaApp.getQueue();
		String url = URLFactory.getBangdanDetailUrl(type);
		StringRequest request = new StringRequest(url,
				new Listener<String>() {
					public void onResponse(String json) {
						try {
							BangdanDetail bangdan = JSONParser.parseBangdanDetail(json);
							callback.onBangdanDataLoaded(bangdan);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						
					}
				});
		queue.add(request);
	}
	
	/**
	 * ���ظ赥�б�����
	 */
	public void loadGedanList(final GedanListCallback callback){
		RequestQueue queue = DaDaApp.getQueue();
		String url = URLFactory.getGedanListUrl();
		StringRequest request = new StringRequest(url, 
				new Listener<String>() {
					public void onResponse(String json) {
						try {
							JSONObject obj = new JSONObject(json);
							JSONArray ary = obj.getJSONArray("content");
							List<Gedan> gedans = JSONParser.parseGedanList(ary);
							callback.onGedanListLoaded(gedans);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					public void onErrorResponse(VolleyError error) {
					}
				});
		queue.add(request);
	}
	
	/**
	 * ���ذ��б�
	 * ����http���󣬻�ȡ���б�����  json��ʽ
	 * ����json  ��װʵ������� �õ�:List<Bangdan>
	 */
	public void loadBangdanList(final BangdanListCallback callback){
		RequestQueue queue = DaDaApp.getQueue();
		//StringRequest
		String url = URLFactory.getBangdanListUrl();
		StringRequest request = new StringRequest(
				url, new Listener<String>() {
					//��Ӧ���غ�  ���߳���ִ�и÷���
					public void onResponse(String json) {
						try {
							JSONObject obj = new JSONObject(json);
							JSONArray ary = obj.getJSONArray("content");
							List<Bangdan> bangdans = JSONParser.parseBangdanList(ary);
							//����callback�ķ���  �Ѻ���ҵ�񽻸�callback
							callback.onBangdanListLoaded(bangdans);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					//������ʧ��  ���߳���ִ�и÷���
					public void onErrorResponse(VolleyError error) {
					}
				});
		queue.add(request);
	}

	/***
	 * ���ر��������б� ��Ҫ��ѯϵͳContentProvider  �������Ӧ�ó������ݿ����л�������ݵĻ� ��Ҫͬ����װ����
	 * @param musicListCallback
	 */
	public void loadLocalMusicList(final MusicListCallback musicListCallback) {
		AsyncTask<String, String, List<Music>> task = new AsyncTask<String, String, List<Music>>(){
			protected List<Music> doInBackground(String... params) {
				List<Music> musics = loadMediaContentProvider();
				DBHelper helper = DBHelper.getInstance();
				List<Music> dbMusics = helper.findAll();
				for(int i=0; i<musics.size(); i++){
					Music m = musics.get(i);
					for(int j=0; j<dbMusics.size(); j++){
						Music m2 = dbMusics.get(j);
						if(m.getTitle().equals(m2.getTitle())&& m.getAuthor().equals(m2.getAuthor())){
							musics.set(i, m2);
						}
					}
				}
				return musics;
			}
			protected void onPostExecute(List<Music> result) {
				musicListCallback.onMusicListLoaded(result);
			}
		};
		task.execute();
		
	}
	
	
	private List<Music> loadMediaContentProvider(){
		ContentResolver r = DaDaApp.getApp().getContentResolver();
		Uri uri = Media.EXTERNAL_CONTENT_URI;
		String[] projection = new String[]{
				Media.TITLE,
				Media.ARTIST,
				Media.DATA,
				Media.DURATION,
				Media.ALBUM
		};
		Cursor c = r.query(uri, projection , Media.DURATION+">200*1000", null, Media._ID+" desc");
		List<Music> musics = new ArrayList<Music>();
		while(c.moveToNext()){
			Music m = new Music();
			m.setTitle(c.getString(0));
			m.setAuthor(c.getString(1));
			m.setFilepath(c.getString(2));
			m.setFile_duration(c.getInt(3));
			m.setAlbum_title(c.getString(4));
			SongInfo info = new SongInfo();
			info.setTitle(m.getTitle());
			info.setAuthor(m.getAuthor());
			info.setAlbum_title(m.getAlbum_title());
			m.setSongInfo(info);
			musics.add(m);
		}
		c.close();
		return musics;
	}

	/***
	 * ͨ�����������������ѯͼƬ�б�
	 * @param name
	 * @param author
     */
	public void searchPicsBySongNameAndAuthor(String name, String author, final SearchMatchPicListCallback callback ) {
		if(name.equals("")){
			return;
		}
		String url = URLFactory.getMatchLrcUrl(name, author);

		StringRequest req = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String s) {

			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {

			}
		});
		DaDaApp.getApp().getQueue().add(req);
	}
}








