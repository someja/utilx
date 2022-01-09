package io.github.someja.utilsx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class DateUtils {
	
	/**
	 * 默认时间格式
	 */
	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 日期格式
	 */
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	/**
	 * 月格式
	 */
	private static final String MONTH_FORMAT = "yyyy-MM";

	public static final String format(Calendar c) {
		return format(c, DEFAULT_FORMAT);
	}
	
	public static final String format(Calendar c, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(c.getTime());
	}
	
	public static final String format(Date d) {
		return format(d, DEFAULT_FORMAT);
	}
	
	public static final String format(Date d, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(d);
	}
	
	public static final Date parse(String dateStr) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return formatter.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static final Date parse(String dateStr, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try {
			return formatter.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static final Calendar parse2Calendar(String dateStr) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(formatter.parse(dateStr));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return c;
	}
	
	public static final Calendar parse2Calendar(String dateStr, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(formatter.parse(dateStr));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return c;
	}
	
	/**
	 * 获取一个日期的范围（包含年月日）
	 * @param begin
	 * @param end
	 * @return 是一个List，元素为Calendar
	 */
	public static final List<Calendar> dayRange(String begin, String end) {
		Date beginTime = parse(begin, DATE_FORMAT);
		Date endTime = parse(end, DATE_FORMAT);
		
		List<Calendar> cs = new ArrayList<>();
		while(!beginTime.after(endTime)) {
			Calendar c = Calendar.getInstance();
			c.setTime(beginTime);
			cs.add(c);
			
			beginTime.setTime(beginTime.getTime() + 3600 * 24);
		}
		
		return cs;
	}
	
	/**
	 * 获取一个日期的范围（只包含年月）
	 * @param begin
	 * @param end
	 * @return 是一个List，元素为Calendar
	 */
	public static final List<Calendar> monthRange(String begin, String end) {
		Date beginTime = parse(begin, MONTH_FORMAT);
		Date endTime = parse(end, MONTH_FORMAT);
		
		List<Calendar> cs = new ArrayList<>();
		Calendar beginCal = Calendar.getInstance();
		beginCal.setTime(beginTime);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endTime);
		while(!beginCal.after(endCal)) {
			Calendar c = Calendar.getInstance();
			c.setTime(beginCal.getTime());
			cs.add(c);
			
			beginCal.add(Calendar.MONTH, 1);
		}
		
		return cs;
	}
	
}
