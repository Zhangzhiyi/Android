package com.et.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.ViewFlipper;

import com.baoyz.widget.PullRefreshLayout;
import com.baoyz.widget.PullRefreshLayout.OnRefreshListener;

public class TestListView extends Activity implements OnTouchListener, OnGestureListener{
	private Button mButton;
	private ListView mListView;
	private LinearLayout headView;
	private ViewFlipper mFlipper;
	//手势检测
	private GestureDetector mGestureDetector;
	//滑动像素
	public static final int FLING_MIN_DISTANCE = 30;
	public static final int FLING_VELOCITY_X = 30;
	
	int mFirstVisibleItem = 0;
	int mFirstVisibleTop = 0;
	Handler handler;
	
	private LinearLayout popupMenu1;
	private LinearLayout popupMenu2;
	
	private PullRefreshLayout mPullRefreshLayout;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mButton = (Button) findViewById(R.id.button1);
        
        handler = new Handler(){
        	public void handleMessage(android.os.Message msg) {
        		
        	};
        };
        mGestureDetector = new GestureDetector(this);
        popupMenu1 = (LinearLayout) findViewById(R.id.popupmenu1);
        popupMenu2 = (LinearLayout) findViewById(R.id.popupmenu2);
        //绑定Layout里面的ListView
        mListView = (ListView) findViewById(R.id.ListView01);
        headView = (LinearLayout) inflater.inflate(R.layout.imageviewfilp, null);
        mFlipper = (ViewFlipper) headView.findViewById(R.id.viewFlipper1);
//        headView.setOnTouchListener(this);
        headView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Log.i("TestListView", "MotionEvent.ACTION_DOWN");
					return true;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					Log.i("TestListView", "MotionEvent.ACTION_UP");
					return false;
				}
				return false;
			}
		});
        mFlipper.setDisplayedChild(0);
//        mListView.addHeaderView(headView, null, false);
//        mListView.setEnabled(false);
        //生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<40;i++)
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
        //去掉横线
        mListView.setDividerHeight(0);
        //添加并且显示
        mListView.setAdapter(listItemAdapter);
        
        //添加点击
        mListView.setOnItemClickListener(new OnItemClickListener() {

			 
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				setTitle("点击第"+arg2+"个项目");
			}
		});
        mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				mListView.setSelection(3);//这样写有时没有效果，可以用post()这样写来解决
				mListView.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						mListView.setSelection(3);
//						mListView.smoothScrollToPosition(12);
					}
				});
				//mListView.setSelectionFromTop(3, 30);
				//FooterView可以删除再添加，HeadView不可以
//				mListView.removeFooterView(headView);
				
			}
		});
      //添加长按点击
        mListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			 
			public void onCreateContextMenu(ContextMenu menu, View v,
											ContextMenuInfo menuInfo) {
				menu.setHeaderTitle("长按菜单-ContextMenu");   
				menu.add(0, 0, 0, "弹出长按菜单0");
				menu.add(0, 1, 0, "弹出长按菜单1");   
			}
		}); 
        mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == SCROLL_STATE_IDLE) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
