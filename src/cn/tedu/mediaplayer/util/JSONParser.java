package cn.tedu.mediaplayer.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.tedu.mediaplayer.entity.Bangdan;
import cn.tedu.mediaplayer.entity.BangdanDetail;
import cn.tedu.mediaplayer.entity.Billboard;
import cn.tedu.mediaplayer.entity.Gedan;
import cn.tedu.mediaplayer.entity.Music;
import cn.tedu.mediaplayer.entity.SongInfo;
import cn.tedu.mediaplayer.entity.SongUrl;


/** json解析工具类 */
public class JSONParser {
	/**
	 * 解析json字符串  得到榜单集合
	 * @param ary
	 * @return List<Bangdan>
	 * @throws JSONException 
	 */
	public static List<Bangdan> parseBangdanList(JSONArray ary) throws JSONException {
		List<Bangdan> bangdans = new ArrayList<Bangdan>();
		for(int i=0; i<ary.length(); i++){
			JSONObject o = ary.getJSONObject(i);
			Bangdan bd = new Bangdan(
					o.getInt("type"),
					o.getInt("count"), 
					o.getString("name"), 
					o.getString("comment"), 
					o.getString("web_url"), 
					o.getString("pic_s192"), 
					o.getString("pic_s444"), 
					o.getString("pic_s260"), 
					o.getString("pic_s210"), 
					null
			);
			//解析content json数组
			JSONArray array = o.getJSONArray("content");
			List<Music> ms = new ArrayList<Music>();
			for(int j=0; j<array.length(); j++){
				JSONObject musicObj = array.getJSONObject(j);
				Music music = null;
				try{
					music= new Music();
					music.setTitle(musicObj.getString("title"));
					music.setAuthor(musicObj.getString("author"));
					music.setSong_id(musicObj.getString("song_id"));
					music.setAlbum_title(musicObj.getString("album_title"));
				}catch(JSONException e){
				}
				ms.add(music);
			}
			bd.setContent(ms);
			bangdans.add(bd);
		}
		return bangdans;
	}

	/**
	 * 解析歌单jsonarray  得到List<Gedan>
	 * @param ary 
	 * [{},{},{},{},{}......]
	 * @return
	 * @throws JSONException 
	 */
	public static List<Gedan> parseGedanList(JSONArray ary) throws JSONException {
		List<Gedan> gedans = new ArrayList<Gedan>();
		for(int i=0; i<ary.length(); i++){
			JSONObject o = ary.getJSONObject(i);
			Gedan g = new Gedan(
					o.getString("listid"), 
					o.getString("listenum"), 
					o.getString("collectnum"), 
					o.getString("title"), 
					o.getString("pic_300"), 
					o.getString("tag"), 
					o.getString("desc"), 
					o.getString("pic_w300"), 
					o.getString("width"), 
					o.getString("height"), 
					null);
			JSONArray array = o.getJSONArray("songIds");
			List<String> ids = new ArrayList<String>();
			for(int j=0; j<array.length(); j++){
				ids.add(array.getString(j));
			}
			g.setSongIds(ids);
			gedans.add(g);
		}
		return gedans;
	}

	/**
	 * 解析获取一个榜单详情对象
	 * @param json { song_list:[{},{},{},{}],  billboard:{} }
	 * @return
	 * @throws JSONException 
	 */
	public static BangdanDetail parseBangdanDetail(String json) throws JSONException {
		BangdanDetail bangdan = new BangdanDetail();
		JSONObject o = new JSONObject(json);
		//解析音乐列表
		JSONArray songAry = o.getJSONArray("song_list");
		List<Music> musics= new ArrayList<Music>();
		for(int i=0; i<songAry.length(); i++){
			JSONObject obj = songAry.getJSONObject(i);
			Music music = new Music(
					obj.getInt("file_duration"), 
					obj.getString("artist_id"), 
					obj.getString("pic_big"), 
					obj.getString("pic_small"), 
					obj.getString("publishtime"), 
					obj.getString("lrclink"), 
					obj.getString("title"), 
					obj.getString("song_id"), 
					obj.getString("author"), 
					obj.getString("album_title"), 
					obj.getString("album_id"), 
					obj.getString("artist_name"));
			musics.add(music);
		}
		bangdan.setMusics(musics);
		//解析billboard  基本信息
		JSONObject billObj = o.getJSONObject("billboard");
		Billboard billboard = new Billboard(
				billObj.getString("billboard_type"), 
				billObj.getString("billboard_no"), 
				billObj.getString("update_date"), 
				billObj.getString("billboard_songnum"), 
				billObj.getString("name"), 
				billObj.getString("comment"), 
				billObj.getString("pic_s640"), 
				billObj.getString("pic_s444"), 
				billObj.getString("pic_s260"), 
				billObj.getString("pic_s210"), 
				billObj.getString("web_url"));
		bangdan.setBillboard(billboard);
		return bangdan;
	}

