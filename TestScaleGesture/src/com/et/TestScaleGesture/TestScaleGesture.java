package com.et.TestScaleGesture;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class TestScaleGesture extends Activity implements OnTouchListener, 
			ScaleGestureDetector.OnScaleGestureListener, OnGestureListener, OnDoubleTapListener{
	
	private ImageView mImageView;
	private GestureDetector mGestureDetector;
	private ScaleGestureDetector mScaleGestureDetector;
	private OnTouchListener mOnTouchListener;
	private ScaleGestureDetector.OnScaleGestureListener mOnScaleGestureListener;
	float curScaleX = 1.0f, curScaleY = 1.0f;
	
	int screenWidth, screenHeight;
	int drawableWidth, drawableHeight;
	float leftDelta, topDelta;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);       
        mImageView = (ImageView) findViewById(R.id.imageView1);  
        mGestureDetector = new GestureDetector(this);
        mGestureDetector.setOnDoubleTapListener(this);
        mScaleGestureDetector = new ScaleGestureDetector(this, this);
        mImageView.setOnTouchListener(this);
        
        Drawable drawable = mImageView.getDrawable();
        //获取drawable的宽高
        drawableWidth = drawable.getMinimumWidth();
        drawableHeight = drawable.getMinimumHeight();
		Log.i("drawableWidth", String.valueOf(drawableWidth));
		Log.i("drawableHeight", String.valueOf(drawableHeight));
		//获取屏幕的宽高
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		Log.i("screenWidth", String.valueOf(screenWidth));
		Log.i("screenHeight", String.valueOf(screenHeight));
		
		
		Matrix matrix=new Matrix();
		leftDelta = (float)screenWidth/2-drawableWidth/2;
		topDelta = (float)screenHeight/2-drawableHeight/2;
		//因为ImageView的ScaleType为matrix,系统只会将图片画在左上角,所以要将图片移动屏幕中间
		matrix.setTranslate(leftDelta, topDelta);
		mImageView.setImageMatrix(matrix);
    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		mGestureDetector.onTouchEvent(event);
		mScaleGestureDetector.onTouchEvent(event);
		return true;
	}
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub
		if(detector.isInProgress()){
			Log.i("onScale", "onScale");
			//获取现在事件两点距离
			float curSpan = detector.getCurrentSpan();
			//获取前一次事件两点距离
			float preSpan = detector.getPreviousSpan();
			float deltaSpan = (curSpan - preSpan)/(2*detector.getTimeDelta());
			Log.i("deltaSpan", String.valueOf(deltaSpan));
			Matrix matrix=new Matrix();
			float preX = curScaleX;
			float preY = curScaleY;
			curScaleX = curScaleX + deltaSpan;
			curScaleY = curScaleY + deltaSpan;		 
			if(curScaleX<1)
				curScaleX = 1.0f;
			if(curScaleX>6)
				curScaleX = 6.0f;
			if(curScaleY<1)
				curScaleY = 1.0f;
			if(curScaleY>6)
				curScaleY = 6.0f;
			Log.i("x", String.valueOf(curScaleX));
		    matrix.setScale(curScaleX, curScaleY);
		    //计算放大后图片要移动的坐标        (现在放大的图片)-(前一次放大的图片)
		    float left = (curScaleX*drawableWidth - preX*drawableWidth)/2;
		    float top = (curScaleY*drawableHeight - preY*drawableHeight)/2;
		    leftDelta = leftDelta - left;
		    topDelta = topDelta - top;
		    matrix.postTranslate(leftDelta, topDelta);
		    mImageView.setImageMatrix(matrix);
			
		}	
		return true;
	}
	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub
		Log.i("onScaleBegin", "onScaleBegin");		 
	     /**这里要返回true才会运行方法onScale**/
		
		return true;
	}
	@Override
	public void onScaleEnd(ScaleGestureDetector detector) { 
		// TODO Auto-generated method stub
		Log.i("onScaleEnd", "onScaleEnd");
		
	}
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		if(curScaleX == 1.0f){
			final Handler handler = new Handler();
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Matrix matrix=new Matrix();
					if(curScaleX < 3)
						curScaleX = curScaleX + 0.2f;
					else
						curScaleX = 3.0f;
					if(curScaleY < 3)
						curScaleY = curScaleY + 0.2f;
					else
						curScaleY = 3.0f;
					
					if(curScaleX<3)
						handler.postDelayed(this, 80);
					 matrix.setScale(curScaleX, curScaleY);
					 mImageView.setImageMatrix(matrix);
					
				}
			});
		}		
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
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}