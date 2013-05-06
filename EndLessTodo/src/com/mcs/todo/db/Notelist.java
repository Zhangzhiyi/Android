package com.mcs.todo.db;


public class Notelist extends DBTable{
	


	/** 标题 */
	public static final String TITLE = "title";
	/** 内容 */
	public static final String CONTENT = "content";
	/** 创建时间 */
	public static final String CRT_TIME = "crtTime";
	/**标签ID**/
	public static final String CATEGORY_ID = "categoryId";
	
	
	public Notelist(String authority) {
		super(authority, DBConstant.TABLE_NOTELIST);
	}
	public Notelist() {
		// TODO Auto-generated constructor stub
		super(DBConstant.AUTHORITY, DBConstant.TABLE_NOTELIST);
	}
	@Override
	public String getKeyName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initTable() {
		// TODO Auto-generated method stub
		addColumn(TITLE, "text");
		addColumn(CONTENT, "text");
		addColumn(CRT_TIME, "integer");
		addColumn(CATEGORY_ID, "integer");
	}
	
}