package cn.tedu.mediaplayer.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import cn.tedu.mediaplayer.app.DaDaApp;
import cn.tedu.mediaplayer.entity.Music;
import cn.tedu.mediaplayer.entity.SongInfo;

public class DBHelper {
	private SQLiteOpenHelper dbHelper;
	private SQLiteDatabase db;
	private static DBHelper helper;

	public static DBHelper getInstance(){
		if(helper == null){
			helper = new DBHelper(DaDaApp.getApp());
		}
		return helper;
	}

	private DBHelper(Context context) {
		dbHelper = new SqliteHelper(context, "LOCAL_MUSIC_DB", null, 1);
		db = dbHelper.getWritableDatabase();
	}

	/*** 保存关键字 */
	public void saveKeyword(String keyword){
		//先查一下  以前这个关键字有没有被查询过
		Cursor cursor = db.query("KEYWORDS", null, "keyword=?", new String[]{keyword}, null, null, null);
		if(cursor.moveToFirst()){
			ContentValues values = new ContentValues();
			values.put("time",System.currentTimeMillis());
			db.update("KEYWORDS", values, "keyword=?", new String[]{keyword});
			cursor.close();
			return;
		}
		cursor.close();;
		//没有查到  则添加新的关键字
		ContentValues values = new ContentValues();
		values.put("keyword", keyword);
		values.put("time", System.currentTimeMillis());
		db.insert("KEYWORDS", "_id", values);
	}

	/** 查询所有关键字  按时间倒序排序 */
	public List<String> findAllKeywords(){
		List<String> keys = new ArrayList<String>();
		String[] columns = {
				"keyword"
		};
		Cursor cursor = db.query("KEYWORDS",columns,null, null, null, null, "time desc");
		while(cursor.moveToNext()){
			keys.add(cursor.getString(0));
		}
		cursor.close();
		return keys;
	}

	/** 把音乐基本信息缓存到数据库中 */
	public void reSave(Music music){
		//删掉原来的
		db.delete("MUSIC", "title=? and author=?", new String[]{music.getTitle(), music.getAuthor()});
		//保存新建的
		ContentValues values = new ContentValues();
		values.put("song_id", music.getSong_id());
		values.put("title", music.getSongInfo().getTitle());
		values.put("author", music.getSongInfo().getAuthor());
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "dada/"+music.getTitle()+".mp3");
		values.put("filepath", file.getAbsolutePath());
		values.put("album_500_500", music.getSongInfo().getAlbum_500_500());
		values.put("lrclink", music.getSongInfo().getLrclink());
		values.put("duration", music.getSongUrls().get(0).getFile_duration());
		values.put("pic_small", music.getSongInfo().getPic_small());
		values.put("album_title", music.getSongInfo().getAlbum_title());
		values.put("add_time", System.currentTimeMillis());
		db.insert("MUSIC", "_id", values);
	}
	
	//查询数据库中的所有歌曲
	public List<Music> findAll(){
		List<Music> musics = new ArrayList<Music>();
		String[] columns = new String[]{
			"_id",
			"song_id",
			"title",
			"author",
			"filepath",
			"album_500_500",
			"lrclink",
			"duration",
			"pic_small",
			"album_title"
		};
		Cursor cursor = db.query("MUSIC", columns , null, null, null, null, null, null);
		while(cursor.moveToNext()){
			Music music = new Music();
			SongInfo info = new SongInfo();
			music.setSong_id(cursor.getString(1));
			info.setSong_id(cursor.getString(1));
			music.setTitle(cursor.getString(2));
			info.setTitle(cursor.getString(2));
			music.setAuthor(cursor.getString(3));
			info.setAuthor(cursor.getString(3));
			music.setFilepath(cursor.getString(4));
			info.setAlbum_500_500(cursor.getString(5));
			music.setLrclink(cursor.getString(6));
			info.setLrclink(cursor.getString(6));
			music.setPic_small(cursor.getString(8));
			info.setPic_small(cursor.getString(8));
			music.setAlbum_title(cursor.getString(9));
			info.setAlbum_title(cursor.getString(9));
			music.setSongInfo(info);
			musics.add(music);
		}
		cursor.close();
		return musics;
	}
	
}







