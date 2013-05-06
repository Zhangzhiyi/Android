package com.yao_guet.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class TestScrollLayout extends Activity {
	
	private CustomScrollLayout mScrollLayout;
	private Button mButton;
	private int[] drawables = {R.drawable.a1, R.drawable.a2, R.drawable.a3,
							   R.drawable.a4, R.drawable.a5, R.drawable.a6, 
							   R.drawable.a7, R.drawable.a8};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        setContentView(R.layout.scrolllayout_gallery);   
        mButton = (Button) findViewById(R.id.button1);
        mScrollLayout = (CustomScrollLayout) findViewById(R.id.ScrollLayoutTest);
        for (int i = 0; i < drawables.length; i++) {
			ImageView mImageView = new ImageView(this);
			mImageView.setScaleType(ScaleType.FIT_CENTER);
			mImageView.setImageResource(drawables[i]);
			mImageView.setOnTouchListener(mOnTouchListener);
			mImageView.setId(i);
			mScrollLayout.addView(mImageView);
		}
        //mScrollLayout.requestLayout();
        mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mScrollLayout.snapToScreen(2); 
			}
		});
    }
    OnTouchListener mOnTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			Log.i("1111", "1111");
			return false;
		}
	};
}