package com.mcs.todo.db;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public abstract class DBTable{
	
	public static final String _ID = "_id";
	
	private String authority;
	private String tableName;
	private Uri contentUri;
	private UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	public static final int YES = 1;
	public static final int SEARCH_SUGGEST = 3; 
	
	
	private HashMap<String, String> projectionMap = new HashMap<String, String>();
	
	/**
	 * 保存所有的列名
	 */
	private ArrayList<String> columns = new ArrayList<String>();
	/**
	 * 与列名相对的类型
	 */
	private ArrayList<String> types = new ArrayList<String>();
	
	public DBTable(String authority, String tableName) {
		this.authority = authority;
		this.tableName = tableName;
		contentUri = Uri.parse("content://" + this.authority + "/" + tableName);
		/**这部分可以注销,因为在MCSProvider没有用到UriMatcher去解析Uri,UriMatcher只不过是操作Uri的工具类**/
		//uriMatcher.addURI(authority, tableName, YES);
		//uriMatcher.addURI(authority, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
		//uriMatcher.addURI(authority, SearchManager.SUGGEST_URI_PATH_QUERY+"/*", SEARCH_SUGGEST);
		/* 增加默认的_id项 */
		addColumn("_id", "integer");
			
		initTable();
	}
	
	public String getName() { return tableName; }
	
	public Uri getContentUri() {
		return contentUri;
	}
	
	public void addColumn(String name, String type) {
		columns.add(name);
		types.add(type);
		
		projectionMap.put(name, name);
	}
	
	/**
	 * 返回列数
	 */
	public int count() {
		return columns.size();
	}
	
	public boolean matchUri(Uri uri) {
		if(uriMatcher.match(uri) == YES)
			return true;
		else
			return false;	
	}
	
	
	/**
	 * 返回列名数组
	 */
	public String[] getColumns() {
		return (String[])columns.toArray();
	}
	
	public HashMap<String, String> getProjectionMap() {
		return projectionMap;
	}
	
	/**
	 * 返回由列名组成的String数组
	 */
	public String[] getColumnName() {
		String[] s = new String[1];
		return columns.toArray(s);
	}
	
	/**
	 * 返回一个本表的SQL CREATE TABLE语句
	 */
	public String createTableString() {
		String s = null;
		s = "create table " + tableName + " " + "(" + "_id" + " integer primary key,";
		for(int i = 1; i < columns.size() - 1; i++) {
			s = s + " " + columns.get(i) + " " + types.get(i) + ",";
		}
		int last = columns.size() - 1;
		s += " " + columns.get(last) + " " + types.get(last) + ");";
		return s;
	}
	
	/**
	 * 以String的模式返回数据库值
	 * @param name 要取得的字段名
	 * @param cur 
	 * @return
	 */
	public String getValue(String name, Cursor cur) {
		String type = null;
		for(int i = 0; i < columns.size(); i++) {
			if(columns.get(i).equals(name)) {
				type = types.get(i);
			}
		}
		
		if(type != null) {
			if(type.equals("integer")) // 按理说，用Long来容纳所有的integer是没有问题的
				return String.valueOf(cur.getLong(cur.getColumnIndex(name)));
			else if(type.equals("text"))
				return cur.getString(cur.getColumnIndex(name));
		}
		return null;
	}
	
	/**
	 * ContentValues版本的getValue
	 * @param name
	 * @param cv
	 * @return
	 */
	public String getValue(String name, ContentValues cv) {
		String type = null;
		for(int i = 0; i < columns.size(); i++) {
			if(columns.get(i).equals(name)) {
				type = types.get(i);
			}
		}
		
		if(type != null) {
			if(type.equals("integer")) // 按理说，用Long来容纳所有的integer是没有问题的
				return String.valueOf(cv.getAsLong(name));
			else if(type.equals("text"))
				return cv.getAsString(name);
		}
		return null;
	}
	
	/**
	 * 改写这个函数来实现不同表的主键值的实现
	 * @param cv 执行insert操作时候用到的ContentValues
	 */
	public void prepareKeyValue(ContentValues cv) {}
	
	/**
	 * 改写本方法来创建自己的表，通过addColumn方法加入不同的列
	 */
	public abstract void initTable();
	
	/**
	 * 返回本表主键的名字
	 */
	public abstract String getKeyName();

}
