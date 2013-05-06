package com.fengjian.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DragListActivity extends Activity {
    
    private static List<String> list = null;
    private DragListAdapter adapter = null;
    
    public static List<String> groupKey= new ArrayList<String>();
    private List<String> navList = new ArrayList<String>();
    private List<String> moreList = new ArrayList<String>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drag_list_activity);
        
        initData();
        
        DragListView dragListView = (DragListView)findViewById(R.id.drag_list);
        adapter = new DragListAdapter(this, list);
        dragListView.setAdapter(adapter);
    }
    
    public void initData(){
        //数据结果
        list = new ArrayList<String>();
        
        //groupKey存放的是分组标签
        groupKey.add("A组");
        groupKey.add("B组");
        
        for(int i=0; i<5; i++){
            navList.add("A选项"+i);
        }
        list.add("A组");
        list.addAll(navList);
        
        for(int i=0; i<8; i++){
            moreList.add("B选项"+i);
        }
        list.add("B组");
        list.addAll(moreList);
    }
    
    public static class DragListAdapter extends ArrayAdapter<String>{

        public DragListAdapter(Context context, List<String> objects) {
            super(context, 0, objects);
        }
        
        public List<String> getList(){
            return list;
        }
        
        @Override
        public boolean isEnabled(int position) {
            if(groupKey.contains(getItem(position))){
                //如果是分组标签，返回false，不能选中，不能点击
                return false;
            }
            return super.isEnabled(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            
            View view = convertView;
            if(groupKey.contains(getItem(position))){
                //如果是分组标签，就加载分组标签的布局文件，两个布局文件显示效果不同
                view = LayoutInflater.from(getContext()).inflate(R.layout.drag_list_item_tag, null);
            }else{
                //如果是正常数据项标签，就加在正常数据项的布局文件
                view = LayoutInflater.from(getContext()).inflate(R.layout.drag_list_item, null);
            }
            
            TextView textView = (TextView)view.findViewById(R.id.drag_list_item_text);
            textView.setText(getItem(position));
            
            return view;
        }
    }
}