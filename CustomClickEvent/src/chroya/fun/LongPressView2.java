package chroya.fun;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class LongPressView2 extends View{
	private int mLastMotionX, mLastMotionY;
	//是否移动了
	private boolean isMoved;
	//长按的runnable
	private Runnable mLongPressRunnable;
	//移动的阈值
	private static final int TOUCH_SLOP = 20;

	public LongPressView2(Context context) {
		super(context);
		mLongPressRunnable = new Runnable() {
			
			@Override
			public void run() {				
				performLongClick();
			}
		};
	}

	public boolean dispatchTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			isMoved = false;
			/**根据长按多长时间来响应长按事件，可以相应修改第二个参数来实现**/
			postDelayed(mLongPressRunnable, ViewConfiguration.getLongPressTimeout());
			break;
		case MotionEvent.ACTION_MOVE:
			if(isMoved) break;
			if(Math.abs(mLastMotionX-x) > TOUCH_SLOP 
					|| Math.abs(mLastMotionY-y) > TOUCH_SLOP) {
				//移动超过阈值，则表示移动了
				isMoved = true;
				removeCallbacks(mLongPressRunnable);
			}
			break;
		case MotionEvent.ACTION_UP:
			//释放了
			removeCallbacks(mLongPressRunnable);
			break;
		}
		return true;
	}
}
