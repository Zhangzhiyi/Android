package com.mcs.todo.db;

import java.util.Calendar;

import com.mcs.client.sync.ItemState;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class ContentManager {
	
	private Context context;
	private DBAdapter db;
	private DBAdapter dc;
	private DBTable table;
	private SharedPreferences mSharePreferences;

	 
	public ContentManager(Context context,String tablename) {
		// TODO Auto-generated constructor stub
		this.context = context;
		
		db = new DBAdapter(context, tablename);
		dc = new DBAdapter(context, tablename + "_changelist");
		table = TableFactory.getTable(tablename);
						
	}
	public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return db.query(projection, selection, selectionArgs, sortOrder);
	}
	
	public Uri insert(ContentValues values) {
		
		Uri uri = db.insert(values);		
		ContentValues changValues = new ContentValues();
		Cursor cursor = dc.query(null, null, null, null);
		int count = cursor.getCount();
		int key;
		if(count==0){
			key = (int) (getLastValues()+1);
		}
		else{
			key = (int) (getLastValues()+count+1);
		}
		changValues.put(ChangeList.KEY, key);
		changValues.put(ChangeList.OPERATION, (int)ItemState.NEW);
		Log.i("crtTime", String.valueOf(values.getAsLong(Todolist.CRT_TIME)));
		/**获取crtTime一定要用getAsLong,不用getAsInteger,否则数值差好远**/
		/**貌似SQLite数据库Integer类型可以存储long类型数据**/
		changValues.put(ChangeList.ITEM_ID,values.getAsLong(Todolist.CRT_TIME));
		dc.insert(changValues);
		cursor.close();
		return uri; 
	}
	public int delete(String selection, String[] selectionArgs){
		
		String[] projection = {table.getKeyName()};
		Cursor cursor = db.query(projection, selection, null, null);
		if(cursor.getCount()>0){
			for(int i=0;i<cursor.getCount();i++){
				cursor.moveToPosition(i);
				ContentValues changValues = new ContentValues();
				Cursor c = dc.query(null, null, null, null);
				int count = c.getCount();
				
				int key;
				if(count==0){
					key = (int) (getLastValues()+1);
				}
				else{
					key = (int) (getLastValues()+count+1);
				}
				changValues.put(ChangeList.KEY, key);
				changValues.put(ChangeList.OPERATION,(int)ItemState.DEL);
				/**对比getLong和getInt获取的crtTime的值，发现两个值完全不一样**/
				Log.i("long crtTime", String.valueOf(cursor.getLong(cursor.getColumnIndex(table.getKeyName()))));
				Log.i("int crtTime", String.valueOf(cursor.getInt(cursor.getColumnIndex(table.getKeyName()))));
				
				changValues.put(ChangeList.ITEM_ID,cursor.getLong(cursor.getColumnIndex(table.getKeyName())));		
				dc.insert(changValues);
				c.close();
			}
		}		
		int count = db.delete(selection, selectionArgs);
		cursor.close();
		return count;
		
	}
	public int update(ContentValues values, String selection, String[] selectionArgs) {
		String[] projection = {table.getKeyName()};
		Cursor cursor = db.query(projection, selection, null, null);
		if(cursor.getCount()>0){
			for(int i=0;i<cursor.getCount();i++){
				cursor.moveToPosition(i);
				ContentValues changValues = new ContentValues();
				Cursor c = dc.query(null, null, null, null);
				int count = c.getCount();
				int key;
				if(count==0){
					key = (int) (getLastValues()+1);
				}
				else{
					key = (int) (getLastValues()+count+1);
				}
				changValues.put(ChangeList.KEY, key);
				changValues.put(ChangeList.OPERATION,(int)ItemState.UPD);
				changValues.put(ChangeList.ITEM_ID,cursor.getLong(cursor.getColumnIndex(table.getKeyName())));		
				dc.insert(changValues);
			}
		}		
		int count = db.update(values, selection, selectionArgs);
		cursor.close();
		return count;
		
	}	
	public long getLastValues(){
		mSharePreferences = context.getSharedPreferences("com.mcs.todo_preferences", Context.MODE_PRIVATE);
		long last = mSharePreferences.getLong("last", -1);
		return last;
		
	}
	
}
