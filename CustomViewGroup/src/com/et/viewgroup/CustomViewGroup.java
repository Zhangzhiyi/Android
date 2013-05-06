package com.et.viewgroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class CustomViewGroup extends ViewGroup {

	public CustomViewGroup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public CustomViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setClipChildren(true);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		int count = getChildCount();
		int childLeft = 0;
		for (int i = 0; i < count; i++) {
			View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {
				int childWidth = childView.getMeasuredWidth();
				childView.layout(childLeft, 0, childLeft + childWidth, childView.getMeasuredHeight());
				childLeft = childLeft + childWidth;
			}
			
		}
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int count = getChildCount();   
        for (int i = 0; i < count; i++) {   
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);   
        }  
	}
	
	public void setHalfWidthOfChild(int index){
		View view = getChildAt(index);
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		int childHeightSpec ;
//		childHeightSpec = ViewGroup.getChildMeasureSpec(0, 0, layoutParams.height);
		childHeightSpec = MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), MeasureSpec.EXACTLY);
		int childWidthSpec;
		int measurWidth = view.getMeasuredWidth() - 3;
		childWidthSpec = MeasureSpec.makeMeasureSpec(view.getMeasuredWidth() - 3, MeasureSpec.EXACTLY);
//		if (layoutParams.width > 0) {
//			childWidthSpec = MeasureSpec.makeMeasureSpec(layoutParams.width, MeasureSpec.EXACTLY);
//		}else{
//			childWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//		}
		view.measure(childWidthSpec, childHeightSpec);
		onLayout(true, 0, 0, 0, 0);
		invalidate();
		
	}
	public int x = 10;
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.dispatchDraw(canvas);
		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Log.i("onDraw", "onDraw");
		super.onDraw(canvas);
	}

}
