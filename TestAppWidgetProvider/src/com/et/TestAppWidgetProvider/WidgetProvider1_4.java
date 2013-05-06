package com.et.TestAppWidgetProvider;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class WidgetProvider1_4 extends AppWidgetProvider {
		
	//测试Widget的生命周期
	/**PendingIntent使用总结：
	 * PendingIntent封装Intent的注意事项：要注意创建PendingIntent封装的Intent的是否相同。
	 * 判断Intent是否同一个，就要判断Intent的action,data,category三者是否都相同。如果创建多个PendingIntent包装相同Intent,
	 * 则PendingIntent都是相同的。所以此时要注意PendingIntent的 getActivity(Context, int, Intent, int), 
	 * getBroadcast(Context, int, Intent, int), and getService(Context, int, Intent, int)第四个参数根据需求而变化。
	 * 例如FLAG_UPDATE_CURRENT可以更新Intent中的extra。如果第四个参数设为0,则获取各个PendingIntent封装的Intent中extra
	 * 都是第一个创建PendingIntent对象封装中的Intent的extra。**/
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
			Log.i(WIDGET, "appWidgetId:"+i+":"+String.valueOf(appWidgetIds[i]));
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget1_4);
			Intent intent = new Intent();
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			intent.setAction("com.et.WidgetProvider1_4.action");
			//点击按钮发送Intent让自身广播接收
			/**注意：PendingIntent第四个参数一定要用PendingIntent.FLAG_UPDATE_CURRENT,否则在onReceive方法获取的id一直是0
			 * PendingIntent.FLAG_UPDATE_CURRENT参数可以更新PendingIntent封装中的Intent的extra中的数据
			 * **/
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			//发现并没有运行onUpdate方法
			views.setOnClickPendingIntent(R.id.jifen, pendingIntent);
			AppWidgetManager.getInstance(context).updateAppWidget(appWidgetIds[i], views);
		
		}
		
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		Log.i(WIDGET, "onReceive");
		if(intent.getAction().equals("com.et.WidgetProvider1_4.action")){
			Bundle bundle = intent.getExtras();
			//点击widget上的按钮进入广播系统并不能自动识别widget的id,要手动putid到intent里面
			//删除widget时系统可以识别widget的id
			if(bundle!=null){
				int id = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
				Log.i(WIDGET, "onReceive_appWidgetId:"+String.valueOf(id));
				/*int[] appWidgetIds = bundle.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
				for(int i=0;i<appWidgetIds.length;i++){
					Log.i(WIDGET, "appWidgetId[]:"+String.valueOf(appWidgetIds[i]));
				}*/
				Intent sIntent = new Intent(context, UpdateService.class);
				sIntent.putExtra("widgetId", id);
				context.startService(sIntent);
			}
		}
		
		
	}
	public static void updateRemoteView(Context context, int widgetId){
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget1_4);
		views.setViewVisibility(R.id.progressbar, View.GONE);
		int color = Color.BLACK;
		views.setInt(R.id.TextView01, "setTextColor", color);
		/**注意:这个更新方法可以在外部service运行更新widget***/
		//AppWidgetManager.getInstance(this).updateAppWidget(compoent,remoteViews);
		/**注意:这个更新方法要在重写了AppWidgetProvider类内部运用才可以更新widget的,在外部service运行是无效果的
		 * 具体可以看SDK**/
		AppWidgetManager.getInstance(context).updateAppWidget(widgetId,views);
	}
	
	/** 更新widget的Service 很奇怪,内部服务类要变成静态启动才不会出错? */
	public static class UpdateService extends Service {
		@Override
		public void onCreate() {
			// TODO Auto-generated method stub
			super.onCreate();
		}
		@Override
	    public int onStartCommand(Intent intent, int flags, int startId) {			
			// widget更新不需要保证每次都调用好
			int widgetId = intent.getIntExtra("widgetId", 0);
			if(widgetId!=0)
				updateRemoteView(this, widgetId);
			return START_NOT_STICKY;
		}
		@Override
		public IBinder onBind(Intent arg0) {
			return null;
		}
	}
}
