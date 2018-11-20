package cn.tedu.mediaplayer.entity;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** ��������������� */
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

	/** ���ʶ��������һ�и���ı� 
	 * 	  line: [03:57.70][03:20.00][02:08.00][01:09.00]ԭ������һ������ݰ�����
	 * 	  
	 * */
	public void addLine(String line){
		if(line.trim().equals("")){ //��һ�� ûд��
			return;
		}
		if(!line.contains(".")){ //��һ�в����ϸ�ʽҪ��
			return;
		}
		if(line.length()>=10){
			//������һ��:  [03:57.70][03:20.00][02:08.00][01:09.00]ԭ������һ������ݰ�����
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
	 * ͨ��ʱ��  ��ȡ�����ı�
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





