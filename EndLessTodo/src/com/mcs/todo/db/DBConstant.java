package com.mcs.todo.db;

public class DBConstant {
	/** 数据库名 */
	public static final String DATABASE_NAME = "mcs.db"; 
	
	/** ContentProvider的Authority */
	public static final String AUTHORITY = "com.mcs.todo";
	
	// 各个表的表名
	public static final String TABLE_TODOLIST = "todolist";
	public static final String TABLE_NOTELIST = "notelist";
	public static final String TABLE_NOTECATEGORY = "notecategory";
	public static final String TABLE_WIDGET_SETTING = "widgetsetting";
	
	public static final String TABLE_TODOLIST_CHANGELIST = "todolist_changelist";
}
