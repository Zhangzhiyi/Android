package com.et.testapplication;

import java.util.HashMap;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.os.Process;
import android.util.Log;

public class MyApplication extends Application {

	public static final String TAG = MyApplication.class.getSimpleName();
	/** 贯穿整个程序的全局变量 **/
	String name = "ET";

	/**
	 * 一个组件附属所在进程刚开始创建运行的时候肯定会运行Application的onCreate()方法，
	 * 所以多进程的时候会运行多次Application的onCreate方法，即有多个Application变量
	 */
	public static Object objcet = null;
	
	
	public static HashMap<String, PackageInfo> UPDATE_APP_MAP = new HashMap<String, PackageInfo>();
	public static void setUPDATE_APP_MAP(HashMap<String, PackageInfo> uPDATE_APP_MAP) {
		UPDATE_APP_MAP = uPDATE_APP_MAP;
//		UPDATE_APP_MAP.clear();
//		UPDATE_APP_MAP.putAll(uPDATE_APP_MAP);
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, "onCreate():" + Process.myPid());

		startService(new Intent(this, ServiceA.class));
		startService(new Intent(this, ServiceB.class));
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		Log.i(TAG, "onLowMemory()");
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		Log.i(TAG, "onTerminate()");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
