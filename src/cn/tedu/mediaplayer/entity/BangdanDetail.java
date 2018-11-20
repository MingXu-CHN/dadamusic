package cn.tedu.mediaplayer.entity;

import java.util.List;

/**
 * 封装完整的榜单信息
 */
public class BangdanDetail {
	private List<Music> musics;
	private Billboard billboard;

	public BangdanDetail() {
		// TODO Auto-generated constructor stub
	}

	public BangdanDetail(List<Music> musics, Billboard billboard) {
		super();
		this.musics = musics;
		this.billboard = billboard;
	}

	public List<Music> getMusics() {
		return musics;
	}

	public void setMusics(List<Music> musics) {
		this.musics = musics;
	}

	public Billboard getBillboard() {
		return billboard;
	}

	public void setBillboard(Billboard billboard) {
		this.billboard = billboard;
	}

}
