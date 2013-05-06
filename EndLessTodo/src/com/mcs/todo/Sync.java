package com.mcs.todo;

import java.net.InetAddress;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.mcs.client.android.service.IMCSConnect;

public class Sync {
	 // 与服务绑定的变量
	InetAddress host = null;
    private IMCSConnect serviceConnect = null;
    private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			serviceConnect = IMCSConnect.Stub.asInterface(service);
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			serviceConnect = null;
		}
    };

}
