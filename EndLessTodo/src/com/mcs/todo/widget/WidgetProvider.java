package com.mcs.todo.widget;

import java.util.HashMap;
import java.util.Map;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.mcs.todo.Main;
import com.mcs.todo.R;
import com.mcs.todo.db.DBAdapter;
import com.mcs.todo.db.DBConstant;
import com.mcs.todo.db.Todolist;
import com.mcs.todo.db.WidgetSetting;
import com.mcs.todo.res.Strings;
import com.mcs.todo.utils.Time;

public class WidgetProvider extends AppWidgetProvider {
	
	public static final String TAG = "TodoWidget";
	
	/** Uri所用的scheme */
	public static final String URI_SCHEME = "mcs";
	
	// 用于查询的两个字符串
	static String dateSelection;
	static String tagSelection; 
	static String sort = Todolist.BEGIN_TIME_LONG + " DESC";

	static final String[] projection = {
		Todolist._ID,
		Todolist.BEGIN_DATE,
		Todolist.BEGIN_TIME,
		Todolist.TITLE,
		// 用于比较，确定显示颜色
		Todolist.BEGIN_TIME_LONG,
		Todolist.ALERT_TIME,
		Todolist.TAG
	};
	
	public static final String WIDGET_IDS = "widgetIds";
	@Override
	public void onUpdate (Context ctx, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.d(TAG, "on update");
		Intent intent = new Intent(ctx, UpdateService.class);
		intent.putExtra(WIDGET_IDS, appWidgetIds);
		// 在widget中没法绑定服务，只能启动一个临时的更新服务来bind远程服务
		ctx.startService(intent);
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// delete的时候要删除啊
		DBAdapter db = new DBAdapter(context, DBConstant.TABLE_WIDGET_SETTING);
		for(int i = 0; i < appWidgetIds.length; i++) {
			db.delete(WidgetSetting.WIDGET_ID + "=" + appWidgetIds[i], null);
			position.remove(appWidgetIds[i]);
		}
	}
	
	/** Receiver接受的action */
	public static final String ACTION_NEXT = "com.mcs.todo.widget.next";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if(intent.getAction().equals(ACTION_NEXT)) {
			int id = intent.getIntExtra(WidgetSetting.WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			isIncrease.put(id, true);
			int[] ids = {id};
			updateWidget(context, ids);
		}
	}
	
	/** 更新widget */
	public static void updateWidget(Context ctx, int[] widgetIds) {
		AppWidgetManager widgetMgr = AppWidgetManager.getInstance(ctx);
		for(int i = 0; i < widgetIds.length; i++) {
			RemoteViews rViews = getUpdatedViews(ctx, widgetIds[i]);
			widgetMgr.updateAppWidget(widgetIds[i], rViews);
		}
	}
	
	/** 记录不同的widget上cursor当前的位置 */
	static Map<Integer, Integer> position = new HashMap<Integer, Integer>();
	/** 指定id相应的值如果是true，那么nextPos在返回位置的同时，要position++ */
	static Map<Integer, Boolean> isIncrease = new HashMap<Integer, Boolean>();
	
	/** 返回Cursor的下一个位置 */
	static int nextPos(int id, int count) {
		Integer pos = position.get(id);
		Boolean incre = isIncrease.get(id);
		if(null != pos){
			if (null != incre && incre) {
				pos++;
				if (pos < count) {
					position.put(id, pos);
				} else {
					// 到了最后一个就返回到第一个
					position.put(id, 0);
					pos = 0;
				}
				// 只是收到广播以后的那一次是增加的
				isIncrease.put(id, false);
			}
		}else {
			// Cursor之前没有移动过，posList返回的pos就是null，所以设置位置为0；
			position.put(id, 0);
			pos = 0;
		}
		return pos;
	}
	