//							mListView.smoothScrollToPositionFromTop(mFirstVisibleItem, 0);
//							mListView.smoothScrollToPosition(mFirstVisibleItem);
						}
					});
					
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
//				if (mFirstVisibleItem == firstVisibleItem) {
//					if (mListView.getChildAt(0) != null) {
//						if (mFirstVisibleTop - mListView.getChildAt(0).getTop() < 0) {
//							toastDown();
//						}else if(mFirstVisibleTop - mListView.getChildAt(0).getTop() > 0){
//							toastUp();
//						}
//					}
//				}else{
//					if (mFirstVisibleItem - firstVisibleItem < 0) {
//						toastUp();
//					}else if (mFirstVisibleItem - firstVisibleItem > 0){
//						toastDown();
//					}
//				}
//				mFirstVisibleItem = firstVisibleItem;
//				if (mListView.getChildAt(0) != null) {
//					mFirstVisibleTop = mListView.getChildAt(0).getTop();
//				}
				/**firstVisibleItem表示在现时屏幕第一个ListItem(部分显示的ListItem也算)
				 在整个ListView的位置（下标从0开始）**/
				Log.i("firstVisibleItem", String.valueOf(firstVisibleItem));
				/**visibleItemCount表示在现时屏幕可以见到的ListItem(部分显示的ListItem也算)总数**/
				Log.i("visibleItemCount", String.valueOf(visibleItemCount));
				/**totalItemCount表示ListView的ListItem总数**/
				Log.i("totalItemCount", String.valueOf(totalItemCount));
				
				/**listView.getFirstVisiblePosition()表示在现时屏幕第一个ListItem(第一个ListItem部分显示也算)
				 * 在整个ListView的位置（下标从0开始）**/
				Log.i("firstPosition", String.valueOf(mListView.getFirstVisiblePosition()));
				/**listView.getLastVisiblePosition()表示在现时屏幕最后一个ListItem(最后ListItem要完全显示出来才算)
				 * 在整个ListView的位置（下标从0开始）**/				
				Log.i("lasPosition", String.valueOf(mListView.getLastVisiblePosition()));
				
				Log.i("mListView.getScrollY()", String.valueOf(mListView.getScrollY()));
				if (mListView.getChildAt(0) != null) {
					Log.i("mListView.getChildAt(0).getTop()", String.valueOf(mListView.getChildAt(0).getTop()));
				}
			}
		});
        
        mPullRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mPullRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				mPullRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    	mPullRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
			}
		});
    }
	
	//长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		setTitle("点击了长按菜单里面的第"+item.getItemId()+"个项目"); 
		return super.onContextItemSelected(item);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		//Log.i("MotionEvent", "" + event.getAction());
		mGestureDetector.onTouchEvent(event);
		return true;
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub 
		Log.i("onFling", "onFling");
		if ((e1.getX() - e2.getX()) > FLING_MIN_DISTANCE) {
			mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
			mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
			if (mFlipper.getDisplayedChild() != mFlipper.getChildCount() - 1) {
				Log.i("fling left", "fling left");
				mFlipper.showNext();
			}
		}else if((e2.getX() - e1.getX()) > FLING_MIN_DISTANCE){
			mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
			mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
			if (mFlipper.getDisplayedChild() != 0) {
				Log.i("fling right", "fling right");
				mFlipper.showPrevious();
			}
		}
		return false;
	}
	
	public void toastUp(){
//		Toast.makeText(this, "向上滚动", Toast.LENGTH_SHORT).show();
		Log.i("向上滚动", "向上滚动");
		if (popupMenu1.getVisibility() == View.VISIBLE) {
			Animation leftOut = AnimationUtils.loadAnimation(this, R.anim.push_left_out);
			popupMenu1.startAnimation(leftOut);
			popupMenu1.setVisibility(View.GONE);
		}
		
		if (popupMenu2.getVisibility() == View.VISIBLE) {
			Animation rightIn = AnimationUtils.loadAnimation(this, R.anim.push_right_out);
			popupMenu2.startAnimation(rightIn);
			popupMenu2.setVisibility(View.GONE);
		}
	}
	public void toastDown(){
//		Toast.makeText(this, "向下滚动", Toast.LENGTH_SHORT).show();
		Log.i("向下滚动", "向下滚动");
		if (popupMenu1.getVisibility() == View.GONE) {
			Animation leftIn = AnimationUtils.loadAnimation(this, R.anim.push_left_in);
			popupMenu1.startAnimation(leftIn);
			popupMenu1.setVisibility(View.VISIBLE);
		}
		
		if (popupMenu2.getVisibility() == View.GONE) {
			Animation rightIn = AnimationUtils.loadAnimation(this, R.anim.push_right_in);
			popupMenu2.startAnimation(rightIn);
			popupMenu2.setVisibility(View.VISIBLE);
		}
		
	}
}