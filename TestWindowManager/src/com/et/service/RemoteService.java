package com.et.service;

import com.et.TestWindowManager.R;
import com.et.service.IRemoteService.Stub;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.AbsListView.LayoutParams;

public class RemoteService extends Service {
	public final static String TAG = "RemoteService";
	
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams layoutParams;
	private LayoutInflater inflater;
	private View windowView;
	private Button button;
	private Handler handler;
	private int startX = 0;
	private int startY = 0;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onCreat");
		 mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE); 
		 inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		 windowView = inflater.inflate(R.layout.windows_view, null);
		
		 layoutParams = new WindowManager.LayoutParams();
		 layoutParams.x = 0;
		 layoutParams.y = 0;
		 layoutParams.gravity = Gravity.LEFT|Gravity.TOP;
		 layoutParams.width = 200;
		 layoutParams.height = 100;
		 layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
		 					  |WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
		 layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		 button = (Button) windowView.findViewById(R.id.Button01);
		 button.setVisibility(View.GONE);
		 button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/**发现是响应了button的点击,只是没有改变button焦点颜色**/
				Log.i(TAG, "button");
			}
		});
		 windowView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// 获取相对屏幕的坐标，即以屏幕左上角为原点
				int x = (int) event.getRawX();
				int y = (int) event.getRawY() - 25; // 25是系统状态栏的高度
				Log.i(TAG+" x:", String.valueOf(x));
				Log.i(TAG+" y:", String.valueOf(y));
				
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					startX = (int) event.getX();
					startY = y - layoutParams.y;
					/**好奇怪,v.getTop()的值一直是0~  可能顶级窗口系统无办法测出大小**/
					//startY = y - v.getTop();
					Log.i("getTop()", String.valueOf(v.getTop()));
					return true;
				}
				if(event.getAction() == MotionEvent.ACTION_MOVE){
					layoutParams.x = x - startX;
					layoutParams.y = y - startY;
					Log.i("layoutParams.x", String.valueOf(layoutParams.x));
					Log.i("layoutParams.y", String.valueOf(layoutParams.y));
					mWindowManager.updateViewLayout(v, layoutParams);
					return true;
				}
				
				
				return false;
			}
		});
		 windowView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "onClick");
			}
		});
		 handler = new Handler(){
			 @Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				 if(msg.what == 1){
					 mWindowManager.addView(windowView, layoutParams);
				 }
				super.handleMessage(msg);
			}
		 };
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onBind");
		if(intent.getAction().equals("com.et.service.addwindows"))
			return IRemoteService;
		else
			return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_STICKY;
	}
	
	private final IRemoteService.Stub IRemoteService = new Stub() {
		
		@Override
		public void sendMessage() throws RemoteException {
			// TODO Auto-generated method stub
			Message msg = Message.obtain();
			msg.what = 1;
			Log.i(TAG, "sendMessage");
			handler.sendMessage(msg);
			
		}
	};
}
