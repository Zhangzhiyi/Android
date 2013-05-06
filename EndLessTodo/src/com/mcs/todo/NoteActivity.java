package com.mcs.todo;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcs.todo.db.DBAdapter;
import com.mcs.todo.db.DBConstant;
import com.mcs.todo.db.Notecategory;
import com.mcs.todo.db.Notelist;
import com.mcs.todo.res.CustomListView;
import com.mcs.todo.utils.Time;

public class NoteActivity extends Activity {
					
	 public static DBAdapter db;
	 public Cursor noteCursor;
	 public Cursor cateCursor;
	
	 public static DBAdapter dc;
	 /**放置标签View和还没有标签名的备忘**/
	 public static CustomListView listView;
	 /**放置标签的layout**/
	 public RelativeLayout categoryLayout;
	 /**点击进入标签查看备忘的列表**/
	 private ListView categoryListView;
	 
	 public static NoteAdapter noteAdapter;
	 public CategoryAdapter categoryAdapter;
	
	 
	 private Vibrator mVibrator;
	 public static WindowManager lp; 
	 
	 /**categoryId记录点击进入标签的id**/
	 public static int categoryId = 0;
	 /**isCategory记录是否点击进入标签**/
	 public boolean isCategory = false;
	 public TextView categroyTitle;
	 public boolean isMove;	 
	 public boolean isDelete;
	 public List<Entry> items = new ArrayList<Entry>();;
	 public List<Integer> noteCheckBox;
	 public List<Integer> categoryCheckBox;
	 public List<Integer> categoryChildCheckBox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.note);
		
		categoryListView = (ListView) findViewById(R.id.CategoryListView);
		categoryLayout = new RelativeLayout(this);
		categoryLayout.setPadding(0, 0, 0, 8);
		listView = (CustomListView)findViewById(R.id.listview);
		
		mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		lp = (WindowManager) getSystemService(WINDOW_SERVICE);
		db = new DBAdapter(this, DBConstant.TABLE_NOTELIST);	
		dc = new DBAdapter(this, DBConstant.TABLE_NOTECATEGORY);
		
					
		noteCursor = db.query(null, Notelist.CATEGORY_ID+" = "+0, null, Notelist.CRT_TIME+" DESC ");		
		startManagingCursor(noteCursor);  
		
		/**加入ListView的HeadView**/
		addCategoryHeadView();
		
		/**创建实体链表 **/
		buildeEntry(noteCursor);
		noteAdapter = new NoteAdapter(this);		
		listView.addHeaderView(categoryLayout,null,false);		
		listView.setAdapter(noteAdapter);
		
		noteCheckBox = new ArrayList<Integer>();
		categoryCheckBox = new ArrayList<Integer>();
		categoryChildCheckBox = new ArrayList<Integer>();
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				/**因为这个Listview加入了headview，所以position要减去1**/
				position = position - 1;
				
				Log.i("position", String.valueOf(position));
				noteCursor.moveToPosition(position);				
				id = noteCursor.getLong(0);
				CustomListView.moveId = id;
				String title = noteCursor.getString(1);
				String content = noteCursor.getString(2);
				int categoryId = noteCursor.getInt(4);
				Intent noteEdit = new Intent(NoteActivity.this,NoteEdit.class);
				Bundle bundle = new Bundle();
				bundle.putLong(Notelist._ID, id);
				bundle.putString(Notelist.TITLE, title);
				//bundle.putString(Notelist.CONTENT, content);
				bundle.putInt(Notelist.CATEGORY_ID, categoryId);
				noteEdit.putExtras(bundle);
				startActivityForResult(noteEdit, REQUEST_EDIT);
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				/**因为这个Listview加入了headview，所以position要减去1**/
				position = position - 1;				 
				noteCursor.moveToPosition(position);				
				id = noteCursor.getLong(0);
				CustomListView.moveId = id;
				mVibrator.vibrate(new long[]{0,50,0,0}, -1);
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				CustomListView.moveEntry = items.get(position);
				//要另外见一个moveview布局文件,虽然一样，但是如果用上面同一个noteitem布局文件，会出现空指针错误
				CustomListView.moveView = inflater.inflate(R.layout.moveview, null);
				TextView moveTitle = (TextView) listView.moveView.findViewById(R.id.move_title);
				moveTitle.setText(listView.moveEntry.getTitle());
				items.remove(position);
				listView.invalidateViews();
				listView.setEnabled(false);
				listView.moveFlag = true;
				listView.itemView = view;    
				listView.movePosition = position;
				
				 WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
				 localLayoutParams.x = 0;
				 localLayoutParams.y = -240;
				 localLayoutParams.width = LayoutParams.FILL_PARENT;
				 localLayoutParams.height = LayoutParams.WRAP_CONTENT;
				 localLayoutParams.alpha = 100;
				
				 lp.addView(CustomListView.moveView,localLayoutParams);
				 
				 /**如果headView不在屏幕上，设置为显示第一项**/
				 listView.setSelection(0);
				return false;
			}   
		});
		categoryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				cateCursor.moveToPosition(position);				
				id = cateCursor.getLong(0);
				CustomListView.moveId = id;
				String title = cateCursor.getString(1);
				String content = cateCursor.getString(2);
				int categoryId = cateCursor.getInt(4);
				Intent noteEdit = new Intent(NoteActivity.this,NoteEdit.class);
				Bundle bundle = new Bundle();
				bundle.putLong(Notelist._ID, id);
				bundle.putString(Notelist.TITLE, title);
				//bundle.putString(Notelist.CONTENT, content);
				bundle.putInt(Notelist.CATEGORY_ID, categoryId);
				noteEdit.putExtras(bundle);
				startActivityForResult(noteEdit, REQUEST_EDIT);
			}
		});   
		categoryListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				cateCursor.moveToPosition(position);				
				id = cateCursor.getLong(0);
				CustomListView.moveId = id;
				String title = cateCursor.getString(1);
				mVibrator.vibrate(new long[]{0,50,0,0}, -1);
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				CustomListView.moveView = inflater.inflate(R.layout.moveview, null);
				TextView moveTitle = (TextView) listView.moveView.findViewById(R.id.move_title);
				moveTitle.setText(title);
				isMove = true;
				
								
				categoryListView.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
				localLayoutParams.x = 0;
				localLayoutParams.y = -240;
				localLayoutParams.width = LayoutParams.FILL_PARENT;
				localLayoutParams.height = LayoutParams.WRAP_CONTENT;
				localLayoutParams.alpha = 100;		
				lp.addView(CustomListView.moveView,localLayoutParams);
				
				isCategory =false;
				return false;
			}
		});
		/**这里是进入了categoryListView长按Item选放出到CustomListView
		 * TMD~原来listView.setVisibility(View.VISIBLE);触摸屏幕还是categoryListView触发的**/
		categoryListView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int rawX = (int) event.getRawX();
				int rawY = (int) event.getRawY();
				Log.i("CX", String.valueOf(rawX));
				Log.i("CY", String.valueOf(rawY));
				if(isMove){
					switch(event.getAction()){
					case MotionEvent.ACTION_MOVE:
						WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
						localLayoutParams.x = 0;
						/**不明白为什么y=0时moveView在y=240纵坐标处**/
						localLayoutParams.y = rawY-240;
						localLayoutParams.alpha = 100;
						localLayoutParams.width = LayoutParams.FILL_PARENT;
						localLayoutParams.height = LayoutParams.WRAP_CONTENT;
						lp.updateViewLayout(CustomListView.moveView, localLayoutParams);
						break;
					case MotionEvent.ACTION_UP:
						int x = (int) event.getX();
						int y = (int) event.getY();
						lp.removeView(CustomListView.moveView);
						listView.setCoordinates();
						int count = CustomListView.listRect.size();
						boolean flag = false;
						for(int i=0;i<count;i++){
							Rect rect = CustomListView.listRect.get(i);
							if(rect.contains(x, y)){
								flag = true;
								View child = CustomListView.headView.getChildAt(i);
								/**获得View的id,也是标签在表中的id**/
								int id = child.getId();
								ContentValues values = new ContentValues();
								values.put(Notelist.CATEGORY_ID, id);
								NoteActivity.db.update(values, Notelist._ID+" = "+ CustomListView.moveId, null);
								/**更改标签title的数字**/
								Cursor a = dc.query(null, Notecategory._ID+" = "+id, null, null);
								startManagingCursor(a);
								a.moveToFirst();
								String name = a.getString(1);
								a.close();
								Cursor c = db.query(null, "categoryId "+" = "+id, null, null);								
								TextView categoryName = (TextView) child.findViewById(R.id.category_title);
								categoryName.setText(name+"("+c.getCount()+")");
								c.close();
								break;
							}
						}		
						if(!flag){
							ContentValues values = new ContentValues();
							values.put(Notelist.CATEGORY_ID, 0);
							NoteActivity.db.update(values, Notelist._ID+" = "+ CustomListView.moveId, null);
					
							Cursor a = NoteActivity.dc.query(null, Notecategory._ID+" = "+categoryId, null, null);
							startManagingCursor(a);
							a.moveToFirst();
							String name = a.getString(1);
							a.close();
							Log.i("name", name);
							Cursor c = NoteActivity.db.query(null, "categoryId "+" = "+categoryId, null, null);						
							categroyTitle.setText(name+"("+c.getCount()+")");
							c.close();
							
							noteCursor = db.query(null, Notelist.CATEGORY_ID+" = "+0, null, Notelist.CRT_TIME+" DESC ");	
							buildeEntry(noteCursor);
							noteAdapter = new NoteAdapter(NoteActivity.this);
							listView.setAdapter(noteAdapter);
						}	
						isMove = false;
						break;				
					}
				}
				return false;
			}
		});
		
	}
	
	private void addCategoryHeadView(){ 
		Cursor category = dc.query(null, null, null, null);
		startManagingCursor(category);
		categoryLayout.removeAllViews();
		if(category!=null){
			if(category.moveToFirst()){
				int index = 0;
				int previous = 0;
				do{
					 final int id = category.getInt(0);
					 String name = category.getString(1);
					 int count;
					 Cursor c = db.query(null, "categoryId "+" = "+id, null, null);
					 startManagingCursor(c);
					 if(c!=null)
						 count = c.getCount();
					 else
						 count = 0;
					 LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					 View  view = inflater.inflate(R.layout.notecategory, null);
					 TextView categoryName = (TextView) view.findViewById(R.id.category_title);					 
					 categoryName.setText(name+"("+count+")");
					 RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
					 if(index==0){
						 
						 LayoutParams.addRule(RelativeLayout.ABOVE);
						 categoryLayout.addView(view,LayoutParams);
						 view.setId(id);
						 previous = id;
					 }
					 else{     
						 LayoutParams.addRule(RelativeLayout.BELOW,previous);
						 view.setId(id);
						 previous = id;
						 categoryLayout.addView(view,LayoutParams);
					 }	
					 final CheckBox checkBox = (CheckBox) view.findViewById(R.id.CheckBox);
					 checkBox.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(checkBox.isChecked()){
								if(!categoryCheckBox.contains(id))
									categoryCheckBox.add(id);								
							}
							else{
								if(categoryCheckBox.contains(id)){									
									int index = categoryCheckBox.indexOf(id);
									categoryCheckBox.remove(index);
								}
							}
						}
					});
					 view.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Log.i("tag22222222222222", "msg");
							isCategory = true;
							categoryId = v.getId();
							categroyTitle = (TextView) v.findViewById(R.id.category_title);
							cateCursor = db.query(null, Notelist.CATEGORY_ID+" = "+categoryId, null, null);
							startManagingCursor(cateCursor);
							categoryAdapter = new CategoryAdapter(NoteActivity.this);
							categoryListView.setAdapter(categoryAdapter);
							listView.setVisibility(View.GONE);
							categoryListView.setVisibility(View.VISIBLE);
						}
					});
					 index++;
				}while(category.moveToNext());
			}			
		}
	}
	 
	
	public void buildeEntry(Cursor cursor){
		items.clear();
		if(cursor!=null){
			if(cursor.moveToFirst()){
				do{
					Entry entry = new Entry();
					int id = cursor.getInt(0);
					entry.setId(id);
					String title = cursor.getString(1);
					entry.setTitle(title);
					items.add(entry);
				}while(cursor.moveToNext());
			}
		}		
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		if(isDelete){
			isDelete = false;
			displayCheckBox();
		}
		else{
			if(isCategory){
				categoryListView.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				displayCheckBox();
				categoryId = 0;
				isCategory = false;
			}
			else{
				finish();
			}
		}
		
	}
	
	public static final int MENU_ADD_NOTE = Menu.FIRST;
	public static final int MENU_ADD_CATEGORY = Menu.FIRST+1;
	public static final int MENU_DELETE = Menu.FIRST+2;
	public static final int MENU_COMFIRM = Menu.FIRST+3;
	public static final int MENU_CANCEL = Menu.FIRST+4;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_ADD_NOTE, 0, "新建备忘");
		menu.add(0, MENU_ADD_CATEGORY, 0, "新建标签");
		menu.add(0, MENU_DELETE, 0, "删除");
		
		return true;
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		
		if(isDelete)
		{
			menu.removeGroup(0);
			menu.removeGroup(1);
			menu.add(1, MENU_COMFIRM, 0, "确定");
			menu.add(1, MENU_CANCEL, 0, "取消");
		}
		else{
			menu.removeGroup(0);
			menu.removeGroup(1);
			menu.add(0, MENU_ADD_NOTE, 0, "新建备忘");			
			if(!isCategory){
				menu.add(0, MENU_ADD_CATEGORY, 0, "新建标签");
			}
			menu.add(0, MENU_DELETE, 0, "删除");
		}
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case MENU_ADD_NOTE:
				Intent addNote = new Intent(NoteActivity.this,NoteEdit.class);
				startActivityForResult(addNote, REQUEST_ADD);
				return true;
			case MENU_ADD_CATEGORY:
				showDialog(DIALOG_ADD_CATEGORY);
				return true;
			case MENU_DELETE:
				isDelete = true;
				displayCheckBox();
				return true;
			case MENU_COMFIRM:
				operationDelete();
				return true;
			case MENU_CANCEL:
				isDelete = false;
				displayCheckBox();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void displayCheckBox(){
		
			noteCheckBox.clear();
			listView.setAdapter(noteAdapter);
						
			categoryCheckBox.clear();
			int count = categoryLayout.getChildCount();
			for(int i=0;i<count;i++)
			{
				LinearLayout linear = (LinearLayout) categoryLayout.getChildAt(i);
				CheckBox checkBox = (CheckBox) linear.findViewById(R.id.CheckBox);
				if(isDelete){
					linear.setClickable(false);
					checkBox.setVisibility(View.VISIBLE);
				}
				else{				
					linear.setClickable(true);
					checkBox.setVisibility(View.INVISIBLE);
				}			
			}
			categoryChildCheckBox.clear();
			categoryListView.setAdapter(categoryAdapter);
	}
	public void operationDelete(){
		if(!isCategory){
			int size = noteCheckBox.size();
			Log.i("size", String.valueOf(size));
			for(int i=0;i<size;i++){
				int id = noteCheckBox.get(i);				
				db.delete( Notelist._ID+" = "+id,null);			
			}		
			noteCursor = db.query(null, Notelist.CATEGORY_ID+" = "+0, null, Notelist.CRT_TIME+" DESC ");
			buildeEntry(noteCursor);
			isDelete = false;
			listView.setAdapter(noteAdapter);	
			
			size = categoryCheckBox.size();
			for(int i=0;i<size;i++){
				int id = categoryCheckBox.get(i);
				dc.delete(Notecategory._ID+" = "+id, null);
				db.delete(Notelist.CATEGORY_ID+" = "+id, null);	
				int count = categoryLayout.getChildCount();
				for(int j=0;j<count;j++){
					View view = categoryLayout.getChildAt(j);
					if(view.getId() == id){
						categoryLayout.removeView(view);
						break;
					}
				}
			}
			int count = categoryLayout.getChildCount();
			for(int i=0;i<count;i++){
				LinearLayout linear = (LinearLayout) categoryLayout.getChildAt(i);
				CheckBox checkBox = (CheckBox) linear.findViewById(R.id.CheckBox);
				checkBox.setVisibility(View.INVISIBLE);
				linear.setClickable(true);
			}
		}
		else{
			int size = categoryChildCheckBox.size();
			for(int i=0;i<size;i++){
				int id = categoryChildCheckBox.get(i);				
				db.delete( Notelist._ID+" = "+id,null);			
			}
			cateCursor = db.query(null, Notelist.CATEGORY_ID+" = "+categoryId, null, null);
			isDelete = false;
			categoryListView.setAdapter(categoryAdapter);	
		}		
	}
	public static final int DIALOG_ADD_CATEGORY = 0;
	@Override
	protected Dialog onCreateDialog(int id) {
		//return super.onCreateDialog(id);
		switch(id){
			case DIALOG_ADD_CATEGORY:
				final EditText edit = new EditText(this);
				return new AlertDialog.Builder(this)
				.setTitle("新建标签")
				.setView(edit)
				.setPositiveButton("确定", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String name = edit.getText().toString();
						ContentValues values = new ContentValues();
						values.put("name", name);
						dc.insert(values);
						addCategoryHeadView();
					}
				})
				.setNegativeButton("取消", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				})
				.create();
				
		}
		return null;
	}
	 public static final int REQUEST_ADD = 1; 
	 public static final int REQUEST_EDIT = 2; 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("tag111111111", "msg");
		noteCursor = db.query(null, Notelist.CATEGORY_ID+" = "+0, null, Notelist.CRT_TIME+" DESC ");
		buildeEntry(noteCursor);
		listView.setAdapter(noteAdapter);
		
		if(isCategory){
			cateCursor = db.query(null, Notelist.CATEGORY_ID+" = "+categoryId, null, Notelist.CRT_TIME+" DESC ");
			categoryListView.setAdapter(categoryAdapter);
		}
		
		/*switch(requestCode){
			case REQUEST_ADD:
				if(resultCode == RESULT_OK){
					Bundle extras = data.getExtras();
					if(extras!=null){				
						int categoryId = extras.getInt(Notelist.CATEGORY_ID);
						noteCursor = db.query(null, Notelist.CATEGORY_ID+" = "+categoryId, null, null);			
					}
					items.clear();
					buildeEntry(noteCursor);
					listView.setAdapter(adapter);
					Log.i("tag", "msg");
				}
				
				break;
			case REQUEST_EDIT:
				if(resultCode == RESULT_OK){
					Bundle extras = data.getExtras();
					if(extras!=null){				
						int categoryId = extras.getInt(Notelist.CATEGORY_ID);
						noteCursor = db.query(null, Notelist.CATEGORY_ID+" = "+categoryId, null, null);			
					}
					items.clear();
					buildeEntry(noteCursor);
					listView.setAdapter(adapter);
					Log.i("tag", "msg");
				}
				break;
		}*/	
	}
	


 public class NoteAdapter extends BaseAdapter{
		
		private class ViewHolder{
			TextView title;
			TextView content;
			CheckBox checkBox;
			TextView crtTime;
			
			
		}
		private Context context;
		public NoteAdapter(Context context) {
			this.context = context;
			
		}
		
		public List<Entry> getItems() {
			return items;
		}
		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			 if (convertView == null)
			 {
				 LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				 convertView = inflater.inflate(R.layout.noteitem, null);
				 holder = new ViewHolder();
				 holder.checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox);
				 holder.title = (TextView) convertView.findViewById(R.id.note_title);
				 holder.crtTime = (TextView) convertView.findViewById(R.id.crt_time);
				 //holder.content = (TextView) convertView.findViewById(R.id.note_content);				 
				 convertView.setTag(holder);
			 }
			 else
			 {
				 holder = (ViewHolder) convertView.getTag();
			 }
			 
			 if(isDelete){
				 holder.crtTime.setVisibility(View.INVISIBLE);
			 }
			 else{
				 
				  holder.crtTime.setVisibility(View.VISIBLE);
				  noteCursor.moveToPosition(position);
				  int index = noteCursor.getColumnIndex(Notelist.CRT_TIME);
				  long millis = noteCursor.getLong(index);
				  holder.crtTime.setText(Time.getDateString(millis));
			 }
				 
			
			 
			 final Entry entry = items.get(position);
			 final int id = entry.getId();
			 holder.title.setText(entry.getTitle());
			 if(isDelete)
				 holder.checkBox.setVisibility(View.VISIBLE);
			 else
				 holder.checkBox.setVisibility(View.INVISIBLE);
			 
			 holder.checkBox.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(holder.checkBox.isChecked()){
						
						if(!noteCheckBox.contains(id))
							noteCheckBox.add(id);
					}
					else{
						if(noteCheckBox.contains(id)){
							int index = noteCheckBox.indexOf(id);
							noteCheckBox.remove(index);
						}
					}
				}
			});
			return convertView;
		}
		/**刷新List<Entry> items**/
		public void refreshList(){
			noteCursor = db.query(null, Notelist.CATEGORY_ID+" = "+0, null, Notelist.CRT_TIME+" DESC ");
			buildeEntry(noteCursor);
		}
		/** 当松开手势时加回移动的ListItem**/
		public void addMoveViewPosition(int position,Entry moveEntry){
		items.add(position, moveEntry);
		listView.invalidateViews();
		
		}
 	}
 
 	public class CategoryAdapter extends BaseAdapter{
 		private class ViewHolder{
			TextView title;
			TextView content;
			CheckBox checkBox;
			TextView crtTime;
		}
 		private Context context;
 		public CategoryAdapter(Context context) {
 			this.context = context;
		}
		@Override
		public int getCount() {
			return cateCursor.getCount();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			 if (convertView == null)
			 {
				 LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				 convertView = inflater.inflate(R.layout.noteitem, null);
				 holder = new ViewHolder();
				 holder.checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox);
				 holder.title = (TextView) convertView.findViewById(R.id.note_title);
				 //holder.content = (TextView) convertView.findViewById(R.id.note_content);	
				 holder.crtTime = (TextView) convertView.findViewById(R.id.crt_time);
				 convertView.setTag(holder);
			 }
			 else
			 {
				 holder = (ViewHolder) convertView.getTag();
			 }
			 cateCursor.moveToPosition(position);
			 final int id = cateCursor.getInt(0);
			 String title = cateCursor.getString(1);
			 holder.title.setText(title);
			 if(isDelete){
				 holder.crtTime.setVisibility(View.INVISIBLE);
			 }
			 else{				 
				  holder.crtTime.setVisibility(View.VISIBLE);
				  int index = cateCursor.getColumnIndex(Notelist.CRT_TIME);
				  long millis = cateCursor.getLong(index);
				  holder.crtTime.setText(Time.getDateString(millis));
			 }
			 if(isDelete)
				 holder.checkBox.setVisibility(View.VISIBLE);
			 else
				 holder.checkBox.setVisibility(View.INVISIBLE);
			 holder.checkBox.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(holder.checkBox.isChecked()){
							
							if(!categoryChildCheckBox.contains(id))
								categoryChildCheckBox.add(id);
						}
						else{
							if(categoryChildCheckBox.contains(id)){
								int index = categoryChildCheckBox.indexOf(id);
								categoryChildCheckBox.remove(index);
							}
								
						}
					}
				});
			 return convertView;
		}
 		
 	}
 
 	public class Entry{
 		private int id;
 		private String title;
 		
 		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getTitle() {
 			return title;
 		}

 		public void setTitle(String title) {
 			this.title = title;
 		}
	
 	}
}