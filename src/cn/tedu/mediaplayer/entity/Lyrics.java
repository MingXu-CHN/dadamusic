package cn.tedu.mediaplayer.entity;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 描述完整歌词内容 */
public class Lyrics {
	private List<LrcLine> lines = new ArrayList<LrcLine>();

	public Lyrics() {
	}

	public Lyrics(List<LrcLine> lines) {
		super();
		this.lines = lines;
	}

	public List<LrcLine> getLines() {
		return lines;
	}

	public void setLines(List<LrcLine> lines) {
		this.lines = lines;
	}

	/** 向歌词对象中添加一行歌词文本 
	 * 	  line: [03:57.70][03:20.00][02:08.00][01:09.00]原谅我这一生不羁放纵爱自由
	 * 	  
	 * */
	public void addLine(String line){
		if(line.trim().equals("")){ //这一行 没写字
			return;
		}
		if(!line.contains(".")){ //这一行不符合格式要求
			return;
		}
		if(line.length()>=10){
			//解析这一行:  [03:57.70][03:20.00][02:08.00][01:09.00]原谅我这一生不羁放纵爱自由
			String regex="\\d{2}:\\d{2}";
			Pattern p=Pattern.compile(regex);
			Matcher m=p.matcher(line);
			while(m.find()){
				Log.i("info", m.group()+"\t\t\t\t\t"+line.substring(line.lastIndexOf("]")+1));
				String time = m.group();
				String content = line.substring(line.lastIndexOf("]")+1);
				LrcLine l = new LrcLine(time, content);
				this.lines.add(l);
			}
		}
	} 
	
	/**
	 * 通过时间  获取该行文本
	 */
	public String getContent(String time){
		for(int i=0; i<lines.size(); i++){
			LrcLine line = lines.get(i);
			if(time.equals(line.getTime())){
				return line.getContent();
			}
		}
		return null;
	}

	public int getPosition(String time){
		for(int i=0; i<lines.size(); i++){
			LrcLine line = lines.get(i);
			if(time.equals(line.getTime())){
				return i;
			}
		}
		return -1;
	}

	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<lines.size(); i++){
			sb.append(lines.get(i).toString()+"\n");
		}
		return sb.toString();
	}
	
}





