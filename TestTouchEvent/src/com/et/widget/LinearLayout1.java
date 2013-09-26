package com.et.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class LinearLayout1 extends LinearLayout {
	
	public static final String TAG = LinearLayout1.class.getName();
	public LinearLayout1(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
	}
	public LinearLayout1(Context context, AttributeSet attrs) {
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
				
				/**requestDisallowInterceptTouchEvent(true)方法,
		         * True if the child does not want the parent to intercept touch events.
		         * 可以使触摸事件跳过onInterceptTouchEvent直接分发LinearLayout布局的childView.
		         * 
		         * 
		         * 实际用途看TestHeaderListView项目
		         * **/
				//requestDisallowInterceptTouchEvent(true); //这里不会跳过onInterceptTouchEvent的事件处理，在这里设置是没有效的
				break;
			case MotionEvent.ACTION_MOVE:
				Log.i(TAG + " dispatchTouchEvent", "MotionEvent.ACTION_MOVE");
				requestDisallowInterceptTouchEvent(true);	//跳过onInterceptTouchEvent的move事件和up事件
				break;
			case MotionEvent.ACTION_UP:
				Log.i(TAG + " dispatchTouchEvent", "MotionEvent.ACTION_UP");
				//requestDisallowInterceptTouchEvent(true);  //跳过onInterceptTouchEvent的up事件
				break;
			case MotionEvent.ACTION_CANCEL:
				Log.i(TAG + " dispatchTouchEvent", "MotionEvent.ACTION_CANCEL");
				break;
		}
		return super.dispatchTouchEvent(ev);
	}
	/**
	 * 由于onInterceptTouchEvent()的机制比较复杂，上面的说明写的也比较复杂，总结一下，基本的规则是：
	1. down事件首先会传递到onInterceptTouchEvent()方法
	2. 如果该ViewGroup的onInterceptTouchEvent()在接收到down事件处理完成之后return false，那么后续的move, up等事件将继续会先传递给该ViewGroup，之后才和down事件一样传递给最终的目标view的onTouchEvent()处理。
	3. 如果该ViewGroup的onInterceptTouchEvent()在接收到down事件处理完成之后return true，那么后续的move, up等事件将不再传递给onInterceptTouchEvent()，而是和down事件一样传递给该ViewGroup的onTouchEvent()处理，注意，目标view将接收不到任何事件。
	4. 如果最终需要处理事件的view的onTouchEvent()返回了false，那么该事件将再一次被传递至其上一层次的view的onTouchEvent()处理。
	5. 如果最终需要处理事件的view 的onTouchEvent()返回了true，那么后续事件将可以继续传递给该view的onTouchEvent()处理。**/
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int action = ev.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				Log.i(TAG + " onInterceptTouchEvent", "MotionEvent.ACTION_DOWN");
				/**1、如果这里返回true,那么onInterceptTouchEvent只会接收到一次down事件，且move和后续的up事件以后都
				 * 不会传递到这里和子view,之后的move和up事件都交给onTouchEvent来处理
				 * 
				 * 2、如果这里返回false且子view的onTouchEvent的down事件返回true，那么后续move和up事件都会传递到子view
				 * 
				 * 3、如果这里返回false且子view的onTouchEvent的down事件返回false,那么onInterceptTouchEvent只会收到这
				 * 一次down事件，后续的move、up事件都不会传递到这里和子view
				 * **/
				
//				return true;
				break;
			case MotionEvent.ACTION_MOVE:
				Log.i(TAG + " onInterceptTouchEvent", "MotionEvent.ACTION_MOVE");
				/**如果这里返回true,那么onInterceptTouchEvent只会接收到一次move事件，且move和后续的up事件以后都
				 * 不会传递到这里和子view,而且这时view会受到cancel事件，之后的move和up事件都交给onTouchEvent来处理,
				 * **/
				//return true;
				break;
			case MotionEvent.ACTION_UP:
				Log.i(TAG + " onInterceptTouchEvent", "MotionEvent.ACTION_UP");
				/**如果这里返回true,那么onInterceptTouchEvent会接收到一次up事件,且up事件不会传给子view,而且这时view会受到cancel事件
				 * 但是这次up事件也不交给onTouchEvent处理
				 * **/
				//return true;
				break;
			case MotionEvent.ACTION_CANCEL:
				Log.i(TAG + " onInterceptTouchEvent", "MotionEvent.ACTION_CANCEL");
				break;
		}
		//return false;//父类方法就是return false
		return super.onInterceptTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				Log.i(TAG + " onTouchEvent", "MotionEvent.ACTION_DOWN");
				return true;
				//break;
			case MotionEvent.ACTION_MOVE:
				Log.i(TAG + " onTouchEvent", "MotionEvent.ACTION_MOVE");
				return true;
				//break;
			case MotionEvent.ACTION_UP:
				Log.i(TAG + " onTouchEvent", "MotionEvent.ACTION_UP");
				return true;
			case MotionEvent.ACTION_CANCEL:
				Log.i(TAG + " onTouchEvent", "MotionEvent.ACTION_CANCEL");
				return true;
				//break;
		}
		return super.onTouchEvent(event);
	}
}
