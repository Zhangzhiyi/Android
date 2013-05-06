package com.et.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class MyTextView extends TextView {
	
	public static final String TAG = MyTextView.class.getName();
	
	public MyTextView(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		
	}
	public MyTextView(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int action = ev.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				Log.i(TAG + " dispatchTouchEvent", "MotionEvent.ACTION_DOWN");
				break;
			case MotionEvent.ACTION_MOVE:
				Log.i(TAG + " dispatchTouchEvent", "MotionEvent.ACTION_MOVE");
				break;
			case MotionEvent.ACTION_UP:
				Log.i(TAG + " dispatchTouchEvent", "MotionEvent.ACTION_UP");
				break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				Log.i(TAG + " onTouchEvent", "MotionEvent.ACTION_DOWN");
				/**注意：接收down事件，如果这里返回false，后续的move、up事件接收不到。
				而且父View会重新执行一次down事件**/
				return true;
//				break;
			case MotionEvent.ACTION_MOVE:
				Log.i(TAG + " onTouchEvent", "MotionEvent.ACTION_MOVE");
				//这里的返回值不影响后续事件接收
				return false;
//				break;
			case MotionEvent.ACTION_UP:
				Log.i(TAG + " onTouchEvent", "MotionEvent.ACTION_UP");
				//这里的返回值不影响后续事件接收
				return true;
//				break;
		}
		return false;
//		return super.onTouchEvent(event);
	}
	
	/*@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		//这里测试到运行父类super.onTouchEvent(event)返回的是false,所以只能接收到down事件，而后续的move、up都接收不了
		boolean flag = super.onTouchEvent(event);
		Log.i("flag",String.valueOf(flag));
		return super.onTouchEvent(event);
	}*/
}
