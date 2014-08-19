package com.et.TestRemoteService;

import java.util.List;

import com.et.TestRemoteService.R;
import com.et.service.Constant;
import com.et.service.IMyService;
import com.et.service.IRemoteService;
import com.et.service.IRemoteServiceCallback;
import com.et.service.Product;
import com.et.service.RemoteService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    
	private IMyService iMyService ;
	private IRemoteService iRemoteService;
	
	private Button start_remote_service;
	private Button stop_remote_service;
	private Button bind_aidl;
	private Button unbind_aidl;
	private Button start_aidl;
	private TextView processText;
	private TextView aidlText;
	
	private Button bind_remotecallback_service;
	private Button unbind_remotecallback_service;
	private TextView callbackText;
	private TextView threadbackText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle("MainActivity");
        
       
        /**打印进程号,判断与服务是否在同一进程**/
        int pid = Process.myPid();
    	Log.i("MainActivity Process ID", String.valueOf(pid));
    	
        start_remote_service = (Button) findViewById(R.id.start_service);
        stop_remote_service = (Button) findViewById(R.id.stop_service);
        bind_aidl = (Button) findViewById(R.id.bind_aidl);
        unbind_aidl = (Button) findViewById(R.id.unbind_aidl);
        unbind_aidl.setEnabled(false);
        start_aidl = (Button) findViewById(R.id.start_aidl);
        start_aidl.setEnabled(false);
        processText = (TextView) findViewById(R.id.process);
        aidlText = (TextView) findViewById(R.id.aidl_text);
        
        processText.setText("进程号："+String.valueOf(pid));
      
        
        start_remote_service.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/**直接调用远程服务**/
				 /**如果在mainfest.xml文件的service设置了android：process属性,service则另起一个进程运行**/
				startService(new Intent("com.et.service.REMOTE_SERVICE"));
				 
				/**在这里启动线程,线程与这个activity在同一进程**/
				//new Thread(new TestThread()).start();
				/**在这里启动activity,启动的activity与这个activity在同一进程**/
				//startActivity(new Intent(MainActivity.this,TestActivity.class));
			}
		});
        stop_remote_service.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopService(new Intent("com.et.service.REMOTE_SERVICE"));
			}
		});
        bind_aidl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/**activity绑定远程的AIDL服务也是共存亡的,activity销毁(暂时跳转到其他activity是还没有销毁的),
				 * 绑定的服务也会销毁**/
				 bindService(new Intent("com.et.service.IMyService"), iMyServiceConnection, BIND_AUTO_CREATE);
				 //unbind_aidl.setEnabled(true);
				// start_aidl.setEnabled(true);
				 
			}
		});
        unbind_aidl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				unbindService(iMyServiceConnection);
				unbind_aidl.setEnabled(false);
				start_aidl.setEnabled(false);
				
			}
		});
        start_aidl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 try {
					 /**在此证明不同进程访问同一个类的同一静态变量也是有各自不同的值的**/
					 Constant.var = 555;
					 RemoteService.value = 666;
					aidlText.setText(iMyService.test());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Product product = new Product();
            	product.setId(1);
            	product.setName("ET");
            	product.setPrice(0);
				try {
					iMyService.tranObject(product);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/**好奇怪，在这里打印类的静态变量value的值不是在aidl接口方法里重新赋值的值，而是上面的555与666**/
				/** 因为进程不同，就算是静态变量也是不同的**/
				Log.i("value", String.valueOf(RemoteService.value));
				Log.i("flag", String.valueOf(Constant.flag));
			}
		});
        /**Handler只能在同一进程内传递消息**/
        /**如果在同一进程上,在service的handler发送消息才可以接受,否则会出错**/
        /*handler = new Handler(){
        	public void handleMessage(Message msg) { 
        		switch(msg.what){
       		 		case 1:
       		 			Toast.makeText(MainActivity.this, "get message from service", Toast.LENGTH_SHORT).show();
       		 			break;
       		 	}     		 
                super.handleMessage(msg); 
           } 
       };*/
        
        bind_remotecallback_service = (Button) findViewById(R.id.bind_remotecallback);
        unbind_remotecallback_service = (Button) findViewById(R.id.unbind_remotecallback);
        unbind_remotecallback_service.setEnabled(false);
        callbackText = (TextView) findViewById(R.id.text_remotecallback);
        threadbackText = (TextView) findViewById(R.id.text_threadback);
        bind_remotecallback_service.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bindService(new Intent("com.et.service.IRemoteService"), iRemoteServiceConnection, BIND_AUTO_CREATE);
				callbackText.setText("Binding.");
				unbind_remotecallback_service.setEnabled(true);
			}
		});
        unbind_remotecallback_service.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(iRemoteService!=null){
					try {
						iRemoteService.unregisterCallback(mCallback);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				unbindService(iRemoteServiceConnection);
				callbackText.setText("Unbinding.");
				threadbackText.setText("unfinish");
				unbind_remotecallback_service.setEnabled(false);
			}
		});
    }
    
    private ServiceConnection iMyServiceConnection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			iMyService = IMyService.Stub.asInterface(service);
			try {
					/**在这里也可以立马调用AIDL声明的方法 **/
					aidlText.setText(iMyService.test());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			unbind_aidl.setEnabled(true);
			start_aidl.setEnabled(true);
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			iMyService = null;
			unbind_aidl.setEnabled(false);
			start_aidl.setEnabled(false);
		}   	
    };
    
    
    private ServiceConnection iRemoteServiceConnection = new ServiceConnection() {
    	@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			iRemoteService = IRemoteService.Stub.asInterface(service);			
			try {
				iRemoteService.registerCallback(mCallback);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			iRemoteService = null;
			callbackText.setText("Disconnected.");
		}		
	};
	
	private IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub() {
		
		@Override
		public void valueChanged(int value) throws RemoteException {
			// TODO Auto-generated method stub
			mHandler.sendMessage(mHandler.obtainMessage(BUMP_MSG, value, 0));
			mHandler.sendMessage(mHandler.obtainMessage(THREAD_MSG, value, 0));
		}

		@Override
		public void productChanged(Product product) throws RemoteException {
			// TODO Auto-generated method stub
			String s = "";
			s = "Product.id = " + product.getId() + "\n";
			s += "Product.name = " + product.getName()
					+ "\n";
			s += "Product.price = " + product.getPrice()
					+ "\n";
			/**打印从远程service传过来的对象product信息**/
			Log.i("Product",s);
			
		}

		@Override
		public void productListChanged(List<Product> list)
				throws RemoteException {
			// TODO Auto-generated method stub
			for(int i=0;i<list.size();i++){
				Product product = list.get(i);
				String s = "";
				s = "Product.id = " + product.getId() + "\n";
				s += "Product.name = " + product.getName()
						+ "\n";
				s += "Product.price = " + product.getPrice()
						+ "\n";
				/**打印从远程service传过来的对象product链表信息**/
				Log.i("Product",s);
			}
			
		}

		@Override
		public void bookChanged(Book book) throws RemoteException {
			// TODO Auto-generated method stub
			String s = "";
			s = "Book name is: " + book.getBookName()+"\n"+
			  "Author is: " + book.getAuthor() + "\n" +
			  "PublishTime is: " + book.getPublishTime() +"\n"+
			  "content is:" + new String(book.getContent());
			Log.i("Book",s);
		}
	};
	 public static final int BUMP_MSG = 1;
	 public static final int THREAD_MSG = 10;
    private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		 switch (msg.what) {
             case BUMP_MSG:
                 callbackText.setText("Received from service: " + msg.arg1);
                 break;
             case THREAD_MSG:
            	 threadbackText.setText("threadback:complete");
            	 break;
             default:
                 super.handleMessage(msg);
    		 }
    	}
    };
}