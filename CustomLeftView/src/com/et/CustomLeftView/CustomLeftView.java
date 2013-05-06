package com.et.CustomLeftView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.AbsListView.LayoutParams;

public class CustomLeftView extends LinearLayout {
	
	private PopupWindow mPopupWindow;
	private int mHighLightIndex = 4;
	public static final int	CHILDVIEW_COUNT = 5;
	public static final int NORMAL_WIDTH = 145;
	public static final int NORMAL_HEIGHT = 45;
	public static final int HIGHT_WIDTH = 180;
	public static final int NORMAL_FONTSIZE = 18;
	public static final int HIGHT_FONTSIZE = 24;
	float density;
	public CustomLeftView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public CustomLeftView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mPopupWindow = new PopupWindow(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mPopupWindow.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		density = context.getResources().getDisplayMetrics().density;
	}
	public void setHighlightView(int index){
		Log.i("index", "" + index);
		Button preHighlightView = (Button) getChildAt(mHighLightIndex);
		preHighlightView.setTextSize(TypedValue.COMPLEX_UNIT_SP, NORMAL_FONTSIZE);
		preHighlightView.getBackground().setState(View.ENABLED_STATE_SET);
		updateViewLayout(preHighlightView, new LinearLayout.LayoutParams((int) (NORMAL_WIDTH * density), (int) (NORMAL_HEIGHT * density)));
		
		
		Button highlightView = (Button) getChildAt(index);
		if (highlightView == null) {
			return;
		}
		highlightView.setTextSize(TypedValue.COMPLEX_UNIT_SP, HIGHT_FONTSIZE);
		highlightView.getBackground().setState(View.SELECTED_STATE_SET);
		updateViewLayout(highlightView, new LinearLayout.LayoutParams((int) (HIGHT_WIDTH * density), (int) (NORMAL_HEIGHT * density)));
		
		mHighLightIndex = index;
	}
	public void setNormalView(int index){
		Button preHighlightView = (Button) getChildAt(index);
		preHighlightView.setTextSize(TypedValue.COMPLEX_UNIT_SP, NORMAL_FONTSIZE);
		preHighlightView.getBackground().setState(View.ENABLED_STATE_SET);
		updateViewLayout(preHighlightView, new LinearLayout.LayoutParams((int) (NORMAL_WIDTH * density), (int) (NORMAL_HEIGHT * density)));
	}
	public void show(View parent){
		int lenght = getChildCount();
		for (int i = 0; i < lenght; i++) {
			Button button = (Button) getChildAt(i);
			if (i == mHighLightIndex) {
				setHighlightView(mHighLightIndex);
			}else{
				setNormalView(i);
			}
		}
		//开始布局动画
		startLayoutAnimation();
		int[] offest = new int[2];
		parent.getLocationInWindow(offest);
		mPopupWindow.setContentView(this);
		mPopupWindow.setWidth((int) (HIGHT_WIDTH * density));
		mPopupWindow.setHeight((int) (NORMAL_HEIGHT * density * CHILDVIEW_COUNT));
		mPopupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, offest[0], offest[1] - getTotalHeight());
	}
	public boolean isShowing(){
		return mPopupWindow != null && mPopupWindow.isShowing();
	}
	public int getTotalHeight(){
		return (int) (NORMAL_HEIGHT * density * CHILDVIEW_COUNT);
	}
	public int getChildViewHeight(){
		return (int) (NORMAL_HEIGHT * density);
	}
	public void hide(){
		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
			setNormalView(mHighLightIndex);
			mHighLightIndex = 4;
		}
	}
	
}
