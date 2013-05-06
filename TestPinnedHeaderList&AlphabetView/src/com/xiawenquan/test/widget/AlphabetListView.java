package com.xiawenquan.test.widget;

import java.util.HashMap;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiawenquan.test.asyncqueryhandler.R;
import com.xiawenquan.test.widget.AlphabetView.OnTouchingLetterChangedListener;

public class AlphabetListView extends RelativeLayout {
	private Context mContext;
	private PinnedHeaderListView mListView;
	private AlphabetView mAlphabetView;
	private HashMap<String, Integer> alphaIndexer;
	private TextView overlay;
	private Handler handler;
	private OverlayThread overlayThread;

	public AlphabetListView(Context context) {
		super(context);
		init(context);
	}

	public AlphabetListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public AlphabetListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
	}
	private void init(Context context) {

		mContext = context;
		LayoutInflater.from(context)
				.inflate(R.layout.alphabet_list, this, true);
		mListView = (PinnedHeaderListView) findViewById(R.id.list_view);
		mAlphabetView = (AlphabetView) findViewById(R.id.alphabet_view);
		handler = new Handler();
		overlayThread = new OverlayThread();
		initOverlay();
		
		mAlphabetView
				.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

					public void onTouchingLetterChanged(String s) {
						if (alphaIndexer == null) {
							// throw new
							// RuntimeException("setAlphabetIndex方法未赋值");
						} else {
							final Integer position = alphaIndexer.get(s);
							if (position != null) {
								mListView.setSelection(position);
								overlay.setText(s);
								overlay.setVisibility(View.VISIBLE);
								handler.removeCallbacks(overlayThread);
								// 延迟一秒后执行，让overlay为不可见
								handler.postDelayed(overlayThread, 1500);
							} else if ("搜".equals(s)) {
								mListView.setSelection(0);
								overlay.setText(s);
								overlay.setVisibility(View.VISIBLE);
								handler.removeCallbacks(overlayThread);
								// 延迟一秒后执行，让overlay为不可见
								handler.postDelayed(overlayThread, 1500);
							}
						}
					}
				});
	}

	/**
	 * 设置首字母title再ListView中的位置
	 * 
	 * @param alphaIndexer
	 */
	public void setAlphabetIndex(HashMap<String, Integer> alphaIndexer) {
		this.alphaIndexer = alphaIndexer;
	}

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		final WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		final WindowManager windowManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}
	/**
	 * 设置overlay不可见
	 */
	public class OverlayThread implements Runnable {

		public void run() {
			overlay.setVisibility(View.GONE);
		}
	}

	public void setListViewBackgroundResource(int resid) {
		if (mListView != null) {
			mListView.setBackgroundResource(resid);
		}
	}

	public void setListViewBackgroundDrawable(Drawable d) {
		if (mListView != null) {
			mListView.setBackgroundDrawable(d);
		}
	}

	public void setListViewDivider(Drawable divider) {
		if (mListView != null) {
			mListView.setDivider(divider);
		}
	}

	public void setAdapter(BaseAdapter adapter) {
		if (mListView != null) {
			mListView.setAdapter(adapter);
		}
	}
	public void setOnScrollListener(OnScrollListener onScrollListener){
		if (mListView != null) {
			mListView.setOnScrollListener(onScrollListener);
		}
	}
	public void setPinnedHeaderView(int layoutId){
		if (mListView != null) {
			mListView.setPinnedHeaderView(LayoutInflater.from(getContext())
					.inflate(layoutId, mListView, false));
		}
	}
	public void setListViewsetVisibility(int visibility) {
		if (mListView != null) {
			mListView.setVisibility(visibility);
		}
	}

	/**
	 * 设置字母列表默认颜色
	 * 
	 * @param color
	 */
	public void setDefaultColor(int color) {
		if (mAlphabetView != null) {
			mAlphabetView.setDefaultColor(color);
		}
	}

	/**
	 * 设置字母列表选中颜色
	 * 
	 * @param color
	 */
	public void setSelectColor(int color) {
		if (mAlphabetView != null) {
			mAlphabetView.setSelectColor(color);
		}
	}

	/**
	 * 设置字体大小
	 * 
	 * @param size
	 */
	public void setTextSize(float size) {
		if (mAlphabetView != null) {
			mAlphabetView.setTextSize(size);
		}
	}

	/**
	 * 设置搜索icon
	 * 
	 * @param resid
	 */
	public void setSearchIcon(int resid) {
		if (mAlphabetView != null) {
			mAlphabetView.setSearchIcon(resid);
		}
	}

	/**
	 * 设置搜索icon
	 * 
	 * @param resid
	 */
	public void setSearchIcon(Drawable drawable) {
		if (mAlphabetView != null) {
			mAlphabetView.setSearchIcon(drawable);
		}
	}

	/**
	 * 设置是否显示搜索icon
	 * 
	 * @param isShowSearchIcon
	 */
	public void setShowSearchIcon(boolean isShowSearchIcon) {
		if (mAlphabetView != null) {
			mAlphabetView.setShowSearchIcon(isShowSearchIcon);
		}
	}

	/**
	 * 设置索引浮窗字号
	 * 
	 * @param size
	 */
	public void setOverlayTextSize(float size) {
		if (overlay != null) {
			overlay.setTextSize(size);
		}
	}

	/**
	 * 设置索引浮窗字体颜色
	 * 
	 * @param color
	 */
	public void setOverlayTextColor(int color) {
		if (overlay != null) {
			overlay.setTextColor(color);
		}
	}

	/**
	 * 设置索引浮窗背景
	 * 
	 * @param resId
	 */
	public void setOverlayBackground(int resid) {
		if (overlay != null) {
			overlay.setBackgroundResource(resid);
		}
	}

	public void setOnItemClickListener(final OnItemClickListener listener) {
		if (mListView != null) {
			mListView
					.setOnItemClickListener(new ListView.OnItemClickListener() {

						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							if (listener != null) {
								listener.onItemClick(arg0, arg1, arg2, arg3);
							}

						}
					});
		}
	}

	public void setOnItemLongClickListener(
			final OnItemLongClickListener listener) {
		if (mListView != null) {
			mListView
					.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							if (listener != null) {
								listener.onItemLongClick(arg0, arg1, arg2, arg3);
							}
							return false;
						}
					});
		}
	}

	public interface OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id);
	}

	public interface OnItemLongClickListener {
		public void onItemLongClick(AdapterView<?> parent, View view,
				int position, long id);
	}

	public void setAlphabetViewVisibility(int visibility) {
		if (mAlphabetView != null) {
			mAlphabetView.setVisibility(visibility);
		}
	}
}
