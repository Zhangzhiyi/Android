package com.xiawenquan.test.widget;

import com.xiawenquan.test.asyncqueryhandler.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class AlphabetView extends View {
	private Bitmap bitmap = null;
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	private int choose = -1;
	private Paint paint = new Paint();
	private boolean isShowSearchIcon = true;
	private String[] alphabetList = { "#", "A", "B", "C", "D", "E", "F", "G",
			"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };
	private int mDefaultColor = Color.parseColor("#6a737d");
	private int mSelectColor = Color.parseColor("#3399ff");
	private float textSize = getResources().getDimension(
			R.dimen.alphabet_text_size);

	public AlphabetView(Context context) {
		super(context);
		init(context);
	}

	public AlphabetView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public AlphabetView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.alphabet_search_icon);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		final int height = getHeight();
		final int width = getWidth();
		int singleHeight = height / alphabetList.length;
		final int size = alphabetList.length;
		for (int i = 0; i < size; i++) {
			if (alphabetList[i].equals("#")) {
				if (isShowSearchIcon) {
					float xPos = width / 2 - bitmap.getWidth() / 2;
					float yPos = bitmap.getHeight() / 2;

					// if(i == choose) {// 设置搜索icon选中效果
					// final Bitmap b =
					// BitmapFactory.decodeResource(getResources(),
					// R.drawable.icon);
					// canvas.drawBitmap(b, xPos, yPos, paint);
					// } else {
					canvas.drawBitmap(bitmap, xPos, yPos, paint);
					// }
					paint.reset();
				}
			} else {
				paint.setColor(mDefaultColor);
				paint.setAntiAlias(true);
				paint.setTextSize(textSize);
				if (i == choose) {
					paint.setColor(mSelectColor);
					paint.setFakeBoldText(true);
				}
				float xPos = width / 2 - paint.measureText(alphabetList[i]) / 2;
				float yPos = singleHeight * i + singleHeight;
				;
				canvas.drawText(alphabetList[i], xPos, yPos, paint);
				paint.reset();
			}
		}

	}

	/**
	 * 设置字母列表默认颜色
	 * 
	 * @param color
	 */
	protected void setDefaultColor(int color) {
		mDefaultColor = color;
	}

	/**
	 * 设置字母列表选中颜色
	 * 
	 * @param color
	 */
	protected void setSelectColor(int color) {
		mSelectColor = color;
	}

	/**
	 * 设置字体大小
	 * 
	 * @param size
	 */
	protected void setTextSize(float size) {
		textSize = size;
	}

	/**
	 * 设置搜索icon
	 * 
	 * @param resid
	 */
	protected void setSearchIcon(int resid) {
		bitmap = BitmapFactory.decodeResource(getResources(), resid);
	}

	/**
	 * 设置搜索icon
	 * 
	 * @param resid
	 */
	protected void setSearchIcon(Drawable drawable) {
		bitmap = ((BitmapDrawable) drawable).getBitmap();
	}

	/**
	 * 设置是否显示搜索icon
	 * 
	 * @param isShowSearchIcon
	 */
	public void setShowSearchIcon(boolean isShowSearchIcon) {
		this.isShowSearchIcon = isShowSearchIcon;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * alphabetList.length);

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (oldChoose != c && listener != null) {
				if (c >= 0 && c < alphabetList.length) {
					if (alphabetList[c].equals("#")) {
						if (isShowSearchIcon) {
							listener.onTouchingLetterChanged("搜");
						}
					} else {
						listener.onTouchingLetterChanged(alphabetList[c]);
					}
					choose = c;
					setBackgroundResource(R.drawable.alphabet_list_bg);
					invalidate();
				}
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (oldChoose != c && listener != null) {
				if (c >= 0 && c < alphabetList.length) {
					if (alphabetList[c].equals("#")) {
						if (isShowSearchIcon) {
							listener.onTouchingLetterChanged("搜");
						}
					} else {
						listener.onTouchingLetterChanged(alphabetList[c]);
					}
					choose = c;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			choose = -1;
			setBackgroundDrawable(null);
			invalidate();
			break;
		}
		return true;
	}

	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}
}
