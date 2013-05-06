package com.mcs.todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.mcs.todo.utils.Time;

/**
 * 月视图
 * @author dbds
 */
public class Month extends Activity implements ViewSwitcher.ViewFactory {
	
	private TextView title;
	private ViewSwitcher switcher;
	private Calendar ca;
	private Button okBtn;
	private Button cancelBtn;
	
    // ViewFlipper所用的动画
    private Animation leftIn;
    private Animation rightOut;
    private Animation rightIn;
    private Animation leftOut;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.month);
		
		title = (TextView)findViewById(R.id.month_title);
		switcher = (ViewSwitcher)findViewById(R.id.month_switcher);
		okBtn = (Button)findViewById(R.id.month_ok);
		cancelBtn = (Button)findViewById(R.id.month_cancel);
		
		switcher.setFactory(this);
		
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		ca = Time.getInstance();
		((MonthView)switcher.getCurrentView()).update();
	}
	
	private static final int VIEW_ID = 1;
	
	private MonthView getMonthView() {
		MonthView view = new MonthView(this);
		view.setId(VIEW_ID);
		view.setLayoutParams(new ViewSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		view.setLongClickable(true);
		return view;
	}
	
	@Override
	public View makeView() {
		return this.getMonthView();
	}
	
	// 我自定义的View
	public class MonthView extends View {
		
		private GestureDetector gestureDetector;

		// 这个带attr的构造函数必须有，否则无法从xml文件导入属性
		public MonthView(Context context) {
			super(context);
			gestureDetector = new GestureDetector(new MyGestureListener());
		}
		
		@Override
		protected void onDraw (Canvas canvas) {
			float width = width();
			float height = height();
			// ----------------------------画背景----------------------------
			Paint linePaint = new Paint(); // 深色
			linePaint.setColor(Color.BLACK);
			linePaint.setAntiAlias(true);
			
			int cPadding = 2; // 列的微调
			int rPadding = 2; // 行的微调
			for(int i = 1; i < 7; i++) { // 画列的分割线
				canvas.drawLine(cPadding+i*width, 0, cPadding+i*width, getHeight(), linePaint);
			}
			for(int i = 1; i < 6; i++) { // 画行的分割线
				canvas.drawLine(0, rPadding+i*height, getWidth(), rPadding+i*height, linePaint);
			}
			
			// ----------------------------画内容-----------------------------
			Paint textPaint = new Paint();
			textPaint.setAntiAlias(true);
			textPaint.setTextSize(20);
			// 画本月之前的日期
			textPaint.setColor(Color.LTGRAY);
			for(int i = 0; i < weekOfDayOne-1; i++) {
				drawTile(canvas, cPadding+i*width(), 0, width, height, days[i], textPaint);
			}
			// 画本月的日期
			textPaint.setColor(Color.BLACK);
			for(int i = 0; i < maxDay; i++) {
				drawTile(canvas, cPadding+((i+weekOfDayOne-1)%7)*width, rPadding+((i+weekOfDayOne-1)/7)*height, width, height, days[i+weekOfDayOne-1], textPaint);
			}
			// 画本月之后的日期
			textPaint.setColor(Color.LTGRAY);
			for(int i = 0; i < 42-(weekOfDayOne+maxDay)+1; i++) {
				drawTile(canvas, cPadding+((i+weekOfDayOne+maxDay-1)%7)*width, rPadding+((i+weekOfDayOne+maxDay-1)/7)*height, width, height, days[i+weekOfDayOne+maxDay-1], textPaint);
			}
			
			Paint hilight = new Paint();
			hilight.setColor(0xaaee0000);
			if(selected != null) {
				canvas.drawRect(selected, hilight);
			}
		}

		/** 本月第一天是星期几 */
		private int weekOfDayOne;
		/** 本月日期的最大值 */
		private int maxDay;
		/** 按行列排列的视图内的本月日期 */
		private int[] days = new int[6*7];
		
		public void toNextMonth() {
			ca.add(Calendar.MONTH, 1);
		}
		
		public void toPreviousMonth() {
			ca.add(Calendar.MONTH, -1);
		}
		
		
		public void update() {
			
			// 设置title栏信息
			DateFormat df = new SimpleDateFormat("yyyy年MM月");
			title.setText(df.format(ca.getTime()));
			// 获得本月的最大天数
			maxDay = ca.getActualMaximum(Calendar.DAY_OF_MONTH);
			// 获得本月第一天是星期几
			int back = ca.get(Calendar.DAY_OF_MONTH); // 缓存一下今天的日期，用于带回恢复
			ca.set(Calendar.DAY_OF_MONTH, 1);
			weekOfDayOne = ca.get(Calendar.DAY_OF_WEEK);
			// TODO 中国人的习惯，第一天是星期一不是星期天，所以要减一下
			weekOfDayOne--;
			// 当1号是星期天的时候，根据中国人习惯，应该赋值为7
			if(weekOfDayOne == 0) weekOfDayOne = 7;
			// 恢复初始的设置
			ca.set(Calendar.DAY_OF_MONTH, back);
			
			// ---------------------初始化days数组--------------------
			// 初始化最前面的几项
			toPreviousMonth(); // 前移一个月
			int previousMax = ca.getActualMaximum(Calendar.DAY_OF_MONTH);
			for(int i = weekOfDayOne - 2; i >= 0; i--) {
				days[i] = previousMax;
				previousMax--;
			}
			toNextMonth(); // 恢复
			// 载入本月日期
			for(int i = 0; i < maxDay; i++) {
				// 由于数组从0开始，而日期从1开始，所以要-1，之后又+1
				days[i+weekOfDayOne-1] = i+1; 
			}
			// 初始化末尾的几个数字
			for(int i = 0; i < 42-(weekOfDayOne+maxDay)+1; i++) {
				days[i+weekOfDayOne+maxDay-1] = i+1;
			}
		}
		
		/** 画日期的方块 */
		private void drawTile(Canvas canvas, float baseX, float baseY, float width, float height, 
							  int day, Paint paint) {
			float x = baseX + 5;
			float y = baseY + 20;
			
			canvas.drawText(String.valueOf(day), x, y, paint);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if(gestureDetector.onTouchEvent(event))
	    		return true;
	    	else 
	    		return false;
		}
	  
	    // 动画的方向
	    private static final int FLIP_RIGHTIN = 0;
	    private static final int FLIP_LEFTIN = 1;
	    
	    /** 滑动expandList */
	    private void flipView(int direction) {
	    	int width = getWidth();
	    	leftIn = new TranslateAnimation(-width, 0, 0, 0);
	    	leftIn.setDuration(300);
	    	rightOut = new TranslateAnimation(0, width, 0, 0);
	    	rightOut.setDuration(300);
	    	rightIn = new TranslateAnimation(width, 0, 0, 0);
	    	rightIn.setDuration(300);
	    	leftOut = new TranslateAnimation(0, -width, 0, 0);
	    	leftOut.setDuration(300);
	    	switch(direction) {
	    	case FLIP_RIGHTIN:
	    		switcher.setInAnimation(rightIn);
	    		switcher.setOutAnimation(leftOut);
	    		break;
	    	case FLIP_LEFTIN:
	    		switcher.setInAnimation(leftIn);
	    		switcher.setOutAnimation(rightOut);
	    		break;
	    	}
	    	MonthView view = (MonthView) switcher.getNextView();
	    	view.update();
			switcher.showNext();
	    }
	    
		class MyGestureListener implements GestureDetector.OnGestureListener {
	    	
			@Override
			public boolean onDown(MotionEvent event) {
				select(event.getX(), event.getY());
				return true;
			}
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				if(Math.abs(e2.getX() - e1.getX()) > 100) {
					if(e2.getX() < e1.getX()) { // 从右向左滑动
						toNextMonth();
						flipView(FLIP_RIGHTIN);
					}else if(e2.getX() > e1.getX()) { // 从左向右滑动
						toPreviousMonth();
						flipView(FLIP_LEFTIN);
					}
					return true;
				}
				return false;
			}
			@Override
			public void onLongPress(MotionEvent e) {
			}
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				return false;
			}
			@Override
			public void onShowPress(MotionEvent e) {
			}
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}
	    }
		
		/** 被选中的方块 */
		private Rect selected = null;
		
		// 选中某个坐标对应的方块
		public void select(float x, float y) {
			// 设置当前选中的日期
			ca.set(Calendar.DAY_OF_MONTH, getDay(x, y));
			ca.set(Calendar.MONTH, getMonth(x, y));
			
			// 设置选中的Rect
			int width = (int)width();
			int height = (int)height();
		
			int left = ((int)(x / width) * width) + 1;
			int top = ((int)y / height) * height + 1;
			int right = left + width;
			int bottom = top + height;
			selected = new Rect(left, top, right, bottom);
			
			// 更新屏幕
			invalidate();
		}
		
		/** 返回指定坐标上的日期 */
		private int getDay(float x, float y) {
			return days[(int)(y / height()) * 7 + (int)(x / width())];
		}
		
		/** 返回指定坐标上的月份 */
		private int getMonth(float x, float y) {
			if((int)(y / height()) * 7 + (int)(x / width()) < weekOfDayOne-1)
				return ca.get(Calendar.MONTH)-1;
			else if((int)(y / height()) * 7 + (int)(x / width()) < weekOfDayOne+maxDay-1)
				return ca.get(Calendar.MONTH);
			else
				return ca.get(Calendar.MONTH)+1;
		}
		
		/** 返回列宽 */
		private float width() {
			return getWidth() / 7;
		}
		
		/** 返回行高 */
		private float height() {
			return getHeight() / 6;
		}
	}

}
