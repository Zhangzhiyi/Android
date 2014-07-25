package com.et.TestViewPager;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ViewPagerDot extends LinearLayout implements ViewPager.OnPageChangeListener {

	private Drawable mCurrentDrawable = null;
	private Drawable mNormalDrawable = null;
	private ViewPager mViewPager = null;
	private int mScrollState;
	private Context mContext = null;
	private ArrayList<ImageView> mImageViewList = null;
	private int count = 0;
	public int mCurrentPage = 0;

	public ViewPagerDot(Context context, boolean isDetail) {
		super(context);
		this.mContext = context;
	}

	public ViewPagerDot(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		loadDefaultDrawable();
	}

	private void loadDefaultDrawable() {
		Resources resources = getResources();
		mNormalDrawable = resources.getDrawable(R.drawable.indicator);
		mCurrentDrawable = resources.getDrawable(R.drawable.indicator_cur);
	}

	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		invalidate();
	}

	public void onPageSelected(int position) {
		if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
			invalidate();
		}
		setPage(position);
	}

	public void onPageScrollStateChanged(int state) {
		mScrollState = state;
	}

	public void setViewPager(ViewPager view) {
		if (view.getAdapter() == null) {
			throw new IllegalStateException("ViewPager does not have adapter instance.");
		}
		mViewPager = view;
		mViewPager.setOnPageChangeListener(this);
		init();
		invalidate();
	}

	private void init() {
		count = mViewPager.getAdapter().getCount();
		int tempCount = count;
		mImageViewList = new ArrayList<ImageView>();
		removeAllViews();
		for (int i = 0; i < tempCount; i++) {
			ImageView imageView = new ImageView(mContext);
			LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER;
			imageView.setLayoutParams(params);
			imageView.setPadding(8, 0, 8, 0);
			addView(imageView);
			if (i == 0) {
				imageView.setImageDrawable(this.mCurrentDrawable);
				mImageViewList.add(imageView);
			} else {
				imageView.setImageDrawable(this.mNormalDrawable);
				mImageViewList.add(imageView);
			}
		}
	}

	public void setPage(int curPage) {

		if (curPage >= count || curPage < 0 || curPage == this.mCurrentPage) {
			return;
		}
		mImageViewList.get(curPage).setImageDrawable(this.mCurrentDrawable);
		mImageViewList.get(this.mCurrentPage).setImageDrawable(this.mNormalDrawable);
		mCurrentPage = curPage;

	}
}
