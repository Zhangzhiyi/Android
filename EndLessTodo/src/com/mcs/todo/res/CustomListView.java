package com.mcs.todo.res;

import java.util.ArrayList;
import java.util.List;

import com.mcs.todo.NoteActivity;
import com.mcs.todo.R;
import com.mcs.todo.NoteActivity.Entry;
import com.mcs.todo.db.Notecategory;
import com.mcs.todo.db.Notelist;
import com.mcs.todo.db.Todolist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;

import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

public class CustomListView extends ListView{
	private Context context;
	public static View moveView;
	public View itemView;
	public boolean moveFlag;
	public static Entry moveEntry;
	public static int movePosition;
	public static long moveId;
	public TextView text; 
	public static ArrayList<Coordinates> listCoordinates;
	public static  List<Rect> listRect ;
	public static RelativeLayout headView;
	
	public CustomListView(Context context) {
		super(context);
		this.context = context;
		
	}
	/** 继续系统的widget的自定义View一定要加这个构造函数，否则运行会出错**/
	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int rawX = (int) ev.getRawX();
		int rawY = (int) ev.getRawY();
		Log.i("X", String.valueOf(rawX));
		Log.i("Y", String.valueOf(rawY));
		int counts = getHeaderViewsCount();
		Log.i("counts", String.valueOf(counts));
				
		setCoordinates();
							
