package com.et.TestAppWidgetProvider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

		//测试Widget的生命周期
		public static final String WIDGET = "WidgetProvider";
		@Override
		public void onEnabled(Context context) {
			// TODO Auto-generated method stub
			super.onEnabled(context);
			Log.i(WIDGET, "onEnabled");
		}
		@Override
		public void onDisabled(Context context) {
		// TODO Auto-generated method stub
			super.onDisabled(context);
			Log.i(WIDGET, "onDisabled");
		}
		@Override
		public void onDeleted(Context context, int[] appWidgetIds) {
			// TODO Auto-generated method stub
			super.onDeleted(context, appWidgetIds);
			Log.i(WIDGET, "onDeleted");
		}
		@Override
		public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
			// TODO Auto-generated method stub
			super.onUpdate(context, appWidgetManager, appWidgetIds);
			Log.i(WIDGET, "onUpdate");
			for(int i=0;i<appWidgetIds.length;i++){
				Log.i(WIDGET, "appWidgetId:"+String.valueOf(appWidgetIds[i]));
				RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
				Intent intent = new Intent();
				intent.setAction("com.et.WidgetProvider.action");
				intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
				//点击按钮发送自身广播
				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				//发现并没有运行onUpdate方法
				views.setOnClickPendingIntent(R.id.telfare_query, pendingIntent);
				AppWidgetManager.getInstance(context).updateAppWidget(appWidgetIds[i], views);
			
			}
		}
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			super.onReceive(context, intent);
			Log.i(WIDGET, "onReceive");
			Bundle bundle = intent.getExtras();
			//点击widget上的按钮进入广播系统并不能自动识别widget的id,要手动putid到intent里面
			//删除widget时系统可以识别widget的id
			if(bundle!=null){
				int id = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
				Log.i(WIDGET, "onReceive_appWidgetId:"+String.valueOf(id));
			}
			
		}
		
		
}
