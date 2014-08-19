package com.et.testapplication;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

public class MyApplication extends Application {
	
	public static final String TAG = MyApplication.class.getSimpleName();
	/**贯穿整个程序的全局变量**/
	String name = "ET";
	
	public static List<String> list = null;
	/**
	 * 一个组件附属所在进程刚开始创建运行的时候肯定会运行Application的onCreate()方法，所以多进程的时候会运行多次Application的onCreate方法，即有多个Application变量
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, "onCreate()");
		if (list == null) {
			Log.i(TAG, "create static List!");
			list = new ArrayList<String>();
		}
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
