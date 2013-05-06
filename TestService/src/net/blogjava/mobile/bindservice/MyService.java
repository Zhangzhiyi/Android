package net.blogjava.mobile.bindservice;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

public class MyService extends Service
{
	public static final String TAG = "MyService";
	
	/**说说service与activity共存亡的情况：
	 * 1、startService->销毁Activity，service存在
	 * 2、startService->bindService->销毁Activity service存在
	 * 3、bindService->销毁Activity service销毁
	 * **/
    private MyBinder myBinder = new MyBinder();
	@Override
	public IBinder onBind(Intent intent)
	{
		Log.d("MyService", "onBind");
		return myBinder;
	}
	@Override
	public void onCreate()
	{
		Log.d("MyService", "onCreate");
		/**打印进程号,判断与服务是否在同一进程**/
		/**因为没有在配置xml文件设定service的remote属性,所以就算这里有用binder也是在同一进程的**/
        int pid = Process.myPid();
        Log.d("MyService:pid", String.valueOf(pid));
		super.onCreate();
		stopSelf();
	}

	@Override
	public void onDestroy()
	{
		Log.d("MyService", "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onRebind(Intent intent)
	{
		Log.d("MyService", "onRebind");
		super.onRebind(intent);
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		//重复调用startService，startId递增
		Log.d("MyService", "onStart :" + startId);
		super.onStart(intent, startId);
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		Log.d("MyService", "onUnbind");
		
		return super.onUnbind(intent);
	}
	/**这种绑定服务只适合本地服务绑定，远程服务绑定要用AIDL，具体参考工程TestRemoteService**/
	public class MyBinder extends Binder
	{
		MyService getService()
		{
			return MyService.this;
		}
	}
	
	public void callBack(){
		Log.i(TAG, "callBack");
	}
}
