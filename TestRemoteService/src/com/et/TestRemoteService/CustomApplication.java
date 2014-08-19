package com.et.TestRemoteService;

import java.util.List;

import android.app.ActivityManager;
import android.app.Application;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Process;
import android.util.Log;

public class CustomApplication extends Application {
	public static final String TAG = "CustomApplication";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, "onCreate:" + Process.myPid() + " processName:" + getProcessNameByPid(this, Process.myPid()));
	}
	
	public static String getProcessNameByPid(Context context, int pid){
		ActivityManager activityManager = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
		List<RunningAppProcessInfo>processInfos = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : processInfos) {
			if (runningAppProcessInfo.pid == pid) {
				return runningAppProcessInfo.processName;
			}
		}
		return null;
	}
}
