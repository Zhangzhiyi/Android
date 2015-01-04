package com.zwayen.main;

import java.util.ArrayList;
import java.util.HashMap;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.zwayen.main.R.id;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

public class TestToolBarActivity extends ActionBarActivity implements View.OnClickListener {

	public static final int INDEX_HOME = 0;
	public static final int INDEX_APPS = 1;
	public static final int INDEX_GAMES = 2;
	public static final int INDEX_MUSIC = 3;
	public static final int INDEX_WALLPAPERS = 4;
	public static final int INDEX_RINGTONES = 5;
	public static final int INDEX_VIDEO = 6;

	private DrawerLayout mDrawerLayout;
	private Toolbar mToolbar;
	private ActionBarDrawerToggle mActionBarDrawerToggle;
	private ListView mListView;
	private ListViewAutoScrollHelper mListViewAutoScrollHelper;
	private View mHeaderView;
	private Toolbar mHeaderToolbar;
	private Button mHeaderButton;
	private HorizontalScrollView mHorizontalScrollView;
	private LinearLayout mScrollLayout;
	private LinearLayout mGuideBarLayout;
	private RelativeLayout mGuideBarParentLayout;
	private Button[] mTabButtons;
	private int mCurHeaderTop = 0;
	private int mCurTabIndex = INDEX_HOME;
	private int mFirstVisibleItem = -1;
	
