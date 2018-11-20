package cn.tedu.mediaplayer.entity;

import java.util.List;

/** 封装歌单数据 */
public class Gedan {
	private String listid;
	private String listenum;
	private String collectnum;
	private String title;
	private String pic_300;
	private String tag;
	private String desc;
	private String pic_w300;
	private String width;
	private String height;
	private List<String> songIds;

	public Gedan() {
		// TODO Auto-generated constructor stub
	}

	public Gedan(String listid, String listenum, String collectnum, String title, String pic_300, String tag,
			String desc, String pic_w300, String width, String height, List<String> songIds) {
		super();
		this.listid = listid;
		this.listenum = listenum;
		this.collectnum = collectnum;
		this.title = title;
		this.pic_300 = pic_300;
		this.tag = tag;
		this.desc = desc;
		this.pic_w300 = pic_w300;
		this.width = width;
		this.height = height;
		this.songIds = songIds;
	}

	public String getListid() {
		return listid;
	}

	public void setListid(String listid) {
		this.listid = listid;
	}

	public String getListenum() {
		return listenum;
	}

	public void setListenum(String listenum) {
		this.listenum = listenum;
	}

	public String getCollectnum() {
		return collectnum;
	}

	public void setCollectnum(String collectnum) {
		this.collectnum = collectnum;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPic_300() {
		return pic_300;
	}

	public void setPic_300(String pic_300) {
		this.pic_300 = pic_300;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPic_w300() {
		return pic_w300;
	}

	public void setPic_w300(String pic_w300) {
		this.pic_w300 = pic_w300;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public List<String> getSongIds() {
		return songIds;
	}

	public void setSongIds(List<String> songIds) {
		this.songIds = songIds;
	}

}
