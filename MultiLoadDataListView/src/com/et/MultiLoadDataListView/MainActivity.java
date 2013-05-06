package com.et.MultiLoadDataListView;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class MainActivity extends Activity {
	public static final int MENU_SEND = 1;
	public static final int MENU_BACK = 2;
	
	private ListView listView ;
	private ArrayList<HashMap<String,Object>> listItems =  new ArrayList<HashMap<String,Object>>();;
	/**记录checkbox的状态**/
	private ArrayList<Integer> curItems= new ArrayList<Integer>();
	private int curCount = 10;
	private int curPosition = -1;
	private int allCount;
	private Button addBtn;
	private Button allBtn;
	private Button inverseBtn;
	private Button sendBtn;
	private EditText edit;
	private LinearLayout addLinearLayout;
	private int lastItem = 0;
	private int total = 0;
	private LinearLayout footViewLayout;
	private TextView textView;
	private Cursor cur;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.recommend);
		listView = (ListView) findViewById(R.id.ListView01);
		addBtn = (Button) findViewById(R.id.add);
		allBtn = (Button) findViewById(R.id.all);
		inverseBtn = (Button) findViewById(R.id.inverse);
		sendBtn = (Button) findViewById(R.id.sendbutton);
		edit = (EditText) findViewById(R.id.EditText01);
		addLinearLayout = (LinearLayout) findViewById(R.id.LinearLayout03);
		
		
		footViewLayout = new LinearLayout(this);
		footViewLayout.setOrientation(LinearLayout.HORIZONTAL);
		footViewLayout.setGravity(Gravity.CENTER);
		footViewLayout.setPadding(0, 10, 0, 10);
		ProgressBar progress = new ProgressBar(this);
		footViewLayout.addView(progress);
		textView = new TextView(this);
		textView.setText("获取更多数据");
		textView.setTextSize(22);
		footViewLayout.addView(textView);
		
	
		/**获取联系人**/
		//getAllContacts();
		cur = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		allCount = cur.getCount();
		//int size = listItems.size();
		if(curCount>allCount)
			curCount = allCount;
		else
			curCount = 10;
		for(int i=0;i<curCount;i++){
			curItems.add(0);
		}
		getTenContacts();
		
		
		final PhoneNumberAdapter adapter = new PhoneNumberAdapter(this);
		listView.addFooterView(footViewLayout);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new OnScrollListener() {
			/**判断滚动条的状态**/
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				 /**当滚动在停止状态计算记录下标的lastItem加上1是否等于现在ListItem的总数**/
				 if((lastItem + 1) == total && scrollState == OnScrollListener.SCROLL_STATE_IDLE && curCount < allCount){
					  int offest = 0 ; 
			          if((curCount+10)<allCount){
			        	  curCount+=10;
			        	  offest = 10;
			          }
			          else{
			        	  offest = allCount - curCount;
			        	  curCount = curCount + offest;
			          }
			          for(int i=0;i<offest;i++)
			        	  curItems.add(0);
			          getTenContacts();
			          adapter.notifyDataSetChanged();
			    }

			}
			/**ListView滚动就调用此方法,判断ListView在屏幕上最后一个ListItem在整个ListView的位置**/
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				/**注意:如果ListView有footView和HeaderView它们也是ListItem,也会计算在内的**/
				
				/**firstVisibleItem表示在现时屏幕第一个ListItem(部分显示的ListItem也算)
				 在整个ListView的位置（下标从0开始）**/
				Log.i("firstVisibleItem", String.valueOf(firstVisibleItem));
				/**visibleItemCount表示在现时屏幕可以见到的ListItem(部分显示的ListItem也算)总数**/
				Log.i("visibleItemCount", String.valueOf(visibleItemCount));
				/**totalItemCount表示ListView的ListItem总数,ListView有footView和HeaderView也会计算在内**/
				Log.i("totalItemCount", String.valueOf(totalItemCount));
				total = totalItemCount;
				
				/**listView.getFirstVisiblePosition()表示在现时屏幕第一个ListItem(第一个ListItem部分显示也算)
				 * 在整个ListView的位置（下标从0开始）**/
				Log.i("firstPosition", String.valueOf(listView.getFirstVisiblePosition()));
				/**listView.getLastVisiblePosition()表示在现时屏幕最后一个ListItem(最后ListItem要完全显示出来才算)
				 * 在整个ListView的位置（下标从0开始）**/				
				Log.i("lasPosition", String.valueOf(listView.getLastVisiblePosition()));
				/**计算屏幕显示最后一项的下标(部分显示也算)，下标从0开始**/
				lastItem = firstVisibleItem + visibleItemCount - 1 ;
				Log.i("lastItem:", String.valueOf(lastItem));
				//test和lastItem数值一样,跳入源码看getLastVisiblePosition()方法就知道了
			    int test = view.getLastVisiblePosition();
			    Log.i("test:", String.valueOf(test));
			    

			}
		});
		addBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String phoneNumber = edit.getText().toString();
				final Button button = new Button(MainActivity.this);
				button.setText(phoneNumber);
				addLinearLayout.addView(button);
				button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						button.setVisibility(View.GONE);
					}
				});
			}
		});
		allBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<curCount;i++){
					curItems.set(i, 1);
				}
				//在这里用listView.invalidate();没效果(调用ondraw()方法，刷新画布);  要用方法invalidateViews();区别看SDK文档
				listView.invalidateViews();
				
				
			}
		});	
		inverseBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<curCount;i++){
					if(curItems.get(i) == 1)
						curItems.set(i, 0);
					else
						curItems.set(i, 1);
				}
				listView.invalidateViews();
			}
		});
		sendBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "短信正在发送……", Toast.LENGTH_SHORT).show();
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_SEND, 0, "发送");
		menu.add(0, MENU_BACK, 0, "返回");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stubh
		switch (item.getItemId()){
			case MENU_SEND:
				Toast.makeText(this, "短信正在发送……", Toast.LENGTH_SHORT).show();
				return true;
			case MENU_BACK:
				return true;
			
		}
		return false;
	}
	public void getTenContacts(){
		
		for(curPosition=curPosition+1;curPosition<curCount;curPosition++){
			if(cur.moveToPosition(curPosition)){
				//读取ContactsContract.Contacts._ID和ContactsContract.Contacts.DISPLAY_NAME的列数
				int idColumn  = cur.getColumnIndex(ContactsContract.Contacts._ID);        
		        int displayNameColumn = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		        
		        	HashMap<String,Object> hash = new HashMap<String, Object>();
		        	//获得联系人的ID号   
		            String contactId = cur.getString(idColumn); 
		            hash.put("ID", contactId);
		            //获得联系人姓名   
		            String disPlayName = cur.getString(displayNameColumn);  
		            hash.put("name", disPlayName);
		            
		          //查看该联系人有多少个电话号码。如果没有这返回值为0   
		            int phoneCount = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));   
		            if(phoneCount>0){
		            	String phones = "";
		            	//获得联系人的电话号码的Cursor,过滤出手机号码 " and " + ContactsContract.CommonDataKinds.Phone.TYPE + " = " + 
						//ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
		            	Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
		            							null, ContactsContract.CommonDataKinds.Phone._ID + " = " + contactId  
		            							, null, null);
		            	if(phone.moveToFirst()){  
		            		//需求只是显示联系人一个号码就可以
		            		//do{
		            			//读取联系人号码
		            			int phoneNumberColumn = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
		            			String phoneNumber = phone.getString(phoneNumberColumn);
		            			Log.i("phoneNumber", phoneNumber);
		            			
		            			phones = phoneNumber;
		            		//}while(phone.moveToNext());	            		
		            	}
		            	hash.put("phone", phones);
		            	phones = "";              	
		            	phone.close();
		            }
		            listItems.add(hash);
			}
		}
		
		
	}
	public void getAllContacts(){
		Cursor cur = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		
		
		if(cur.moveToFirst()){
			
			//读取ContactsContract.Contacts._ID和ContactsContract.Contacts.DISPLAY_NAME的列数
			int idColumn  = cur.getColumnIndex(ContactsContract.Contacts._ID);        
	        int displayNameColumn = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
	        do{
	        	HashMap<String,Object> hash = new HashMap<String, Object>();
	        	//获得联系人的ID号   
	            String contactId = cur.getString(idColumn); 
	            hash.put("ID", contactId);
	            //获得联系人姓名   
	            String disPlayName = cur.getString(displayNameColumn);  
	            hash.put("name", disPlayName);
	            
	          //查看该联系人有多少个电话号码。如果没有这返回值为0   
	            int phoneCount = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));   
	            if(phoneCount>0){
	            	String phones = "";
	            	//获得联系人的电话号码的Cursor,过滤出手机号码 " and " + ContactsContract.CommonDataKinds.Phone.TYPE + " = " + 
					//ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
	            	Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
	            							null, ContactsContract.CommonDataKinds.Phone._ID + " = " + contactId  
	            							, null, null);
	            	if(phone.moveToFirst()){  
	            		//需求只是显示联系人一个号码就可以
	            		//do{
	            			//读取联系人号码
	            			int phoneNumberColumn = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
	            			String phoneNumber = phone.getString(phoneNumberColumn);
	            			Log.i("phoneNumber", phoneNumber);
	            			
	            			phones = phoneNumber;
	            		//}while(phone.moveToNext());	            		
	            	}
	            	hash.put("phone", phones);
	            	phones = "";   
	            	
	            	phone.close();
	            }
	            
	            listItems.add(hash);
	        }while(cur.moveToNext());

		}
		cur.close();
	}
	public void sendSMS(){
		SmsManager smsManager = SmsManager.getDefault();
		
		
	}
	public class PhoneNumberAdapter extends BaseAdapter{
		private class ViewHolder{
			TextView name;
			TextView phoneNumber;
			CheckBox checkBox;
		}
		private Context context;
		
		public PhoneNumberAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			/*if(curCount>listItems.size())
				return listItems.size();
			else
				return curCount;*/
			return listItems.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			 if (convertView == null)
			 {
				 LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				 convertView = inflater.inflate(R.layout.phonenumberitem, null);
				 holder = new ViewHolder();
				 holder.checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox01);
				 holder.name = (TextView) convertView.findViewById(R.id.name);
				 holder.phoneNumber = (TextView) convertView.findViewById(R.id.phonenumber);	
				 convertView.setTag(holder);
			 }
			 else
			 {
				 holder = (ViewHolder) convertView.getTag();
			 }
			 HashMap<String,Object> hash = listItems.get(position);
			 String name = (String) hash.get("name");
			 String phones = (String) hash.get("phone");
			 holder.name.setText(name);
			 //holder.phoneNumber.setText(phones);
			 int flag = curItems.get(position);
			 Log.i(String.valueOf(position), String.valueOf(flag));
			 if(flag == 1)
				 holder.checkBox.setChecked(true);
			 else
				 holder.checkBox.setChecked(false);
			 holder.checkBox.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(holder.checkBox.isChecked()){
						Log.i("position", String.valueOf(holder.checkBox.isChecked()));
						curItems.set(position, 1);
						//holder.checkBox.setChecked(false);
					}
					else{
						curItems.set(position, 0);
						//holder.checkBox.setChecked(true);
					}		
				}
			});
			return convertView;
		}
		
	}
}
