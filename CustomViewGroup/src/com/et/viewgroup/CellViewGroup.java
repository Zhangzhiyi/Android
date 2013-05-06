package com.et.viewgroup;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class CellViewGroup extends ViewGroup {

	
	boolean isAnimation = false;
	
	public void setAnimation(boolean isAnimation) {
		this.isAnimation = isAnimation;
	}
	public CellViewGroup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public CellViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Log.i(CellViewGroup.class.getName(), "dispatchDraw");
		if (isAnimation) {
			canvas.translate(100, 0);
		}
		super.dispatchDraw(canvas);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		Log.i(CellViewGroup.class.getName(), "onLayout");
		for (int i = 0; i < getChildCount(); i++) {
			View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {
				CellLayoutParams cellLayoutParams = (CellLayoutParams) childView.getLayoutParams();
				childView.layout(cellLayoutParams.cellX, cellLayoutParams.cellY, cellLayoutParams.cellX + cellLayoutParams.width, cellLayoutParams.cellY + cellLayoutParams.height);
			}
			
			
		}
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		Log.i(CellViewGroup.class.getName(), "onMeasure");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		for (int i = 0; i < getChildCount(); i++) {
			View childView = getChildAt(i);
			CellLayoutParams cellLayoutParams = (CellLayoutParams) childView.getLayoutParams();
			childView.measure(MeasureSpec.makeMeasureSpec(cellLayoutParams.width, MeasureSpec.EXACTLY), 
					MeasureSpec.makeMeasureSpec(cellLayoutParams.height, MeasureSpec.EXACTLY));
		}
	}
	public static class CellLayoutParams extends ViewGroup.MarginLayoutParams{
		
		int cellX;
		int cellY;
		
		public CellLayoutParams(int width, int height) {
			super(width, height);
			// TODO Auto-generated constructor stub
		}
		public CellLayoutParams(int x, int y, int width, int height){
			super(width, height);
			// TODO Auto-generated constructor stub
			cellX = x;
			cellY = y;
		}
		
		
		
	}
}	
