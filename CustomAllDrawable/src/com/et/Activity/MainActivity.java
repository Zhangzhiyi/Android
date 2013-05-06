package com.et.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{
	
	
	private Button mButton01;
	private Button mButton02;
	private Button mButton03;
	private Button mButton04;
	private Button mButton05;
	int index = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mButton01 = (Button) findViewById(R.id.button1);
        mButton02 = (Button) findViewById(R.id.button2);
        mButton03 = (Button) findViewById(R.id.button3);
        mButton04 = (Button) findViewById(R.id.button4);
        mButton05 = (Button) findViewById(R.id.button5);
        mButton01.setOnClickListener(this);
        mButton02.setOnClickListener(this);
        mButton03.setOnClickListener(this);
        mButton04.setOnClickListener(this);
        mButton05.setOnClickListener(this);
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.button1:
				Intent mIntent01 = new Intent(MainActivity.this, ClipDrawableActivity.class);
				startActivity(mIntent01);
				break;
			case R.id.button2:
				Intent mIntent02 = new Intent(MainActivity.this, LevelListActivity.class);
				startActivity(mIntent02);
				break;
			case R.id.button3:
				Intent mIntent03 = new Intent(MainActivity.this, InsetDrawableActivity.class);
				startActivity(mIntent03);
				break;
			case R.id.button4:
				Intent mIntent04 = new Intent(MainActivity.this, TransitionDrawableActivity.class);
				startActivity(mIntent04);
				break;
			case R.id.button5:
				Intent mIntent05 = new Intent(MainActivity.this, TestImageViewActivity.class);
				startActivity(mIntent05);
				break;

		}
	}
}