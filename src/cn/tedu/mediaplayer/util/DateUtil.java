package cn.tedu.mediaplayer.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static SimpleDateFormat SDF = new SimpleDateFormat("mm:ss");
	public static Date date = new Date();
	/**
	 * �Ѻ���ֵ ת�� mm:ss ��ʽ���ַ���
	 * @param time
	 * @return
	 */
	public static String parseTime(long time){
		date.setTime(time);
		return SDF.format(date);
	}
}



