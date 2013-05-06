package com.mcs.client.android.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.R;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.mcs.client.android.Env;
import com.mcs.client.mina.MinaClient;
import com.mcs.client.sync.ItemState;
import com.mcs.client.sync.SyncItem;
import com.mcs.client.sync.SyncTer;
import com.mcs.framework.message.mina.Auth;
import com.mcs.framework.message.mina.Data;
import com.mcs.framework.terminal.Terminal;
import com.mcs.todo.Main;
import com.mcs.todo.db.DBAdapter;
import com.mcs.todo.db.DBConstant;
import com.mcs.todo.db.Todolist;
import com.mcs.todo.res.Strings;
import com.mcs.todo.utils.Time;

public class MCSService extends Service implements Terminal{
	private static final Logger logger = LoggerFactory.getLogger("MCSService");
	private final Env env = Env.getInstance();
	private SyncTer syncTer;
	private MinaClient client;
	
	private static final String TAG = "MCSService";
	
	@Override
	public void onCreate() {
		Log.i(MCSService.class.getName(), "Create service.");
				  
		
		client = env.getMinaClient();
		syncTer = env.getSyncTer();
		
		instance = this; 
		
		HandlerThread thread = new HandlerThread("MCSService", Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		// 以下用来控制thread
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		
		mNotificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	am = (AlarmManager) getSystemService(ALARM_SERVICE);
	}
	
	@Override
	public void onDestroy() {
		mServiceLooper.quit();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "Start MCS service.");
		if(intent!=null){
			String action = intent.getAction();
			if (action.equals(Strings.ALERT_SERVICE_ACTION))
			{
				alarmId = intent.getIntExtra("alarmId", 0);
				String title = intent.getStringExtra("alarmTitle");
				String content = intent.getStringExtra("alarmContent");
				String warnFreq = intent.getStringExtra("alarmWarnFreq");
				
				int index = 0;
				int count = Strings.todoNew_spinner_warnFreq.length;
				for(int i=0;i<count;i++)
				{
					if(warnFreq.equals(Strings.todoNew_spinner_warnFreq[i]))
					{
						index = i;	
						break;						
					}
				}
				/*重新封装Intent*/
				PendingIntent sender = PendingIntent.getService(this, 1, intent, alarmId);
				
				switch (index){
					case 0:
						Notification(title,title,content);
						break;
					case 1:
						Notification(title,title,content);
						am.set(AlarmManager.RTC_WAKEUP, Time.getNextDayMillis(), sender);
						break;
					case 2:
						am.set(AlarmManager.RTC_WAKEUP, Time.getNextDayMillis(), sender);
						if(Time.isWorkDay())
							Notification(title,title,content);					
						break;
					case 3:
						Notification(title,title,content);
						am.set(AlarmManager.RTC_WAKEUP, Time.getNextWeekMillis(), sender);
						break;
					case 4:
						Notification(title,title,content);
						am.set(AlarmManager.RTC_WAKEUP, Time.getNextMonthMillis(), sender);
						break;
					case 5:
						Notification(title,title,content);
						am.set(AlarmManager.RTC_WAKEUP, Time.getNextYearMillis(), sender);
						break;
				}
				
			}
		}
		
		
		// Service持续运行，直到我们显式地stop		
		return START_STICKY;
	}
	
	private final IMCSConnect.Stub mConnectBinder = new IMCSConnect.Stub () {
		@Override
		public void connectToServier(String id, byte[] cred) throws RemoteException {
			logger.info("Service connect to server.");
			client.connect();
			Auth auth = new Auth(id, cred);
			logger.info("Auth.");
			client.send(auth);
		}

		@Override
		public void disconnect() throws RemoteException {
			logger.info("Service disconnect from server.");
			client.quit();
		}
	};
	
	private final IMCSSync.Stub mSyncBinder = new IMCSSync.Stub() {
		@Override
		public void sync(String dataType) throws RemoteException {
			String clientId = env.getClientId();
			syncTer.fireSync(clientId, dataType);
		}
	};
	private final IMCSTranValue.Stub mTranValue = new IMCSTranValue.Stub() {
		
		@Override
		public void transValue(List<SyncItem> items, int operation)
				throws RemoteException {
			// TODO Auto-generated method stub
			switch(operation){
				case ItemState.ALL_ITEM_MSG:
					ItemState.allItems = (ArrayList<SyncItem>) items;
					ItemState.flag = true;
					break;
				case ItemState.OPTIMIZED_ITEM_MSG:
					ItemState.optimizedItems = (ArrayList<SyncItem>) items;
					ItemState.flag = true;
					break;
				case ItemState.FILL_ITEM_MSG:
					ItemState.fillItems = (ArrayList<SyncItem>) items;
					ItemState.flag = true;
					break;
			}
			
			
		}
	};
	final RemoteCallbackList<IRemoteServiceCallback> mCallbacks  
							= new RemoteCallbackList<IRemoteServiceCallback>();