	/***
	 * 解析songurl 的 json数组
	 * @param urlAry  [{},{},{},{}]
	 * @return
	 * @throws JSONException 
	 */
	public static List<SongUrl> parseSongUrlArray(JSONArray urlAry) throws JSONException {
		List<SongUrl> urls = new ArrayList<SongUrl>();
		for(int i=0; i<urlAry.length(); i++){
			JSONObject o = urlAry.getJSONObject(i);
			SongUrl url = new SongUrl(
					o.getInt("file_size"), 
					o.getInt("file_duration"), 
					o.getInt("file_bitrate"), 
					o.getString("show_link"), 
					o.getString("file_extension"), 
					o.getString("file_link"), 
					o.getString("hash"));
			urls.add(url);
		}
		return urls;
	}

	/**
	 * 封装SongInfo对象
	 * @param infoObj
	 * @return
	 * @throws JSONException 
	 */
	public static SongInfo parseSongInfo(JSONObject infoObj) throws JSONException {
		SongInfo info = new SongInfo(
				infoObj.getString("pic_huge"),
				infoObj.getString("album_1000_1000"),
				infoObj.getString("pic_singer"),
				infoObj.getString("album_500_500"),
				infoObj.getString("compose"),
				infoObj.getString("artist_500_500"),
				infoObj.getString("file_duration"),
				infoObj.getString("album_title"),
				infoObj.getString("title"),
				infoObj.getString("pic_radio"),
				infoObj.getString("language"),
				infoObj.getString("lrclink"),
				infoObj.getString("pic_big"),
				infoObj.getString("pic_premium"),
				infoObj.getString("artist_480_800"),
				infoObj.getString("artist_1000_1000"),
				infoObj.getString("artist_640_1136"),
				infoObj.getString("publishtime"),
				infoObj.getString("songwriting"),
				infoObj.getString("share_url"),
				infoObj.getString("author"),
				infoObj.getString("pic_small"),
				infoObj.getString("song_id")
				);
		return info;
	}

	/***
	 * 通过关键字查询的建议列表 解析封装成字符串集合
	 * @param response
	 * @return
	 * @throws JSONException 
	 */
	public static List<String> parseSearchSuggest(String response) throws JSONException {
		List<String> data = new ArrayList<String>();
		JSONObject obj = new JSONObject(response);
		//解析歌手列表
		if(obj.has("artist")){  //解析歌手信息
			JSONArray ary = obj.getJSONArray("artist");
			for(int i=0; i<ary.length() && i<4; i++){
				JSONObject o = ary.getJSONObject(i);
				data.add(o.getString("artistname"));
			}
		}
		//解析歌曲列表
		if(obj.has("song")){ //解析歌曲列表
			JSONArray ary = obj.getJSONArray("song");
			for(int i=0; i<ary.length() && i<4; i++){
				JSONObject o = ary.getJSONObject(i);
				data.add(o.getString("songname")+"-"+o.getString("artistname"));
			}
		}
		//解析专辑列表
		if(obj.has("album")){ //解析专辑列表
			JSONArray ary = obj.getJSONArray("album");
			for(int i=0; i<ary.length() && i<4; i++){
				JSONObject o = ary.getJSONObject(i);
				data.add(o.getString("albumname"));
			}
		}
		
		return data;
	}
	
	/**
	 * 解析jsonary  获取List<Music>
	 * @param ary
	 * @return
	 * @throws JSONException 
	 */
	public static List<Music> parseMusicList(JSONArray ary) throws JSONException {
		List<Music> musics = new ArrayList<Music>();
		for(int i=0; i<ary.length(); i++){
			JSONObject o = ary.getJSONObject(i);
			Music music = new Music();
			music.setTitle(o.getString("title"));
			music.setSong_id(o.getString("song_id"));
			music.setAuthor(o.getString("author"));
			music.setArtist_id(o.getString("artist_id"));
			music.setAlbum_title(o.getString("album_title"));
			musics.add(music);
		}
		return musics;
	}

}








