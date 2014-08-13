package com.example.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TestMusicControl extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		intent.getBooleanExtra("play", true);
		Toast.makeText(context, "music is playing", Toast.LENGTH_SHORT).show();
	}

}
