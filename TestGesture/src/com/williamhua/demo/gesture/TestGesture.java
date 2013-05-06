package com.williamhua.demo.gesture;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class TestGesture extends Activity implements OnTouchListener,OnGestureListener ,OnDoubleTapListener{
	GestureDetector mGestureDetector;
	private static final int FLING_MIN_DISTANCE = 100;
	private static final int FLING_MIN_VELOCITY = 200;

	private ViewFlipper mViewFlipper;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mGestureDetector = new GestureDetector(this);
		//关联双击的接口,但是奇怪的是没有这一句双击也有响应
		mGestureDetector.setOnDoubleTapListener(this);

		TextView tv = (TextView) findViewById(R.id.page);
		/**在这里用View设置监听，所以只是触摸关联的View才有效，不是整个activity都有效**/
		tv.setOnTouchListener(this);
		tv.setText(R.string.text);
		//记得加上下面这句，否则onFiling()事件没有响应;只有这样，view才能够处理不同于Tap（轻触）的hold
		//（即ACTION_MOVE，或者多个ACTION_DOWN），
		//我们同样可以通过layout定义中的android:longClickable来做到这一点
		tv.setLongClickable(true);
		
		mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper1);
		mViewFlipper.setOnTouchListener(this);
	}
    /**这样设置手势对整个activity都有效**/
	/*@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}*/
	
	public boolean onTouch(View v, MotionEvent event) {
		// OnGestureListener will analyzes the given motion event
		return mGestureDetector.onTouchEvent(event);
		
	}
	/*
	 * （与onDown，onLongPress比较 onDown只要Touch down一定立刻触发。 
 	 *	而Touchdown后过一会没有滑动先触发onShowPress再是onLongPress。 
	 *	所以Touchdown后一直不滑动，onDown->onShowPress->onLongPress这个顺序触发。 ） 
	 */
	
	// 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
	public boolean onDown(MotionEvent e) {
		// do nothing
		Toast.makeText(this, "onDown", Toast.LENGTH_SHORT).show();
		return false;
	}
	// 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
	public void onLongPress(MotionEvent e) {
		// do nothing
		Toast.makeText(this, "LongPress", Toast.LENGTH_SHORT).show();
	}

	/**用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
	 * e1是第一个down的触摸事件,之后都不会改变;e2是根据不断的move来改变的
	 * distanceX和distanceY都是前一个的e2和现在e2相减的值**/
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// do nothing
		Log.i("onScroll:e1X", String.valueOf(e1.getX()));
		Log.i("onScroll:e2X", String.valueOf(e2.getX()));
		Log.i("onScroll:e1Y", String.valueOf(e1.getY()));
		Log.i("onScroll:e2Y", String.valueOf(e2.getY()));
		Log.i("onScroll:distanceX", String.valueOf(distanceX));
		Log.i("onScroll:distanceY", String.valueOf(distanceY));
		return false;
	}

	// 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发  
	// 注意和onDown()的区别，强调的是没有松开或者拖动的状态  
	public void onShowPress(MotionEvent e) {
		// do nothing

	}

	// 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发 
	public boolean onSingleTapUp(MotionEvent e) {
		// do nothing
		Toast.makeText(this, "onSingleTapUp", Toast.LENGTH_SHORT).show();
		Log.i("onSingleTapUp", "onSingleTapUp");
		return false;
	}
	
	 /*onFling()事件的处理，onFling()方法中每一个参数的意义我写在注释中了，
	  * 需要注意的是Fling事件的处理代码中，除了第一个触发Fling的ACTION_DOWN和最后一个ACTION_MOVE
	  * 中包含的坐标等信息外，我们还可以根据用户在X轴或者Y轴上的移动速度作为条件。
	  * 比如下面的代码中我们就在用户移动超过100个像素，且X轴上每秒的移动速度大于200像素时才进行处理。
	  */
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// 参数解释： 05.    
		// e1：第1个ACTION_DOWN MotionEvent 06.    
		// e2：最后一个ACTION_MOVE MotionEvent 07.    
		// velocityX：X轴上的移动速度，像素/秒 08.    
		// velocityY：Y轴上的移动速度，像素/秒 09.    
		// 触发条件 ： 10.    
		// X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒 
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			// Fling left
			Toast.makeText(this, "Fling Left", Toast.LENGTH_SHORT).show();
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			// Fling right
			Toast.makeText(this, "Fling Right", Toast.LENGTH_SHORT).show();
		}

		return false;
	}
	//测试双击
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.i("onDoubleTap", "onDoubleTap");
		Toast.makeText(this, "onDoubleTap", Toast.LENGTH_SHORT).show();
		return false;
	}
	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}