package com.mcs.todo.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.mcs.client.sync.SyncItem;
import com.mcs.client.sync.SyncSource;
import com.mcs.client.sync.TimeAnchor;
import com.mcs.client.sync.TodoRecord;

public class LocalTodoSource{
	private DBAdapter db;
	private DBAdapter dc;
	public LocalTodoSource(Context context) {
		// TODO Auto-generated constructor stub
		db = new DBAdapter(context, DBConstant.TABLE_TODOLIST);
		dc = new DBAdapter(context, DBConstant.TABLE_TODOLIST_CHANGELIST);
	}
	public void addItems(ArrayList<SyncItem> items) {
		// TODO Auto-generated method stub
		for(int i=0;i<items.size();i++){
			SyncItem addItem = items.get(i);
			byte[] content = addItem.getContent();
			TodoRecord todo = new TodoRecord();
			todo.unpack(content);
			ContentValues values = todo.getContentValues();
			db.insert(values);			
		}
	}
	public void deleteItems(ArrayList<SyncItem> items) {
		// TODO Auto-generated method stub
		for(int i=0;i<items.size();i++){
			SyncItem deleteItem = items.get(i);
			byte[] content = deleteItem.getContent();
			TodoRecord todo = new TodoRecord();
			todo.unpack(content);
			db.delete(Todolist.CRT_TIME + " = "+todo.getCrtTime(), null);
		}
	}
	public void updateItems(ArrayList<SyncItem> items) {
		// TODO Auto-generated method stub
		for(int i=0;i<items.size();i++){
			SyncItem updateItem = items.get(i);
			byte[] content = updateItem.getContent();
			TodoRecord todo = new TodoRecord();
			todo.unpack(content);
			ContentValues values = todo.getContentValues();
			db.update(values, Todolist.CRT_TIME + " = "+todo.getCrtTime(), null);
		}
	}
	
	public void fillItems(ArrayList<SyncItem> items) {
		// TODO Auto-generated method stub
		for(int i=0;i<items.size();i++){
			SyncItem item = items.get(i);
			long crtTime = Long.parseLong(item.getKey());
			Cursor cursor = db.query(null, Todolist.CRT_TIME+" = "+crtTime, null, null);
			if(cursor.moveToFirst()){
				long beginTime = cursor.getLong(cursor.getColumnIndex(Todolist.BEGIN_TIME_LONG));
				long endTime = cursor.getLong(cursor.getColumnIndex(Todolist.END_TIME_LONG));
				long alertTime = cursor.getLong(cursor.getColumnIndex(Todolist.ALERT_TIME));
				String userId = cursor.getString(cursor.getColumnIndex(Todolist.USER_ID));
				String location = cursor.getString(cursor.getColumnIndex(Todolist.LOCATION));
				String title = cursor.getString(cursor.getColumnIndex(Todolist.TITLE));
				String content = cursor.getString(cursor.getColumnIndex(Todolist.CONTENT));
				int state = cursor.getInt(cursor.getColumnIndex(Todolist.STATE));
				int importance = cursor.getInt(cursor.getColumnIndex(Todolist.IMPORTANCE));
				int warnFreq = cursor.getInt(cursor.getColumnIndex(Todolist.WARN_FREQUENCY));
				String tag = cursor.getString(cursor.getColumnIndex(Todolist.TAG));
				
				TodoRecord record = new TodoRecord(userId, title, content, location, tag, crtTime,
						beginTime, endTime, alertTime, state, importance, warnFreq);
				byte[] SyncItemCotent = record.pack();
				
				item.setContent(SyncItemCotent);
			}
			cursor.close();
		}
	}

	
	public ArrayList<SyncItem> getAllItems() {
		// TODO Auto-generated method stub
		ArrayList<SyncItem> all = new ArrayList<SyncItem>();
		Cursor cursor = db.query(null, null, null, null);
		if(cursor.getCount()!=0){
			cursor.moveToFirst();
			do{			
				long crtTime = cursor.getLong(cursor.getColumnIndex(Todolist.CRT_TIME));			
				long beginTime = cursor.getLong(cursor.getColumnIndex(Todolist.BEGIN_TIME_LONG));
				long endTime = cursor.getLong(cursor.getColumnIndex(Todolist.END_TIME_LONG));
				long alertTime = cursor.getLong(cursor.getColumnIndex(Todolist.ALERT_TIME));
				String userId = cursor.getString(cursor.getColumnIndex(Todolist.USER_ID));
				String location = cursor.getString(cursor.getColumnIndex(Todolist.LOCATION));
				String title = cursor.getString(cursor.getColumnIndex(Todolist.TITLE));
				String content = cursor.getString(cursor.getColumnIndex(Todolist.CONTENT));
				int state = cursor.getInt(cursor.getColumnIndex(Todolist.STATE));
				int importance = cursor.getInt(cursor.getColumnIndex(Todolist.IMPORTANCE));
				int warnFreq = cursor.getInt(cursor.getColumnIndex(Todolist.WARN_FREQUENCY));
				String tag = cursor.getString(cursor.getColumnIndex(Todolist.TAG));;
					
				TodoRecord record = new TodoRecord(userId, title, content, location, tag, crtTime,
							beginTime, endTime, alertTime, state, importance, warnFreq);
				byte[] SyncItemCotent = record.pack();
				SyncItem item  = new SyncItem(String.valueOf(crtTime), "text/plain", SyncItemCotent, 'N', -1);					 
				all.add(item);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return all;
	}
	public ArrayList<SyncItem> getOptimizedChangedItemState() {
		// TODO Auto-generated method stub
		ArrayList<SyncItem> changes = new ArrayList<SyncItem>();
		ArrayList<Long> visit = new ArrayList<Long>();		
		Cursor cursor = dc.query(null, null, null, null);
		if(cursor.getCount()!=0){
			cursor.moveToFirst();
			do{
				long item_id = cursor.getLong(cursor.getColumnIndex(ChangeList.ITEM_ID));
				SyncItem item = new SyncItem();
				
				/**如果此item_id没有被 访问过**/
				if(!visit.contains(item_id)){
					item.setKey(String.valueOf(item_id));
					Cursor c = dc.query(null, ChangeList.ITEM_ID + " = "+item_id, null, null);
					c.moveToFirst();
					char firstOperation = (char) c.getInt(c.getColumnIndex(ChangeList.OPERATION));
					c.moveToLast();
					char lastOperation = (char) c.getInt(c.getColumnIndex(ChangeList.OPERATION));
					/**如果只有一条数据则这个item_id只进行过一次操作**/
					if(c.getCount()==1){																		
						item.setState(firstOperation);						
					}
					else{
						if(firstOperation=='N'&&lastOperation=='U'){
							item.setState(firstOperation);
						}else{
							if(firstOperation=='U'&&lastOperation=='D')
								item.setState(lastOperation);
							else{
								if(firstOperation=='U'&&lastOperation=='U')
									item.setState(firstOperation);
							}
						}
					}
					changes.add(item);
					visit.add(item_id);	
					c.close();
				}
								
			}while(cursor.moveToNext());
		}
		cursor.close();
		return changes;
	}
	public ArrayList<SyncItem> getChangedItemState() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void clearChangeList(){
		dc.delete(null, null);
	}
}
