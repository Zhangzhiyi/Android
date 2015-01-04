package com.et.CustomHorizontalScrollView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private HorizontalScrollView mHorizontalScrollView;
	int downX;
	int moveX;
	boolean flag ; 
	boolean click = true;
	private ImageView mImageView21;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);
        mHorizontalScrollView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				
				if(action == MotionEvent.ACTION_DOWN){
					downX = (int) event.getX();
					flag = true;
					return false;
				}
				if(action == MotionEvent.ACTION_MOVE){
					moveX = (int) event.getX();					
					int width = mHorizontalScrollView.getWidth();
					//获取HorizontalScrollView下的LinearLayout的宽度,很奇怪这个LinearLayout宽度会大于HorizontalScrollView
					int childViewWidth = mHorizontalScrollView.getChildAt(0).getWidth();
					int scrollX = mHorizontalScrollView.getScrollX();
					Log.i("width", String.valueOf(mHorizontalScrollView.getWidth()));
					Log.i("childview width", String.valueOf(mHorizontalScrollView.getChildAt(0).getWidth()));
					Log.i("scrollX", String.valueOf(mHorizontalScrollView.getScrollX()));
					if((childViewWidth - width) == scrollX && (moveX - downX)<0&&flag){
						Toast.makeText(MainActivity.this, "已经滑到右尽头", Toast.LENGTH_SHORT).show();
						flag = false;
					}
					if(scrollX == 0 && (moveX - downX)>0&&flag){
						Toast.makeText(MainActivity.this, "已经滑到左尽头", Toast.LENGTH_SHORT).show();
						flag = false;
					}
				}
				if(action == MotionEvent.ACTION_UP){
					flag = false;
				}
				
				return false;
			}
		});
        
        mImageView21 = (ImageView) findViewById(R.id.imageView21);
        
        mImageView21.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(click){
					mImageView21.setBackgroundResource(R.drawable.zt_menu_fcous);
					click = false;
				}else{
					mImageView21.setBackgroundResource(0);
					click = true;
				}
			}
		});
    }
}