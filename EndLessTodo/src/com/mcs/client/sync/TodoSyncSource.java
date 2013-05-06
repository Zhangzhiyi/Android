package com.mcs.client.sync;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;

import com.mcs.client.android.service.MCSService;
import com.mcs.todo.db.ChangeList;
import com.mcs.todo.db.DBAdapter;
import com.mcs.todo.db.DBConstant;
import com.mcs.todo.db.Todolist;


public class TodoSyncSource implements SyncSource{

	private static final Logger logger = LoggerFactory.getLogger("SyncSource");
	private DBAdapter db;
	private DBAdapter dc;
	private SharedPreferences mSharePreferences;
	private Context context = MCSService.getInstance();
	public TodoSyncSource(String id,String dataType) {
		// TODO Auto-generated constructor stub
		db = new DBAdapter(context, DBConstant.TABLE_TODOLIST);
		dc = new DBAdapter(context, DBConstant.TABLE_TODOLIST_CHANGELIST);
		mSharePreferences = context.getSharedPreferences("com.mcs.todo_preferences", Context.MODE_PRIVATE);
	}
	/**慢同步用到,可能是客户端或者服务器没有同步过,所以返回todolist表的所有数据,不是changlist**/
	@Override
	public ArrayList<SyncItem> getAllItems() {
		// TODO Auto-generated method stub
		logger.info("Get all item from source.");
		ItemState.flag = false;
		Message msg = new Message();
		msg.what = ItemState.ALL_ITEM_MSG;
		MCSService.mServiceHandler.sendMessage(msg);
		while(!ItemState.flag){
			
		}
		return ItemState.allItems;
		
		
	}
	@Override
	public ArrayList<SyncItem> getOptimizedChangedItemState(long timeline) {
		// TODO Auto-generated method stub
		logger.info("Prepare Optimizedchangelist.");
		ItemState.flag = false;
		Message msg = new Message();
		msg.what = ItemState.OPTIMIZED_ITEM_MSG;
		MCSService.mServiceHandler.sendMessage(msg);
		while(!ItemState.flag){
			
		}
		return ItemState.optimizedItems;
		
	}
	@Override
	public ArrayList<SyncItem> getChangedItemState(long timeline) {
		// TODO Auto-generated method stub
		logger.info("Prepare changelist.");
		
		return null;
		
	}
	@Override
	public void fillItems(ArrayList<SyncItem> items) {
		// TODO Auto-generated method stub
		logger.info("Fill items: {}.", items.size());
		ItemState.flag = false;
		Message msg = new Message();
		msg.what = ItemState.FILL_ITEM_MSG;
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList(ItemState.FILL_ITEM_KEY, items);
		msg.setData(bundle);		
		MCSService.mServiceHandler.sendMessage(msg);
		while(!ItemState.flag){
			
		}
		items = ItemState.fillItems;
	}	
	
	@Override
	public void addItems(ArrayList<SyncItem> items) {
		// TODO Auto-generated method stub
		logger.info("Add items: {}.", items.size());
		Message msg = new Message();
		msg.what = ItemState.ADD_ITEM_MSG;
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList(ItemState.ADD_ITEM_KEY, items);
		msg.setData(bundle);		
		MCSService.mServiceHandler.sendMessage(msg);
		
	
	}
	@Override
	public void updateItems(ArrayList<SyncItem> items) {
		// TODO Auto-generated method stub
		logger.info("Update items: {}.", items.size());
		Message msg = new Message();
		msg.what = ItemState.UPDATE_ITEM_MSG;
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList(ItemState.UPDATE_ITEM_KEY, items);
		msg.setData(bundle);		
		MCSService.mServiceHandler.sendMessage(msg);
	
	}
	@Override
	public void deleteItems(ArrayList<SyncItem> items) {
		// TODO Auto-generated method stub
		logger.info("Delete items: {}.", items.size());
		Message msg = new Message();
		msg.what = ItemState.DELETE_ITEM_MSG;
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList(ItemState.DELETE_ITEM_KEY, items);
		msg.setData(bundle);		
		MCSService.mServiceHandler.sendMessage(msg);
		

	}
	

	@Override
	public TimeAnchor loadTimeAnchor() {
		// TODO Auto-generated method stub
		long last = mSharePreferences.getLong("last", SyncCode.INVALID_TIMEANCHOR);
		long next = mSharePreferences.getLong("next", SyncCode.INVALID_TIMEANCHOR);
		TimeAnchor anchor = new  TimeAnchor(last, next);
		return anchor;
	}

	@Override
	public void saveTimeAnchor(TimeAnchor anchor) {
		// TODO Auto-generated method stub
		long last = anchor.getLast();
		long next = anchor.getNext();
		mSharePreferences.edit()
		.putLong("last", last)
		.putLong("next", next)
		.commit();		
	}
	
	
}