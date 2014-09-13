package com.et.TestHandlerMessage;

import java.util.ArrayList;

import com.et.TestHandlerMessage.R.id;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
	
	public static final String TAG = "MainActivity";
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private Button button5;
	private ThreadHandler mThreadHandler;
	//注意：这个主线程的Handler是在其他线程创建的
	private MainHandler mMainHandler;
	private Handler handler;
	private TextView textView;
	
	ArrayList<Data> datas = new ArrayList<Data>();
	public static class Data{
		
		int i;
		String s;
		public Data(int i, String s) {
			// TODO Auto-generated constructor stub
			this.i = i;
			this.s = s;
		}
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        
        textView = (TextView) findViewById(R.id.TextView01);
        		
        new MyThread().start();
        
        Data item = new Data(0, "0");
        datas.add(item);
    }
    //记录延迟发送的消息数量
    int delayMsgCount = 0;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub		
		switch (v.getId()) {
			case R.id.button1:
				Message msg1 = Message.obtain();
				msg1.what = 1;
				mThreadHandler.sendMessage(msg1);
				break;
			case R.id.button2:
				if (mMainHandler != null) {
//					Message msg2 = Message.obtain();
//					msg2.what = 2;
//					mMainHandler.sendMessageDelayed(msg2, 2000);
					Data item = datas.get(0);
					Message msg3 = Message.obtain();
					msg3.what = 3;
					msg3.obj = item;
					mMainHandler.sendMessageDelayed(msg3, 1000);
					item.i = 2;
				}
				else{
					textView.setText("mMainHandler还没有创建");
				}
				break;
			case R.id.button3:
				/**结论：可以取消在延迟时间内要发送的message,如果有多个同样的消息，会取消所有**/
//				mMainHandler.removeMessages(2);
//				Data item = datas.get(0);
//				mMainHandler.removeMessages(3, item);
				mMainHandler.removeCallbacksAndMessages(null); // remove all messages
				break;
			case R.id.button4:
				mMainHandler.sendEmptyMessage(4);
				break;
			case R.id.button5:
				/**结论：当handler要延迟发送一个message的时候，在延迟时间内还没发送的时候判断是true;
				 * 		 当handler已经发送出去的message，判断就为false;
				 * **/
				
//				if (mMainHandler.hasMessages(2)) {
//					((Button)v).setText("有消息");
//				}else{
//					((Button)v).setText("无消息");
//				}
				Data item1 = datas.get(0);
				if (mMainHandler.hasMessages(3, item1)) {
					((Button)v).setText("有消息");
				}else{
					((Button)v).setText("无消息");
				}
				break;
		
		}
	}
	class MainHandler extends Handler{
		int count = 0;
		public MainHandler(Looper looper){
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
				case 1:
					textView.setText("mMainHandler在子线程创建，并接收到其他线程发来的消息");
					break;
				case 2:
					delayMsgCount ++ ;
					button4.setText("接收到延迟发送的消息:" + delayMsgCount);
					break;
				case 3:
					delayMsgCount ++ ;
					Data data = (Data) msg.obj;
					button4.setText(String.valueOf(delayMsgCount));
					break;
				case 4:
					count ++;
					int i = 0;
					while (i < 10000) {
						Log.i(TAG + ":" + count, "" + i);
						i ++;
					}
					break;
			}
		}
	}
	class ThreadHandler extends Handler{
		public ThreadHandler(Looper looper){
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			/**这个Handler是属于线程的，所以不能在这里更新UI**/
			//button1.setText("1111111");
			/**获取主线程的Looper对象**/
			Looper looper = Looper.getMainLooper();
			mMainHandler = new MainHandler(looper);
			mMainHandler.sendEmptyMessage(1);
			super.handleMessage(msg);
		}
		
	}
	class MyThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.i("MyThread", "MyThread");
			//创建该线程的Looper对象
			Looper.prepare();
			/**这个handler在线程里面创建是属于子线程的handler**/
			handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					//不在UI线程更新UI会出错
//					button3.setText("1111111111");
				}
			};
			mThreadHandler = new ThreadHandler(Looper.myLooper());		
			//循环查看消息队列是否有消息
			Looper.loop();
		}
	}
}