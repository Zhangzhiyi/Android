package com.et.main;

import com.et.viewgroup.CellViewGroup;
import com.et.viewgroup.CustomViewGroup;
import com.et.viewgroup.CellViewGroup.CellLayoutParams;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {

	private CustomViewGroup mCustomViewGroup;
	private CellViewGroup mCellViewGroup;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button7;
	private Handler handler;
	
	private Button childView;
	
	private Button cellChildView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mCustomViewGroup = (CustomViewGroup) findViewById(R.id.customViewGroup1);
		mCellViewGroup = (CellViewGroup) findViewById(R.id.cellViewGroup1);
		childView = (Button) mCustomViewGroup.getChildAt(1);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		button7 = (Button) findViewById(R.id.button7);
		handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
					case 0:
						post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (childView.getWidth() <= 0) {
									removeCallbacks(this);
								}else{
									mCustomViewGroup.setHalfWidthOfChild(1);
								}
							}
						});
						break;
	
					case 1:
						childView.layout(childView.getLeft() - 20, childView.getTop(), childView.getRight(), childView.getBottom());
						break;
				}
				
			}
		};
		button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);
				
			}
		});
		button2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				handler.sendEmptyMessage(1);
				Log.i("after", "" + cellChildView.getLeft());
				
			}
		});
		button3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCustomViewGroup.updateViewLayout(childView,new ViewGroup.LayoutParams(50, 50));
				cellChildView = new Button(MainActivity.this);
				cellChildView.setText("1111");
				CellLayoutParams cellLayoutParams = new CellLayoutParams(0, 0, 100, 100);
				mCellViewGroup.addView(cellChildView, cellLayoutParams);
			}
		});
		button7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TranslateAnimation translateAnimation = new TranslateAnimation(0, 100, 0, 0);
				translateAnimation.setDuration(1000);
//				translateAnimation.setFillAfter(true);
				Log.i("before", "" + cellChildView.getLeft());
				translateAnimation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						Log.i("onAnimationStart", "" + cellChildView.getLeft());
						/**运行动画删除自己**/
//						mCellViewGroup.removeView(cellChildView);
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						Log.i("onAnimationEnd", "" + cellChildView.getLeft());
						mCellViewGroup.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								/**View运行完动画后left、top、right、bottom数值都是不变的，再延迟重新加入一次，不延迟有闪烁。**/
								CellLayoutParams cellLayoutParams = new CellLayoutParams(100, 0, 100, 100);
								cellChildView.setLayoutParams(cellLayoutParams);
//								mCellViewGroup.updateViewLayout(cellChildView, cellLayoutParams);
//								mCellViewGroup.addView(cellChildView, cellLayoutParams);
								//这里left也没变的原因是cellChildView还没及时加入到CellViewGroup
								Log.i("after", "" + cellChildView.getLeft());
							}
						}, 10);
					}
				});
				cellChildView.startAnimation(translateAnimation);
			}
		});
	}
}
