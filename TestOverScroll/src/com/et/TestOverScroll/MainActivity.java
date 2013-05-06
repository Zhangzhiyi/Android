package com.et.TestOverScroll;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.et.CustomListView.CustomListView;

public class MainActivity extends Activity {
	
//	private ListView mListView;
	private CustomListView mListView;
	private LinearLayout mLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
//        mListView = (ListView) findViewById(R.id.listView1);
        mLayout = (LinearLayout) findViewById(R.id.linearLayout1);
        mListView = new CustomListView(this);
        mLayout.addView(mListView);
        mListView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
      //生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> listItem 
        	= new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<20;i++)
        {
        	HashMap<String, Object> map = new HashMap<String, Object>();
        	map.put("ItemImage", R.drawable.checked);//图像资源的ID
        	map.put("ItemTitle", "Level "+i);
        	map.put("ItemText", "Finished in 1 Min 54 Secs, 70 Moves! ");
        	listItem.add(map);
        }
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter mAdapter = new SimpleAdapter(this,listItem,//数据源 
            R.layout.list_items,//ListItem的XML实现
            //动态数组与ImageItem对应的子项        
            new String[] {"ItemImage","ItemTitle", "ItemText"}, 
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID
            new int[] {R.id.ItemImage,R.id.ItemTitle,R.id.ItemText}
        );
        
        mListView.setAdapter(mAdapter);
        
        
        
        
    }
}