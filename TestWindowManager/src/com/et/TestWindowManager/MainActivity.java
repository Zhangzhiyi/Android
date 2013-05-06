package com.et.TestWindowManager;

import com.et.service.IRemoteService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.AbsListView.LayoutParams;

public class MainActivity extends Activity {
	
	private Button mCallLocal;
	private Button mBindService;
	private Button mCallRemote;
	private IRemoteService iRemoteService;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mCallLocal = (Button) findViewById(R.id.calllocal);
        mBindService = (Button) findViewById(R.id.bindservice);
        mCallRemote = (Button) findViewById(R.id.callremote);
        mCallRemote.setEnabled(false);
         
        
        
        mCallLocal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE); 
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View windowView = inflater.inflate(R.layout.windows_view, null);
				
				WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
				 layoutParams.x = 0;
				 layoutParams.y = 120;
				 layoutParams.width = 200;
				 layoutParams.height = 100;
				 layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				 					  |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
				 					  |WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
				 layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
				 mWindowManager.addView(windowView, layoutParams);
				 
			}
		});
		mBindService.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bindService(new Intent("com.et.service.addwindows"), iRemoteServiceConnection, BIND_AUTO_CREATE);
				mCallRemote.setEnabled(true);
			}
		});
        mCallRemote.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 //windowManager.addView(windowsView,layoutParams);
				try {
					iRemoteService.sendMessage();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
        
    }
    
    private ServiceConnection iRemoteServiceConnection = new ServiceConnection() {
    	
    	@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
    		iRemoteService = IRemoteService.Stub.asInterface(service);
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			iRemoteService = null;
		}
	};
}