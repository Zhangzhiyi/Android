package com.et.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.et.CustomAnimation.CustomAnimation;

public class MainActivity extends Activity {
	
	private ImageView mImageView1;
	private ImageView mImageView2;
	private Button button1;
	private Button button2;
	private LinearLayout mLinearLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mImageView1 = (ImageView) findViewById(R.id.imageView1);
        mImageView1.startAnimation(new CustomAnimation());
        
        mImageView2 = (ImageView) findViewById(R.id.imageView2);
        
        mLinearLayout  = (LinearLayout) findViewById(R.id.linearLayout1);
        
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//иак╦  
				AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.1f, 1.0f);  
				alphaAnimation1.setDuration(100);  
				alphaAnimation1.setRepeatCount(Animation.INFINITE);  
				alphaAnimation1.setRepeatMode(Animation.REVERSE);  
				mImageView2.startAnimation(alphaAnimation1);
			}
		});
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				TranslateAnimation translateAnimation = new TranslateAnimation(0, button1.getWidth(), 0, 0);
//				translateAnimation.setDuration(1000);
//				translateAnimation.setFillAfter(true);
//				mLinearLayout.startAnimation(translateAnimation);
				mLinearLayout.scrollTo(100, 0);
				button1.setVisibility(View.GONE);
				mLinearLayout.requestLayout();
//				button1.setAnimation(translateAnimation);
//				button1.startAnimation(translateAnimation);
//				button1.setVisibility(View.VISIBLE);
			}
		});
    }
}