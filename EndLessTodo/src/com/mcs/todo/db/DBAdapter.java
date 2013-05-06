package com.mcs.todo.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * 访问Content Provider的界面
 * @author dbds
 */
public class DBAdapter {
	
	protected Uri uri;
	
	protected Context context;
	protected ContentResolver contentResolver;	
	protected String tableName;
	
	public DBAdapter(Context ctx, String tableName) {
		context = ctx;
		contentResolver = context.getContentResolver();
		this.tableName = tableName;
		uri = Uri.parse("content://" + DBConstant.AUTHORITY + "/" + this.tableName);
	}
	
	public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
	}
	
	public Uri insert(ContentValues values) {
		return contentResolver.insert(uri, values);
	}
	
	public int delete(String selection, String[] selectionArgs) {
		return contentResolver.delete(uri, selection, selectionArgs);
	}
	
	public int update(ContentValues values, String selection, String[] selectionArgs) {
		return contentResolver.update(uri, values, selection, selectionArgs);
	}
}
