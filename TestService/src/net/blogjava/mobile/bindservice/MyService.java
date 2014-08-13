package net.blogjava.mobile.bindservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

public class MyService extends Service {
	public static final String TAG = "MyService";
	private volatile Looper mServiceLooper;
	private volatile ServiceHandler mServiceHandler;
	public static final int MSG_HANDLER_ONE = 1;
	public static final int MSG_CANCEL_ALARM = 2;
	public static final int MSG_ELAPSED_TIME = 3;
	private AlarmManager mAlarmManager;
	private final class ServiceHandler extends Handler {
		private int count = 0;
		public ServiceHandler(Looper looper) {
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_HANDLER_ONE:
				//目前用腾讯安全管家还有360卫士杀掉进程之后Handler都不能收发信息
				count ++;
				Log.i(TAG, "handleMessage:" + count);
				mServiceHandler.sendEmptyMessageDelayed(MSG_HANDLER_ONE, 1000);
				break;
			case MSG_CANCEL_ALARM:
				Intent receiver = new Intent(MyService.this, SampleAlarmReceiver.class);
				receiver.setAction(SampleAlarmReceiver.ACTION_ALARM_RECEIVER);
				PendingIntent alarmIntent = PendingIntent.getBroadcast(MyService.this, 0, receiver, 0);
				mAlarmManager.cancel(alarmIntent);
				break;
			case MSG_ELAPSED_TIME:
				//运行两次耗时的操作，两次操作是串行的，并不是并行的
				int i = 0;
				while (i < 1000) {
					i ++;
					Log.i(TAG, "" + i);
				}
				break;
			}
		}
	}
	/**
	 * 说说service与activity共存亡的情况： 1、startService->销毁Activity，service存在
	 * 2、startService->bindService->销毁Activity service存在
	 * 3、bindService->销毁Activity service销毁
	 * **/
	private MyBinder myBinder = new MyBinder();

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind");
		return myBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
		HandlerThread thread = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		mServiceHandler.sendEmptyMessageDelayed(MSG_HANDLER_ONE, 3000);
		/** 打印进程号,判断与服务是否在同一进程 **/
		/** 因为没有在配置xml文件设定service的remote属性,所以就算这里有用binder也是在同一进程的 **/
		int pid = Process.myPid();
		Log.d("MyService:pid", String.valueOf(pid));
		
		mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
		mServiceLooper.quit();
	}

	@Override
	public void onRebind(Intent intent) {
		Log.d(TAG, "onRebind");
		super.onRebind(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// 重复调用startService，startId递增
		Log.d(TAG, "onStart :" + startId);
		// 在设置-应用程序管理把当前的Service结束掉AlarmManager还会继续跑。
		//目前用腾讯安全管家还有360卫士杀掉进程在某些机型上AlarmManager还是会继续跑如Nexus 5，而有些有不能跑了如三星9100。
		// 如果在小米手机上如果应用的自动启动权限(只有MIUI系统才有这个权限)是关闭的话，用小米系统自带的清理工具杀掉当前应用进程那么AlarmManager是不会在后台运行的.
		// 还有问题要注意，在MIUI开发版上AlarmManager是不起作用的
		Intent receiver = new Intent(SampleAlarmReceiver.ACTION_ALARM_RECEIVER);
//		receiver.setAction(SampleAlarmReceiver.ACTION_ALARM_RECEIVER);
		//根据FLAG_NO_CREATE参数返回值来是否已经创建了PendingIntent来判断是否起了AlarmManager。如果创建了就返回之前创建的PendingIntent，否则就不创建直接返回null
		PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, receiver, PendingIntent.FLAG_NO_CREATE);
		if (alarmIntent == null) {
			Log.i(TAG, "Alarm now create!");
			alarmIntent = PendingIntent.getBroadcast(this, 0, receiver, 0);
			mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, 1000, 1000, alarmIntent);
//			mAlarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 5000, alarmIntent);
		}else{
			Log.i(TAG, "Alarm is already active!");
		}
		//第二个参数requestCode来唯一标识每一个PendingIntent.
		PendingIntent alarmIntent2 = PendingIntent.getBroadcast(this, 1, receiver, PendingIntent.FLAG_NO_CREATE); //alarmIntent2 = null
		//10秒发消息取消此AlarmManager服务
//		mServiceHandler.sendEmptyMessageDelayed(MSG_CANCEL_ALARM, 10000);
		
//		mServiceHandler.sendEmptyMessage(MSG_ELAPSED_TIME);
//		mServiceHandler.sendEmptyMessage(MSG_ELAPSED_TIME);
		return START_STICKY;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(TAG, "onUnbind");
		return super.onUnbind(intent);
	}

	/** 这种绑定服务只适合本地服务绑定，远程服务绑定要用AIDL，具体参考工程TestRemoteService **/
	public class MyBinder extends Binder {
		MyService getService() {
			return MyService.this;
		}
	}

	public void callBack() {
		Log.i(TAG, "callBack");
	}
}
