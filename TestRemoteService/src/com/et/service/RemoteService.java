package com.et.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.et.Activity.TestActivity;
import com.et.Activity.TestThread;

public class RemoteService extends Service {
	
	public static int value = 1;
	
	@Override
	public void onCreate()
	{
		Log.d("RemoteService", "onCreate");
		/**打印进程号,判断与服务是否在同一进程**/
		int pid = Process.myPid();
		Log.i("RemoteService Process ID", String.valueOf(pid));
				
		/**Handler只能在同一进程内传递消息**/
		Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				Toast.makeText(RemoteService.this, "service", Toast.LENGTH_SHORT).show();
				value = 77;
				Constant.flag = true;
				super.handleMessage(msg);
			}
		};
		/**在这里启动线程,线程与这个远程service在同一进程**/
		new Thread(new TestThread(handler)).start();
		
		/**在这里启动activity,启动的activity与这个service是不在同一进程的，奇怪的是又与MainActivity同一进程了**/
		Intent mIntent = new Intent(RemoteService.this,TestActivity.class);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//没有这句会出错
		//startActivity(mIntent);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("RemoteService", "onStart");
		
		//TestServiceActivity.handler.sendEmptyMessage(1);
		
		return START_STICKY;
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("RemoteService", "onBind");
		if(intent.getAction().equals("com.et.service.IMyService"))
			return IMyService;
		else
			return null;
	}
	@Override
	public boolean onUnbind(Intent intent)
	{
		Log.d("RemoteService", "onUnbind");
		
		return super.onUnbind(intent);
	}
	@Override
	public void onDestroy()
	{
		Log.d("RemoteService", "onDestroy");
	}

	@Override
	public void onRebind(Intent intent)
	{
		Log.d("RemoteService", "onRebind");
		super.onRebind(intent);
	}	
	public static void setHandler(Handler h){
		
	}
	
	
	private final IMyService.Stub IMyService = new IMyService.Stub() {
		
		@Override
		public String test() throws RemoteException {
			// TODO Auto-generated method stub
			value = 10;
			Constant.var = 100;
			Log.i("RemoteService value", String.valueOf(value));
			Log.i("RemoteService flag", String.valueOf(Constant.flag));
			return "Test";
			
		}
		/**从客服端传对象到远程服务**/
		@Override
		public void tranObject(Product product) throws RemoteException {
			// TODO Auto-generated method stub
			String s = "";
			s = "Product.id = " + product.getId() + "\n";
			s += "Product.name = " + product.getName()
					+ "\n";
			s += "Product.price = " + product.getPrice()
					+ "\n";
			/**打印从客服端传对象到远程服务product信息**/
			Log.i("Product",s);
			
		}
	};
}
