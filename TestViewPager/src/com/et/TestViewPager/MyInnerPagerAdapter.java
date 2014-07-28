package com.et.TestViewPager;

import java.util.ArrayList;

import com.et.TestViewPager.TestViewPagerActivity.FragmentPagerAdater;

import android.content.Context;
import android.graphics.Matrix.ScaleToFit;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

public class MyInnerPagerAdapter extends PagerAdapter {

	private final static String TAG = "MyPagerAdater";
	ArrayList<Integer> drawables;
	Context context;
	public MyInnerPagerAdapter(Context context, ArrayList<Integer> drawables) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.drawables = drawables;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		Log.i(TAG, "getCount()");
//		return drawables.size();
		return drawables.size();
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		Log.i(TAG, "instantiateItem");
		ImageView imageView = new ImageView(context);
		imageView.setScaleType(ScaleType.FIT_XY);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		imageView.setLayoutParams(params);
		imageView.setImageResource(drawables.get(position));
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
		container.removeView((View) object);
		Log.i(TAG, "destroyItem");
	}

}
