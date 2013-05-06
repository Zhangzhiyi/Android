package com.mcs.todo.db;


public class Notecategory extends DBTable{
	
	public static final String NAME = "name";
	public Notecategory(String authority) {
		super(authority,DBConstant.TABLE_NOTECATEGORY);
		// TODO Auto-generated constructor stub
	}
	public Notecategory() {
		// TODO Auto-generated constructor stub
		super(DBConstant.AUTHORITY,DBConstant.TABLE_NOTECATEGORY);
	}
	@Override
	public String getKeyName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initTable() {
		// TODO Auto-generated method stub
		addColumn(NAME, "text");
	}
	
}