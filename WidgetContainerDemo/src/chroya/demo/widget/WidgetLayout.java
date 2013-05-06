package chroya.demo.widget;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 承载widget的容器
 * @author chroya
 */
public class WidgetLayout extends ViewGroup {
	//存放touch的坐标
	private int[] cellInfo = new int[2];
	private OnLongClickListener mLongClickListener;	
	private OnTouchListener mTouchListener;
	private boolean isMove = false;
	public WidgetLayout(Context context) {
		super(context);
		mLongClickListener = new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				isMove = true;
				return false;
			}
		};
		mTouchListener = new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int x=(int)event.getRawX();//注意这里用getRaw
				int y=(int)event.getRawY();
				
				int startX = (int) event.getX();
				int startY = y - v.getTop();
				if(isMove){
					switch(event.getAction()){
						case MotionEvent.ACTION_DOWN:
							Log.i("ACTION_DOWN", String.valueOf("x:"+x+"y:" + y));
							
							return true;
						case MotionEvent.ACTION_MOVE:
							Log.i("ACTION_MOVE", String.valueOf("x:"+x+"y:" + y));
							
							LayoutParams lp = (LayoutParams) v.getLayoutParams();
							lp.x = x ;
							lp.y = y ;
							/**用这个方法强制view重新布局在layout上的位置**/
							requestLayout();
							return true;
						case MotionEvent.ACTION_UP:
							isMove = false;
							Log.i("ACTION_UP", String.valueOf("x:"+x+"y:" + y));
							return true;			
					}
				}
				return false;
			}
		};
	}
	
	public void addInScreen(View child, int width, int height) {
		LayoutParams lp = new LayoutParams(width, height);
		lp.x = cellInfo[0];
		lp.y = cellInfo[1];
		child.setOnLongClickListener(mLongClickListener);
		child.setOnTouchListener(mTouchListener);
		/**addView(View child, ViewGroup.LayoutParams params)指定了layout parameters,
		 * 可以通过View的getLayoutParams()获取到这个LayoutParams**/
		addView(child, lp);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		LayoutParams lp;
		for(int index=0; index<getChildCount(); index++) {
			lp = (LayoutParams) getChildAt(index).getLayoutParams();
			getChildAt(index).measure(
					//MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY, lp.width), 
					//MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY, lp.height)
					MeasureSpec.makeMeasureSpec(lp.width,MeasureSpec.EXACTLY), 
					MeasureSpec.makeMeasureSpec(lp.height,MeasureSpec.EXACTLY)
					);

		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		//if(event.getAction() == MotionEvent.ACTION_DOWN){
			cellInfo[0] = (int)event.getX();
			cellInfo[1] = (int)event.getY();
			Log.i("cellInfo[0]", String.valueOf(cellInfo[0]));
			Log.i("cellInfo[1]", String.valueOf(cellInfo[1]));
		//}
		
		return super.dispatchTouchEvent(event);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		LayoutParams lp;
		for(int index=0; index<getChildCount(); index++) {
			/**通过View的getLayoutParams()获取到这个LayoutParams**/
			lp = (LayoutParams) getChildAt(index).getLayoutParams();
			getChildAt(index).layout(lp.x, lp.y, lp.x+lp.width, lp.y+lp.height);
			
		}
	}
	
	public static class LayoutParams extends ViewGroup.LayoutParams {
		int x;
		int y;

		public LayoutParams(int width, int height) {
			super(width, height);
		}		
	}
}
