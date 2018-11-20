package cn.tedu.mediaplayer.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/** url地址工厂 */
public class URLFactory {
	
	/** 获取歌单列表的url地址 */
	public static String getGedanListUrl() {
		String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.diy.gedan&page_no=1&page_size=50&from=ios&from=ios&version=5.8.3&cuid=511e7b5ebe3cb83b66008608b586c12220e6b5aa&channel=appstore&operator=0";
		return url;
	}
	
	/** 获取榜单列表的url地址 */
	public static String getBangdanListUrl() {
		String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billCategory&format=json&from=ios&kflag=2&from=ios&version=5.8.3&cuid=511e7b5ebe3cb83b66008608b586c12220e6b5aa&channel=appstore&operator=0";
		return url;
	}

	public static String getBangdanDetailUrl(int type) {
		String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billList&type="+type+"&format=json&offset=0&size=100&from=ios&fields=title,song_id,author,resource_type,havehigh,is_new,has_mv_mobile,album_title,ting_uid,album_id,charge,all_rate,mv_provider&from=ios&version=5.8.3&cuid=24fc6b080e10ed8522fbb16da65619eafdf1db28&channel=appstore&operator=0";
		return url;
	}

	/**
	 * 通过songId获取歌曲详细信息的url
	 * @param songId
	 * @return
	 */
	public static String getSongInfoUrl(String songId) {
		String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.song.getInfos&ts=1475164135&songid="+songId+"&nw=2&l2p=395.1&lpb=320000&ext=MP3&format=json&from=ios&usup=1&lebo=0&aac=0&ucf=4&vid=&res=1&e=FuQ105WnGRfnAYDe2H%2FYvCkKi8tA3QhyC6fmlIiYJHY%3D&channel=24fc6b080e10ed8522fbb16da65619eafdf1db28&cuid=appstore&from=ios&version=2.2.0";
		return url;
	}

	/**
	 * 获取搜索建议的url路径
	 * @param keyword  有可能是中文
	 * @return
	 */
	public static String getSearchSuggestUrl(String keyword) {
		try {
			keyword = URLEncoder.encode(keyword, "utf-8");
			String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.search.catalogSug&format=json&query="+keyword;
			return url;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 获取搜索音乐列表的url
	 * @param keyword
	 * @return
	 */
	public static String getSearchMusicUrl(String keyword) {
		try {
			keyword = URLEncoder.encode(keyword, "utf-8");
			String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.search.common&format=json&query="+keyword+"&page_no=1&page_size=50";
			return url;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getCheckUpdateUrl() {
		String url = "http://durh.tech/dadamusic/checkupdate.json";
		return url;
	}

	public static String getMatchLrcUrl(String name, String author) {
		String url = "";
		try {
			if(author.equals("")){
				name = URLEncoder.encode(name, "utf-8");
				url = "http://geci.me/api/lyric/"+name;
				return url;
			}else{
				name = URLEncoder.encode(name, "utf-8");
				author = URLEncoder.encode(author, "utf-8");
				url = "http://geci.me/api/lyric/"+name+"/"+author;
				return url;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return url;
	}
}
