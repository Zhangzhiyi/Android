package net.blogjava.mobile.aidl;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends Service
{ 
	
	public class MyServiceImpl extends IMyService.Stub
	{

		@Override
		public String getValue() throws RemoteException
		{
			// TODO Auto-generated method stub
			return "Android/OPhone开发讲义";
		}

		@Override
		public void changeValue(int value) throws RemoteException {
			// TODO Auto-generated method stub
			Log.i("value", String.valueOf(value));
		}
		
	}

	@Override
	public IBinder onBind(Intent intent)
	{		
		return new MyServiceImpl();
	}

	
}
