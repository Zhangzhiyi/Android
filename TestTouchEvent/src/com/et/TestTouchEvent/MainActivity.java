package com.et.TestTouchEvent;

import com.et.widget.LinearLayout1;
import com.et.widget.MyTextView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class MainActivity extends Activity {
	public static final String TAG = "MainActivity$MyTextView";
	private LinearLayout1 linearLayout1;
	private MyTextView myTextView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        linearLayout1= (LinearLayout1) findViewById(R.id.LinearLayout);
        myTextView = (MyTextView) findViewById(R.id.tv);
        myTextView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				int x = (int) event.getX();
				int y = (int) event.getY();
				//Log.i(TAG + " onTouch", "x = "+ x + " y =" + y);
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						Log.i(TAG + " onTouch", "MotionEvent.ACTION_DOWN");
						/**
						 * 这里返回true就消费了此事件，就不会运行在MyTextView重写onTouchEvent方法down事件，
						 * 而返回false就会再执行一次MyTextView类重写onTouchEvent方法；
						 * 注意：如果这里返回false,onTouchEvent方法也是返回false,那么这里和onTouchEvent也会接收
						 * 不到后续move、up事件;而此时这里返回true，onTouchEvent方法返回false，这里也
						 * 可以接收到后续的move、up事件，因为这里返回true，所以不会再次运行onTouchEvent方法down事件;**/
//						return false;
						//linearLayout1.requestDisallowInterceptTouchEvent(true);//还是运行了跳过了linearLayout1的onInterceptTouchEvent的down事件，但是没有收到后续的move事件和up事件
						break;
					case MotionEvent.ACTION_MOVE:
						Log.i(TAG + " onTouch", "MotionEvent.ACTION_MOVE");
						/**
						 * 这里返回true就消费了此事件，就不会运行在MyTextView重写onTouchEvent方法move事件，
						 * 而返回false就会再执行一次MyTextView类重写onTouchEvent方法move事件，
						 * 传送move事件onTouchEvent返回值不影响后续接收事件
						 * **/
						//return true;
						//linearLayout1.requestDisallowInterceptTouchEvent(true); //跳过了linearLayout1的onInterceptTouchEvent的move事件和up事件
						break;
					case MotionEvent.ACTION_UP:
						Log.i(TAG + " onTouch", "MotionEvent.ACTION_UP");
						/**
						 * 这里返回true就消费了此事件，就不会运行在MyTextView重写onTouchEvent方法up事件，
						 * 而返回false就会再执行一次MyTextView类重写onTouchEvent方法up事件，
						 * 传送up事件onTouchEvent返回值不影响后续接收事件
						 * **/
//						return true;
						break;
					case MotionEvent.ACTION_CANCEL:
						Log.i(TAG + " onTouch", "MotionEvent.ACTION_CANCEL");
						break;
				}
				return false;
			}
		});
    }
}