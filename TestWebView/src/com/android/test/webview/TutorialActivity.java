package com.android.test.webview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

public class TutorialActivity extends Activity implements OnTouchListener,
	OnGestureListener{
	
	public final static String Type= "Type";
	
	private int sections_cnt = 8;
	
	//手势检测
	private GestureDetector mGestureDetector;
	//滑动像素
	private final int SliderPixD = 100;
	//滑动速度
	private final int SliderPixV = 30;
	
	ViewFlipper mViewFlipper;
	LinearLayout mbottom;
	
	List<ImageView> imglist = new ArrayList<ImageView>();
	
	int pagenum = 0;
	int inType = -1;
	
	boolean has_set_left = false;
	boolean has_set_right = false;
	
	String main_url = "file:///android_asset/zh/tutorial_zh_page3.html";
	
	String[] html_url = {"file:///android_asset/zh/tutorial_zh_page3.html",
						 "file:///android_asset/zh/tutorial_zh_page4.html",
						 "file:///android_asset/zh/tutorial_zh_page3.html",
						 "file:///android_asset/zh/tutorial_zh_page4.html",
						 "file:///android_asset/zh/tutorial_zh_page3.html",
						 "file:///android_asset/zh/tutorial_zh_page4.html",
						 "file:///android_asset/zh/tutorial_zh_page3.html",
						 "file:///android_asset/zh/tutorial_zh_page4.html"};
	private String[] keys = {"ver","lang"};;
	private String[] values;
	
	//获取该页对应的链接
	private String GetUrl(String ver,String lang,int num){
		
		String n = String.valueOf(num);
		String ret = "http://goodphone.mobi/keyboard/assets/" + "/" + ver + "/" + lang + "/" + n + ".html";
		//String ret = "http://goodphone.mobi/keyboard/assets/" + "//" + values[0] + "//" + lang + "//" + n + ".html";
		return ret;
	}
		
	
	public void HandleLoad(int which,String url){
		
		WebView wv = (WebView) mViewFlipper.getChildAt(which);
		
		wv.loadUrl(url);

	}
	public void DrawBottom(boolean isleft){
		if(isleft){
			imglist.get(pagenum+1).setImageResource(R.drawable.tutor_next);
			imglist.get(pagenum).setImageResource(R.drawable.tutor_cur);
		}
		else{
			imglist.get(pagenum-1).setImageResource(R.drawable.tutor_next);
			imglist.get(pagenum).setImageResource(R.drawable.tutor_cur);
		}
	}
	
	public void HandleLeft(){
		
		if(pagenum != 0){
			
			if(!has_set_left)
			{
				mViewFlipper.setOutAnimation(this, R.anim.tutorial_left_out);
				mViewFlipper.setInAnimation(this, R.anim.tutorial_left_in);
				has_set_right = false;
				has_set_left = true;
			}
			

			--pagenum;
			if(pagenum == 0){
				left.setImageResource(R.drawable.left_unable);
				right.setImageResource(R.drawable.right_enable);
				HandleLoad(pagenum,main_url);	
			}
			else{
				left.setImageResource(R.drawable.left_enable);
				right.setImageResource(R.drawable.right_enable);
				HandleLoad(pagenum - 1,html_url[pagenum - 1]);

			}
			
			DrawBottom(true);
			mViewFlipper.showPrevious();
		}
		
	}
	
	public void HandleRight(){
		if(pagenum != sections_cnt -1){
			
			if(!has_set_right)
			{
				mViewFlipper.setOutAnimation(this, R.anim.tutorial_right_out);
				mViewFlipper.setInAnimation(this, R.anim.tutorial_right_in);
				has_set_right = true;
				has_set_left = false;
			}
			

			++pagenum;
			if(pagenum == sections_cnt-1){
				left.setImageResource(R.drawable.left_enable);
				right.setImageResource(R.drawable.right_unable);
				//GoKeyboardSetting.SetHasReadTutorialFin(this, true);
				//HandleLoad(pagenum,GetUrl(values[0],values[1],pagenum));
			}
			else{
				left.setImageResource(R.drawable.left_enable);
				right.setImageResource(R.drawable.right_enable);
				//HandleLoad(pagenum,GetUrl(values[0],values[1],pagenum));
				HandleLoad(pagenum+1,html_url[pagenum+1]);
			}
			
			DrawBottom(false);
			mViewFlipper.showNext();
		}
	}
	
	private ImageView left;
	private ImageView right;
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		for(int i=0;i<mViewFlipper.getChildCount();i++){
			WebView wv = (WebView)mViewFlipper.getChildAt(i);
			wv.clearCache(false);
			wv.destroy();
		}
		
		mViewFlipper.clearAnimation();
		
		mGestureDetector.setOnDoubleTapListener(null);
		
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.tutorial);
		
		Intent scrintent = getIntent();
		inType = scrintent.getIntExtra(Type, -1);
		
		
		
		mGestureDetector = new GestureDetector(this); 
		
		int totalwidth = this.getResources().getDisplayMetrics().widthPixels;
		int perwidth = totalwidth/(sections_cnt + 2);
		
		LinearLayout.LayoutParams lp;
		RelativeLayout.LayoutParams rp;
				
		mViewFlipper = (ViewFlipper) this.findViewById(R.id.webview_filpper);
				
		mbottom = (LinearLayout) this.findViewById(R.id.tutor_bottom);
		
		left = (ImageView) this.findViewById(R.id.LeftPage);
		rp = (RelativeLayout.LayoutParams) left.getLayoutParams();
		rp.width = perwidth;
		rp.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		left.setLayoutParams(rp);
		left.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HandleLeft();
			}
		});

		int i=0;
		for(i=0;i<sections_cnt;i++){
			
			//
			WebView wv = new WebView(this);
			mViewFlipper.addView(wv);
			wv.setVerticalScrollBarEnabled(false);
			wv.setOnTouchListener(this);
			
			//
			ImageView iv = new ImageView(this);
			mbottom.addView(iv);
			
			lp = (LayoutParams) iv.getLayoutParams();
			lp.width = perwidth;
			lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
			iv.setLayoutParams(lp);

			if(i == 0){
				iv.setImageResource(R.drawable.tutor_cur);
			}
			else{
				iv.setImageResource(R.drawable.tutor_next);
			}
			imglist.add(iv);
		}
		
		right = (ImageView) this.findViewById(R.id.RightPage);
		rp = (RelativeLayout.LayoutParams) right.getLayoutParams();
		rp.width = perwidth;
		rp.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		right.setLayoutParams(rp);
		right.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HandleRight();
			}
		});	
		
		HandleLoad(0,html_url[0]);
		HandleLoad(1,html_url[1]);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(mGestureDetector.onTouchEvent(event)){
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}
	

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// 
		//向左 下一页
		if( (e1.getX() - e2.getX()) > SliderPixD
				&& Math.abs(velocityX) > SliderPixV){
			
			HandleRight();	
			return true;
		}
		//向右 上一页
		else if( (e2.getX() - e1.getX())> SliderPixD
				&& Math.abs(velocityX) > SliderPixV){
			HandleLeft();
			
			return true;
			
		}
		
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub		
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