	private LinearLayout contentLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.toolbar);
		contentLayout = (LinearLayout) findViewById(R.id.content_layout);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
		mDrawerLayout.setDrawerListener(new DemoDrawerListener());
		mToolbar = (Toolbar) findViewById(R.id.demo_toolbar);
		mToolbar.setTitle("Toolbar");
		mToolbar.setLogo(R.drawable.ic_launcher);
		setSupportActionBar(mToolbar);
		init();
		mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
		getSupportActionBar().hide();
		mListView = (ListView) findViewById(R.id.listView1);
		// 生成动态数组，加入数据
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 40; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", R.drawable.checked);// 图像资源的ID
			map.put("ItemTitle", "Level " + i);
			map.put("ItemText", "Finished in 1 Min 54 Secs, 70 Moves! ");
			listItem.add(map);
		}
		// 生成适配器的Item和动态数组对应的元素
		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// 数据源
				R.layout.list_items,// ListItem的XML实现
				// 动态数组与ImageItem对应的子项
				new String[] { "ItemImage", "ItemTitle", "ItemText" },
				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { R.id.ItemImage, R.id.ItemTitle, R.id.ItemText });
		// 去掉横线
		mListView.setDividerHeight(0);

		mHeaderView = LayoutInflater.from(this).inflate(R.layout.view_header_placeholder, null);
		mHeaderToolbar = (Toolbar) mHeaderView.findViewById(R.id.demo_toolbar);
		mHeaderButton = (Button) mHeaderView.findViewById(R.id.button1);
		mGuideBarParentLayout = (RelativeLayout) mHeaderView.findViewById(id.guidebar_parent);
		mGuideBarLayout = (LinearLayout) mHeaderView.findViewById(R.id.guidebar_layout);
		mHorizontalScrollView = (HorizontalScrollView) mHeaderView.findViewById(R.id.horizontalScrollView1);
		mScrollLayout = (LinearLayout) mHeaderView.findViewById(R.id.scroll_Linearlayout);
		final int childCount = mScrollLayout.getChildCount();
		mTabButtons = new Button[childCount];
		for (int i = 0; i < childCount; i++) {
			mTabButtons[i] = (Button) mScrollLayout.getChildAt(i);
			mTabButtons[i].setOnClickListener(this);
		}
		mHorizontalScrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				if (action == MotionEvent.ACTION_UP) {
					int scrollX = mHorizontalScrollView.getScrollX();
					int linearlayoutWidth = mScrollLayout.getWidth();
					int itemWidth = (int) (linearlayoutWidth / childCount + 0.5f);
					final int index = Math.round((float) Math.abs(scrollX) / itemWidth);
					mHorizontalScrollView.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							mHorizontalScrollView.smoothScrollTo(mTabButtons[index].getLeft(), 0);
						}
					});
				}
				return false;
			}
		});

		// mHeaderToolbar.setTitle("ListViewHeader Toolbar");
		mHeaderToolbar.setBackgroundColor(android.R.color.transparent);
		int height = mHeaderView.getHeight();
		mListView.addHeaderView(mHeaderView, null, false);
		mHeaderView.bringToFront();
		height = mHeaderView.getHeight();
		// 添加并且显示
		mListView.setAdapter(listItemAdapter);
		mListViewAutoScrollHelper = new ListViewAutoScrollHelper(mListView);
		mListViewAutoScrollHelper.setEnabled(true);
		mListView.setOnTouchListener(mListViewAutoScrollHelper);
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == SCROLL_STATE_IDLE && mFirstVisibleItem == 0) {
					if (mCurHeaderTop < toolbarHeight) {
						float fraction = mCurHeaderTop / toolbarHeight;
						long percent = (long) (500 * fraction);
						Log.i("fraction", "fraction:" + fraction + " percent:" + percent);
						if (mCurHeaderTop > toolbarHeight / 3) {
							ValueAnimator valueAnimator = ValueAnimator.ofInt(0, (int) (toolbarHeight + 0.5)).setDuration(500);
							valueAnimator.setInterpolator(new AccelerateInterpolator());
							valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

								@Override
								public void onAnimationUpdate(ValueAnimator animation) {
									// TODO Auto-generated method stub
									Log.i("ValueAnimator", "animation.getAnimatedValue() = " + animation.getAnimatedValue() + " getAnimatedFraction() = "
											+ animation.getAnimatedFraction());
									float fraction = animation.getAnimatedFraction();
									if (fraction != 0.0) {
										animationDependFraction(fraction);
									}

									int value = (Integer) animation.getAnimatedValue();
									// 需要判断一下当前的值是否大于Header当前的top值，因为设置了setCurrentPlayTime有一定的延迟
									if (value > mCurHeaderTop) {
										int offset = value - mCurHeaderTop;
										Log.i("offset", "offset:" + offset);
										mListViewAutoScrollHelper.scrollTargetBy(0, offset);
									}
								}
							});
							valueAnimator.start();
							valueAnimator.setCurrentPlayTime((long) (500 * fraction));
						} else {
							ValueAnimator valueAnimator = ValueAnimator.ofInt(mCurHeaderTop, 0).setDuration(200);
							valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

								@Override
								public void onAnimationUpdate(ValueAnimator animation) {
									// TODO Auto-generated method stub
									int value = (Integer) animation.getAnimatedValue();
									int offset = value - mCurHeaderTop;
									mListViewAutoScrollHelper.scrollTargetBy(0, offset);
								}
							});
							valueAnimator.start();
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				int top = 0;
				// Log.i("mListView", "mHeaderView:" + mHeaderView.getTop());
				mFirstVisibleItem = firstVisibleItem;
				if (firstVisibleItem == 0) {
					if (mListView.getChildAt(0) != null) {
						top = mListView.getChildAt(0).getTop();
						top = Math.abs(top);
						if (mCurHeaderTop == top) {
							return;
						}
						mCurHeaderTop = top;
						if (top <= toolbarHeight) {
							final float fraction = Math.abs(top) / toolbarHeight;
							mHorizontalScrollView.post(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									animationDependFraction(fraction);
								}
							});
						}
						if (top > toolbarHeight) {
//							LinearLayout.LayoutParams layoutParams = new LayoutParams(mHorizontalScrollView.getWidth(), mHorizontalScrollView.getHeight());
							((ViewGroup)mGuideBarLayout.getParent()).removeView(mGuideBarLayout);
//							mHorizontalScrollView.setLayoutParams(layoutParams);
							contentLayout.addView(mGuideBarLayout, 0);
						} else {
							((ViewGroup)mGuideBarLayout.getParent()).removeView(mGuideBarLayout);
							RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mGuideBarLayout.getWidth(), mGuideBarLayout.getHeight());
							layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
							layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
							mGuideBarLayout.setLayoutParams(layoutParams);
							mGuideBarParentLayout.addView(mGuideBarLayout);
						}
					}
					Log.i("mListView", "top:" + top);
				}
			}
		});
		mHeaderButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ValueAnimator valueAnimator = ValueAnimator.ofInt(144, 130).setDuration(500);
				valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						// TODO Auto-generated method stub
						Log.i("ValueAnimator",
								"animation.getAnimatedValue() = " + animation.getAnimatedValue() + " getAnimatedFraction() = "
										+ animation.getAnimatedFraction());
						float fraction = animation.getAnimatedFraction();
						animationDependFraction(fraction);
					}
				});
				valueAnimator.start();
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		Rect rect = new Rect();
		mHeaderButton.getGlobalVisibleRect(rect);
		Log.i("getGlobalVisibleRect", rect.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mActionBarDrawerToggle.syncState();
		super.onPostCreate(savedInstanceState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		mActionBarDrawerToggle.onConfigurationChanged(newConfig);
		super.onConfigurationChanged(newConfig);
	}

	private class DemoDrawerListener implements DrawerLayout.DrawerListener {
		@Override
		public void onDrawerOpened(View drawerView) {
			mActionBarDrawerToggle.onDrawerOpened(drawerView);
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			mActionBarDrawerToggle.onDrawerClosed(drawerView);
		}

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			mActionBarDrawerToggle.onDrawerSlide(drawerView, slideOffset);
		}

		@Override
		public void onDrawerStateChanged(int newState) {
			mActionBarDrawerToggle.onDrawerStateChanged(newState);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ic_home_btn:
			changeSelectBtn(INDEX_HOME);
			break;
		case R.id.ic_apps_btn:
			changeSelectBtn(INDEX_APPS);
			break;
		case R.id.ic_games_btn:
			changeSelectBtn(INDEX_GAMES);
			break;
		case R.id.ic_music_btn:
			changeSelectBtn(INDEX_MUSIC);
			break;
		case R.id.ic_wallpapers_btn:
			changeSelectBtn(INDEX_WALLPAPERS);
			break;
		case R.id.ic_ringtones_btn:
			changeSelectBtn(INDEX_RINGTONES);
			break;
		case R.id.ic_video_btn:
			changeSelectBtn(INDEX_VIDEO);
			break;

		}
	}

	public void changeSelectBtn(int index) {
		for (int i = 0; i < mTabButtons.length; i++) {
			if (i == index) {
				if (alphaForegroundSelectedColorSpan.getAlpha() == 1) {
					mTabButtons[i].setText(mTabButtons[i].getText().toString());
				}
				mTabButtons[i].setTextColor(getResources().getColor(android.R.color.white));
				mTabButtons[i].setSelected(true);
			} else {
				if (alphaForegroundColorSpan.getAlpha() == 1) {
					mTabButtons[i].setText(mTabButtons[i].getText().toString());
				}
				mTabButtons[i].setTextColor(getResources().getColor(R.color.guidebar_btn_text_color));
				mTabButtons[i].setSelected(false);
			}
		}
		mHorizontalScrollView.smoothScrollTo(mTabButtons[index].getLeft(), 0);
		mCurTabIndex = index;
	}

	float guideBarBtnWidth = 0;
	float guideBarBtnTransitionWidth = 0;
	float startPaddingTop = 0;
	float finalPaddingTop = 0;
	AlphaForegroundColorSpan alphaForegroundColorSpan;
	AlphaForegroundColorSpan alphaForegroundSelectedColorSpan;
	float guideBarRightWidth = 0;
	float screenWidth;
	float finalScrollWidth;
	float toolbarHeight;

	void init() {
		guideBarBtnWidth = getResources().getDimension(R.dimen.guidebar_btn_width);
		guideBarBtnTransitionWidth = getResources().getDimension(R.dimen.guidebar_btn_transition_width);
		alphaForegroundColorSpan = new AlphaForegroundColorSpan(getResources().getColor(R.color.guidebar_btn_text_color));
		alphaForegroundSelectedColorSpan = new AlphaForegroundColorSpan(getResources().getColor(android.R.color.white));
		startPaddingTop = getResources().getDimension(R.dimen.guidebar_btn_paddingTop);
		finalPaddingTop = getResources().getDimension(R.dimen.guidebar_btn_transition_drawableTop);
		guideBarRightWidth = getResources().getDimension(R.dimen.guidebar_right_section);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();
		finalScrollWidth = screenWidth - guideBarRightWidth;
		toolbarHeight = getResources().getDimension(R.dimen.toolbar_height);
	}

	public void animationDependFraction(float fraction) {

		float curWidth = guideBarBtnWidth - (guideBarBtnWidth - guideBarBtnTransitionWidth) * fraction;
		alphaForegroundColorSpan.setAlpha(1 - fraction);
		alphaForegroundSelectedColorSpan.setAlpha(1 - fraction);

		float curPaddingTop = startPaddingTop + (finalPaddingTop - startPaddingTop) * fraction;

		float curScrollWidth = screenWidth - (screenWidth - finalScrollWidth) * fraction;
		for (int i = 0; i < mScrollLayout.getChildCount(); i++) {
			Button button = (Button) mScrollLayout.getChildAt(i);
			LayoutParams layoutParams = (LayoutParams) button.getLayoutParams();
			layoutParams.width = (int) curWidth;
			button.setLayoutParams(layoutParams);

			SpannableString spannableString = new SpannableString(button.getText());
			if (i == mCurTabIndex) {
				spannableString.setSpan(alphaForegroundSelectedColorSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else {
				spannableString.setSpan(alphaForegroundColorSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			button.setText(spannableString);

			button.setPadding(button.getPaddingLeft(), (int) curPaddingTop, button.getPaddingRight(), button.getPaddingBottom());
		}

		LinearLayout.LayoutParams layoutParams = (LayoutParams) mHorizontalScrollView.getLayoutParams();
		layoutParams.width = (int) curScrollWidth;
		mHorizontalScrollView.setLayoutParams(layoutParams);

		mHorizontalScrollView.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHorizontalScrollView.smoothScrollTo(mTabButtons[mCurTabIndex].getLeft(), 0);
			}
		});
	}
}
