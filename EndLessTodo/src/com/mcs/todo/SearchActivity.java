package com.mcs.todo;

import java.util.Calendar;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import com.mcs.todo.db.DBAdapter;
import com.mcs.todo.db.DBConstant;
import com.mcs.todo.db.Todolist;
import com.mcs.todo.res.Strings;
import com.mcs.todo.utils.Time;


public class SearchActivity extends ListActivity{
	
	private Cursor cursor;
	private LayoutInflater inflater;
	private myAdapter adapter;
	private TextView searchTitle;
	private Calendar ca;
	// 数据库Adapter
    private DBAdapter db;
    private String listProjection[] = {
    		Todolist._ID,
    		Todolist.TITLE,
    		Todolist.BEGIN_DATE,
    		Todolist.BEGIN_TIME_LONG,
    		Todolist.IMPORTANCE
    		};
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.search);
		
		searchTitle = (TextView) findViewById(R.id.search_title);
		
		
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 初始化数据库
    	db = new DBAdapter(this, DBConstant.TABLE_TODOLIST);
    	
    	/* 按下键盘搜索键即调用搜索框 */
        //setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
    	//onSearchRequested();
        
    	Intent intent = getIntent();
    	String query = intent.getStringExtra("query");
    	
		cursor = db.query(listProjection,"title like "+"'%"+query+"%'"+" or "+Todolist.CONTENT+" like "+"'%"+query+"%'", null, null);	  	
    	startManagingCursor(cursor);   	
    	   	
    	adapter = new myAdapter();
    	setListAdapter(adapter);	
    	
		searchTitle.setText("搜索结果"+cursor.getCount()+"条"); 
    	 /***  点击Item跳转  ***/
    	 getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.i("id", String.valueOf(id));
				/**不能用上面方法的参数id，因为id的值一直是0，要Cursor移动到position再获取id**/
				Log.i("position", String.valueOf(position));				
				cursor.moveToPosition(position);
				id = cursor.getLong(0);
				long beginTimeLong = cursor.getLong(3);
				ca = Time.getInstance();
				ca.setTimeInMillis(beginTimeLong);
				
				Intent mIntent = new Intent(SearchActivity.this,Main.class);
				mIntent.setAction(Strings.SEARCH_TODO_ACTION);
				mIntent.putExtra(Todolist._ID, id);
				startActivity(mIntent);
				finish();
				
			}
		});
	}
	
	/***重新查询更新列表***/
	public void query(String str){
		searchTitle.setText("搜索结果");
		cursor = db.query(listProjection,"title like "+"'%"+str+"%'", null, null);		
		adapter.notifyDataSetChanged();
	}
	
	private class myAdapter extends BaseAdapter{
		
		private class ViewHolder{
			TextView title;
			TextView date;
			RatingBar ratingbar;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return cursor.getCount();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			 if (convertView == null)
			 {
				 convertView = inflater.inflate(R.layout.searchitem, null);
				 holder = new ViewHolder();
				 holder.title = (TextView) convertView.findViewById(R.id.search_title);
				 holder.date = (TextView) convertView.findViewById(R.id.search_date);
				 holder.ratingbar = (RatingBar) convertView.findViewById(R.id.search_importance);
				 
				 convertView.setTag(holder);
			 }
			 else
			 {
				 holder = (ViewHolder) convertView.getTag();
			 }
			 cursor.moveToPosition(position);
			 holder.title.setText(cursor.getString(1));
			 holder.date.setText(cursor.getString(2));
			 holder.ratingbar.setRating(cursor.getInt(3));
			 
			return convertView;
		}
		
	}
}