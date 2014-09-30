package com.et.main;
import com.et.testactivity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity {
	public static final String TAG = "MainActivity";
	private Button mFirstBtn;
	
	static Object object ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		object = FirstActivity.object;
		Log.i(TAG, "onCreate");
		setContentView(R.layout.main_layout);
		
		mFirstBtn = (Button) findViewById(R.id.first_button);
		mFirstBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, FirstActivity.class);
				startActivity(intent);
			}
		});
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
	}
}