		if(moveFlag){
			switch(ev.getAction()){
				/***实际上ACTION_DOWN已经没有触发运行了***/
				case MotionEvent.ACTION_DOWN:
					Log.i(getClass().toString(),"action_dowm");					
					/**top也是为0，why？   原来此时moveView还没有加入布局当中**/
					Log.i("down_top", String.valueOf(moveView.getTop()));					
					WindowManager.LayoutParams localLayoutParams1 = new WindowManager.LayoutParams();
					localLayoutParams1.x = 0;
					localLayoutParams1.y = rawY-50;
					localLayoutParams1.alpha = 100;
					localLayoutParams1.width = LayoutParams.FILL_PARENT;
					localLayoutParams1.height = LayoutParams.WRAP_CONTENT;										
					if(itemView!=null){								
						Log.i("LayoutParams.FILL_PARENT", String.valueOf(LayoutParams.FILL_PARENT));
						Log.i("LayoutParams.WRAP_CONTENT", String.valueOf(LayoutParams.WRAP_CONTENT));
						/** 不明白为什么在这里的两个参数的值为0，难怪之前上面LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT两个参数为
						 * moveView.getWidth()和moveView.getHeight()一直没有显示加入的moveView**/
						Log.i("width",String.valueOf(moveView.getWidth()));
						Log.i("height",String.valueOf(moveView.getHeight()));
						NoteActivity.lp.addView(moveView, localLayoutParams1);
					}						
					break;
				case MotionEvent.ACTION_MOVE:
					Log.i(getClass().toString(),"action_move");
					/** 可能是用WindowManager加入View到屏幕，下面四个参数不会变化，分别是0，0，320，39**/
					Log.i("top", String.valueOf(moveView.getTop()));
					Log.i("left", String.valueOf(moveView.getLeft()));
					Log.i("right", String.valueOf(moveView.getRight()));
					Log.i("bottom", String.valueOf(moveView.getBottom()));
					WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
					localLayoutParams.x = 0;
					/**不明白为什么y=0时moveView在y=240纵坐标处**/
					localLayoutParams.y = rawY-240;
					localLayoutParams.alpha = 100;
					localLayoutParams.width = LayoutParams.FILL_PARENT;
					localLayoutParams.height = LayoutParams.WRAP_CONTENT;					
					if(itemView!=null){
							
						NoteActivity.lp.updateViewLayout(moveView, localLayoutParams);
						/**在这里两个参数又有值~  **/
						Log.i("width",String.valueOf(moveView.getWidth()));
						Log.i("height",String.valueOf(moveView.getHeight()));
					}
					
					break;
				case MotionEvent.ACTION_UP:
					Log.i(getClass().toString(),"action_up");
					/** 可能是用WindowManager加入View到屏幕，下面四个参数不会变化，分别是0，0，320，39**/
					Log.i("top", String.valueOf(moveView.getTop()));
					Log.i("left", String.valueOf(moveView.getLeft()));
					Log.i("right", String.valueOf(moveView.getRight()));
					Log.i("bottom", String.valueOf(moveView.getBottom()));
					NoteActivity.lp.removeView(moveView);
					int x = (int) ev.getX();
					int y = (int) ev.getY();
					int count = listRect.size();
					boolean flag = false;
					for(int i=0;i<count;i++){
					Rect rect = listRect.get(i);
					if(rect.contains(x, y)){
						flag = true;
						View child = headView.getChildAt(i);
						/**获得View的id,也是标签在表中的id**/
						int id = child.getId();
						ContentValues values = new ContentValues();
						values.put(Notelist.CATEGORY_ID, id);
						NoteActivity.db.update(values, Notelist._ID+" = "+ moveId, null);
						/**更改标签title的数字**/
						Cursor a = NoteActivity.dc.query(null, Notecategory._ID+" = "+id, null, null);
						a.moveToFirst();
						String name = a.getString(1);
						a.close();
						Cursor c = NoteActivity.db.query(null, "categoryId "+" = "+id, null, null);						
						TextView categoryName = (TextView) child.findViewById(R.id.category_title);
						categoryName.setText(name+"("+c.getCount()+")");
						c.close();
						NoteActivity.noteAdapter.refreshList();
						break;
					}							
				}							
				if(!flag){							
					NoteActivity.noteAdapter.addMoveViewPosition(movePosition, moveEntry);							
				}
					
					moveFlag = false;
					setEnabled(true);
					break;
			}
		}
		return super.onTouchEvent(ev);
	}

	public void setCoordinates(){
		/**getChildAt(0) 是获取当前屏幕第一项ListItem**/
		if(getChildAt(0) instanceof RelativeLayout){
			headView = (RelativeLayout) getChildAt(0);
			listCoordinates = new ArrayList<Coordinates>();
			listRect = new ArrayList<Rect>();
			int count = headView.getChildCount();
			Log.i("headview.count", String.valueOf(count));
			for(int i=0;i<count;i++){
				View child = headView.getChildAt(i);
				if (child != null) {
					Coordinates coord = new Coordinates();
					int left = child.getLeft();
					int top = child.getTop();
					int right = child.getRight();
					int bottom = child.getBottom();
					Log.i("top", String.valueOf(top));
					Log.i("left", String.valueOf(left));
					Log.i("right", String.valueOf(right));
					Log.i("bottom", String.valueOf(bottom));
					coord.setTop(top);
					coord.setBottom(bottom);
					coord.setLeft(left);
					coord.setRight(right);
					Rect rect = new Rect(left, top, right, bottom);
					listRect.add(rect);
					listCoordinates.add(coord);
				}
			}
		}
	}
	public int isContainRect(int x,int y){
		int count = listRect.size();
		for(int i=0;i<count;i++){
			Rect rect = listRect.get(i);
			if(rect.contains(x, y));
				return i;			
		}		
		return -1;
		
	}
	class Coordinates {
		
		private int top = 0;
		private int bottom = 0;
		private int left = 0;
		private int right = 0;
		
		public int getLeft() {
			return left;
		}
		public void setLeft(int left) {
			this.left = left;
		}
		public int getRight() {
			return right;
		}
		public void setRight(int right) {
			this.right = right;
		}
		public int getTop() {
			return top;
		}
		public void setTop(int top) {
			this.top = top;
		}
		public int getBottom() {
			return bottom;
		}
		public void setBottom(int bottom) {
			this.bottom = bottom;
		}
		
		
	}
}