package com.yao_guet.test;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.Scroller;

/**
 * 仿Launcher中的WorkSapce，可以左右滑动切换屏幕的类
 * 
 */
public class CustomScrollLayout extends ViewGroup {

	private static final String TAG = "ScrollLayout";
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	
	private int mCurScreen;
	private int mDefaultScreen = 0;
	
	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_SCROLLING = 1;
	
	private static final int SNAP_VELOCITY = 600;
	
	private int mTouchState = TOUCH_STATE_REST;
	private int mTouchSlop;
	private float mLastMotionX;
	private float mLastMotionY;
	private Camera mCamera = new Camera();

	public CustomScrollLayout(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
		mScroller = new Scroller(context);	
		mCurScreen = mDefaultScreen;
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		setStaticTransformationsEnabled(true);
	}

	public CustomScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		// TODO Auto-generated constructor stub
		super(context, attrs, defStyle);
		mScroller = new Scroller(context);
		
		mCurScreen = mDefaultScreen;
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		setStaticTransformationsEnabled(true);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		Log.i("onLayout", "onLayout");
		if (changed) {
			int childLeft = 0;
			final int childCount = getChildCount();
			
			for (int i=0; i<childCount; i++) {
				final View childView = getChildAt(i);
				if (childView.getVisibility() != View.GONE) {
					final int childWidth = childView.getMeasuredWidth();
					childView.layout(childLeft, 0, 
							childLeft+childWidth, childView.getMeasuredHeight());
					childLeft += childWidth;
				}
			}
		}
	}


    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   
    	Log.e(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);   
  
        final int width = MeasureSpec.getSize(widthMeasureSpec);   
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);   
        if (widthMode != MeasureSpec.EXACTLY) {   
            throw new IllegalStateException("ScrollLayout only canmCurScreen run at EXACTLY mode!"); 
        }   
  
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);   
        if (heightMode != MeasureSpec.EXACTLY) {   
            throw new IllegalStateException("ScrollLayout only can run at EXACTLY mode!");
        }   
  
        // The children are given the same width and height as the scrollLayout   
        final int count = getChildCount();   
        for (int i = 0; i < count; i++) {   
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);   
        }   
        // Log.e(TAG, "moving to screen "+mCurScreen);   
        scrollTo(mCurScreen * width, 0);         
    }  
    
    /**
     * According to the position of current layout
     * scroll to the destination page.
     */
    public void snapToDestination() {
    	final int screenWidth = getWidth();
    	//screenWidth始终都是屏幕的宽度，不是所有页面加起来的宽度
    	Log.i("screenWidth", String.valueOf(screenWidth));
    	
    	final int destScreen = (getScrollX()+ screenWidth/2)/screenWidth;
    	snapToScreen(destScreen);
    }
    
    public void snapToScreen(int whichScreen) {
    	// get the valid layout page
    	whichScreen = Math.max(0, Math.min(whichScreen, getChildCount()-1));
    	if (getScrollX() != (whichScreen*getWidth())) {
    		Log.i("ScrollX()", String.valueOf(getScrollX()));
    		final int delta = whichScreen*getWidth()-getScrollX();
    		mScroller.startScroll(getScrollX(), 0, 
    				delta, 0, Math.abs(delta)*2);
    		mCurScreen = whichScreen;
    		invalidate();		// Redraw the layout
    	}
    }
    
    public void setToScreen(int whichScreen) {
    	whichScreen = Math.max(0, Math.min(whichScreen, getChildCount()-1));
    	mCurScreen = whichScreen;
    	scrollTo(whichScreen*getWidth(), 0);
    }
    
    public int getCurScreen() {
    	return mCurScreen;
    }
    
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		
		final int action = event.getAction();
		final float x = event.getX();
		final float y = event.getY();
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.e(TAG, "event down!");
			if (!mScroller.isFinished()){
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			break;
			
		case MotionEvent.ACTION_MOVE:
			Log.e(TAG, "event : move"); 
			int deltaX = (int)(mLastMotionX - x);
			mLastMotionX = x;
			
            scrollBy(deltaX, 0);
			break;
			
		case MotionEvent.ACTION_UP:
			Log.e(TAG, "event : up");   
            // if (mTouchState == TOUCH_STATE_SCROLLING) {   
            final VelocityTracker velocityTracker = mVelocityTracker;   
            velocityTracker.computeCurrentVelocity(1000);   
            int velocityX = (int) velocityTracker.getXVelocity();   

            Log.e(TAG, "velocityX:"+velocityX); 
            
            if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {   
                // Fling enough to move left   
            	Log.e(TAG, "snap left");
                snapToScreen(mCurScreen - 1);   
            } else if (velocityX < -SNAP_VELOCITY   
                    && mCurScreen < getChildCount() - 1) {   
                // Fling enough to move right   
            	Log.e(TAG, "snap right");
                snapToScreen(mCurScreen + 1);   
            } else {   
                snapToDestination();   
            }   

            if (mVelocityTracker != null) {   
                mVelocityTracker.recycle();   
                mVelocityTracker = null;   
            }   
            // }   
            mTouchState = TOUCH_STATE_REST;   
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onInterceptTouchEvent-slop:"+mTouchSlop);
		
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE) && 
				(mTouchState != TOUCH_STATE_REST)) {
			return true;
		}
		
		Log.i("11111111", "1111111111");
		final float x = ev.getX();
		final float y = ev.getY();
		
		switch (action) {			
			case MotionEvent.ACTION_DOWN:
				Log.i("onInterceptTouchEvent", "MotionEvent.ACTION_DOWN");
				mLastMotionX = x;
				mLastMotionY = y;
				mTouchState = mScroller.isFinished()? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
				break;
				
				
			/**发现下面onInterceptTouchEvent的move和up事件都没有运行到
			 * 因为没有重写布局里面控件的onTouchEvent事件且返回true，所以onInterceptTouchEvent不能分发事件，只接收到down事件
			 * ViewGroup的onTouchEvent处理了move、up事件**/
			case MotionEvent.ACTION_MOVE:
				Log.i("onInterceptTouchEvent", "MotionEvent.ACTION_MOVE");
				final int xDiff = (int)Math.abs(mLastMotionX-x);
				if (xDiff>mTouchSlop) {
					mTouchState = TOUCH_STATE_SCROLLING;
					
				}
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				Log.i("onInterceptTouchEvent", "MotionEvent.ACTION_UP");
				mTouchState = TOUCH_STATE_REST;
				break;
			}
			
			return mTouchState != TOUCH_STATE_REST;
	}
	public int getCenterOfScrollLayout(){
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
        + getPaddingLeft();
	}
	/**获取现时ItemView的宽度中间值**/
	public int getCenterOfItemView(View view){
		int id = view.getId();
		Log.i(id+":view.getLeft()", String.valueOf(view.getLeft()));
		Log.i(id+":getScrollX()", String.valueOf(getScrollX()));
		if (mCurScreen != 0)
			return view.getLeft() + getScrollX()/mCurScreen + view.getWidth() / 2;
		else
			return centerofScrollLayout;
	}
	int centerofScrollLayout;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		centerofScrollLayout = getCenterOfScrollLayout();
		super.onSizeChanged(w, h, oldw, oldh);
		
	}
	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		// TODO Auto-generated method stub
	   int childCenter = getCenterOfItemView(child);
	   int y = 0;
	   if (childCenter == centerofScrollLayout) {
		   y = 0;
	   }else{
		   int gap = childCenter - centerofScrollLayout;
//		   if (gap > 0) {
//			   y = y + gap;
//		   }else{
//			   y = y + gap;
//		   }
		   y = y + gap;
	   }
	   t.clear();
	   t.setTransformationType(Transformation.TYPE_MATRIX);
	   final Matrix matrix = t.getMatrix();  
	   mCamera.save();  
	   //mCamera.rotateY(y);
	   mCamera.getMatrix(matrix);  
	   matrix.preTranslate(-(child.getWidth()/2), -(child.getHeight()/2));  
	   matrix.postTranslate((child.getWidth()/2), (child.getHeight()/2));
	   mCamera.restore();  
	   return true;
	}
	
}
