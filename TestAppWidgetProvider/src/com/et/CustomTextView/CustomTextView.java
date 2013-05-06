package com.et.CustomTextView;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.TextView;


public class CustomTextView extends TextView{
	
	private Scroller mSlr;

	// milliseconds for a round of scrolling

	private int mRndDuration = 10000;

	// the X offset when paused

	private int mXPaused = 0;

	// whether it's being paused

	private boolean mPaused = true;
	public CustomTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setSingleLine();
		setEllipsize(null);
		setVisibility(INVISIBLE);
	}
	
	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setSingleLine();
		setEllipsize(null);
		setFocusable(true);
		setVisibility(INVISIBLE);
	}
	/**

	　　* begin to scroll the text from the original position

	　　*/

	public void startScroll() {

		// begin from the very right side
		mXPaused = -1 * getWidth();//乘以-1,mXPaused是负数了
		// assume it's paused
		mPaused = true;
		resumeScroll();

	}

	/**
	* resume the scroll from the pausing point
	* */

	public void resumeScroll() {
		if (!mPaused)
			return;
		// Do not know why it would not scroll sometimes
		// if setHorizontallyScrolling is called in constructor.
		setHorizontallyScrolling(true);
		// use LinearInterpolator for steady scrolling
		mSlr = new Scroller(this.getContext(), new LinearInterpolator());
		setScroller(mSlr);
		int scrollingLen = calculateScrollingLen();
		Log.i("scrollingLen",String.valueOf(scrollingLen) );
		int distance = scrollingLen - (getWidth() + mXPaused);//  getWidth() + mXPaused = 0; 不明白作者为什么加上这个
		Log.i("getWidth()", String.valueOf(getWidth()));
		Log.i("mXPaused", String.valueOf(mXPaused));		
		Log.i("distance", String.valueOf(distance));
		int duration = (new Double(mRndDuration * distance * 1.00000/ scrollingLen)).intValue();
		Log.i("duration", String.valueOf(duration));
		setVisibility(VISIBLE);
		//mXPaused = -320  mXPaused表示开始滚动位置相对于scroller的偏移量，-320即是在屏幕右边开始出现
		//distance表示滚动的长度，要想TextView的字符串穿过整个屏幕，distance = 字符串长度+屏幕长度
		mSlr.startScroll(mXPaused, 0, distance, 0, duration);
		mPaused = false;
	}
	/**
	* calculate the scrolling length of the text in pixel
	*
	* @return the scrolling length in pixels
	*/

	private int calculateScrollingLen() {

		TextPaint tp = getPaint();
		Rect rect = new Rect();
		String strTxt = getText().toString();
		tp.getTextBounds(strTxt, 0, strTxt.length(), rect);
		/**得到TextView的字符串长度**/
		Log.i("TextLenght", String.valueOf(rect.width()));
		int scrollingLen = rect.width() + getWidth();
		rect = null;
		
		return scrollingLen;
	}

	/**
	 * pause scrolling the text
	 */

	public void pauseScroll() {

		if (null == mSlr)
			return;
		if (mPaused)
			return;
		mPaused = true;
		// abortAnimation sets the current X to be the final X,
		// and sets isFinished to be true
		// so current position shall be saved
		mXPaused = mSlr.getCurrX();
		mSlr.abortAnimation();
	}

	@Override
	 /**
	　* override the computeScroll to restart scrolling when finished so as that
	　* the text is scrolled forever
	　*/
	/**当滚动完一次就自动调用此函数**/
	public void computeScroll() {

		super.computeScroll();
		if (null == mSlr) 
			return;
		if (mSlr.isFinished() && (!mPaused)) {
			this.startScroll();
		}
	}

	public int getRndDuration() {
		return mRndDuration;
	}

	public void setRndDuration(int duration) {
			this.mRndDuration = duration;
	}

	public boolean isPaused() {
		return mPaused;
	}	
}