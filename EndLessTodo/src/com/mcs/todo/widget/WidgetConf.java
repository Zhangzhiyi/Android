package com.mcs.todo.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.mcs.todo.R;
import com.mcs.todo.db.DBAdapter;
import com.mcs.todo.db.DBConstant;
import com.mcs.todo.db.WidgetSetting;
import com.mcs.todo.res.Strings;
import com.mcs.todo.utils.Tag;

public class WidgetConf extends Activity{
	
	private Spinner modeSP;
	private Spinner tagSP;
	private Button okBtn;
	private Button cancelBtn;
	private LinearLayout tagChoose;
	
	private DBAdapter db;
	
	int widgetId;
	
	public WidgetConf() {
		super();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widgetconf);
		
		// 先将结果设置成取消
		setResult(RESULT_CANCELED);
		
		db = new DBAdapter(this, DBConstant.TABLE_WIDGET_SETTING);
		
		modeSP = (Spinner)findViewById(R.id.widget_conf_mode);
		tagSP = (Spinner)findViewById(R.id.widget_conf_tag);
		okBtn = (Button)findViewById(R.id.widget_conf_ok);
		cancelBtn = (Button)findViewById(R.id.widget_conf_cancel);
		tagChoose = (LinearLayout)findViewById(R.id.widget_conf_tagchoose);
		
		// 初始化Spinner
		ArrayList<String> modeList = new ArrayList<String>();
        for(int i = 0; i < Strings.widget_conf_modes.length; i++) {
        	modeList.add(Strings.widget_conf_modes[i]);
        }
		ArrayAdapter<String> modeAdapter = 
        	new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, modeList);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSP.setAdapter(modeAdapter);
        
        Tag.init(this); // Tag使用之前必须初始化
        ArrayList<String> tagList = Tag.getInstance().getTagList();
        ArrayAdapter<String> tagAdapter = 
        	new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tagList);
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSP.setAdapter(tagAdapter);
        
        // 获得本次配置所对应的id
        Intent data = this.getIntent();
		Bundle extras = data.getExtras();
        if (extras != null) {
            widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
        
        // 初始化按钮
        okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveResult();
				
				// 设置Activity的返回结果
		        Intent resultValue = new Intent();
		        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		        setResult(RESULT_OK, resultValue);
		        
		        int[] ids = {widgetId};
				WidgetProvider.updateWidget(WidgetConf.this, ids);
				finish();
			}
        }); 
        cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
        });
        
        // 根据选项来选择是否显示标签选择
        tagChoose.setVisibility(View.INVISIBLE);
        modeSP.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch(modeSP.getSelectedItemPosition()) {
				case 0:
					tagChoose.setVisibility(View.INVISIBLE);
					break;
				case 1:
					tagChoose.setVisibility(View.VISIBLE);
					break;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
        });
	}
	
	/** 将配置的结果保存到数据库 */
	private void saveResult() {
		int mode = WidgetSetting.MODE_DATE;
		String tag = null;
        
        switch(modeSP.getSelectedItemPosition()) {
        case 0:
        	mode = WidgetSetting.MODE_DATE;
        	break;
        case 1:
        	mode = WidgetSetting.MODE_TAG;
        	break;
        }
        
		ContentValues cv = new ContentValues();
		cv.put(WidgetSetting.WIDGET_ID, widgetId);
		cv.put(WidgetSetting.MODE, mode);
		
		if(mode == WidgetSetting.MODE_TAG) {
			tag = (String) tagSP.getSelectedItem();
			cv.put(WidgetSetting.TAG, tag);
		}
		
		db.insert(cv);
	}
}
