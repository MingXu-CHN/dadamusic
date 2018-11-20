package cn.tedu.mediaplayer.entity;

import java.util.List;

/** 描述一个榜单对象 */
public class Bangdan {
	private int type;
	private int count;
	private String name;
	private String comment;
	private String web_url;
	private String pic_s192;
	private String pic_s444;
	private String pic_s260;
	private String pic_s210;
	private List<Music> content;

	public Bangdan() {
		// TODO Auto-generated constructor stub
	}

	public Bangdan(int type, int count, String name, String comment, String web_url, String pic_s192, String pic_s444,
			String pic_s260, String pic_s210, List<Music> content) {
		super();
		this.type = type;
		this.count = count;
		this.name = name;
		this.comment = comment;
		this.web_url = web_url;
		this.pic_s192 = pic_s192;
		this.pic_s444 = pic_s444;
		this.pic_s260 = pic_s260;
		this.pic_s210 = pic_s210;
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getWeb_url() {
		return web_url;
	}

	public void setWeb_url(String web_url) {
		this.web_url = web_url;
	}

	public String getPic_s192() {
		return pic_s192;
	}

	public void setPic_s192(String pic_s192) {
		this.pic_s192 = pic_s192;
	}

	public String getPic_s444() {
		return pic_s444;
	}

	public void setPic_s444(String pic_s444) {
		this.pic_s444 = pic_s444;
	}

	public String getPic_s260() {
		return pic_s260;
	}

	public void setPic_s260(String pic_s260) {
		this.pic_s260 = pic_s260;
	}

	public String getPic_s210() {
		return pic_s210;
	}

	public void setPic_s210(String pic_s210) {
		this.pic_s210 = pic_s210;
	}

	public List<Music> getContent() {
		return content;
	}

	public void setContent(List<Music> content) {
		this.content = content;
	}

}
