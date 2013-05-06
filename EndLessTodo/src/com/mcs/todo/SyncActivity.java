package com.mcs.todo;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import com.mcs.client.android.Env;
import com.mcs.client.android.service.IMCSConnect;
import com.mcs.client.android.service.IMCSSync;
import com.mcs.client.android.service.IMCSTranValue;
import com.mcs.client.android.service.IRemoteService;
import com.mcs.client.android.service.IRemoteServiceCallback;
import com.mcs.client.android.service.MCSService;
import com.mcs.client.mina.MinaClient;
import com.mcs.client.sync.DataType;
import com.mcs.client.sync.ItemState;
import com.mcs.client.sync.SyncItem;
import com.mcs.todo.db.DBAdapter;
import com.mcs.todo.db.DBConstant;
import com.mcs.todo.db.LocalTodoSource;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class SyncActivity extends Activity{
		
	private int flag;
	
	private Env env = Env.getInstance();
	private LocalTodoSource mLocalTodoSource;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.sync);
		
		bindService(new Intent(IMCSConnect.class.getName()), 
				mConnectCnt, Context.BIND_AUTO_CREATE);
        bindService(new Intent(IMCSSync.class.getName()), 
        		mSyncCnt, Context.BIND_AUTO_CREATE);
        bindService(new Intent(IRemoteService.class.getName()), 
        		iRemoteServiceConnection, BIND_AUTO_CREATE);
        bindService(new Intent(IMCSTranValue.class.getName()), 
        		mTranConnection, BIND_AUTO_CREATE);
        				
        findViews();
        setListener();	
        
        mLocalTodoSource = new LocalTodoSource(this);
        
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(iRemoteServiceConnection);
		unbindService(mConnectCnt);
		unbindService(mTranConnection);
		unbindService(mSyncCnt);
	}
	
	private Button syncAll;
	private ImageView syncContact;
	private ImageView syncTodo;
	private ImageView syncNote;
	private ImageView contactProgress;
	private ImageView todoProgress;
	private ImageView noteProgress;
	private Button connect;
	private Button sync;
	private Button disconnect;
	
	private void findViews(){
		syncAll = (Button) findViewById(R.id.sync_all);
		
		syncContact = (ImageView) findViewById(R.id.contact_bg);
		contactProgress = (ImageView) findViewById(R.id.sync_contact_progress);
		
		syncTodo = (ImageView) findViewById(R.id.todo_bg);
		todoProgress = (ImageView) findViewById(R.id.sync_todo_progress);
		
		syncNote = (ImageView) findViewById(R.id.note_bg);
		noteProgress = (ImageView) findViewById(R.id.sync_todo_progress);
		
		connect = (Button) findViewById(R.id.connect);
		sync = (Button) findViewById(R.id.sync);
		disconnect = (Button) findViewById(R.id.disconnect);
	}
	private void setListener() {
		// TODO Auto-generated method stub
		syncAll.setOnClickListener(onClick);
		syncContact.setOnClickListener(onClick);
		syncTodo.setOnClickListener(onClick);
		
		connect.setOnClickListener(onClick);
		sync.setOnClickListener(onClick);
		disconnect.setOnClickListener(onClick);
		
	}  
	private OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
				case R.id.sync_all:
					break;
				case R.id.contact_bg:
					syncContact.setEnabled(false);
					flag = 1;
					handler.post(new Runnable() {					
						@Override
						public void run() {
							// TODO Auto-generated method stub
							flag++;
							Log.i("tag", String.valueOf(flag));
							switch(flag){
								case 1:
									contactProgress.setImageResource(R.drawable.icon_progress_sync_1);
									break;
								case 2:	
									contactProgress.setImageResource(R.drawable.icon_progress_sync_2);
									break;
								case 3:	
									contactProgress.setImageResource(R.drawable.icon_progress_sync_3);
									break;
								case 4:		
									contactProgress.setImageResource(R.drawable.icon_progress_sync_4);
									break;
								case 5:	
									contactProgress.setImageResource(R.drawable.icon_progress_sync_5);
									break;
								case 6:	
									contactProgress.setImageResource(R.drawable.icon_progress_sync_6);
									break;
								case 7:
									contactProgress.setImageResource(R.drawable.icon_progress_sync_1);
									contactProgress.setImageResource(R.drawable.icon_complete);
									
									flag = 0;
									break;
							}
							if(flag==0){
								handler.removeCallbacks(this);
								syncContact.setEnabled(true);
							}
							else
								handler.postDelayed(this, 150);
						}
					});
					break;
				case R.id.todo_bg:
					break;
				case R.id.note_bg:
					break;
				case R.id.connect:
					try {
						mConnect.connectToServier(env.getClientId(), null);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case R.id.sync:
					try {
						mSync.sync(DataType.SYNC_TODO);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case R.id.disconnect:
					try {
						mConnect.disconnect();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
			}
		}
	};
	
	private IMCSConnect mConnect;
	
	private ServiceConnection mConnectCnt = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mConnect = IMCSConnect.Stub.asInterface(service);
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mConnect = null;
		}
    	
    };
    
    private IMCSSync mSync;
    
    private ServiceConnection mSyncCnt = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mSync = IMCSSync.Stub.asInterface(service);
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mSync = null;
		}
    };
    
    private IRemoteService iRemoteService;
    
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
			
		}		
	};
	private IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub() {
		
		@Override
		public void callBackValue(int value) throws RemoteException {
			// TODO Auto-generated method stub
			
			switch(value){
				case ItemState.ALL_ITEM_MSG:
					ArrayList<SyncItem> all = mLocalTodoSource.getAllItems();
					mTran.transValue(all, ItemState.ALL_ITEM_MSG);
					break;
				case ItemState.OPTIMIZED_ITEM_MSG:
					ArrayList<SyncItem> change = mLocalTodoSource.getOptimizedChangedItemState();
					mTran.transValue(change, ItemState.OPTIMIZED_ITEM_MSG);
					break;
				case ItemState.CLEAR_CHANGElIST:
					Log.i("clear changeList", "clear changeList");
					mLocalTodoSource.clearChangeList();			
					break;
			}
		}

		@Override
		public void callBackSyncItem(int operation, List<SyncItem> items)
				throws RemoteException {
			// TODO Auto-generated method stub
			switch(operation){
				case ItemState.ADD_ITEM_MSG:
					mLocalTodoSource.addItems((ArrayList<SyncItem>) items);
					break;
				case ItemState.DELETE_ITEM_MSG:
					mLocalTodoSource.deleteItems((ArrayList<SyncItem>) items);
					break;
				case ItemState.UPDATE_ITEM_MSG:
					mLocalTodoSource.updateItems((ArrayList<SyncItem>) items);
					break;
				
				case ItemState.FILL_ITEM_MSG:
					mLocalTodoSource.fillItems((ArrayList<SyncItem>) items);
					mTran.transValue(items, ItemState.FILL_ITEM_MSG);
					break;
			}			
		}
		
	};
	private IMCSTranValue mTran;
	private ServiceConnection mTranConnection = new ServiceConnection() {
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mTran = IMCSTranValue.Stub.asInterface(service);
			
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			mTran = null;
		}		
	
	};
	public static final int COMPLETE_MSG = 1;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			 switch(msg.what){
			 	/**同步完成**/
			 	case COMPLETE_MSG:
			 			/**清空changelist表**/
			 			DBAdapter db = new DBAdapter(SyncActivity.this, DBConstant.TABLE_TODOLIST_CHANGELIST);
			 			db.delete(null, null);		 			
			 		break;
	         }
			super.handleMessage(msg);
		}
	};
	
}