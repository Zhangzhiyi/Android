package com.mcs.todo.db;

public class WidgetSetting extends DBTable {
	
	public static final String WIDGET_ID = "widgetId";
	/** 显示的模式：日期 or 标签 */
	public static final String MODE = "mode";
	public static final int MODE_DATE = 0;
	public static final int MODE_TAG = 1;
	/** 标签模式下需要显示的标签 */
	public static final String TAG = "tag";

	public WidgetSetting() {
		super(DBConstant.AUTHORITY, DBConstant.TABLE_WIDGET_SETTING);
	}

	@Override
	public String getKeyName() {
		return WIDGET_ID;
	}

	@Override
	public void initTable() {
		addColumn(WIDGET_ID, "integer");
		addColumn(MODE, "integer");
		addColumn(TAG, "text");
	}

}
