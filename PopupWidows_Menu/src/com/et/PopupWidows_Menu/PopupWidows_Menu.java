package com.et.PopupWidows_Menu;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PopupWidows_Menu extends Activity {
    /** Called when the activity is first created. */
	
	private View mview;
	private PopupWindow mpop;
	private ListView mlist;
	private ArrayList<HashMap<String,String>> moption;
	private SimpleAdapter madapter;
	private boolean flag = false;
	private Button button;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mview = inflater.inflate(R.layout.menu, null);
        button = (Button) findViewById(R.id.Button01);
        
        mpop = new PopupWindow(mview, 500,200,true);
        mpop.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        mpop.setOutsideTouchable(true);
        mlist = (ListView) mview.findViewById(R.id.mList);
        
       
        initialMenu();
        String[] from = {"Option"};   
        int[] to = {android.R.id.text1};   
        madapter = new SimpleAdapter(this, moption, android.R.layout.simple_list_item_1, from,to);   
           
        mlist.setAdapter(madapter);  
        mlist.setOnItemClickListener(new OnItemClickListener(){   
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,   
                    long arg3) {   
                // TODO Auto-generated method stub  
            	
                doMenu(arg2);                  
                mpop.dismiss();   
            }   
               
        });  
        /***PopupWindow显示时焦点在ListView上,所以要监听ListView的键盘点击事件来取消PopupWindow**/
        mlist.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(keyCode == KeyEvent.KEYCODE_MENU&&mpop.isShowing()&&!flag){
					mpop.dismiss();
					flag = false;
				}
				flag = false;
				Log.i("111111", "2222222");
				return false;
			}
		});
        /***这个是重点:监听点击PopuWindow外面取消PopuWindow**/
        mpop.getContentView().setOnTouchListener(new OnTouchListener() {		
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_UP) {
					mpop.dismiss();
					flag = false;
				}
				return false;
			}
		});
        
    }
	
	public boolean onCreateOptionsMenu(Menu menu) { 
		if(!flag){
			
			mpop.showAtLocation(findViewById(R.id.mainlayout), Gravity.BOTTOM, 0, 0);
			flag = true;
		}
		

		return false;   
    }  

	public void initialMenu(){   
        moption = new ArrayList<HashMap<String,String>>();   
           
        HashMap<String,String> map1 = new HashMap<String,String>();   
        map1.put("Option", "新增");   
        moption.add(map1);   
           
        HashMap<String,String> map2 = new HashMap<String,String>();   
        map2.put("Option", "删除");   
        moption.add(map2);   
           
        HashMap<String,String> map3 = new HashMap<String,String>();   
        map3.put("Option", "属性");   
        moption.add(map3);   
           
    }  
	public void doMenu(int i){   
        switch(i){   
        case 0:   
            break;   
           
        case 1:   
            break;   
                   
        }   
           
        Toast.makeText(this, "Menu "+i+" is selected!", Toast.LENGTH_LONG).show();   
    }  
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			
			Log.i("tag", "msg");
			mpop.dismiss();
			return true;
		}
		return false;
	}
	

}