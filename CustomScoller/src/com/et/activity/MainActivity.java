package com.et.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private LinearLayout mLinearLayout;
	private Button mButton01;
	private Button mButton02;
	private TextView mTextView;
	/**总结：View调用scrollTo(int x, int y)来实现滚动， int x为负数，是向右滚动；int y为负数，是向下滚动
	 * 1、scrollTo(int x, int y)实现滚动的范围是这个view所占的空间，滚动超出view的空间就会不可见
	 * 2、scrollTo(int x, int y)实现滚动的距离是以父布局第一次确定view的起始位置为相对位置的，例如：如果一个view调用scrollTo(-10, 0)
	 * 向右滚动了10dp，再调用scrollTo(-10, 0)是不会再向右滚动的，因为不是以滚动之后的位置为起始位置。
	 * **/
	/**控件：Scroller (利用Scroller可以得到某一时间某一点的位置，再调用view的scrollTo来实现滚动动画)
	 * startScroll (int startX, int startY, int dx, int dy, int duration);
	 * 
	 * **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mLinearLayout = (LinearLayout) findViewById(R.id.LinearLayout01);
        mButton01 = (Button) findViewById(R.id.button1);
        mButton02 = (Button) findViewById(R.id.button2);
        mTextView = (TextView) findViewById(R.id.textView1);
        mButton01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//mTextView.scrollTo(-100, 0);
				mLinearLayout.scrollTo(-100, 0);
				
				Log.i("mLinearLayout.getScrollX()", String.valueOf(mLinearLayout.getScrollX()));
				
			}
		});
        mButton02.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int scrollX = mTextView.getScrollX();
				Log.i("scrollX", String.valueOf(scrollX));
				mTextView.scrollTo(-50, -50);
				Log.i("mTextView.getLeft()", String.valueOf(mTextView.getLeft()));
			}
		});
    }
}