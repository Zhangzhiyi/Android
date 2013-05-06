package com.mcs.todo;

import java.util.Calendar;

import com.mcs.todo.db.DBAdapter;
import com.mcs.todo.db.DBConstant;
import com.mcs.todo.db.Notelist;
import com.mcs.todo.res.LinedEditText;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class NoteEdit extends Activity{
	
	private EditText editTitle;
	//private EditText editContent;
	private DBAdapter db;
	private Cursor cursor;
	private boolean flag = false;
	private long id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.noteedit);
		
		db = new DBAdapter(this, DBConstant.TABLE_NOTELIST);
		
		editTitle = (LinedEditText) findViewById(R.id.note_edit_title);
		//editContent = (LinedEditText) findViewById(R.id.note_edit_content);
		
		
		/**判断是否更新**/
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			id = bundle.getLong(Notelist._ID, -1);
			String title = bundle.getString(Notelist.TITLE);
			//String content = bundle.getString(Notelist.CONTENT);
			editTitle.setText(title);
			//editContent.setText(content);
			/**设置更新标志**/		
			flag = true;
		}	
		
				
	}
	/** 设置按回退键保存 **/
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		ContentValues values = new ContentValues();
		
		//values.put(Notelist.CONTENT, editContent.getText().toString());
		if (flag){
			values.put(Notelist.TITLE, editTitle.getText().toString());
			db.update(values, Notelist._ID +" = "+ id, null);		
		}
		else
		{
			if(!editTitle.getText().toString().equals("")){
				/**新建设置默认标签**/
				values.put(Notelist.TITLE, editTitle.getText().toString());
				values.put(Notelist.CATEGORY_ID, NoteActivity.categoryId);
				long millis = Calendar.getInstance().getTimeInMillis();
				values.put(Notelist.CRT_TIME, millis);
				db.insert(values);
			}
			
		}
			
		Intent save = new Intent();
		setResult(RESULT_OK,save);
		finish();
		
	}	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
	}
}