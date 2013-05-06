package com.et.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class LevelListActivity extends Activity {
	
	private Button mButton01;
	private Button mButton02;
	private ImageView mImageView01;
	private ImageView mImageView02;
	private int index = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.levellistdrawable);
		
		mButton01 = (Button) findViewById(R.id.button1);
		mButton02 = (Button) findViewById(R.id.button2);
		mImageView01 = (ImageView) findViewById(R.id.imageView1);
		mImageView02 = (ImageView) findViewById(R.id.imageView2);
		mImageView01.setImageLevel(10000);
		mImageView02.setImageLevel(10000);
		mButton01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (index) {
					case 0:
						mImageView01.setImageLevel(8000);
						index = 1;
						break;
					case 1:
						mImageView01.setImageLevel(6000);
						index = 2;
						break;
					case 2:
						mImageView01.setImageLevel(3000);
						index = 0;
						break;
				}
			}
		});
		mButton02.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mImageView02.setImageLevel(mImageView02.getDrawable().getLevel()-1000);
			}
		});
	}
}
