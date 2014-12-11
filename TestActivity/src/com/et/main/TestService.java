package com.et.main;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TestService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		TestStatic.getInstance().startServiceLoop();
		return super.onStartCommand(intent, flags, startId);
	}
}
