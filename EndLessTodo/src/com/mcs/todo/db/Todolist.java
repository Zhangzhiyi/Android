package com.mcs.todo.db;

/**
 * 待办事项表的定义
 * @author dbds
 *
 */
public class Todolist extends DBTable {
	
	public static final String DEFAULT_SORT_ORDER = null;

	// 不要忘了还有一个_ID的项存在
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
	
	/**
	 * 处理状态 
	 * 0.未过时待完成 1.启动准备，进行中 2.已完成 3.过时且没完成
	*/
	public static final String STATE = "state";
	public static final int STATE_UNDONE = 0;
	public static final int STATE_DONE = 1;
	
	/** 开始准备时间 */
	public static final String BEGIN_DATE = "beginDate";
	public static final String BEGIN_TIME = "beginTime";
	/** 以long的形式记录开始时间，以利于比较 */
	public static final String BEGIN_TIME_LONG = "beginTimeLong";
	
	/** 任务完成时间 */
	public static final String END_DATE = "endDate";
	public static final String END_TIME = "endTime";
	public static final String END_TIME_LONG = "endTimeLong";
	
	/** 开始提醒的时间*/
	public static final String ALERT_TIME = "alertTime";
	/** 提醒频率 */
	public static final String WARN_FREQUENCY = "warnFreq";

	/** 标签名 */
	public static final String TAG = "tag";

	/**
	 * 此构造函数用于访问数据库
	 */
	public Todolist(String authority) {
		super(authority, DBConstant.TABLE_TODOLIST);
	}
	
	/**
	 * 普通数据表有他固定的名字
	 */
	public Todolist() {
		super(DBConstant.AUTHORITY, DBConstant.TABLE_TODOLIST);
	}
	

	@Override
	public void initTable() {
		addColumn(BEGIN_TIME, "text");
		addColumn(BEGIN_DATE, "text");
		addColumn(BEGIN_TIME_LONG, "integer");
		addColumn(USER_ID, "integer");
		addColumn(LOCATION, "text");
		addColumn(CRT_TIME, "integer");
		addColumn(TITLE, "text");
		addColumn(CONTENT, "text");
		addColumn(STATE, "integer");
		addColumn(IMPORTANCE, "integer");
		addColumn(END_DATE, "text");
		addColumn(END_TIME, "text");
		addColumn(END_TIME_LONG, "integer");
		addColumn(ALERT_TIME, "integer");
		addColumn(WARN_FREQUENCY, "text");
		addColumn(TAG, "text");
	}

	@Override
	public String getKeyName() {
		// 使用创建时间做为待办的key
		return CRT_TIME;
	}
}
