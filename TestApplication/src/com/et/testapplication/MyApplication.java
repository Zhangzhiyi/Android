package com.et.testapplication;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

public class MyApplication extends Application {
	
	public static final String TAG = MyApplication.class.getSimpleName();
	/**贯穿整个程序的全局变量**/
	String name = "ET";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onCreate()");
		super.onCreate();
	}
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onLowMemory()");
		super.onLowMemory();
	}
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onTerminate()");
		super.onTerminate();
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
