package com.et.TestMotionEvent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public static final String TAG = "MainActivity";
	private TextView disX;
	private TextView disY;
	
	private TextView touX;
	private TextView touY;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        disX = (TextView) findViewById(R.id.disx);
        disY = (TextView) findViewById(R.id.disy);
        
        touX = (TextView) findViewById(R.id.toux);
        touY = (TextView) findViewById(R.id.touy);
        TelephonyManager telephonyManager=(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String imei=telephonyManager.getDeviceId();
        Log.i(TAG, "imei:" + imei);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    	// TODO Auto-generated method stub
    	int x = (int) ev.getX();
    	int y = (int) ev.getY();
    	disX.setText(String.valueOf(x));
    	disY.setText(String.valueOf(y));
    	/**这个deviceId不是IMEI号**/
    	int deviceId = ev.getDeviceId();
    	Log.i(TAG, "deviceId:" + deviceId);
//    	MotionEvent event = MotionEvent.obtain(ev);
    	/**反转X和Y的坐标**/
//    	event.setLocation(y, x);
//    	return super.dispatchTouchEvent(event);
    	return super.dispatchTouchEvent(ev);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	// TODO Auto-generated method stub
    	int x = (int) event.getX();
    	int y = (int) event.getY();
    	touX.setText(String.valueOf(x));
    	touY.setText(String.valueOf(y));
    	return false;
    }
}