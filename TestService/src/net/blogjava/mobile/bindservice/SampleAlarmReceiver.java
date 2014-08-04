package net.blogjava.mobile.bindservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SampleAlarmReceiver extends BroadcastReceiver {
	public static final String ACTION_ALARM_RECEIVER = "ACTION_ALARM_RECEIVER";
	public static final String TAG = "SampleAlarmReceiver";
	private static int  count = 0;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		count ++ ;
		Log.i(TAG, "onReceive:" + count);
	}

}
