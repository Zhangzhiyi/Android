package com.et.testheaderlistview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.et.testheaderlistview.R;

public class MainActivity extends Activity {

	private ListView listView;
	private LinearLayout linearLayout;
	private LinearLayout headerLayout;
	private CustomScrollLayout customScrollLayout;
	int[] drawables = { R.drawable.drawable1, R.drawable.drawable2, R.drawable.drawable3 };

	int startX;
	int startY;
	
	int startX2;
	int startY2;
	
	boolean isIntercept;
	boolean isIntercept2;

	public ScrollView mScrollView;
	
	public List<AppInfo> lists = new ArrayList<AppInfo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		linearLayout = (LinearLayout) findViewById(R.id.linearLaout1);
		listView = (ListView) findViewById(R.id.listView1);
		MyAdapter myAdapter = new MyAdapter(this);
		for (int i = 0; i < 30; i++) {
			lists.add(new AppInfo());
		}
		myAdapter.setData(lists);
		headerLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.scroll_layout, null);
		customScrollLayout = (CustomScrollLayout) headerLayout.findViewById(R.id.customScrollLayout);
		mScrollView = (ScrollView) headerLayout.findViewById(R.id.scrollView1);
		for (int i = 0; i < drawables.length; i++) {
			ImageView mImageView = new ImageView(this);
			mImageView.setImageResource(drawables[i]);
			mImageView.setId(i);
			customScrollLayout.addView(mImageView);
		}
		listView.addHeaderView(headerLayout);
		listView.setAdapter(myAdapter);
		customScrollLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				int x = (int) event.getX();
				int y = (int) event.getY();
				if (Math.abs(x - startX) > 50 && !isIntercept) {
					isIntercept = true;
				}
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getX();
					startY = (int) event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					/**
					 * 当ScrollLayout发生左右移动的时候， the child does not want the
					 * parent to intercept touch events.
					 **/
					if (isIntercept) {
						/**
						 * requestDisallowInterceptTouchEvent(true)方法, True if
						 * the child does not want the parent to intercept touch
						 * events.
						 * 可以使触摸事件跳过onInterceptTouchEvent直接分发LinearLayout布局的childView
						 * .
						 * 
						 * 并且需要每一次触摸事件都要调用,否则只在调用之后那一次触摸事件有效
						 */
						listView.requestDisallowInterceptTouchEvent(true);
					}
					break;
				case MotionEvent.ACTION_UP:
					listView.requestDisallowInterceptTouchEvent(false);
					isIntercept = false;
					break;
				}
				return false;
			}
		});
		mScrollView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				int x = (int) event.getX();
				int y = (int) event.getY();
				if (Math.abs(y - startY2) > 50 && !isIntercept2) {
					isIntercept2 = true;
				}
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					startX2 = (int) event.getX();
					startY2 = (int) event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					if (isIntercept2) {
						listView.requestDisallowInterceptTouchEvent(true);
					}
					break;
				case MotionEvent.ACTION_UP:
					listView.requestDisallowInterceptTouchEvent(false);
					isIntercept2 = false;
					break;
				case MotionEvent.ACTION_CANCEL:
					break;
				}
				return false;
			}
		});
	}

	public static class MyAdapter extends BaseAdapter {

		Context context;
		public List<AppInfo> lists;
		public MyAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
		}
		public void setData(List<AppInfo> lists){
			this.lists = lists;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (lists != null) {
				return lists.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflate.inflate(R.layout.list_items, null);
				viewHolder = new ViewHolder();
				viewHolder.textView1 = (TextView) convertView.findViewById(R.id.ItemTitle);
				viewHolder.textView2 = (TextView) convertView.findViewById(R.id.ItemText);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.textView1.setText("Level" + position);
			viewHolder.textView2.setText("Finished in 1 Min 54 Secs, 70 Moves! ");
			return convertView;
		}
		private static class ViewHolder {
			TextView textView1;
			TextView textView2;
		}
	}

	
}