	private final IRemoteService.Stub mRemoteService = new IRemoteService.Stub.Stub() {
		@Override
		public void registerCallback(IRemoteServiceCallback cb)
				throws RemoteException {
			if(cb!=null)
				mCallbacks.register(cb);
		}
		@Override
		public void unregisterCallback(IRemoteServiceCallback cb)
				throws RemoteException {
			if(cb!=null)
				mCallbacks.unregister(cb);				
		}		
	};
	@Override
	public IBinder onBind(Intent intent) {
		if(intent.getAction().equals(IMCSConnect.class.getName())) {
			logger.info("Bind to connect service.");
			return mConnectBinder;
		}else if(intent.getAction().equals(IMCSSync.class.getName())) {
			logger.info("Bind to sync service.");
			return mSyncBinder;
		}else if(intent.getAction().equals(IRemoteService.class.getName())){
			logger.info("Bind to IRemote service.");
			return mRemoteService;
		}else if(intent.getAction().equals(IMCSTranValue.class.getName())){
			logger.info("Bind to IRemote service.");
			return mTranValue;
		}
		return null;
	}
	
	private void loadPreference() {
		// TODO 载入地址和端口
	}

	@Override
	public void receiveMessage(Data msg){
		
	}
	
	private static Service instance = null;
	/** 服务的消息队列 */
	private Looper mServiceLooper = null;
	public static Handler mServiceHandler = null;
	
	private AlarmManager am;
	
	private NotificationManager mNotificationManager;
	private int alarmId =0;
	
	
	/** 返回Service对象 */
	public static Service getInstance() {
		return instance;
	}
	
	private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        /** 接收推送信息的处理  **/
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
            	case ItemState.ADD_ITEM_MSG:          	
            		callBackSyncItem(msg, ItemState.ADD_ITEM_MSG, ItemState.ADD_ITEM_KEY);
            		break;
            	case ItemState.DELETE_ITEM_MSG:
            		callBackSyncItem(msg, ItemState.DELETE_ITEM_MSG, ItemState.DELETE_ITEM_KEY);           	
            		break;
            	case ItemState.UPDATE_ITEM_MSG:
            		callBackSyncItem(msg, ItemState.UPDATE_ITEM_MSG, ItemState.UPDATE_ITEM_KEY);
            		break;
            	case ItemState.FILL_ITEM_MSG:
            		callBackSyncItem(msg, ItemState.FILL_ITEM_MSG, ItemState.FILL_ITEM_KEY);
            		break;
            	case ItemState.ALL_ITEM_MSG:
            		callBackValue(ItemState.ALL_ITEM_MSG);
            		break;
            	case ItemState.OPTIMIZED_ITEM_MSG:
            		callBackValue(ItemState.OPTIMIZED_ITEM_MSG);
            		break;          	
            	case ItemState.CLEAR_CHANGElIST:
            		callBackValue(ItemState.CLEAR_CHANGElIST);
            		break;
            }
            super.handleMessage(msg);
        }
    }
	private void callBackValue(int value){
		final int N = mCallbacks.beginBroadcast();
        for (int i=0; i<N; i++) {
            try {
                mCallbacks.getBroadcastItem(i).callBackValue(value);
            } catch (RemoteException e) {
                // The RemoteCallbackList will take care of removing
                // the dead object for us.
            }
        }
        mCallbacks.finishBroadcast();
	}
	private void callBackSyncItem(Message msg,int operation,String key){
		Bundle bundle = msg.getData();
		ArrayList<SyncItem> items = bundle.getParcelableArrayList(key);
		final int N = mCallbacks.beginBroadcast();
        for (int i=0; i<N; i++) {
            try {
                mCallbacks.getBroadcastItem(i).callBackSyncItem(operation, items);
            } catch (RemoteException e) {
                // The RemoteCallbackList will take care of removing
                // the dead object for us.
            }
        }
        mCallbacks.finishBroadcast();
	}
	private SharedPreferences mSharedPreferences;
	/** 消息提醒  **/	 
	public void Notification(String tickrText,String Notititle,String Noticontent){
		Notification notification = new Notification(R.drawable.stat_notify_more,tickrText,
				System.currentTimeMillis());
		Intent mIntent = new Intent(this,Main.class);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mIntent.setAction(Strings.NOTIFIACATION_TODO_ACTION);
		// alarmId一定要转long型，否则点击通知不会显示详细内容
		mIntent.putExtra(Todolist._ID, (long)alarmId);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,mIntent, 
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(this, Notititle, Noticontent, contentIntent);
		mSharedPreferences = getSharedPreferences("com.mcs.todo_preferences", MODE_PRIVATE);
		boolean ringtone = mSharedPreferences.getBoolean("defalut_playringtone", false);
		if(ringtone){
			Uri uri = Uri.parse(mSharedPreferences.getString("default_ringtone", null));
			notification.sound = uri;
		}
		boolean vibrate = mSharedPreferences.getBoolean("defalut_vibrate", false);
		if(vibrate){
			notification.vibrate = new long[]{100,250,100,500};
		}
		else{
			notification.vibrate = new long[]{0,0,0,0};
		}
		//notification.defaults = Notification.DEFAULT_ALL;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		mNotificationManager.notify(alarmId, notification);
	}	
	
}
  