package com.mcs.todo.db;


public class  ChangeList extends DBTable{


	/**以发生操作的时间作为key**/
	public static final String KEY = "key";
	public static final String OPERATION = "operation";
	/**以待办的创建时间作为item_id**/
	public static final String ITEM_ID = "item_id";
	
	
	
	public ChangeList(String authority) {
		super(authority,DBConstant.TABLE_TODOLIST_CHANGELIST);
		// TODO Auto-generated constructor stub
	}
	public ChangeList(){
		super(DBConstant.AUTHORITY,DBConstant.TABLE_TODOLIST_CHANGELIST);
	}
	@Override
	public String getKeyName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initTable() {
		// TODO Auto-generated method stub
		addColumn(KEY, "integer");
		addColumn(OPERATION, "integer");
		addColumn(ITEM_ID, "integer");
	}
	
}