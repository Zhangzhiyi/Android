package com.et.service;

import java.util.ArrayList;
import java.util.List;

import com.et.Activity.Book;
import com.et.Activity.TestActivity;
import com.et.Activity.TestCallbackThread;
import com.et.Activity.TestThread;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class RemoteCallbackService extends Service {

	 public static final int REPORT_MSG = 1;
	 public static final int THREAD_MSG = 10;
	 public static final int CALLBACKPRODUCK_MSG = 20;
	 public static final int LISTCALLBACKPRODUCK_MSG = 30;
	 public static final int CALLBACKBOOK_MSG = 40;
	 private int mValue = 0;
	
	@Override
	public void onCreate()
	{
		Log.d("RemoteCallbackService", "onCreate");
		/**打印进程号,判断与服务是否在同一进程**/
		int pid = Process.myPid();
		Log.i("RemoteCallbackService Process ID", String.valueOf(pid));
		/**在这里启动线程,线程与这个远程service在同一进程**/
		new Thread(new TestCallbackThread(mHandler)).start();
		
		/**发送消息**/
		mHandler.sendEmptyMessage(REPORT_MSG);
		/**发送回调对象消息**/
		mHandler.sendEmptyMessage(CALLBACKPRODUCK_MSG);
		/**发送回调对象链表消息**/
		mHandler.sendEmptyMessage(LISTCALLBACKPRODUCK_MSG);
		/**发送回调对象消息(测试aidl文件不在同一个包)**/
		mHandler.sendEmptyMessage(CALLBACKBOOK_MSG);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("RemoteCallbackService", "onStart");
		
		
		return START_STICKY;
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("RemoteCallbackService", "onBind");
		if(intent.getAction().equals("com.et.service.IRemoteService"))
			return IRemoteService;
		else
			return null;
	}
	@Override
	public boolean onUnbind(Intent intent)
	{
		Log.d("RemoteCallbackService", "onUnbind");
		
		return super.onUnbind(intent);
	}
	@Override
	public void onDestroy()
	{
		Log.d("RemoteCallbackService", "onDestroy");
		mHandler.removeMessages(REPORT_MSG);
	}

	@Override
	public void onRebind(Intent intent)
	{
		Log.d("RemoteCallbackService", "onRebind");
		super.onRebind(intent);
	}		
	
	final RemoteCallbackList<IRemoteServiceCallback> mCallbacks  
						= new RemoteCallbackList<IRemoteServiceCallback>();
	
	private final IRemoteService.Stub IRemoteService = new IRemoteService.Stub.Stub() {
		@Override
		public void registerCallback(IRemoteServiceCallback cb)
				throws RemoteException {
			// TODO Auto-generated method stub
			if(cb!=null)
				mCallbacks.register(cb);
		}
		@Override
		public void unregisterCallback(IRemoteServiceCallback cb)
				throws RemoteException {
			// TODO Auto-generated method stub
			if(cb!=null)
				mCallbacks.unregister(cb);				
		}		
	};
	
	 
	private final Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {               
                // It is time to bump the value!
                case REPORT_MSG: {
                    // Up it goes.
                    int value = ++mValue;
                    
                    // Broadcast to all clients the new value.
                    final int N = mCallbacks.beginBroadcast();
                    for (int i=0; i<N; i++) {
                        try {
                            mCallbacks.getBroadcastItem(i).valueChanged(value);
                        } catch (RemoteException e) {
                            // The RemoteCallbackList will take care of removing
                            // the dead object for us.
                        }
                    }
                    mCallbacks.finishBroadcast();
                    sendMessageDelayed(obtainMessage(REPORT_MSG), 1*1000);
                }
                break;
                /**处理从线程传回来的消息**/
                case THREAD_MSG:{
                	final int N = mCallbacks.beginBroadcast();
                    for (int i=0; i<N; i++) {
                        try {
                            mCallbacks.getBroadcastItem(i).valueChanged(THREAD_MSG);
                        } catch (RemoteException e) {
                            // The RemoteCallbackList will take care of removing
                            // the dead object for us.
                        }
                    }
                    mCallbacks.finishBroadcast();
                }                	
                break;
                	/**处理对象回调的消息**/
                case CALLBACKPRODUCK_MSG:{
                	Product product = new Product();
                	product.setId(1);
                	product.setName("ET");
                	product.setPrice(0);
                	final int N = mCallbacks.beginBroadcast();
                    for (int i=0; i<N; i++) {
                        try {
                            mCallbacks.getBroadcastItem(i).productChanged(product);
                        } catch (RemoteException e) {
                            // The RemoteCallbackList will take care of removing
                            // the dead object for us.
                        }
                    }
                    mCallbacks.finishBroadcast();
                }
                break;
                /**处理对象链表回调的消息**/
                case LISTCALLBACKPRODUCK_MSG:{
                	List<Product> list = new ArrayList<Product>();
                	Product product1 = new Product();
                	product1.setId(1);
                	product1.setName("ET");
                	product1.setPrice(0);
                	list.add(product1);
                	Product product2 = new Product();
                	product2.setId(2);
                	product2.setName("AB");
                	product2.setPrice(100);
                	list.add(product2);
                	final int N = mCallbacks.beginBroadcast();
                    for (int i=0; i<N; i++) {
                        try {
                            mCallbacks.getBroadcastItem(i).productListChanged(list);
                        } catch (RemoteException e) {
                            // The RemoteCallbackList will take care of removing
                            // the dead object for us.
                        }
                    }
                    mCallbacks.finishBroadcast();
                }
                break;
                case CALLBACKBOOK_MSG:{
                	Book mBook = new Book();
                	mBook.setBookName("Android Tutor");
                	mBook.setAuthor("Frankie");
                	mBook.setPublishTime(2010);
                	String s = "test";
                	mBook.setContent(s.getBytes());
                	final int N = mCallbacks.beginBroadcast();
                    for (int i=0; i<N; i++) {
                        try {
                            mCallbacks.getBroadcastItem(i).bookChanged(mBook);
                        } catch (RemoteException e) {
                            // The RemoteCallbackList will take care of removing
                            // the dead object for us.
                        }
                    }
                    mCallbacks.finishBroadcast();
                }
                	break;
                
            }
            super.handleMessage(msg);
        }
    };
}
