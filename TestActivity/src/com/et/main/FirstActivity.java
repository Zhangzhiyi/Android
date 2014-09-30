package com.et.main;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;


public class FirstActivity extends Activity {
	public static final String TAG = "FirstActivity";
	//静态Activity变量，如果不在onDestroy强行置null，那么FirstActivity永远都不会释放
//	static FirstActivity mFirstActivity ;
	
	static Object object = new Object();
	
	MyHandler myHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		mFirstActivity = this;
		Log.i(TAG, "onCreate");
		 TextView label = new TextView(this);
		  label.setText("Leaks are bad");
		  
		  setContentView(label);
		  
		  myHandler = new MyHandler(this);
		  myHandler.sendEmptyMessageDelayed(0, 3000);
	}
	static class MyHandler extends Handler{
		private Context context;
		public MyHandler(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
		}
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			sendEmptyMessageDelayed(0, 3000);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(TAG, "onStart");
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume");
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "onPause");
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "onStop");
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "onDestroy");
//		mFirstActivity = null;
	}
}
