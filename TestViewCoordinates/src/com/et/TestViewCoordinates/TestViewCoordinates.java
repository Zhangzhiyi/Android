package com.et.TestViewCoordinates;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class TestViewCoordinates extends Activity implements OnClickListener{
	
	private Button mButton1;
	private Button mButton2;
	private Button mButton3;
	private TextView mTextView;
	
	private Rect rect = new Rect(); 
	private int[] loction = new int[2];
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);
        
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        
        mTextView = (TextView) findViewById(R.id.textView1); 
        
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.button1:
				/**decorView是window中的最顶层view，可以从window中获取到decorView
				获取到程序显示的区域，包括标题栏，但不包括状态栏**/
		        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		        Log.i("DisplayFrame", rect.toString());
		        //状态栏高度
		        int statusHeight = rect.top;//25
		        //通过Window.ID_ANDROID_CONTENT可以获取除去状态栏和标题栏的区域
		        View  contentView = getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
		        //状态栏 + 标题栏 高度
		        int contentViewTop = contentView.getTop();
		        //标题栏 高度
		        int titleHeight = contentViewTop - statusHeight;
		        //View的getLeft , getTop, getBottom, getRight,  这一组是获取相对在它父亲里的坐标
		        Log.i("top", "" + v.getTop());//0   是相对于contentView来计算的
				break;
			case R.id.button2:
				Log.i((String) mButton2.getText(), "left:" + mButton2.getLeft() + "," + "top:" + mButton2.getTop());
				Log.i((String) mButton2.getText(), "" + mButton2.getWidth() + "," + mButton2.getHeight());
				//获取全局坐标系的一个视图区域，返回一个填充的Rect对象；该Rect是基于总整个屏幕的
				mButton2.getGlobalVisibleRect(rect);
				Log.i("getGlobalVisibleRect", rect.toString());
				
				/**这个方法返回的rect有点奇怪，rect只是记录这个view的大小，与坐标无关，
				 * left和top都是0，right=width，bottom=height；
				 *   当View发生滚动top和left就不会是0了，可以跳入源码研究**/
				mButton2.getLocalVisibleRect(rect);
				Log.i("getLocalVisibleRect", rect.toString());
				
				/**以下两个方法是计算该视图在全局坐标系中的x，y值，
				 * （注意这个值是要从屏幕顶端算起，也就是索包括了通知栏的高度）获取在当前屏幕内的绝对坐标*/				
				mButton2.getLocationInWindow(loction);
				Log.i("getLocationInWindow", "left:" + loction[0] + "," + "top:" + loction[1]);
				mButton2.getLocationOnScreen(loction);
				Log.i("getLocationOnScreen", "left:" + loction[0] + "," + "top:" + loction[1]);
				
				break;
			case R.id.button3:
				mButton2.scrollTo(-100, 0);
				mTextView.scrollTo(-100, 0);
				break;
		}
	}
	/**08-08 01:45:54.762: INFO/Button2(296): left:0,top:48
	08-08 01:45:54.762: INFO/Button2(296): 73,48
	08-08 01:45:54.772: INFO/getGlobalVisibleRect(296): Rect(0, 98 - 73, 146)
	08-08 01:45:54.772: INFO/getLocalVisibleRect(296): Rect(0, 0 - 73, 48)
	08-08 01:45:54.799: INFO/getLocationInWindow(296): left:0,top:98
	08-08 01:45:54.799: INFO/getLocationOnScreen(296): left:0,top:98
	**/
}