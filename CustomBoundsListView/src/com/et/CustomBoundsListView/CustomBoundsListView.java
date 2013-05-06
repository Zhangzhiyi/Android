package com.et.CustomBoundsListView;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

public class CustomBoundsListView extends ListView {
	
	public static final String LOG = "CustomBoundsListView";
	public boolean bounce = false;
	public int firstY = 0, secondY = 0;
	public CustomBoundsListView(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
	}
	public CustomBoundsListView(Context context, AttributeSet attrs) {
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
			/**获取显示在屏幕的第一个和最后一个ListItem在整个ListView的位置**/
			int firstPos = getFirstVisiblePosition();
			int lastPos = getLastVisiblePosition();
			int count = getCount();
			/*Log.i("firstPos", String.valueOf(firstPos));
			Log.i("lastPos", String.valueOf(lastPos));
			Log.i("count", String.valueOf(count));*/
			
			if(firstPos != 0&&lastPos != (count-1))
				return false;
			
			if(firstPos ==0 || lastPos == (count-1)){
				//Log.i(LOG, "onScroll");
				View firstView = getChildAt(firstPos);
				View lastView = getChildAt(lastPos-firstPos);
				/**判断滚动条在上方且已不能再拖动**/
				if(firstPos == 0&&firstView.getTop()==0){				
					Log.i("firstView.getTop()", String.valueOf(firstView.getTop()));
					if(!bounce){
						/**记录此时触摸点的Y坐标**/
						firstY = (int) e2.getRawY();
						Log.i("firstY", String.valueOf(firstY));
						bounce = true;
					}
					if(bounce){
						/**如果手指继续上下拖动记录此时的Y坐标**/
						secondY = (int) e2.getRawY();
						Log.i("secondY", String.valueOf(secondY));
					}
				
						int distance = firstY - secondY;
						scrollTo(0, distance);
						Log.i("distance", String.valueOf(distance));
						return true;
					
				}
				/**判断滚动条在下方且已不能再拖动**/
				if(lastPos == (count-1)&&lastView.getBottom()==getHeight()){
					Log.i("lastView.getBottom()", String.valueOf(lastView.getBottom()));
					if(!bounce){
						firstY = (int) e2.getRawY();
						bounce = true;
					}
					if(bounce){
						secondY = (int) e2.getRawY();
					}
					int distance = firstY - secondY;
					Log.i("distance", String.valueOf(distance));
					scrollTo(0, distance);
					return true;
					
					
				}
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
			Log.i(LOG, "GestureDetector:onDown");
			return false;
		}
	});
	/**所有点击事件都是首先执行dispatchTouchEvent,然后在此方法执行父类的dispatchTouchEvent分发给其他点击事件响应;
	 * 如果在此方法不执行父类方法只返回false,则只响应dispatchTouchEvent的down事件,不会响应其他点击而且不会分发其他事件
	 * 如果在此方法不执行父类方法只返回true,则只响应dispatchTouchEvent的down和up事件,也不会分发其他点击事件**/
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.i(LOG, "dispatchTouchEvent"); 
		
		if(ev.getAction()==MotionEvent.ACTION_UP){
			bounce = false;
			/**这里创建个动画让ListView滑动一段距离**/
			Rect rect = new Rect();
			/**发现rect.top和上面得到的distance的值一样...**/
			getLocalVisibleRect(rect);
			TranslateAnimation am = new TranslateAnimation( 0, 0, -rect.top, 0);
			Log.i("rect.top", String.valueOf(rect.top));
			am.setDuration(400);
			startAnimation(am);
			/**不要上面的动画ListView会极快地滑动会(0,0),效果不好**/
			scrollTo(0, 0);
		}
		/**在这里响应手势事件**/
		mGestureDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
		
	}
	/**在这里onInterceptTouchEvent只会响应down事件**/
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.i(LOG, "onInterceptTouchEvent");
		return super.onInterceptTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.i(LOG, "onTouchEvent");
		//mGestureDetector.onTouchEvent(ev);
		return super.onTouchEvent(ev);
		
		
	}
	
	
}
