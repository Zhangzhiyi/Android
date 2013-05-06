package com.et.main;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.et.CustomBoundsListView.CustomBoundsListView;


public class MainActivity extends Activity {
	
	private CustomBoundsListView customListView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        customListView = (CustomBoundsListView) findViewById(R.id.listview);
        
      //生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> listItem 
        	= new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<10;i++)
        {
        	HashMap<String, Object> map = new HashMap<String, Object>();
        	map.put("ItemImage", R.drawable.checked);//图像资源的ID
        	map.put("ItemTitle", "Level "+i);
        	map.put("ItemText", "Finished in 1 Min 54 Secs, 70 Moves! ");
        	listItem.add(map);
        }
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源 
            R.layout.list_items,//ListItem的XML实现
            //动态数组与ImageItem对应的子项        
            new String[] {"ItemImage","ItemTitle", "ItemText"}, 
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID
            new int[] {R.id.ItemImage,R.id.ItemTitle,R.id.ItemText}
        );
        
        customListView.setAdapter(listItemAdapter);
    }
}