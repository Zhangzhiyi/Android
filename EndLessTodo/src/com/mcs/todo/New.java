package com.mcs.todo;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.mcs.todo.db.Todolist;
import com.mcs.todo.res.Strings;
import com.mcs.todo.utils.Time;

public class New extends Activity{
	
	protected EditText titleEdit;
	protected EditText contentEdit;
	protected Button beginDateBtn;
	protected Button beginTimeBtn;
	protected Button endDateBtn;
	protected Button endTimeBtn;
	protected Spinner warnFreqSpinner;
	protected Button alertTimeBtn;
	protected RatingBar importanceRB;
	protected EditText tagEdit;
	protected Button finishBtn;
	protected Button discardBtn;
	
	protected int beginYear=0, endYear=0;
    protected int beginMonth=0, endMonth=0;
    protected int beginDay=0, endDay=0;
    protected int beginHour=0, endHour=0;
    protected int beginMinute=0, endMinute=0;
    protected int alertHour=0, alertMinute=5; // 默认提前5分钟提醒
    
    protected ArrayList<String> warnFreqList;
    protected ArrayList<String> alertTimeList;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newitem);
        
        titleEdit = (EditText)findViewById(R.id.todonew_title);
        contentEdit = (EditText)findViewById(R.id.todonew_content);
        beginDateBtn = (Button)findViewById(R.id.todonew_begindate);
        beginTimeBtn = (Button)findViewById(R.id.todonew_begintime);
        endDateBtn = (Button)findViewById(R.id.todonew_enddate);
        endTimeBtn = (Button)findViewById(R.id.todonew_endtime);
        warnFreqSpinner = (Spinner)findViewById(R.id.todonew_warnfreq);
        alertTimeBtn = (Button)findViewById(R.id.todonew_alerttime);
        importanceRB = (RatingBar)findViewById(R.id.todonew_importance);
        tagEdit = (EditText)findViewById(R.id.todonew_tag);
        finishBtn = (Button)findViewById(R.id.todonew_finish);
        discardBtn = (Button)findViewById(R.id.todonew_discard);
        
        // 设置按钮的各个Listener
        beginDateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(BEGIN_DATE);
			}
        });
        endDateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(END_DATE);
			}
        });
        beginTimeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(BEGIN_TIME);
			}
        });
        endTimeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(END_TIME);
			}
        });
        alertTimeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(ALERT_TIME);
			}
        });
        discardBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setResult(RESULT_CANCELED, null);
				finish();
			}
        });
        
        // 初始化时间，并更新按钮上的文字
        Calendar ca = Time.getInstance();
        beginYear=endYear=ca.get(Calendar.YEAR);
        beginMonth=endMonth=ca.get(Calendar.MONTH);
        beginDay=endDay=ca.get(Calendar.DAY_OF_MONTH);
        // 这里比较重要，如果不是HOUR_OF_DAY只是DAY的话，就无法区别12时制和24时制了
        beginHour=endHour=ca.get(Calendar.HOUR_OF_DAY); 
        beginMinute=endMinute=ca.get(Calendar.MINUTE);
        
        // 初始化Spinner
        warnFreqList = new ArrayList<String>();
        for(int i = 0; i < Strings.todoNew_spinner_warnFreq.length; i++) {
        	warnFreqList.add(Strings.todoNew_spinner_warnFreq[i]);
        }

        ArrayAdapter<String> warnFreqAdapter = 
        	new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, warnFreqList);
        warnFreqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        warnFreqSpinner.setAdapter(warnFreqAdapter);
        
        // 此处提供子类更改初始值的机会
        init();
	}
	
	/** 在此函数中初始化各种值 */
	protected void init() {
        finishBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent result = new Intent();
				data2Intent(result, true);
	    		// 设置结果
	    		setResult(RESULT_OK, result); 
	    		finish();
			}
        });
        
		beginDateBtn.setText(Time.getDateString(beginYear, beginMonth, beginDay));
        endDateBtn.setText(Time.getDateString(endYear, endMonth, endDay));
		beginTimeBtn.setText(Time.getTimeString(beginHour, beginMinute));
        endTimeBtn.setText(Time.getTimeString(endHour, endMinute));
        alertTimeBtn.setText(Time.getTimeString(alertHour, alertMinute, "HH小时mm分钟"));
	}
	
	protected void data2Intent(Intent result, boolean putCrtTime) {
		if(putCrtTime) {
			long crtTime = System.currentTimeMillis();
			Log.i("new crtTime", String.valueOf(crtTime));
			result.putExtra(Todolist.CRT_TIME, crtTime);
		}
		
		String title = titleEdit.getText().toString();
		String content = contentEdit.getText().toString();
		int importance = ((int)importanceRB.getRating());
		String beginDate = Time.getDateString(beginYear, beginMonth, beginDay);
		String beginTime = Time.getTimeString(beginHour, beginMinute);
		String endDate = Time.getDateString(endYear, endMonth, endDay);
		String endTime = Time.getTimeString(endHour, endMinute);
		String warnFreq = warnFreqList.get(warnFreqSpinner.getSelectedItemPosition());
		String tag = tagEdit.getText().toString();
		// 如果没有标签就将标签赋值为默认
		if(tag.equals("")) tag = Strings.tag_def;
		
		// 计算alertTime
		long alertTime = Time.getAlertTime(alertHour, alertMinute);
		
		Calendar ca = Calendar.getInstance();
		ca.set(beginYear, beginMonth, beginDay, beginHour , beginMinute, 60);
		long beginTimeLong = ca.getTimeInMillis();
		
		ca.set(endYear, endMonth, endDay, endHour, endMinute, 60);
		long endTimeLong = ca.getTimeInMillis();
		
		result.putExtra(Todolist.TITLE, title);
		result.putExtra(Todolist.CONTENT, content);
		result.putExtra(Todolist.IMPORTANCE, importance);
		result.putExtra(Todolist.BEGIN_DATE, beginDate);
		result.putExtra(Todolist.BEGIN_TIME, beginTime);
		result.putExtra(Todolist.BEGIN_TIME_LONG, beginTimeLong);
		result.putExtra(Todolist.END_DATE, endDate);
		result.putExtra(Todolist.END_TIME, endTime);
		result.putExtra(Todolist.END_TIME_LONG, endTimeLong);
		result.putExtra(Todolist.ALERT_TIME, alertTime);
		result.putExtra(Todolist.WARN_FREQUENCY, warnFreq);
		result.putExtra(Todolist.TAG, tag);
	}
	
	// 四个时间拾取对话框的ID
	protected static final int BEGIN_DATE = 0;
	protected static final int BEGIN_TIME = 1;
	protected static final int END_DATE = 2;
	protected static final int END_TIME = 3;
	protected static final int ALERT_TIME = 4;
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case BEGIN_DATE:
			return new DatePickerDialog(this, beginDateSetListener, beginYear, beginMonth, beginDay);
		case END_DATE:
			return new DatePickerDialog(this, endDateSetListener, endYear, endMonth, endDay);
		case BEGIN_TIME:
			return new TimePickerDialog(this, beginTimeSetListener, beginHour, beginMinute, true);
		case END_TIME:
			return new TimePickerDialog(this, endTimeSetListener, endHour, endMinute, true);
		case ALERT_TIME:
			return new TimePickerDialog(this, alertTimeSetListener, alertHour, alertMinute, true);
		}
		return null;
	}
	@Override
    protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case BEGIN_DATE:
			((DatePickerDialog) dialog).updateDate(beginYear, beginMonth, beginDay); break;
		case END_DATE:
			((DatePickerDialog) dialog).updateDate(endYear, endMonth, endDay); break;
		case BEGIN_TIME:
			((TimePickerDialog) dialog).updateTime(beginHour, beginMinute); break;
		case END_TIME:
			((TimePickerDialog) dialog).updateTime(endHour, endMinute); break;
		case ALERT_TIME:
			((TimePickerDialog) dialog).updateTime(alertHour, alertMinute); break;
		}
    }    
	
	protected DatePickerDialog.OnDateSetListener beginDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			beginYear = year;
			beginMonth = monthOfYear;
			beginDay = dayOfMonth;
			
			// 比较开始时间和结束时间，开始时间不能比结束时间还大，根据开始时间实时调整结束时间
			if(beginYear > endYear){
				endYear = beginYear;
			}else if(beginYear == endYear) {
				if(beginMonth > endMonth) {
					endMonth = beginMonth;
				}else if(beginMonth == endMonth) {
					if(beginDay > endDay) {
						endDay = beginDay;
					}
				}
			}
			
			beginDateBtn.setText(Time.getDateString(beginYear, beginMonth, beginDay));
			endDateBtn.setText(Time.getDateString(endYear, endMonth, endDay));
		}
	};
	protected DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			
			if(year > beginYear) {
				endYear = year;
				endMonth = monthOfYear;
				endDay = dayOfMonth;
			}else {
				endYear = beginYear;
				if(monthOfYear > beginMonth) {
					endMonth = monthOfYear;
					endDay = dayOfMonth;
				}else {
					endMonth = beginMonth;
					if(dayOfMonth > beginDay) {
						endDay = dayOfMonth;
					}else {
						endDay = beginDay;
					}
				}
			}
						
			endDateBtn.setText(Time.getDateString(endYear, endMonth, endDay));
		}
	};
	protected TimePickerDialog.OnTimeSetListener beginTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			
			beginHour = hourOfDay;
			beginMinute = minute;
			
			if(beginHour > endHour) {
				endHour = beginHour;
			}else if(beginHour == endHour) {
				if(beginMinute > endMinute) {
					endMinute = beginMinute;
				}
			}
			
			
			beginTimeBtn.setText(Time.getTimeString(beginHour, beginMinute));
			endTimeBtn.setText(Time.getTimeString(endHour, endMinute));
		}
	};
	protected TimePickerDialog.OnTimeSetListener endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	
			if(hourOfDay > beginHour) {
				endHour = hourOfDay;
				endMinute = minute;
			}else {
				endHour = beginHour;
				if(minute > beginMinute)
					endMinute = minute;
				else {
					endMinute = beginMinute;
				}
			}
			
			endTimeBtn.setText(Time.getTimeString(endHour, endMinute));
		}
	};
	protected TimePickerDialog.OnTimeSetListener alertTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			alertHour = hourOfDay;
			alertMinute = minute;
			alertTimeBtn.setText(Time.getTimeString(alertHour, alertMinute));
		}
	};
}
