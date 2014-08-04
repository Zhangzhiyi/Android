package net.blogjava.mobile.bindservice;


import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener
{
	private Intent serviceIntent;
	private MyService myService;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//直接创建MyService也可以调用callBack()方法
		MyService myService = new MyService();
		myService.callBack();
		Button btnStartService = (Button) findViewById(R.id.btnStartService);
		Button btnStopService = (Button) findViewById(R.id.btnStopService);
		Button btnBindService = (Button) findViewById(R.id.btnBindService);
		Button btnUnbindService = (Button) findViewById(R.id.btnUnbindService);
		Button btnCallBack = (Button) findViewById(R.id.callback);
		Button btnPrint = (Button) findViewById(R.id.print);
		btnStartService.setOnClickListener(this);
		btnStopService.setOnClickListener(this);
		btnBindService.setOnClickListener(this);
		btnUnbindService.setOnClickListener(this);
		btnCallBack.setOnClickListener(this);
		btnPrint.setOnClickListener(this);
		/**打印进程号,判断与服务是否在同一进程**/
        int pid = Process.myPid();
        Log.d("Main:pid", String.valueOf(pid));
        /**显式启动Service**/
		serviceIntent = new Intent(this, MyService.class);
        /**通过action过滤隐式启动Service,如果在AndroidManifest文件的Service标签的属性android:exported="true",
         * 那么其他工程也可以通过action匹配隐式其他其他工程的Service,如果android:exported="false"则会报错java.lang.SecurityException
         **/
//		serviceIntent = new Intent("com.et.MyService");
	}
	
	/**通过ServiceConnection来获取MyService实例**/
	private ServiceConnection serviceConnection = new ServiceConnection()
	{

		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			myService = null;
			Toast.makeText(Main.this, "Service Failed.", Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			myService = ((MyService.MyBinder) service).getService();
			Toast.makeText(Main.this, "Service Connected.", Toast.LENGTH_LONG)
					.show();

		}
	};

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.btnStartService:
				startService(serviceIntent);
				break;
			case R.id.btnStopService:
				stopService(serviceIntent);
				break;
			case R.id.btnBindService:
				
				bindService(serviceIntent, serviceConnection,
						Context.BIND_AUTO_CREATE);
				break;
			case R.id.btnUnbindService:
				unbindService(serviceConnection);
				break;
			case R.id.callback:
				/**要先绑定服务获取MyService实例才能调用MyService里面的方法**/
				myService.callBack();
				break;
			case R.id.print:
				printServiceName();
				break;
			
		}

	}
	public void printServiceName(){
		ActivityManager manager=(ActivityManager)getSystemService(ACTIVITY_SERVICE);
		List<RunningServiceInfo> mRunningServices = manager.getRunningServices(255);
		for (int i = 0; i < mRunningServices.size(); i++) {
			ComponentName componentName = mRunningServices.get(i).service;
			String packageName = componentName.getPackageName();
			String className = componentName.getShortClassName();
			Log.i(packageName, className);
		}
	}
}