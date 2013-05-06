package com.et.CustomHorizontalScrollView;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;

public class CustomHorizontalScrollView extends HorizontalScrollView {

	public static final String TAG = "CustomHorizontalScrollView";
	public boolean bounce = false;
	public int firstX = 0, secondX = 0;
	public int distance;
	public CustomHorizontalScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	GestureDetector mGestureDetector = new GestureDetector(new OnGestureListener() {
		
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onScroll");
			int scrollX = getScrollX();
			Log.i(TAG + ":scrollX", String.valueOf(scrollX));
			if (!bounce) {
				if ((getScrollX() == 0)||(getChildAt(0).getWidth() - getWidth()) == getScrollX()){
					firstX = (int) e2.getRawX();
					bounce = true;
				}		
			}
			if(bounce){
				secondX = (int) e2.getRawX();
				distance = firstX - secondX;
				Log.i("distance" , ":"+distance);
				getChildAt(0).scrollTo(distance + getScrollX(), 0);
				return true;
			}
			return false;
		}
		   
		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
	});
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int action = ev.getAction();
		if(action == MotionEvent.ACTION_UP&&bounce){	
			bounce = false;
			Rect rect = new Rect();
			getChildAt(0).getLocalVisibleRect(rect);
			TranslateAnimation am = new TranslateAnimation( -distance, 0, 0, 0);
			Log.i("rect.left", String.valueOf(rect.left));
			am.setDuration(400);
			getChildAt(0).startAnimation(am);
			/**不要上面的动画ListView会极快地滑动会(0,0),效果不好**/
			getChildAt(0).scrollTo(0, 0);
		}
		mGestureDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}
}
