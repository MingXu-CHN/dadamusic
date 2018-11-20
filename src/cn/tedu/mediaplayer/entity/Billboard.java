package cn.tedu.mediaplayer.entity;

/***
 * 封装榜单的基本属性信息
 */
public class Billboard {
	private String billboard_type;
	private String billboard_no;
	private String update_date;
	private String billboard_songnum;
	private String name;
	private String comment;
	private String pic_s640;
	private String pic_s444;
	private String pic_s260;
	private String pic_s210;
	private String web_url;

	public Billboard() {
		// TODO Auto-generated constructor stub
	}

	public Billboard(String billboard_type, String billboard_no, String update_date, String billboard_songnum,
			String name, String comment, String pic_s640, String pic_s444, String pic_s260, String pic_s210,
			String web_url) {
		super();
		this.billboard_type = billboard_type;
		this.billboard_no = billboard_no;
		this.update_date = update_date;
		this.billboard_songnum = billboard_songnum;
		this.name = name;
		this.comment = comment;
		this.pic_s640 = pic_s640;
		this.pic_s444 = pic_s444;
		this.pic_s260 = pic_s260;
		this.pic_s210 = pic_s210;
		this.web_url = web_url;
	}

	public String getBillboard_type() {
		return billboard_type;
	}

	public void setBillboard_type(String billboard_type) {
		this.billboard_type = billboard_type;
	}

	public String getBillboard_no() {
		return billboard_no;
	}

	public void setBillboard_no(String billboard_no) {
		this.billboard_no = billboard_no;
	}

	public String getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

	public String getBillboard_songnum() {
		return billboard_songnum;
	}

	public void setBillboard_songnum(String billboard_songnum) {
		this.billboard_songnum = billboard_songnum;
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

	public String getPic_s640() {
		return pic_s640;
	}

	public void setPic_s640(String pic_s640) {
		this.pic_s640 = pic_s640;
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

	public String getWeb_url() {
		return web_url;
	}

	public void setWeb_url(String web_url) {
		this.web_url = web_url;
	}

}
