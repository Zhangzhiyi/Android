package com.et.tabwidget;

import java.util.ArrayList;
import java.util.List;

import android.app.TabActivity;
import android.app.LauncherActivity.ListItem;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity implements OnClickListener{
	
	TabHost mTabHost;
	TabSpec firstTabSpec;
	TabSpec secondTabSpec;
	TabSpec threeTabSpec;
	TabWidget mTabWidget;
	private Button categoryBtn;
    private Button stateBtn;
    private Button sortBtn;
    private Button searchBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		//去状态栏  
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		/**另外写个布局将Tab标签放在底部,如果无此需求就不需要setContentView**/
		setContentView(R.layout.menu1);
		
		mTabHost = getTabHost();
		/* 去除标签下方的白线 */
        mTabHost.setPadding(mTabHost.getPaddingLeft(),
                mTabHost.getPaddingTop(), mTabHost.getPaddingRight(),
                mTabHost.getPaddingBottom() - 5);
        
        /**R.layout.menu1tab存放了3个Tab的内容.mTabHost.getTabContentView()获取Tab存放内容的FrameLayout元素
         * (这个FrameLayout是指R.layout.menu1的FrameLayout),true表示是否嵌套在mTabHost.getTabContentView()元素上**/
        LayoutInflater.from(this).inflate(R.layout.menu1tab, mTabHost.getTabContentView(), true);
			
		mTabHost.addTab(mTabHost.newTabSpec("1")// make a new Tab
						.setIndicator("常用查询",getResources().getDrawable(R.drawable.menu1_tab_icon1))						// set the Title and Icon
						.setContent(R.id.Tab1));
		mTabHost.addTab(mTabHost.newTabSpec("2")// make a new Tab
				.setIndicator("业务查询",getResources().getDrawable(R.drawable.menu1_tab_icon2))						// set the Title and Icon
				.setContent(R.id.Tab2));
		mTabHost.addTab(mTabHost.newTabSpec("3")// make a new Tab
				.setIndicator("其他查询",getResources().getDrawable(R.drawable.menu1_tab_icon3))						// set the Title and Icon
				.setContent(R.id.Tab3));
		
        mTabWidget = mTabHost.getTabWidget();
        
        /**发现更改mTabWidget里面的布局不成功,会使整个mTabWidget布局变黑色**/
        for(int i=0;i<mTabWidget.getChildCount();i++){
        	RelativeLayout view = (RelativeLayout) mTabWidget.getChildAt(i);
        	/**更改RelativeLayout大小会使mTabWidget整个布局变成黑色**/
        	//view.setLayoutParams(new LinearLayout.LayoutParams(0,60));
        	ImageView imageView = (ImageView) view.findViewById(android.R.id.icon);
        	/**ImageView**/
        	imageView.setLayoutParams(new RelativeLayout.LayoutParams(30, 30));
        	imageView.setVisibility(View.GONE);
        }
		
		/**因为各个tab布局的顶部返回按钮id一样,所以分别寻找各个tab布局的顶部imageview设置监听,否则只会监听到第一个tab的按钮* */
		FrameLayout frame = (FrameLayout) findViewById(R.id.tabs);
		for(int i=0;i<frame.getChildCount();i++){
			View view  =frame.getChildAt(i);
			ImageView mImageView = (ImageView) view.findViewById(R.id.iv_back);
			mImageView.setOnClickListener(this);
		}
		
		
		/*ListView mListView01 = (ListView) findViewById(R.id.ListView01);
		String[] tab1Titles = {"金额查询", "实时话费", "历史话费", "详细话费", "积分查询", "账单查询", "其他查询"};
		Bitmap tab1Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.search1);
		List<ListItem> list = new ArrayList<ListItem>();
		for(int i=0;i<tab1Titles.length;i++){
			ListItem item = new ListItem(tab1Titles[i], tab1Bitmap);
			list.add(item);
		}
		mListView01.setAdapter(new ListViewAdapter(this,list));*/
		
		categoryBtn = (Button) findViewById(R.id.todo_sort_categorybtn);
		stateBtn = (Button) findViewById(R.id.todo_sort_statebtn);
		sortBtn = (Button) findViewById(R.id.todo_sort_sortbtn);
		searchBtn = (Button) findViewById(R.id.todo_sort_searchbtn);
		
		/**监听底部按钮,动态设置标签**/
		categoryBtn.setOnClickListener(this);
		stateBtn.setOnClickListener(this);
		sortBtn.setOnClickListener(this);
		searchBtn.setOnClickListener(this);
		
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.iv_back:
				Log.i("111111111111111", "11111111111111111");
				break;
			case R.id.todo_sort_categorybtn:
				this.mTabHost.setCurrentTabByTag("1");
				
				break;
			case R.id.todo_sort_statebtn:
				this.mTabHost.setCurrentTabByTag("2");
				break;
			case R.id.todo_sort_sortbtn:
				this.mTabHost.setCurrentTabByTag("3");
				break;
		}
	}
}
