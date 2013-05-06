package com.mcs.todo;

import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

import com.mcs.todo.db.DBAdapter;
import com.mcs.todo.db.DBConstant;
import com.mcs.todo.db.Todolist;
import com.mcs.todo.utils.Time;

public class Edit extends New{
	
    // 获取的数据库_id
    private long id;

	@SuppressWarnings("unchecked") // 看到黄色的惊叹号，很烦
	@Override
	protected void init() {
		id = this.getIntent().getLongExtra(Todolist._ID, -1);
		this.setTitle("编辑待办");
		finishBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent result = new Intent();
				data2Intent(result, false); // 编辑的状态下不需要改变crtTime
				result.putExtra(Todolist._ID, id);
	    		// 设置结果
	    		setResult(RESULT_OK, result); 
	    		finish();
			}
        });
		
		DBAdapter db = new DBAdapter(this, DBConstant.TABLE_TODOLIST);
		Cursor cur = db.query(null, Todolist._ID + "=" + id, null, null);
		if (null != cur) {
			cur.moveToFirst();
			
			titleEdit.setText(cur.getString(cur.getColumnIndex(Todolist.TITLE)));
			contentEdit.setText(cur.getString(cur.getColumnIndex(Todolist.CONTENT)));
			
			// 初始化时间值
			int[] date = Time.parseDateString(cur.getString(cur.getColumnIndex(Todolist.BEGIN_DATE)));
			beginYear = date[0];
			beginMonth = date[1];
			beginDay = date[2];
			
			int[] time = Time.parseTimeString(cur.getString(cur.getColumnIndex(Todolist.BEGIN_TIME)));
			beginHour = time[0];
			beginMinute = time[1];
			
			date = Time.parseDateString(cur.getString(cur.getColumnIndex(Todolist.END_DATE)));
			endYear = date[0];
			endMonth = date[1];
			endDay = date[2];
			
			time = Time.parseTimeString(cur.getString(cur.getColumnIndex(Todolist.END_TIME)));
			endHour = time[0];
			endMinute = time[1];
			
			// 计算alertHour和alertMinute
			long aTime = cur.getLong(cur.getColumnIndex(Todolist.ALERT_TIME));
			alertHour = Time.getAlertHour(aTime);
			alertMinute = Time.getAlertMinute(aTime);
			
			// 更新按钮上的文字
			beginDateBtn.setText(cur.getString(cur.getColumnIndex(Todolist.BEGIN_DATE)));
			beginTimeBtn.setText(cur.getString(cur.getColumnIndex(Todolist.BEGIN_TIME)));
			endDateBtn.setText(cur.getString(cur.getColumnIndex(Todolist.END_DATE)));
			endTimeBtn.setText(cur.getString(cur.getColumnIndex(Todolist.END_TIME)));
			alertTimeBtn.setText(Time.getTimeString(alertHour, alertMinute, "HH小时mm分钟"));
		
			ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) warnFreqSpinner.getAdapter();
			int position = adapter.getPosition(cur.getString(cur.getColumnIndex(Todolist.WARN_FREQUENCY)));
			warnFreqSpinner.setSelection(position);
			
			importanceRB.setRating(cur.getInt(cur.getColumnIndex(Todolist.IMPORTANCE)));
			tagEdit.setText(cur.getString(cur.getColumnIndex(Todolist.TAG)));
		}
	}
}
