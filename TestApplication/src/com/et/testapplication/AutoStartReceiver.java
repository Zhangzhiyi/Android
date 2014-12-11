package com.et.testapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AutoStartReceiver extends BroadcastReceiver {
	public static final String TAG = "AutoStartReceiver";
	public static final String ACTION_AUTO_START = "com.action.auto_start";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (action.equals(ACTION_AUTO_START)) {
			Log.i(TAG, "AutoStartReceiver:" + ACTION_AUTO_START);
		}
	}

}
