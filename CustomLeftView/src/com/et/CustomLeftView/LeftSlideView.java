package com.et.CustomLeftView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class LeftSlideView extends LinearLayout {
	
	private PopupWindow mPopupWindow;
	public static final int	CHILDVIEW_COUNT = 5;
	public static final int NORMAL_WIDTH = 145;
	public static final int NORMAL_HEIGHT = 45;
	public static final int HIGHT_WIDTH = 180;
	public static final int NORMAL_FONTSIZE = 18;
	public static final int HIGHT_FONTSIZE = 24;
	private int mHighLightIndex ;
	//CustomLeftView顶部到InputVIew顶部之间的距离
	public int gap = 0;
	float density;
	Drawable normalDrawable = null;
	Drawable highLightDrawable = null;
	//StateListDrawable有时导致9切图显示不全.. 
	StateListDrawable[] stateListDrawables = new StateListDrawable[CHILDVIEW_COUNT];
	public LeftSlideView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public LeftSlideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mPopupWindow = new PopupWindow(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mPopupWindow.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mPopupWindow.setFocusable(false);
		mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
		density = context.getResources().getDisplayMetrics().density;
		mHighLightIndex = CHILDVIEW_COUNT -1 ;
		normalDrawable = getResources().getDrawable(R.drawable.leftslide_normal_bg);
		highLightDrawable = getResources().getDrawable(R.drawable.leftslide_highlight_bg);
		
//		for (int i = 0; i < CHILDVIEW_COUNT; i++) {
//			StateListDrawable stateListDrawable = new StateListDrawable();
//			stateListDrawable.addState(View.SELECTED_STATE_SET,highLightDrawable);
//			stateListDrawable.addState(View.FOCUSED_STATE_SET,highLightDrawable);
//			stateListDrawable.addState(View.PRESSED_ENABLED_STATE_SET,highLightDrawable);
//			stateListDrawable.addState(View.ENABLED_STATE_SET, normalDrawable);
//			stateListDrawables[i] = stateListDrawable;
//		}

	}
	

	public void setHighlightView(int index){
		Log.i("index", "" + index);
		if (index < 0 || index > (CHILDVIEW_COUNT - 1)) {
			return ;
		}
		
		Button preHighlightView = (Button) getChildAt(mHighLightIndex);
		preHighlightView.setTextSize(TypedValue.COMPLEX_UNIT_SP, NORMAL_FONTSIZE);
		//preHighlightView.getBackground().setState(View.ENABLED_STATE_SET);
		preHighlightView.setBackgroundDrawable(normalDrawable);
		updateViewLayout(preHighlightView, new LinearLayout.LayoutParams((int) (NORMAL_WIDTH * density), (int) (NORMAL_HEIGHT * density)));
		
		
		Button highlightView = (Button) getChildAt(index);
		highlightView.setTextSize(TypedValue.COMPLEX_UNIT_SP, HIGHT_FONTSIZE);
		//highlightView.getBackground().setState(View.PRESSED_ENABLED_STATE_SET);
		highlightView.setBackgroundDrawable(highLightDrawable);
		updateViewLayout(highlightView, new LinearLayout.LayoutParams((int) (HIGHT_WIDTH * density), (int) (NORMAL_HEIGHT * density)));
		
		mHighLightIndex = index;
	}
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		
	}
	public void setNormalView(int index){
		Button preHighlightView = (Button) getChildAt(index);
		preHighlightView.setTextSize(TypedValue.COMPLEX_UNIT_SP, NORMAL_FONTSIZE);
		//preHighlightView.getBackground().setState(View.ENABLED_STATE_SET);
		preHighlightView.setBackgroundDrawable(normalDrawable);
		updateViewLayout(preHighlightView, new LinearLayout.LayoutParams((int) (NORMAL_WIDTH * density ), (int) (NORMAL_HEIGHT * density)));
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
		setHighlightView(mHighLightIndex);
		int[] offest = new int[2];
		parent.getLocationInWindow(offest);
		mPopupWindow.setContentView(this);
		mPopupWindow.setWidth((int) (HIGHT_WIDTH * density));
		mPopupWindow.setHeight((int) (NORMAL_HEIGHT * density * CHILDVIEW_COUNT));
		mPopupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, offest[0], offest[1] - getTotalHeight());
	}
	public void hide(){
		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
			setNormalView(mHighLightIndex);
			mHighLightIndex = CHILDVIEW_COUNT -1;

		}
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
	public int getGap(){
		return gap;
	}
	public void recycle(){
		if (mPopupWindow != null) {
			mPopupWindow.setContentView(null);
			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			}
		}
	}
}
