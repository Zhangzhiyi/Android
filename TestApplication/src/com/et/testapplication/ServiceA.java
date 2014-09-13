package com.et.testapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

public class ServiceA extends Service {
	
	public static final String TAG = ServiceA.class.getSimpleName();
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, "onCreate():" + Process.myPid());
		if (MyApplication.objcet == null) {
			Log.i(TAG, "create object");
			MyApplication.objcet  = new Object();
		}
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
