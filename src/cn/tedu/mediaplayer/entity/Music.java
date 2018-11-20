package cn.tedu.mediaplayer.entity;

import java.io.Serializable;
import java.util.List;

/** ∑‚◊∞“Ù¿÷–≈œ¢ */
public class Music implements Serializable{
	private int file_duration;
	private String artist_id;
	private String pic_big;
	private String pic_small;
	private String publishtime;
	private String lrclink;
	private String title;
	private String song_id;
	private String author;
	private String album_title;
	private String album_id;
	private String artist_name;
	private String filepath;

	private List<SongUrl> songUrls;
	private SongInfo songInfo;

	public Music() {
		// TODO Auto-generated constructor stub
	}

	public Music(int file_duration, String artist_id, String pic_big, String pic_small, String publishtime,
			String lrclink, String title, String song_id, String author, String album_title, String album_id,
			String artist_name) {
		super();
		this.file_duration = file_duration;
		this.artist_id = artist_id;
		this.pic_big = pic_big;
		this.pic_small = pic_small;
		this.publishtime = publishtime;
		this.lrclink = lrclink;
		this.title = title;
		this.song_id = song_id;
		this.author = author;
		this.album_title = album_title;
		this.album_id = album_id;
		this.artist_name = artist_name;
	}

	public Music(String title, String song_id, String author, String album_title) {
		this.title = title;
		this.song_id = song_id;
		this.author = author;
		this.album_title = album_title;
	}

	public int getFile_duration() {
		return file_duration;
	}

	public void setFile_duration(int file_duration) {
		this.file_duration = file_duration;
	}

	public String getArtist_id() {
		return artist_id;
	}

	public void setArtist_id(String artist_id) {
		this.artist_id = artist_id;
	}

	public String getPic_big() {
		return pic_big;
	}

	public void setPic_big(String pic_big) {
		this.pic_big = pic_big;
	}

	public String getPic_small() {
		return pic_small;
	}

	public void setPic_small(String pic_small) {
		this.pic_small = pic_small;
	}

	public String getPublishtime() {
		return publishtime;
	}

	public void setPublishtime(String publishtime) {
		this.publishtime = publishtime;
	}

	public String getLrclink() {
		return lrclink;
	}

	public void setLrclink(String lrclink) {
		this.lrclink = lrclink;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSong_id() {
		return song_id;
	}

	public void setSong_id(String song_id) {
		this.song_id = song_id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAlbum_title() {
		return album_title;
	}

	public void setAlbum_title(String album_title) {
		this.album_title = album_title;
	}

	public String getAlbum_id() {
		return album_id;
	}

	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}

	public String getArtist_name() {
		return artist_name;
	}

	public void setArtist_name(String artist_name) {
		this.artist_name = artist_name;
	}

	public List<SongUrl> getSongUrls() {
		return songUrls;
	}

	public void setSongUrls(List<SongUrl> songUrls) {
		this.songUrls = songUrls;
	}

	public SongInfo getSongInfo() {
		return songInfo;
	}

	public void setSongInfo(SongInfo songInfo) {
		this.songInfo = songInfo;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

}
