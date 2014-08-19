package com.et.TestViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

public class TestViewPagerActivity extends FragmentActivity {

	public static final String TAG = "TestViewPagerActivity";
	ViewPager mViewPager;
	FragmentPagerAdater pagerAdater;
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

		pagerAdater = new FragmentPagerAdater(getSupportFragmentManager());
		mViewPager.setAdapter(pagerAdater);
		// mViewPager.setAdapter(new ViewPagerAdater(this, lists));
		// pagerTabStrip只对PagerAdapter有效
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
				Log.i("onPageScrolled", "position:" + position + "|" + "positionOffset:" + positionOffset + "|" + "positionOffsetPixels:"
						+ positionOffsetPixels);
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
		// 控制循环滚动的handler
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				int curItem = mViewPager.getCurrentItem();
//				mViewPager.setCurrentItem(curItem + 1, true);
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
					Log.i(TAG, "MotionEvent.ACTION_DOWN");
					handler.removeMessages(1);
					return false;
				case MotionEvent.ACTION_UP:
					Log.i(TAG, "MotionEvent.ACTION_DOWN");
					handler.removeMessages(1);
					handler.sendEmptyMessageDelayed(1, 5000);
					return false;
				case MotionEvent.ACTION_CANCEL:
					Log.i(TAG, "MotionEvent.ACTION_CANCEL");
					handler.removeMessages(1);
					handler.sendEmptyMessageDelayed(1, 5000);
					return false;
				}
				return false;
			}
		});
		/** 用反射更改PagerTabStrip里面的私有变量的值 **/
		pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagerTabStrip);
		// 获取PagerTabStrip类的名字, "android.support.v4.view.PagerTabStrip"
		String className = PagerTabStrip.class.getName();
		// 获取类对象
		Class<?> classObject;
		try {

			classObject = Class.forName(className);
			// 获取私有属性要用declared
			Field privateField = classObject.getDeclaredField("mIndicatorHeight");
			// 设置不进行语言访问检查
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

	/** 可以接受到其它工程Activity的返回值 **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onActivityResult");
	}

	public class FragmentPagerAdater extends FragmentStatePagerAdapter {
		public FragmentPagerAdater(FragmentManager fm) {
			// TODO Auto-generated constructor stub
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			return ArrayListFragment.newInstance(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Integer.MAX_VALUE;
		}

	}

	public static class ArrayListFragment extends Fragment {
		static ArrayListFragment newInstance(int num) {
			ArrayListFragment f = new ArrayListFragment();
			Bundle args = new Bundle();
			args.putInt("num", num);
			f.setArguments(args);
			return f;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.innerpager_item_layout, container, false);
			
			//Banner
			ViewPager viewPager = (ViewPager) layout.findViewById(R.id.viewPager);
			ViewPagerDot viewPagerDot = (ViewPagerDot) layout.findViewById(R.id.details_dot);
			ArrayList<Integer> lists = new ArrayList<Integer>();
			lists.add(R.drawable.drawable1);
			lists.add(R.drawable.drawable2);
			lists.add(R.drawable.drawable3);
			MyInnerPagerAdapter pagerAdater = new MyInnerPagerAdapter(getActivity(), lists);
			viewPager.setAdapter(pagerAdater);
			viewPagerDot.setViewPager(viewPager);
			viewPager.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						Log.i(TAG , "Banner:" + "MotionEvent.ACTION_DOWN");
						return false;
					case MotionEvent.ACTION_MOVE:
						Log.i(TAG, "Banner:" + "MotionEvent.ACTION_MOVE");
						break;
					case MotionEvent.ACTION_UP:
						Log.i(TAG, "Banner:" + "MotionEvent.ACTION_UP");
						return false;
					case MotionEvent.ACTION_CANCEL:
						Log.i(TAG, "Banner:" + "MotionEvent.ACTION_CANCEL");
						return false;
					}
					return false;
				}
			});
			//Banner + ListView
			View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_header, null);
			ViewPager headViewPager = (ViewPager) headerView.findViewById(R.id.viewPager);
			ViewPagerDot headPagerDot = (ViewPagerDot) headerView.findViewById(R.id.details_dot);
			ArrayList<Integer> headlists = new ArrayList<Integer>();
			headlists.add(R.drawable.drawable1);
			headlists.add(R.drawable.drawable2);
			headlists.add(R.drawable.drawable3);
			MyInnerPagerAdapter headPagerAdater = new MyInnerPagerAdapter(getActivity(), headlists);
			headViewPager.setAdapter(headPagerAdater);
			headPagerDot.setViewPager(headViewPager);

			ListView listView = (ListView) layout.findViewById(R.id.listView1);
			ListAdapter listdapter = new ListAdapter(getActivity());
			listView.addHeaderView(headerView);
			listView.setAdapter(listdapter);
			
			headViewPager.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						Log.i(TAG , "Banner + ListView:" + "MotionEvent.ACTION_DOWN");
						return false;
					case MotionEvent.ACTION_MOVE:
						Log.i(TAG, "Banner + ListView:" + "MotionEvent.ACTION_MOVE");
						break;
					case MotionEvent.ACTION_UP:
						Log.i(TAG, "Banner + ListView:" + "MotionEvent.ACTION_UP");
						return false;
					case MotionEvent.ACTION_CANCEL:
						Log.i(TAG, "Banner + ListView:" + "MotionEvent.ACTION_CANCEL");
						return false;
					}
					return false;
				}
			});
			return layout;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
		}
		@Override
		public void onSaveInstanceState(Bundle outState) {
			// TODO Auto-generated method stub
			super.onSaveInstanceState(outState);
			outState.putInt("num", getArguments().getInt("num"));
		}
	}
}