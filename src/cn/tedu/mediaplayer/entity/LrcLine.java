package cn.tedu.mediaplayer.entity;

/** √Ë ˆ∏Ë¥ –– */
public class LrcLine {
	private String time;
	private String content;

	public LrcLine() {
		// TODO Auto-generated constructor stub
	}

	public LrcLine(String time, String content) {
		super();
		this.time = time;
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String toString() {
		return "["+this.time+"]"+this.content;
	}
}
