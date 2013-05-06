package org.pskink.animseekbar;

import android.content.res.Resources;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.Log;
import android.util.StateSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

	
public class AnimSeekBarDrawable extends Drawable implements Runnable {
	static final int[] STATE_FOCUSED = {android.R.attr.state_focused};
	static final int[] STATE_PRESSED = {android.R.attr.state_pressed};
		
	private static final long DELAY = 30;
	private static final String TAG = "AnimSeekBarDrawable";
	private String mText;
	private float mTextWidth;
	private Drawable mProgress;
	private Paint mPaint;
	private Paint mOutlinePaint;
	private float mTextXScale;
	private int mDelta;
	private ScrollAnimation mAnimation;

	public AnimSeekBarDrawable(Resources res, boolean labelOnRight) {
		/**引用系统原来的ProgressBar用的图像**/
		mProgress = res.getDrawable(android.R.drawable.progress_horizontal);
		//mProgress = res.getDrawable(R.drawable.icon);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);
		mPaint.setTextSize(16);
		mOutlinePaint = new Paint(mPaint);
		mOutlinePaint.setStyle(Style.STROKE);
		mOutlinePaint.setStrokeWidth(4);
		mOutlinePaint.setMaskFilter(new BlurMaskFilter(1, Blur.NORMAL));  
		mTextXScale = labelOnRight? 1 : 0;
		mAnimation = new ScrollAnimation();
	}
	
	@Override
	protected void onBoundsChange(Rect bounds) {
		Log.i("onBoundsChange", "onBoundsChange");
		mProgress.setBounds(bounds);
	}
	
	@Override
	protected boolean onStateChange(int[] state) {
		/**获取当前控件是否聚焦点击状态**/
		boolean active = StateSet.stateSetMatches(STATE_FOCUSED, state) | StateSet.stateSetMatches(STATE_PRESSED, state);
		mOutlinePaint.setColor(active? 0xffffffff : 0xffbbbbbb);
		mPaint.setColor(active? 0xff000000 : 0xff606060);
		invalidateSelf();
		return false;
	}
	
	@Override
	public boolean isStateful() {
		return true;
	}
	
	@Override
	protected boolean onLevelChange(int level) {
//		Log.i("onLevelChange", "onLevelChange");
		mText = (level / 100) + " %";
		mTextWidth = mOutlinePaint.measureText(mText);

		if (level < 4000 && mDelta <= 0) {
			mDelta = 1;
			// move to the right
			startScrolling(1);
		} else
		if (level > 6000 && mDelta >= 0) {
			mDelta = -1;
			// move to the left
			startScrolling(0);
		}
		
		return mProgress.setLevel(level);
	}
	
	private void startScrolling(int to) {
		mAnimation.startScrolling(mTextXScale, to);
		scheduleSelf(this, SystemClock.uptimeMillis() + DELAY);
	}

	@Override
	public void draw(Canvas canvas) {
		/**先将系统的ProgressBar用的图像画上画布**/
		mProgress.draw(canvas);
		/**注意：Animation的变量mStart是在getTransformation设为true的，
		 * 不是在Animation的start()或者startNow()方法**/
//		if (mAnimation.hasStarted() && !mAnimation.hasEnded()) {//难怪一直没有运行到里面
		if (!mAnimation.hasEnded()) {
			// pending animation
//			mAnimation.getTransformation(AnimationUtils.currentAnimationTimeMillis(), null);
			mTextXScale = mAnimation.getCurrent();
		}
		
		Rect bounds = getBounds();
		float x = 6 + mTextXScale * (bounds.width() - mTextWidth - 6 - 6);
		float y = (bounds.height() + mPaint.getTextSize()) / 2;
		canvas.drawText(mText, x, y, mOutlinePaint);
		canvas.drawText(mText, x, y, mPaint);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void setAlpha(int alpha) {
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
	}
	/**在run()或者在draw(Canvas canvas)方法不断运行动画**/
	public void run() {
		Log.i("run()", "run()");
		/**因为没有控件来start这个动画，所以要用线程不断调用此方法
		 * applyTransformation是在这个方法里面调用的,getTransformation方法不断计算动画下一帧的变换,
		 * 得到当前时间点的矩阵，在 outTransformation 中返回**/
		mAnimation.getTransformation(AnimationUtils.currentAnimationTimeMillis(), null);
		// close interpolation of mTextX
		mTextXScale = mAnimation.getCurrent();
		if (!mAnimation.hasEnded()) {
			
			/**
			 * 用scheduleSelf不断延迟运行该线程
			 * 跳入源码看到这个方法里面是执行Drawable.Callback接口里面的方法scheduleDrawable(Drawable who, Runnable what, long when)
			 * 在View里面scheduleDrawable方法是一个handler来执行这个线程
			 * **/
			scheduleSelf(this, SystemClock.uptimeMillis() + DELAY);
		}
		/**跳入源码看到这个方法里面是执行Drawable.Callback接口里面的方法invalidateDrawable(Drawable who)
		 * 在View里面invalidateDrawable方法是刷新这个drawable区域**/
		invalidateSelf();
		/**特别注意的是实现Drawable.Callback接口的是所有控件的父类View！！！！！！**/
	}
	
	static class ScrollAnimation extends Animation {
		private static final long DURATION = 750;
		private float mFrom;
		private float mTo;
		private float mCurrent;
		
		public ScrollAnimation() {
			setDuration(DURATION);
			setInterpolator(new DecelerateInterpolator());
		}
		
		public void startScrolling(float from, float to) {
			mFrom = from;
			mTo = to;
			startNow();
			
		}
		
		/**原来此方法会在Animation的getTransformation (long currentTime, Transformation outTransformation)调用**/		
		/**在绘制动画的过程中会反复的调用applyTransformation函数，每次调用参数interpolatedTime值都会变化，
		 * 该参数从0渐 变为1，当该参数为1时表明动画结束。通过参数Transformation 来获取变换的矩阵（matrix），
		 * 通过改变矩阵就可以实现各种复杂的效果。*/
		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			Log.i("interpolatedTime", String.valueOf(interpolatedTime));
			/**注意这里根本就没有用到Matrix来实现动画效果，只不过利用Android动画的原理来获取这个类似移动动画变化的这个变量**/
			mCurrent = mFrom + (mTo - mFrom) * interpolatedTime;
		}
		@Override
		public boolean getTransformation(long currentTime,
				Transformation outTransformation) {
			// TODO Auto-generated method stub
			/**如果没有控件start此动画是不会自动调用getTransformation,要手动调用**/
			Log.i("getTransformation", "getTransformation");
			return super.getTransformation(currentTime, outTransformation);
		}
		public float getCurrent() {
			return mCurrent;
		}
		
	}
}
