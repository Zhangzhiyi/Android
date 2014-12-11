package net.blogjava.mobile.bindservice;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
	
	public static final String TAG = "MyApplication";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, "onCreate");
		
	}
}
