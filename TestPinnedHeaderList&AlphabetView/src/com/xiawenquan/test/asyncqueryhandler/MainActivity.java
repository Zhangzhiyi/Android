package com.xiawenquan.test.asyncqueryhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.xiawenquan.test.adapter.ContactAdapter;
import com.xiawenquan.test.mode.ConteactMode;
import com.xiawenquan.test.utils.Utils;
import com.xiawenquan.test.widget.AlphabetListView;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
/**
 * 简单使用AsyncQueryHandler
 * @author xianweuqan
 *
 */
@SuppressWarnings("unused")
public class MainActivity extends Activity {

	private ArrayList<ConteactMode> modes;
	protected QueryHandler mQueryHandler;
	protected Cursor mCursor = null;
	private AlphabetListView listView;
	private HashMap<String, Integer> alphaIndexer ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//定义要查询的数据
		Uri uri = Uri.parse("content://com.android.contacts/data/phones");
		
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		modes = new ArrayList<ConteactMode>();
		listView = (AlphabetListView) findViewById(R.id.mylistview);
		//在这里加上加载框
		mQueryHandler = new QueryHandler(getContentResolver());
		query(uri);
	}

	/**
	 * 查询数据
	 * @param uri 数据的路径
	 */
	private void query(Uri uri) {
		
		String[] projection = { "_id", "display_name", "data1", "sort_key" };
		mQueryHandler.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc");//按字母排序

	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mCursor != null) {
			mCursor.deactivate();
		}
	}

	class QueryHandler extends AsyncQueryHandler {

		public QueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override//查询完成后调用此方法
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			super.onQueryComplete(token, cookie, cursor);
			setData(cursor);
			//在这里取消加载框
		}
	}
	
	/**
	 * 绑定数据
	 * @param cursor 游标
	 */
	  
	private void setData(Cursor cursor){
		if(cursor != null && cursor.getCount() > 0){
			if(alphaIndexer == null){
				alphaIndexer = new HashMap<String, Integer>();
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					
					cursor.moveToPosition(i);
					//取出每一条数据
					String name = cursor.getString(1);
					String phone = cursor.getString(2);
					String sortKey = cursor.getString(3);
					
					//过滤"+86"
					if(phone.startsWith("+86")){
						phone = phone.substring(3);
					}
					
					//把数据封装在实体中
					ConteactMode mode = new ConteactMode();
					mode.setName(name);
					mode.setPhone(phone);
					
					String firstAlpha = Utils.getAlpha(sortKey);
					mode.setFirstAlpha(firstAlpha);
					//将封装的实体加到数组中
					modes.add(mode);
					
				}
				
				
				for(int i = 0 ; i < modes.size() ; i++){
					//处理当点击某一个字母后，其内容出现在屏幕可见状态下的最上面
					ConteactMode conteactMode = modes.get(i);
					String currentAlpha = conteactMode.getFirstAlpha();
					ConteactMode mode = (i - 1) >= 0 ? modes.get(i - 1)  : null;
					String previewStr = "";
					if(mode != null){
						previewStr = mode.getFirstAlpha();
					}
					if (!previewStr.equals(currentAlpha)) {
						alphaIndexer.put(currentAlpha,i);
					}
				}
				
				//把数据设置到adapter
				ContactAdapter adapter = new ContactAdapter(getApplicationContext());
				adapter.setData(modes);
				
				listView.setAlphabetIndex(alphaIndexer);
				listView.setAdapter(adapter);
				listView.setOnScrollListener(adapter);
				listView.setPinnedHeaderView(R.layout.pinnedlistview_header);
			}
		}
	}
	
}
