package com.et.TestViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class TestViewPagerActivity extends Activity {
	
	public static final String TAG = "TestViewPagerActivity";
	ViewPager mViewPager ;
	MyPagerAdater pagerAdater;
	PagerTabStrip pagerTabStrip;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mViewPager = (ViewPager) findViewById(R.id.viewpagerLayout);
        pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagerTabStrip);
        pagerTabStrip.setTabIndicatorColorResource(android.R.color.white);
        ArrayList<Integer> lists = new ArrayList<Integer>();
        lists.add(R.drawable.drawable1);
        lists.add(R.drawable.drawable2);
        lists.add(R.drawable.drawable3);

        pagerAdater = new MyPagerAdater(this, lists);
        mViewPager.setAdapter(pagerAdater);
        mViewPager.setCurrentItem(6000);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
        	private final static String TAG = "OnPageChangeListener";
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				Log.i("onPageSelected", "position:" + position);
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				Log.i("onPageScrolled", "position:" + position + "|" + "positionOffset:" + positionOffset + "|" + "positionOffsetPixels:" + positionOffsetPixels);
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				switch (state) {
					case ViewPager.SCROLL_STATE_IDLE:
						Log.i("onPageScrollStateChanged", "SCROLL_STATE_IDLE");
						break;
					case ViewPager.SCROLL_STATE_DRAGGING:
						Log.i("onPageScrollStateChanged", "SCROLL_STATE_DRAGGING");
						break;
					case ViewPager.SCROLL_STATE_SETTLING:
						Log.i("onPageScrollStateChanged", "SCROLL_STATE_SETTLING");
						break;
					
				}
			}
		});
        final Handler handler = new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		// TODO Auto-generated method stub
        		int curItem = mViewPager.getCurrentItem();
				mViewPager.setCurrentItem(curItem + 1 , true);
				sendEmptyMessageDelayed(1, 3000);
        	}
        };
        handler.sendEmptyMessageDelayed(1, 5000);
        mViewPager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						handler.removeMessages(1);
						return false;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						handler.sendEmptyMessageDelayed(1, 5000);
						return false;
				}
				return false;
			}
		});
        /**用反射更改PagerTabStrip里面的私有变量的值**/
        pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagerTabStrip);
        //获取PagerTabStrip类的名字,  "android.support.v4.view.PagerTabStrip"
        String className = PagerTabStrip.class.getName();
        //获取类对象
		Class<?> classObject;
		try {
			
			classObject = Class.forName(className);
			//获取私有属性要用declared
			Field privateField = classObject.getDeclaredField("mIndicatorHeight");
			//设置不进行语言访问检查
			privateField.setAccessible(true);
			int mIndicatorHeight = privateField.getInt(pagerTabStrip);
			privateField.setInt(pagerTabStrip, 0);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    /**可以接受到其它工程Activity的返回值**/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	Log.i(TAG, "onActivityResult");
    }
    class MyPagerAdater extends PagerAdapter{
    	
    	private final static String TAG = "MyPagerAdater";
    	ArrayList<Integer> drawables;
    	Context context;
    	public MyPagerAdater(Context context, ArrayList<Integer> drawables) {
			// TODO Auto-generated constructor stub
    		this.context = context;
    		this.drawables = drawables;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
//			Log.i(TAG, "getCount()");
//			return drawables.size();
			return Integer.MAX_VALUE;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			Log.i(TAG, "instantiateItem");
			ImageView imageView = new ImageView(context);
//			imageView.setScaleType(ScaleType.FIT_XY);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			imageView.setLayoutParams(params);
			imageView.setImageResource(drawables.get(position%drawables.size()));
			imageView.setScaleType(ScaleType.FIT_XY);
			container.addView(imageView);
			return imageView;
		}
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return "教程" + position;
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			Log.i(TAG, "isViewFromObject");
			return arg0 == arg1;
		}
		@Override
		public void startUpdate(ViewGroup container) {
			// TODO Auto-generated method stub
			Log.i(TAG, "startUpdate");
			super.startUpdate(container);
		}
		@Override
		public void finishUpdate(ViewGroup container) {
			// TODO Auto-generated method stub
			Log.i(TAG, "finishUpdate");
			super.finishUpdate(container);
		}
    	@Override
    	public void destroyItem(ViewGroup container, int position, Object object) {
    		// TODO Auto-generated method stub
    		Log.i(TAG, "destroyItem");
    		ImageView image = (ImageView) object;
    		Drawable drawable = image.getDrawable();
    		drawable = null;
//    		drawables.remove(position);
    	}
    }
}