package com.et.Activity;

import android.app.Activity;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class TransitionDrawableActivity extends Activity {
		
	private Button mButton01;
	private Button mButton02;
	private ImageButton mImageButton01;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transitiondrawable);
		
		mButton01 = (Button) findViewById(R.id.button1);
		mButton02 = (Button) findViewById(R.id.button2);
		mImageButton01 = (ImageButton) findViewById(R.id.imageButton1);
		
		mButton01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/**TransitionDrawable能够实现渐隐渐显的效果**/
				TransitionDrawable drawable = (TransitionDrawable) mImageButton01.getDrawable();
				drawable.setCrossFadeEnabled(true);
				drawable.startTransition(800);
			}
		});
		mButton02.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TransitionDrawable drawable = (TransitionDrawable) mImageButton01.getDrawable();
				drawable.setCrossFadeEnabled(true);
				drawable.reverseTransition(800);
			}
		});
		mImageButton01.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				TransitionDrawable drawable = (TransitionDrawable) mImageButton01.getDrawable();
				drawable.setCrossFadeEnabled(true);
				drawable.reverseTransition(800);
				mImageButton01.postDelayed(this, 2000);
			}
		});
	}

}
