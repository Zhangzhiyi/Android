package com.mcs.todo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Time {
	
	public static final String YEARSTRING = "-";
	public static final String MONTHSTRING = "-";
	public static final String DAYSTRING = "";
	
	public static final String HOURSTRING = ":";
	public static final String MINUTESTRING = "";
	
	private static Calendar ca = Calendar.getInstance();
	
	/** 返回当前的Calendar对象 */
	public static Calendar getInstance(){
		return ca;
	}
	/** 设置对象ca为现在的系统时间**/
	public static void setToday(){
		ca = Calendar.getInstance();
	}
	
	/** 返回当前的Date */
	public static String getCurrentDate() {
		return getDateString(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH));
	}
	public static void setCurrentDate(long millis){
		ca.setTimeInMillis(millis);
	}
	/** 设置当前查询的日期 */
	public static void setCurrentDate(int year, int month, int day) {
		ca.set(Calendar.YEAR, year);
		ca.set(Calendar.MONTH, month);
		ca.set(Calendar.DAY_OF_MONTH, day);
	}
	
	/** 不改变待办当前日期，只返回今天的日期String */
	public static String getTodayString() {
		Calendar today = Calendar.getInstance();
		return getDateString(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
	}
	
	/** 返回提醒的小时数 */
	public static int getAlertHour(long alertTime) {
		return (int) (alertTime/(60*60*1000));
	}
	
	/** 返回提醒的分钟 */
	public static int getAlertMinute(long alertTime) {
		return (int) ((alertTime%(60*60*1000))/(60*1000));
	}
	
	/** 根据小时和分钟返回long类型的alertTime */
	public static long getAlertTime(int hour, int minute) {
		return (hour*60+minute)*60*1000;
	}
	
	// --------------------------WIDGET用---------------------------------
	
	/** 返回只有月份和日期的短格式的日期字符串 */
	public static String getShortDate(String dateString) {
		DateFormat df = new SimpleDateFormat("MM-dd");
		int[] date = parseDateString(dateString);
		Calendar ca = Calendar.getInstance();
		ca.set(date[0], date[1], date[2]);
		return df.format(ca.getTime());
	}
	
	/** 返回date对应的day of week */
	public static String getWeek(String dateString) {
		DateFormat df = new SimpleDateFormat("E");
		int[] date = parseDateString(dateString);
		Calendar ca = Calendar.getInstance();
		ca.set(date[0], date[1], date[2]);
		return df.format(ca.getTime());
	}
	
	// --------------------------BASE----------------------------------------
	
	/** 当前日期前进一天 */
	public static void toNextDay() {
		// 如果使用roll则在改变day的时候没法改变month
		ca.add(Calendar.DATE, 1);
	}
	
	/** 当前日期提前一天 */
	public static void toPreviousDay() {
		ca.add(Calendar.DATE, -1);
	}
	
	/** 设置当前日期回到今天 */
	public static void backToToday() {
		ca = Calendar.getInstance();
	}
	
	/** 把已有的date字符串解释成int[] */
	public static int[] parseDateString(String dateString) {
		return parseDateString(dateString, "yyyy-MM-dd");
	}
	
	/** 
	 * 把已有的date字符串解释成int[] 
	 * @param pattern pattern遵循SimpleDateFormat的定义
	 */
	public static int[] parseDateString(String dateString, String pattern) {
		// 用DateFormat，不要自定义字符串解析，这样就没有那么多乱七八糟的事情了
		DateFormat df = new SimpleDateFormat(pattern);
		Calendar ca = Calendar.getInstance();
		
		try {
			ca.setTime(df.parse(dateString));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		int[] date = new int[3];
		date[0] = ca.get(Calendar.YEAR);
		date[1] = ca.get(Calendar.MONTH);
		date[2] = ca.get(Calendar.DAY_OF_MONTH);
		return date;
	}
	
	/** 把已有的time字符串解释成int[] */
	public static int[] parseTimeString(String timeString) {
		return parseTimeString(timeString, "HH:mm");
	}
	
	/** 把已有的time字符串解释成int[] */
	public static int[] parseTimeString(String timeString, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		Calendar ca = Calendar.getInstance();
		
		try {
			ca.setTime(df.parse(timeString));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		int[] time = new int[2];
		time[0] = ca.get(Calendar.HOUR_OF_DAY);
		time[1] = ca.get(Calendar.MINUTE);
		return time;
	}
	
	
	/** 返回默认格式的DateString */
	public static String getDateString(int year, int month, int day) {
		return getDateString(year, month, day, "yyyy-MM-dd");
	}
	
	/** 返回默认格式的TimeString */
	public static String getTimeString(int hour, int minute) {
		return getTimeString(hour, minute, "HH:mm");
	}
	/** 返回默认格式的TimeString */
	public static String getDateString(long millis) {
		return getDateString(millis, "MM月dd日");
	}
	
	/** 返回可自定义格式的日期String */
	public static String getDateString(int year, int month, int day, String datePattern) {
		Calendar ca = Calendar.getInstance();
		ca.set(year, month, day);
		DateFormat df = new SimpleDateFormat(datePattern);
		return df.format(ca.getTime());
	}
	
	/** 返回可自定义格式的日期String */
	public static String getTimeString(int hour, int minute, String datePattern) {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.HOUR_OF_DAY, hour);
		ca.set(Calendar.MINUTE, minute);
		DateFormat df = new SimpleDateFormat(datePattern);
		return df.format(ca.getTime());
	}
	
	/** 返回可自定义格式的日期String */
	public static String getDateString(long millis ,String datePattern){
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(millis);
		DateFormat df = new SimpleDateFormat(datePattern);
		return df.format(ca.getTime());
		
	}
	//--------------------------wanrFreq----------------------------
	
	/**返回一天的毫秒数*/
	public static long getNextDayMillis(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);		
		return 	calendar.getTimeInMillis();	
	}
	/**返回一周的毫秒数*/
	public static long getNextWeekMillis(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 7);		
		return 	calendar.getTimeInMillis();	
	}
	public static boolean isWorkDay(){
		Calendar calendar = Calendar.getInstance();
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		if(week==1||week==7)
			return false;
		else
			return true;
	}
	/**返回现在时间下一个月同一天的距离现在的毫秒数*/
	public static long getNextMonthMillis(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1);		
		return 	calendar.getTimeInMillis();			
	}
	/**返回现在时间下一年同一时间的距离现在的毫秒数*/
	public static long getNextYearMillis(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1);
		return 	calendar.getTimeInMillis();	
	}
	
}
