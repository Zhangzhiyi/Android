package com.et.testapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		textView = (TextView) findViewById(R.id.textView1);
		
		MyApplication myApplication = (MyApplication) getApplication();
		textView.setText(myApplication.getName());
	}
	
}
