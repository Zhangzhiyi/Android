package com.mcs.todo.db;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class MCSProvider extends ContentProvider {
	
	public static final String AUTHORITY = DBConstant.AUTHORITY;
	private SQLiteHelper dbHelper = null;
	private ArrayList<String> tables = new ArrayList<String>();
	
	@Override
	public boolean onCreate() {
		dbHelper = new SQLiteHelper(getContext());
		return true;
	}
	
	/**
	 * 从uri中获取表名。
	 * @param uri
	 * @return
	 */
	private String getTableName(Uri uri) {
		String name = uri.getEncodedPath();
		name = name.substring(1);
		return name;
	}
	
	/**
	 * 根据uri返回对应的表对象。
	 * @param uri
	 * @return
	 */
	private DBTable getMatchTable(Uri uri) {
		String name = uri.getEncodedPath();
		name = name.substring(1);
		return TableFactory.getTable(name);
	}
	
	/**
	 * 查询数据库中是否已经存在uri所对应的数据表，否则创建。
	 */
	public boolean checkTable(Uri uri) {
		String name = getTableName(uri);
		return checkTable(name);
	}
	
	/**
	 * 查询数据库中是否已经存在name所对应的数据表，否则创建。
	 */
	public boolean checkTable(String name) {
		if(tables.contains(name)) {
			return true;
		}
		
		String[] columns = {"name"};
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// sqlite_master数据表是sqlite数据库维护的系统数据表
		Cursor cur = db.query("sqlite_master", columns, "name = " + "'" + name + "'", null, null, null, null);
		cur.moveToFirst();
		if(cur.getCount() == 0) {
			DBTable tb = TableFactory.getTable(name);
			db.execSQL(tb.createTableString());
			tables.add(name);
			cur.close();
			return false; // 数据库中没有包含已知表，需要创建新表
		}else {
			cur.close(); // 不要忘了这里还要close，要不然会出错哦
			return true;
		}
	}
	
	@Override
	public String getType(Uri uri) {
		// TODO 返回类型啊。
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
        ContentValues cv;
        if (values != null) {
            cv = new ContentValues(values);
        } else {
            cv = new ContentValues();
        }
        
        // 数据库的插入操作
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String name = getTableName(uri);
        long rowId = db.insert(name, null, cv);
        if (rowId < 0) { 
        	if(checkTable(uri))// 如果出错，且表本来还存在的话，那就真的是数据库错误了。
        		throw new SQLiteException("Failed to insert row into " + uri);
        	else // 插入出错是因为表不存在，所以要重新插入一遍。
        		rowId = db.insert(name, null, cv);
        }
        Uri tableUri = ContentUris.withAppendedId(uri, rowId);
        getContext().getContentResolver().notifyChange(tableUri, null);
        return tableUri; // 返回了一个有ID的URI
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = null;
        Log.i("URI", uri.toString());
		String path = uri.getEncodedPath();
		Log.i("path", path);
		/** 判断是否是系统search传过来的uri:content://com.mcs.todo/search_suggest_query**/
		if(path.substring(1).equals(SearchManager.SUGGEST_URI_PATH_QUERY)){				
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			/**设置查询的表名**/
			qb.setTables("todolist");
			String query = selectionArgs[0];
			Log.i("query", query);
			/**映射列名，因为系统search manager只能识别特定的表名**/
			HashMap<String,String> map = new HashMap<String,String>();
			map.put(Todolist._ID,Todolist._ID);
			map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, Todolist._ID+" AS "+SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
			map.put(SearchManager.SUGGEST_COLUMN_TEXT_1,Todolist.TITLE+" AS "+SearchManager.SUGGEST_COLUMN_TEXT_1);
			map.put(SearchManager.SUGGEST_COLUMN_TEXT_2,Todolist.CONTENT+" AS "+SearchManager.SUGGEST_COLUMN_TEXT_2);
			qb.setProjectionMap(map);
			String[] Columns = {Todolist._ID,SearchManager.SUGGEST_COLUMN_TEXT_1,SearchManager.SUGGEST_COLUMN_TEXT_2,SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID};								
			c = qb.query(db, Columns, Todolist.TITLE+" like "+"'%"+query+"%'"+" or "+Todolist.CONTENT+" like "+"'%"+query+"%'", null, null, null, null);			
			return c;			
		}
		
		DBTable tb = getMatchTable(uri); 						
        try {
        	c = db.query(tb.getName(), projection, selection, selectionArgs, null, null, sortOrder);
        } catch(SQLiteException e) {
        	e.printStackTrace();
        	if(!checkTable(uri)) // 如果出错是表不存在造成的。
        		// 重新query虽然多余，但是不会导致出现null的错误。
        		c = db.query(tb.getName(), projection, selection, selectionArgs, null, null, sortOrder);
        }
        if(c != null)
        	c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase(); 
		String name = getTableName(uri);
		int count = 0;
		try {
			count = db.delete(name, selection, selectionArgs);
		}catch(SQLiteException e) {
        	if(!checkTable(uri)) // 如果出错是表不存在造成的。
        		return -1; // 不存在表
        }
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String name = getTableName(uri);
	    int count = 0;
	    try{
	    	count = db.update(name, values, selection, selectionArgs);
	    }catch(SQLiteException e) {
        	if(!checkTable(uri)) // 如果出错是表不存在造成的。
        		return -1; // 不存在表
        }
	        
	    getContext().getContentResolver().notifyChange(uri, null);
	    return count;
	}

	private class SQLiteHelper extends SQLiteOpenHelper { 
        private static final int DATABASE_VERSION = 1; 
        
        public SQLiteHelper(Context context) { 
        	super(context, DBConstant.DATABASE_NAME, null, DATABASE_VERSION); 
        } 

        @Override 
        public void onCreate(SQLiteDatabase db) { 
        	// 不需要在这里创建表了，当发现不存在表时会自动创建的
        } 

        @Override 
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 
        	int version = oldVersion; 

        	if(version != DATABASE_VERSION) { 
        		// TODO 删除原有的
                // TODO 创建新的 
        	} 
        }
	}
}
