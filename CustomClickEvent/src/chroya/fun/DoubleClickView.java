package chroya.fun;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

public class DoubleClickView extends View {
	private long mFirstDownTime = -1;
	private long mSecondDownTime = -1;
	//是否第一次点击屏幕
	private boolean isFirst = true;
	//是否发生移动
	private boolean isMove = false;
	private OnDoubleClickListener mOnDoubleClickListener;
	
	private static final int DOUBLE_CLICK_TIMEOUT = 500;
	//移动的阈值
	private static final int TOUCH_SLOP = 20;
	private Runnable mDoubleClickRunnable;
	
	int mLastMotionX ;
	int mLastMotionY ;
	public DoubleClickView(Context context) {
		super(context);
		mDoubleClickRunnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				performDoubleClick();
			}
		};
	}
	/**所有点击事件都是从dispatchTouchEvent开始的,所以要重写dispatchTouchEvent**/
	public boolean dispatchTouchEvent(MotionEvent event) {	
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:	
				mLastMotionX = (int) event.getX();
				mLastMotionY = (int) event.getY();
				if (isFirst){
					isFirst = false;
					mFirstDownTime = event.getDownTime();
				}
				else{
					isFirst = true;
					mSecondDownTime = event.getDownTime();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				
				if(isMove) {
					isFirst = true;
					return true;
				}
				if(Math.abs(mLastMotionX-x) > TOUCH_SLOP 
						|| Math.abs(mLastMotionY-y) > TOUCH_SLOP) {
					//移动超过阈值，则表示移动了
					isMove = true;
					removeCallbacks(mDoubleClickRunnable);
					
				}
				break;
			case MotionEvent.ACTION_UP:
				/*if(mFirstDownTime == -1 || 
						(event.getDownTime()-mFirstDownTime)>DOUBLE_CLICK_TIMEOUT) {
					mFirstDownTime = event.getDownTime();
				} else {
					performDoubleClick();
				}*/
				if(isFirst&&(mSecondDownTime - mFirstDownTime)<DOUBLE_CLICK_TIMEOUT&&!isMove){
					post(mDoubleClickRunnable);
					isFirst = true;
					
				}
				isMove = false;
				break;
			}
		return true;
	}
	
	public void performDoubleClick() {
		if(mOnDoubleClickListener != null) {
			mOnDoubleClickListener.onDoubleClick(this);			
		}
	}
	public void setOnDoubleClickListener(OnDoubleClickListener l){
		mOnDoubleClickListener = l;
	}
	public static interface OnDoubleClickListener {
		public void onDoubleClick(View v);
	}
}
