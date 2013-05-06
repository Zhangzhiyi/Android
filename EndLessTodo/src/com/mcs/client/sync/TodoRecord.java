package com.mcs.client.sync;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;

import com.mcs.framework.message.Packer;
import com.mcs.framework.utils.Bytes;

public class TodoRecord implements Packer{
	
	private static final Logger logger = LoggerFactory.getLogger(TodoRecord.class);
	
	/** 用户ID */
	public static final String USER_ID = "userId";
	/** 发生的地点 */
	public static final String LOCATION = "location";
	/** 标题 */
	public static final String TITLE = "title";
	/** 内容 */
	public static final String CONTENT = "content";
	/** 创建时间 */
	public static final String CRT_TIME = "crtTime";
	/** 优先级 */
	public static final String IMPORTANCE = "importance";
	/** 处理状态 */
	public static final String STATE = "state";
	public static final int STATE_UNDONE = 0;
	public static final int STATE_DONE = 1;
	/** 以long的形式记录开始时间，以利于比较 */
	public static final String BEGIN_TIME_LONG = "beginTimeLong";
	/** 任务完成时间 */
	public static final String END_TIME_LONG = "endTimeLong";
	/** 开始提醒的时间*/
	public static final String ALERT_TIME = "alertTime";
	/** 提醒频率 */
	public static final String WARN_FREQUENCY = "warnFreq";
	/** 标签名 */
	public static final String TAG = "tag";

	private long crtTime; // Key
	private long beginTimeLong;
	private long endTimeLong;
	private long alertTime;
	private String userId;
	private String location;
	private String title;
	private String content;
	private int state;
	private int importance;
	private int warnFreq;
	private String tag;
	
	public TodoRecord() {}
	
	public TodoRecord(String userId, String title, String content, String location, String tag, 
					  long crtTime, long beginTime, long endTime, long alertTime,
					  int state, int importance, int warnFreq) {
		this.crtTime 		= crtTime;
		this.beginTimeLong 	= beginTime;
		this.endTimeLong 	= endTime;
		this.alertTime 		= alertTime;
		this.userId 		= userId;
		this.location 		= location;
		this.title 			= title;
		this.content 		= content;
		this.state 			= state;
		this.importance 	= importance;
		this.warnFreq 		= warnFreq;
		this.tag 			= tag;
	}

	public long getBeginTime() {
		return beginTimeLong;
	}

	public void setBeginTime(long beginTimeLong) {
		this.beginTimeLong = beginTimeLong;
	}

	public long getEndTime() {
		return endTimeLong;
	}

	public void setEndTime(long endTimeLong) {
		this.endTimeLong = endTimeLong;
	}

	public long getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(long alertTime) {
		this.alertTime = alertTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public long getCrtTime() {
		return crtTime;
	}

	public void setCrtTime(long crtTime) {
		this.crtTime = crtTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getImportance() {
		return importance;
	}

	public void setImportance(int importance) {
		this.importance = importance;
	}

	public int getWarnFreq() {
		return warnFreq;
	}

	public void setWarnFreq(int warnFreq) {
		this.warnFreq = warnFreq;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public void set(String name, byte[] value) {
		// TODO 实现根据name来设置值
	}
	
	public ContentValues getContentValues(){
		ContentValues values = new ContentValues();
		values.put(CRT_TIME, crtTime);
		values.put(USER_ID, userId);
		values.put(TITLE, title);
		values.put(CONTENT,content);
		values.put(LOCATION,location);
		values.put(STATE, state);
		values.put(IMPORTANCE, importance);
		values.put(BEGIN_TIME_LONG, beginTimeLong);
		values.put(END_TIME_LONG, endTimeLong);
		values.put(ALERT_TIME, alertTime);
		values.put(WARN_FREQUENCY,warnFreq);
		values.put(TAG, tag);
		return values;
		
	}
	void extractData(String value) {
		StringBuilder sb = new StringBuilder(value);
		crtTime 	= Long.valueOf(entryValue(sb, CRT_TIME));
		userId 		= entryValue(sb, USER_ID);
		title 		= entryValue(sb, TITLE);
		content 	= entryValue(sb, CONTENT);
		location 	= entryValue(sb, LOCATION);
		state 		= Integer.valueOf(entryValue(sb, STATE));
		importance 	= Integer.valueOf(entryValue(sb, IMPORTANCE));
		beginTimeLong = Long.valueOf(entryValue(sb, BEGIN_TIME_LONG));
		endTimeLong = Long.valueOf(entryValue(sb, END_TIME_LONG));
		alertTime 	= Long.valueOf(entryValue(sb, ALERT_TIME));
		warnFreq 	= Integer.valueOf(entryValue(sb, WARN_FREQUENCY));
		tag 		= entryValue(sb, TAG);
	}
	
	String entryValue(StringBuilder sb, String entry) {
		int start = sb.indexOf(entry);
		start = sb.indexOf(SEP, start) + 1;
		int end = sb.indexOf(NL, start);
		return sb.substring(start, end);
	}
	
	@Override
	public byte[] pack() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(CRT_TIME		 ).append(SEP).append(String.valueOf(crtTime)).append(NL)
		  .append(USER_ID		 ).append(SEP).append(userId).append(NL)
		  .append(TITLE			 ).append(SEP).append(title).append(NL)
		  .append(CONTENT		 ).append(SEP).append(content).append(NL)
		  .append(LOCATION		 ).append(SEP).append(location).append(NL)
		  .append(STATE			 ).append(SEP).append(String.valueOf(state)).append(NL)
		  .append(IMPORTANCE	 ).append(SEP).append(String.valueOf(importance)).append(NL)
		  .append(BEGIN_TIME_LONG).append(SEP).append(String.valueOf(beginTimeLong)).append(NL)
		  .append(END_TIME_LONG	 ).append(SEP).append(String.valueOf(endTimeLong)).append(NL)
		  .append(ALERT_TIME	 ).append(SEP).append(String.valueOf(alertTime)).append(NL)
		  .append(WARN_FREQUENCY ).append(SEP).append(String.valueOf(warnFreq)).append(NL)
		  .append(TAG			 ).append(SEP).append(tag).append(NL);
		
		String c = sb.toString();
		logger.info("TodoRecord is packed into: {}", c);
		return Bytes.fromString(c);
	}

	@Override
	public void unpack(byte[] value) {
		if(null != value) {
			extractData(new String(value));
		}
	}

}
