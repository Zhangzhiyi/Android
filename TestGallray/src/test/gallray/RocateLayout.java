package test.gallray;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

public class RocateLayout extends FrameLayout {

	public RocateLayout(Context context) {
		super(context);
		this.setStaticTransformationsEnabled(true);
	}

	public RocateLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);
	}

	public RocateLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//设置childView的宽高,这里设置和这个FrameLayout一样大小
		measureChildWithMargins(getChildAt(0), heightMeasureSpec, 0,
				widthMeasureSpec, 0);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		getChildAt(0).layout(0, 0, getHeight(), getWidth());
	}

    @Override
    public ViewParent invalidateChildInParent(final int[] location, final Rect dirty) {
    	ViewParent parent = super.invalidateChildInParent(location, dirty);
    	if(parent != null) {
    		// extend dirty area to draw the child.
    		//Dirty Rectangle 是指需要刷新的区域.  下面一行代码是扩大需要刷新的区域,否则会有残留图片再屏幕上
    		dirty.union(getWidth(), getHeight());
    	}
    	return parent;
    }
    
    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
    	Matrix matrix = t.getMatrix();
    	//将childView旋转90度,变为垂直
    	matrix.setRotate(90, getWidth() / 2, getWidth() / 2);
        return true;
    }
    
//	@Override
//	protected void dispatchDraw(Canvas canvas) {
//		canvas.save();
//		canvas.rotate(90, getWidth() / 2, getWidth() / 2);
//		super.dispatchDraw(canvas);
//		canvas.restore();
//	}

	// TODO: other dispathch method. e.g.key event

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		MotionEvent event = MotionEvent.obtain(ev);
		//以下这行代码很重要,将X坐标和Y坐标调换,即是在X轴移动是Y的值在改变.这样可以使Gallery的触摸滚动方向变成上下 
		event.setLocation(ev.getY(), ev.getX());
		// TODO: multi point
		return super.dispatchTouchEvent(event);
	}
}
