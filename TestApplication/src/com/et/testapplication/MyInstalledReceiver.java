package com.et.testapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyInstalledReceiver extends BroadcastReceiver {
	public static final String TAG = "MyInstalledReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String packageName = intent.getData().getEncodedSchemeSpecificPart();
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
			Log.i(TAG, "ACTION_PACKAGE_ADDED:" + packageName);
		} else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
			Log.i(TAG, "ACTION_PACKAGE_REMOVED:" + packageName);
		} else if (action.equals(Intent.ACTION_PACKAGE_REPLACED)) {
			Log.i(TAG, "ACTION_PACKAGE_REPLACED:" + packageName);
		} else if (action.equals(Intent.ACTION_PACKAGE_RESTARTED)) {
			Log.i(TAG, "ACTION_PACKAGE_RESTARTED:" + packageName);
		}
	}
}
