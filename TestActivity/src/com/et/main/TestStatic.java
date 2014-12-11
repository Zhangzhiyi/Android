package com.et.main;

import java.util.concurrent.TimeUnit;

import android.util.Log;

public class TestStatic {
	public static final String TAG = "TestStatic";
	private static TestStatic mInstance = null;
	private TestStatic() {
		
	}
	public static TestStatic getInstance(){
		if (mInstance == null) {
			mInstance = new TestStatic();
		}
		return mInstance;
	}
	public void startServiceLoop() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int count = 0;
				while (true) {
					count++;
					Log.i(TAG, "Services:" + count);
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	public void startActivityLoop() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int count = 0;
				while (true) {
					count++;
					Log.i(TAG, "Activity:" + count);
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
