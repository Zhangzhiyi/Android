package com.et.TestViewPager;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ViewPagerAdater extends PagerAdapter {

	private final static String TAG = "ViewPagerAdater";
	ArrayList<Integer> drawables;
	Context context;
	public ViewPagerAdater(Context context, ArrayList<Integer> drawables) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.drawables = drawables;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		Log.i(TAG, "getCount()");
//		return drawables.size();
		return Integer.MAX_VALUE;
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		Log.i(TAG, "instantiateItem:" + position);
		LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.innerpager_item_layout, null);
//		ImageView imageView = (ImageView) layout.findViewById(R.id.imageView1);
//		imageView.setImageResource(drawables.get(position%drawables.size()));
		
		ViewPager viewPager = (ViewPager) layout.findViewById(R.id.viewPager);
		ViewPagerDot viewPagerDot = (ViewPagerDot) layout.findViewById(R.id.details_dot);
		ArrayList<Integer> lists = new ArrayList<Integer>();
        lists.add(R.drawable.drawable1);
        lists.add(R.drawable.drawable2);
        lists.add(R.drawable.drawable3);
		MyInnerPagerAdapter pagerAdater = new MyInnerPagerAdapter(context, lists);
		viewPager.setAdapter(pagerAdater);
		viewPagerDot.setViewPager(viewPager);
		
		View headerView = LayoutInflater.from(context).inflate(R.layout.listview_header, null);
		ViewPager headViewPager = (ViewPager) headerView.findViewById(R.id.viewPager);
		ViewPagerDot headPagerDot = (ViewPagerDot) headerView.findViewById(R.id.details_dot);
		ArrayList<Integer> headlists = new ArrayList<Integer>();
		headlists.add(R.drawable.drawable1);
		headlists.add(R.drawable.drawable2);
		headlists.add(R.drawable.drawable3);
		MyInnerPagerAdapter headPagerAdater = new MyInnerPagerAdapter(context, headlists);
		headViewPager.setAdapter(headPagerAdater);
		headPagerDot.setViewPager(headViewPager);
		
		ListView listView = (ListView) layout.findViewById(R.id.listView1);
		ListAdapter listdapter = new ListAdapter(context);
		listView.addHeaderView(headerView);
		listView.setAdapter(listdapter);
		container.addView(layout);
		return layout;
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
		container.removeView((View) object);
		Log.i(TAG, "destroyItem");
	}

}
