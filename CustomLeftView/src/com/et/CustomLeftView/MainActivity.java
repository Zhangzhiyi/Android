package com.et.CustomLeftView;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{

	private Button mButton;
	private Button mButton1;
	private CustomLeftView mCustomLinearLayout;
	private Handler handler;
	private HorizontalScrollView mTopmenuPopupwindows;
	private PopupWindow popupWindow;
	
	private ImageButton fbRecentlyBtn;
	
	private ImageButton fbAndroidBtn;
	private LinearLayout fbAndroidLayout;
	
	private ImageButton fbEmojiBtn;
	private LinearLayout fbEmojiLayout;
	
	private ImageButton fbKoreanBtn;
	private LinearLayout fbKoreanLayout;
	
	private ImageButton fbFaceBtn;
	private LinearLayout fbFaceLayout;
	private Button backBtn;
	private HorizontalScrollView mFacekeyboard;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mButton = (Button) findViewById(R.id.button);
		mButton1 = (Button) findViewById(R.id.button1);
//		mButton1.setPressed(true);
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		mCustomLinearLayout = (CustomLeftView) inflater.inflate(R.layout.leftview, null);
		mTopmenuPopupwindows = (HorizontalScrollView) inflater.inflate(R.layout.topmenu_popupwindow, null).findViewById(R.id.HorizontalScrollView01);
		
		mButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				int x = (int) event.getRawX();
				int y = (int) event.getY();
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						Log.i("ACTION_DOWN", "x:" + x + "|" + "y:" + y);
						handler.removeCallbacks(LeftViewRunnable);
						handler.postDelayed(LeftViewRunnable, 400);
						
						break;
					case MotionEvent.ACTION_MOVE:
						Log.i("ACTION_MOVE", "x:" + x + "|" + "y:" + y);
						if (y < 0) {
							int distance = Math.abs(y);
							int index = (distance / 70);
							mCustomLinearLayout.setHighlightView(4 -index);
						}
						break;
						
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						Log.i("ACTION_UP", "x:" + x + "|" + "y:" + y);
						handler.removeCallbacks(LeftViewRunnable);
						mCustomLinearLayout.hide();
						break;

				}
				return false;
			}
		});
		final int width = getResources().getDisplayMetrics().widthPixels;
		final float density = getResources().getDisplayMetrics().density;
		popupWindow = new PopupWindow(this);
		mButton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("1111111", "111111111111");
				popupWindow.setContentView(mTopmenuPopupwindows);
				popupWindow.setAnimationStyle(R.style.SymPopupAnimation);
				//如果背景设置为空，滑动会出问题,
				popupWindow.setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
				popupWindow.setWidth(width);
				popupWindow.setHeight((int) (65 * density));
				int[] offest = new int[2];
				v.getLocationInWindow(offest);
				popupWindow.showAtLocation(mButton1, Gravity.NO_GRAVITY, offest[0], offest[1] - 50);
			}
		});
		mButton1.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "Long Click", Toast.LENGTH_SHORT).show();
				return true;
			}
		});
		fbRecentlyBtn = (ImageButton) findViewById(R.id.facekeyboard1_recently);
		fbRecentlyBtn.setOnClickListener(this);
		fbAndroidBtn = (ImageButton) findViewById(R.id.facekeyboard1_android);
		fbAndroidBtn.setOnClickListener(this);
		fbAndroidLayout = (LinearLayout) findViewById(R.id.facekeyboard1_android_linearLayout);
		fbEmojiBtn = (ImageButton) findViewById(R.id.facekeyboard1_emoji);
		fbEmojiBtn.setOnClickListener(this);
		fbEmojiLayout = (LinearLayout) findViewById(R.id.facekeyboard1_emoji_linearLayout);
		fbKoreanBtn = (ImageButton) findViewById(R.id.facekeyboard1_korean);
		fbKoreanBtn.setOnClickListener(this);
		fbKoreanLayout = (LinearLayout) findViewById(R.id.facekeyboard1_korean_linearLayout);
		fbFaceBtn = (ImageButton) findViewById(R.id.facekeyboard1_face);
		fbFaceBtn.setOnClickListener(this);
		fbFaceLayout = (LinearLayout) findViewById(R.id.facekeyboard1_face_linearLayout);
		mFacekeyboard = (HorizontalScrollView) findViewById(R.id.facekeyboard);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				centerPoint = (mFacekeyboard.getRight() - mFacekeyboard.getLeft())/2;
				Log.i("11111111Rect", rect.toString());
				if (curLayoutId != 0) {
					View view = findViewById(curLayoutId);
					view.getHitRect(rect);
					Log.i("getHitRect", rect.toString());
//					findViewById(curLayoutId).getLocalVisibleRect(rect);
//					Log.i("getLocalVisibleRect", rect.toString());
					int[] offest = new int[2];
					view.getLocationInWindow(offest);
					Log.i("offest", "" + offest[0] + "|" + offest[1]);
					int layoutLeft = offest[0] - mFacekeyboard.getLeft();
					int centerOfLayout = layoutLeft + view.getWidth()/2;
					int distance = centerOfLayout - centerPoint;
					mFacekeyboard.smoothScrollBy(distance, 0);
					
				}
				
			}
		});
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case R.id.facekeyboard1_recently:
					curBtnId = R.id.facekeyboard1_recently;
					curLayoutId = R.id.facekeyboard1_recently;
					break;
				case R.id.facekeyboard1_android:
					fbAndroidBtn.setVisibility(View.GONE);
					fbAndroidLayout.setVisibility(View.VISIBLE);
					curBtnId = R.id.facekeyboard1_android;
					curLayoutId = fbAndroidLayout.getId();
					break;
				case R.id.facekeyboard1_emoji:
					fbEmojiBtn.setVisibility(View.GONE);
					fbEmojiLayout.setVisibility(View.VISIBLE);
					curBtnId = R.id.facekeyboard1_emoji;
					curLayoutId = fbEmojiLayout.getId();
					break;
				case R.id.facekeyboard1_korean:
					fbKoreanBtn.setVisibility(View.GONE);
					fbKoreanLayout.setVisibility(View.VISIBLE);
					curBtnId = R.id.facekeyboard1_korean;
					curLayoutId = fbKoreanLayout.getId();
					break;
				case R.id.facekeyboard1_face:
					fbFaceBtn.setVisibility(View.GONE);
					fbFaceLayout.setVisibility(View.VISIBLE);
					curBtnId = R.id.facekeyboard1_face;
					curLayoutId = fbFaceLayout.getId();
					break;
			}
				
			}
		};
	}
	Runnable LeftViewRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			mCustomLinearLayout.show(mButton);
		}
	};
	public int centerPoint;
	public int curBtnId = 0;
	public int curLayoutId = 0;
	public Rect rect = new Rect();
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		restoreBtnVisible(curBtnId, curLayoutId);
		handler.sendEmptyMessageDelayed(v.getId(), 200);
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				scrollToCenter(curLayoutId);
			}
		}, 300);
		
	}
	public void restoreBtnVisible(int btnId, int layoutId){
		if (btnId == R.id.facekeyboard1_recently || btnId == 0) {
			return;
		}
		findViewById(btnId).setVisibility(View.VISIBLE);
		findViewById(layoutId).setVisibility(View.GONE);
	}
	public void scrollToCenter(int layoutId){
		centerPoint = (mFacekeyboard.getRight() - mFacekeyboard.getLeft())/2;
		Log.i("11111111Rect", rect.toString());
		if (curLayoutId != 0) {
			View view = findViewById(curLayoutId);
			view.getHitRect(rect);
			Log.i("getHitRect", rect.toString());
//			findViewById(curLayoutId).getLocalVisibleRect(rect);
//			Log.i("getLocalVisibleRect", rect.toString());
			int[] offest = new int[2];
			view.getLocationInWindow(offest);
			Log.i("offest", "" + offest[0] + "|" + offest[1]);
			int layoutLeft = offest[0] - mFacekeyboard.getLeft();
			int centerOfLayout = layoutLeft + view.getWidth()/2;
			int distance = centerOfLayout - centerPoint;
			mFacekeyboard.smoothScrollBy(distance, 0);
			
		}
	}
}
