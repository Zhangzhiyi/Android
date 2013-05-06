package com.mcs.todo.res;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


public class CustomLinearLayout extends LinearLayout{
	
	public CustomLinearLayout(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
	}
	public CustomLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Log.i("tag", "msg");
		int count  = getChildCount();
		if(count!=0)
		{
			for(int i=0;i<count;i++){
				View view = getChildAt(i);
				//Log.i("width",String.valueOf(view.getWidth()));
				//Log.i("height",String.valueOf(view.getHeight()));	
				Log.i("width",String.valueOf(view.getMeasuredWidth()));
				Log.i("height",String.valueOf(view.getMeasuredHeight()));
				int left = view.getLeft();
				int top = view.getTop();
				int right = view.getRight();
				int bottom = view.getBottom();
				Log.i("top", String.valueOf(top));
				Log.i("left", String.valueOf(left));
				Log.i("right", String.valueOf(right));
				Log.i("bottom", String.valueOf(bottom));
				Rect rect = new Rect(left, top, right, bottom);
			}			
		}			
		super.onDraw(canvas);
	}
	
}