	/** 改变显示的模式 */
	static void changeMode(RemoteViews rViews, int mode) {
		switch(mode) {
		case WidgetSetting.MODE_DATE:
			rViews.setViewVisibility(R.id.widget_datebox, View.VISIBLE);
			rViews.setViewVisibility(R.id.widget_tag, View.INVISIBLE);
			// 在日期模式下右边小消息显示标签
			rViews.setViewVisibility(R.id.widget_tagright, View.VISIBLE);
			rViews.setViewVisibility(R.id.widget_timeright, View.INVISIBLE);
			break;
		case WidgetSetting.MODE_TAG:
			rViews.setViewVisibility(R.id.widget_datebox, View.INVISIBLE);
			rViews.setViewVisibility(R.id.widget_tag, View.VISIBLE);
			rViews.setViewVisibility(R.id.widget_tagright, View.INVISIBLE);
			rViews.setViewVisibility(R.id.widget_timeright, View.VISIBLE);
			break;
		}
	}
	
	/** 根据不同的模式来更新 */
	static RemoteViews getUpdatedViews(Context ctx, int widgetId) {
		DBAdapter todoDB = new DBAdapter(ctx, DBConstant.TABLE_TODOLIST);
		DBAdapter settingDB = new DBAdapter(ctx, DBConstant.TABLE_WIDGET_SETTING);
		// 记录各个Id对应的模式
		Map<Integer, Integer> settings = new HashMap<Integer, Integer>();
		// 载入设置
		Cursor cur = settingDB.query(null, null, null, null);
		if(cur != null) {
			if (cur.moveToFirst()) {
				while (!cur.isAfterLast()) {
					// 只是将显示模式载入了设置里面
					settings.put(cur.getInt(cur.getColumnIndex(WidgetSetting.WIDGET_ID)), cur.getInt(cur.getColumnIndex(WidgetSetting.MODE)));
					cur.moveToNext();
				}
			}
			cur.close();
		}
		
		RemoteViews rViews = new RemoteViews(ctx.getPackageName(), R.layout.widget);
		
		Integer mode = settings.get(widgetId);
		
		// 这些辅助变量定义在外面是为了方便后面利用
		String tag = null;
		int count = 0;
		int pos = 0;
		long itemId = -1;
		if(mode != null) {
			// 根据mode改变显示的模式
			changeMode(rViews, mode); 
			switch(mode) {
			case WidgetSetting.MODE_DATE:
				// 设置显示时间
				String today = Time.getTodayString();
				dateSelection = Todolist.BEGIN_DATE + "=" + "'" + today + "'";
				cur = todoDB.query(projection, dateSelection, null, sort);
				if(cur != null) {
					rViews.setTextViewText(R.id.widget_date, Time.getShortDate(today));
					rViews.setTextViewText(R.id.widget_week, Time.getWeek(today));
					if (cur.moveToFirst()) {
						count = cur.getCount();
						pos = nextPos(widgetId, count);
						cur.moveToPosition(pos); // 靠每次update的时候cursor.moveToPosition来实现循环显示待办的功能
						// 获取当前待办的_id
						itemId = cur.getLong(cur.getColumnIndex(Todolist._ID));
						rViews.setTextViewText(R.id.widget_title, cur.getString(cur.getColumnIndex(Todolist.TITLE)));
						rViews.setTextViewText(R.id.widget_tagright, cur.getString(cur.getColumnIndex(Todolist.TAG)));
						rViews.setTextViewText(R.id.widget_time, cur.getString(cur.getColumnIndex(Todolist.BEGIN_TIME)));
						rViews.setTextViewText(R.id.widget_count, String.valueOf(pos+1) + "/" + String.valueOf(count));
					}
					cur.close();
				}else {
					rViews.setTextViewText(R.id.widget_title, Strings.widget_none);
				}
				
				/*// 点击左边的日期进入日期模式主画面
				Intent todoDate = new Intent(ctx, Main.class);
				todoDate.putExtra(WidgetSetting.MODE, WidgetSetting.MODE_DATE);
				todoDate.setData(Uri.parse(URI_SCHEME + "://" + DBConstant.AUTHORITY + "/mode/" + String.valueOf(mode)));
				PendingIntent pTodoDate = PendingIntent.getActivity(ctx, 0, todoDate, PendingIntent.FLAG_UPDATE_CURRENT);
				rViews.setOnClickPendingIntent(R.id.widget_datebox, pTodoDate);*/
				break;
			case WidgetSetting.MODE_TAG:
				// 设置显示标签
				cur = settingDB.query(null, WidgetSetting.WIDGET_ID + "=" + widgetId, null, null);
				if(cur != null) {
					if (cur.moveToFirst()) {
						tag = cur.getString(cur.getColumnIndex(WidgetSetting.TAG));
					}
					cur.close();
				}
				
				if(tag != null) {
					tagSelection = Todolist.TAG + "=" + "'" + tag + "'";
					cur = todoDB.query(projection, tagSelection, null, sort);
					if(cur != null) {
						if (cur.moveToFirst()) {
							count = cur.getCount();
							pos= nextPos(widgetId, count);
							cur.moveToPosition(nextPos(widgetId, count));
							itemId = cur.getLong(cur.getColumnIndex(Todolist._ID));
							rViews.setTextViewText(R.id.widget_tag, tag);
							rViews.setTextViewText(R.id.widget_title, cur.getString(cur.getColumnIndex(Todolist.TITLE)));
							rViews.setTextViewText(R.id.widget_timeright, cur.getString(cur.getColumnIndex(Todolist.BEGIN_TIME)));
							rViews.setTextViewText(R.id.widget_count, String.valueOf(pos+1) + "/" + String.valueOf(count));
						}
						cur.close();
					}else {
						rViews.setTextViewText(R.id.widget_tag, tag);
						rViews.setTextViewText(R.id.widget_title, Strings.widget_none);
					}
				}
				
				/*// 点击昨天的标签，进入标签模式主画面
				Intent todoTag = new Intent(ctx, Main.class);
				todoTag.putExtra(WidgetSetting.MODE, WidgetSetting.MODE_TAG);
				todoTag.putExtra(Todolist.TAG, tag);
				todoTag.setData(Uri.parse(URI_SCHEME + "://" + DBConstant.AUTHORITY + "/mode/" + String.valueOf(mode)));
				PendingIntent pTodoTag = PendingIntent.getActivity(ctx, 0, todoTag, PendingIntent.FLAG_UPDATE_CURRENT);
				rViews.setOnClickPendingIntent(R.id.widget_tag, pTodoTag);*/
				break;
			}
			
			// 点击当前显示的待办，进入相应的画面，并展开那个待办
			Intent item = new Intent(ctx, Main.class);
			/** 加上action来区分Inent**/
			item.setAction(Strings.WIDGET_DISPLAY_ACTION);
			item.putExtra(WidgetSetting.MODE, mode);
			if(mode == WidgetSetting.MODE_TAG && null != tag) {
				item.putExtra(Todolist.TAG, tag);
			}
			// 提供当前显示的待办的_id
			item.putExtra(Todolist._ID, itemId);
			// 在这里加上widgetId的原因是为了防止不同widget中相同项之间的影响
			item.setData(Uri.parse(URI_SCHEME + "://" + DBConstant.AUTHORITY + "/" + widgetId + "/" + String.valueOf(itemId)));
			PendingIntent pItem = PendingIntent.getActivity(ctx, 0, item, PendingIntent.FLAG_UPDATE_CURRENT);
			rViews.setOnClickPendingIntent(R.id.widget_title, pItem);
		}
		
		// 为widget上的下一个按钮设置监听，其实就是发送Broadcast
		Intent next = new Intent(ACTION_NEXT);
		next.putExtra(WidgetSetting.WIDGET_ID, widgetId);
		// 这个非常重要，只有设置了不同的Uri，intent才能被区别开来，这样不同的widget之间才能有不同的行为
		next.setData(Uri.parse(URI_SCHEME + "://" + DBConstant.AUTHORITY + "/widgetid/" + String.valueOf(widgetId)));
		PendingIntent pNext = PendingIntent.getBroadcast(ctx, 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
		rViews.setOnClickPendingIntent(R.id.widget_nextbtn, pNext);
		
		return rViews;
	}
	
	/** 更新widget的Service  */
	public static class UpdateService extends Service {
		@Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
			int[] widgetIds = intent.getIntArrayExtra(WIDGET_IDS);
			updateWidget(this, widgetIds);
			// widget更新不需要保证每次都调用好
			return START_NOT_STICKY;
		}
		@Override
		public IBinder onBind(Intent arg0) {
			return null;
		}
	}
	
}
