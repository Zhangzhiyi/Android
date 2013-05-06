package com.mcs.todo;

import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.FilterQueryProvider;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.mcs.client.android.service.MCSService;
import com.mcs.todo.db.ContentManager;
import com.mcs.todo.db.DBAdapter;
import com.mcs.todo.db.DBConstant;
import com.mcs.todo.db.DBTable;
import com.mcs.todo.db.Todolist;
import com.mcs.todo.db.WidgetSetting;
import com.mcs.todo.res.Strings;
import com.mcs.todo.utils.Tag;
import com.mcs.todo.utils.Time;

public class Main extends Activity implements ViewSwitcher.ViewFactory{
    
    // 滑动界面相关
    private ViewSwitcher switcher;
    private GestureDetector gestureDetector;
    
    // ViewFlipper所用的动画
    private Animation leftIn;
    private Animation rightOut;
    private Animation rightIn;
    private Animation leftOut;
    
    // 顶部infobar的View
    private TextView infoText;
    private Button dateBtn;
    private Button tagBtn;
    private Button addBtn;
    
    
    // 底部的几个Button
    private Button categoryBtn;
    private Button stateBtn;
    private Button sortBtn;
    private Button searchBtn;

    // 数据库Adapter
    //private DBAdapter db;
    private ContentManager db;
    
    // SimpleCursorTreeAdapter构造函数的参数
    private String[] groupProjection = {
    		Todolist._ID,
    		Todolist.BEGIN_DATE,
    		Todolist.BEGIN_TIME, 
    		Todolist.BEGIN_TIME_LONG, // 用于计算和比较
    		Todolist.END_TIME_LONG,
    		Todolist.ALERT_TIME,
    		Todolist.TITLE, 
    		Todolist.STATE, 
    		Todolist.IMPORTANCE
    };
    //	有关提醒的服务和变量
    private AlarmManager am;
    private int alarmId;
    private String alarmTitle;
    private String alarmContent;
    private long alarmBeginTimeLong;
    private long alarmAlertTime;
    private String alarmWarnFreq;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	// 去掉标题栏，一定要在setContentView之前调用
    	requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	setContentView(R.layout.main);
    	
    	// 一定要在使用Tag之前初始化
    	Tag.init(this);
    	
    	//获取闹钟提醒服务
    	am = (AlarmManager) getSystemService(ALARM_SERVICE);
    	
    	// 初始化对象赋值
    	infoText = (TextView)findViewById(R.id.todo_infotext);
    	dateBtn = (Button)findViewById(R.id.todo_sort_datebtn);
    	tagBtn = (Button)findViewById(R.id.todo_sort_tagbtn);
    	addBtn = (Button) findViewById(R.id.todo_add);
    	categoryBtn = (Button)findViewById(R.id.todo_sort_categorybtn);
    	stateBtn = (Button)findViewById(R.id.todo_sort_statebtn);
    	sortBtn = (Button)findViewById(R.id.todo_sort_sortbtn);
    	searchBtn = (Button) findViewById(R.id.todo_sort_searchbtn);
    	switcher = (ViewSwitcher)findViewById(R.id.todo_expandlist_switcher);
    	gestureDetector = new GestureDetector(new MyGestureListener());
    	
    	// 初始化数据库
    	//db = new DBAdapter(this, DBConstant.TABLE_TODOLIST);
    	db = new ContentManager(this, DBConstant.TABLE_TODOLIST);
    	
    	// 必须在初始化数据库之后，因为makeView用到db了
    	switcher.setFactory(this);
    	
    	// 初始化顶部的infobar
    	changeInfoBar(DISPLAY_DATE);
    	//updateInfoButton();
    	updateInfoDate();
    	
    	// 初始化屏幕底部的Button
    	categoryBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(DIALOG_CATEGORY);
			}
    	});
    	stateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(DIALOG_STATE);
			}
    	});
    	sortBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(DIALOG_SORT);
			}
    	});
    	dateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, Month.class);
				// 等着返回选定的日期
				startActivityForResult(intent, REQUEST_CODE_MONTH);
			}
    	});
    	tagBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(DIALOG_TAG);
			}
    	});
    	addBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent newItem = new Intent(Main.this, New.class);
	        	startActivityForResult(newItem, REQUEST_CODE_NEW);
			}
		});
    	searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSearchRequested();
			}
		});
    	
    	// 根据启动时附带的信息来改变初始的窗口模式
    	Intent intent = getIntent();
    
		MyExpandableList view = (MyExpandableList)switcher.getCurrentView();
		if (null != intent) {
			Log.i("notification", "notification");
			if (intent.getAction().equals(Strings.SEARCH_TODO_ACTION)) {
				categoryQuery = Todolist.BEGIN_DATE;
				changeInfoBar(DISPLAY_DATE);
				// updateInfoButton();
				updateInfoDate();
				doFilter();
			} 
			else {
				/** 加上action以便区分是search还是widget传过来的Intent，否则会出错 **/
				if (intent.getAction().equals(Strings.WIDGET_DISPLAY_ACTION)) {
					int mode = intent.getIntExtra(WidgetSetting.MODE,
							WidgetSetting.MODE_DATE);
					switch (mode) {
					case WidgetSetting.MODE_DATE:
						categoryQuery = Todolist.BEGIN_DATE;
						changeInfoBar(DISPLAY_DATE);
						// updateInfoButton();
						/** 设置时间为现在的系统时间 **/
						Time.setToday();
						updateInfoDate();
						doFilter();
						break;
					case WidgetSetting.MODE_TAG:
						categoryQuery = Todolist.TAG;
						String tag = intent.getStringExtra(Todolist.TAG);
						Tag.getInstance().reloadTag();
						Tag.getInstance().setCurrentTag(tag);
						changeInfoBar(DISPLAY_TAG);
						// updateInfoButton();
						updateInfoCategory();
						doFilter();
						break;
					}
				} else {
					/** 接收用户调用search点击了suggestion传过来的Intent **/
					if (intent.getAction().equals("android.intent.action.VIEW")) {
						Uri uri = intent.getData();
						Log.i("MANIN URI", uri.toString());
						/**
						 * 因为传过来的URI是带有ID的，且类MCSProvider没有匹配URI的方法
						 * 所以不能直接查询，会出错，要重新构造一个URI
						 **/
						List<String> list = uri.getPathSegments();
						/** 获取表名 **/
						String tableName = list.get(0);
						/** 获取ID **/
						long id = Long.parseLong(list.get(1));
						/** 重新构造URI **/
						uri = Uri.parse("content://" + DBConstant.AUTHORITY
								+ "/" + tableName);
						Cursor c = managedQuery(uri, null, DBTable._ID + " = "
								+ id, null, null);
						startManagingCursor(c);
						if (c != null) {
							c.moveToFirst();
							int columnIndex = c
									.getColumnIndex(Todolist.BEGIN_TIME_LONG);
							long millis = c.getLong(columnIndex);
							Time.setCurrentDate(millis);
							categoryQuery = Todolist.BEGIN_DATE;
							changeInfoBar(DISPLAY_DATE);
							updateInfoDate();
							doFilter();
						}
					} else {
						/**按下搜索框的搜索按钮**/
						if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
							String query = intent
									.getStringExtra(SearchManager.QUERY);
							/** 跳转到SearchActivity显示搜索结果 **/
							Intent result = new Intent(Main.this,
									SearchActivity.class);
							result.putExtra("query", query);
							startActivity(result);
						}
					}
				}
			}
    		view.doFilter();
    		
        	// 打开当前显示的那一项
    		TodoAdapter adapter = (TodoAdapter)view.getExpandableListAdapter();
        	long itemId = intent.getLongExtra(Todolist._ID, -1);
        	if(itemId != -1) {
        		for(int i = 0; i < adapter.getGroupCount(); i++) {
        			long id = adapter.getGroupId(i);
        			if(itemId == id) {
        				view.expandGroup(i);
        			}
        		}
        	}
    	}
    	else {
    		view.doFilter();
    	}
   
    	registerForContextMenu(switcher.getCurrentView());
    	registerForContextMenu(switcher.getNextView());
    }
    
   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	super.onKeyDown(keyCode, event);
    	if(event.getKeyCode() == KeyEvent.KEYCODE_SEARCH)
    	{
    		Intent search = new Intent(Main.this,SearchActivity.class);
    		//startActivityForResult(search,REQUEST_CODE_SEARCH);
    		startActivity(search);
        	return true;
    	}
    	
    	return false;  	
    }*/
    
    /** 监听用户是否左右滑动，在现实全部的模式下不需要滑动，要注销这个listener */
    private OnTouchListener expandListTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(gestureDetector.onTouchEvent(event))
	    		return true;
	    	else 
	    		return false;
		}
	};
	
	@Override
	public View makeView() {
		return getMyExpandableList();
	}
    
    /** 返回一个设置好的ExpandableListView */
    protected MyExpandableList getMyExpandableList() {
    	MyExpandableList e = new MyExpandableList(Main.this);
    	e.setLayoutParams(new ViewSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    	e.setCacheColorHint(Color.WHITE);
    	e.setScrollingCacheEnabled(true);
    	e.setLongClickable(true);
    	e.setOnTouchListener(expandListTouchListener);
    	e.setGroupIndicator(null); // 去掉那个很不可爱的小箭头
    	// 初始化ExpandableList
    	Cursor cur = db.query(groupProjection, null, null, sortQuery);
    	startManagingCursor(cur);
    	TodoAdapter todoAdapter = new TodoAdapter(this, cur);
    	todoAdapter.setFilterQueryProvider(new TodoFilterQueryProvider());
    	e.setAdapter(todoAdapter);
    	return e;
    }
    
    public class MyExpandableList extends ExpandableListView {
		public MyExpandableList(Context context) {
			super(context);
		}
    	
		/** 构建限制字符串，进行过滤操作 */
	    public void doFilter() {
	    	StringBuffer constraint = new StringBuffer();
	    	if(categoryQuery != null) {
	    		constraint.append("category:").append(categoryQuery).append(NL);
	    	}
	    	if(stateQuery != -1) {
	    		constraint.append("state:").append(stateQuery).append(NL);
	    	}
	    	if(sortQuery != null) {
	    		constraint.append("sort:").append(sortQuery).append(NL);
	    	}
	    	// 执行过滤操作
	    	((TodoAdapter)this.getExpandableListAdapter()).getFilter().filter(constraint.toString());
	    	//todoAdapter.notifyDataSetChanged();
	    }
    }
    
    // 动画的方向
    private static final int FLIP_RIGHTIN = 0;
    private static final int FLIP_LEFTIN = 1;
    
    /** 滑动expandList */
    private void flipExpandList(int direction) {
    	int width = switcher.getCurrentView().getWidth();
    	leftIn = new TranslateAnimation(-width, 0, 0, 0);
    	leftIn.setDuration(300);
    	rightOut = new TranslateAnimation(0, width, 0, 0);
    	rightOut.setDuration(300);
    	rightIn = new TranslateAnimation(width, 0, 0, 0);
    	rightIn.setDuration(300);
    	leftOut = new TranslateAnimation(0, -width, 0, 0);
    	leftOut.setDuration(300);
    	switch(direction) {
    	case FLIP_RIGHTIN:
    		switcher.setInAnimation(rightIn);
    		switcher.setOutAnimation(leftOut);
    		break;
    	case FLIP_LEFTIN:
    		switcher.setInAnimation(leftIn);
    		switcher.setOutAnimation(rightOut);
    		break;
    	}
    	((MyExpandableList)switcher.getNextView()).doFilter();
		switcher.showNext();
    }
    
    class MyGestureListener implements GestureDetector.OnGestureListener {
    	
		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if(Math.abs(e2.getX() - e1.getX()) > 100) {
				if(e2.getX() < e1.getX()) { // 从右向左滑动
					if(categoryQuery.equals(Todolist.BEGIN_DATE)) {
						Time.toNextDay();
						changeInfoBar(DISPLAY_DATE); 
						//updateInfoButton();
						updateInfoDate();
					} else if(categoryQuery.equals(Todolist.TAG)) {
						Tag.getInstance().rollToNext();
						changeInfoBar(DISPLAY_TAG);
						//updateInfoButton();
						updateInfoCategory();
					}
					// 更新过InfoBar以后flip，之后再filter
					flipExpandList(FLIP_RIGHTIN);
				}else if(e2.getX() > e1.getX()) { // 从左向右滑动
					if(categoryQuery.equals(Todolist.BEGIN_DATE)) {
						Time.toPreviousDay();
						changeInfoBar(DISPLAY_DATE);
						//updateInfoButton();
						updateInfoDate();
					}else if(categoryQuery.equals(Todolist.TAG)) {
						Tag.getInstance().rollToPrevious();
						changeInfoBar(DISPLAY_TAG);
						//updateInfoButton();
						updateInfoCategory();
					}
					flipExpandList(FLIP_LEFTIN);
				}
				return true;
			}
			return false;
		}
		@Override
		public void onLongPress(MotionEvent e) {
		}
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return false;
		}
		@Override
		public void onShowPress(MotionEvent e) {
		}
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}
    }
    
    // 底部排序按钮弹出的对话框
    private static final int DIALOG_CATEGORY = 0;
    private static final int DIALOG_STATE = 1;
    private static final int DIALOG_SORT = 2;
    private static final int DIALOG_TAG = 3;
    
	// 用局部变量缓存一下设置，以便一切在点击确定时才起作用
	private String categoryTmp = null;
	private int stateTmp = Todolist.STATE_UNDONE;
	private String sortTmp = null;
	private int modeTmp; // 保存显示的模式
	private OnTouchListener listenerTmp;
	
    @Override
    protected Dialog onCreateDialog(int id) {
    	
    	switch(id) {
    	case DIALOG_CATEGORY:
    		return new AlertDialog.Builder(Main.this)
            .setTitle(Strings.todo_categoryDialog_title)
            .setSingleChoiceItems(Strings.todo_categoryDialog_items, 0, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	// 具体参考Strings.todo_categoryDialog_items
                    switch(which) {
                    case 0: // 以日期的方式显示
                    	categoryTmp = Todolist.BEGIN_DATE;
                    	modeTmp = DISPLAY_DATE;
                    	// 在显示日期的模式下需要左右滑动
                    	listenerTmp = expandListTouchListener;
                    	break;         
                    case 1: // 根据标签来显示
                    	categoryTmp = Todolist.TAG;
                    	modeTmp = DISPLAY_TAG;
                    	// 在显示标签的模式下需要左右滑动
                    	listenerTmp = expandListTouchListener;
                    	break;
                    case 2: // 显示全部
                    	categoryTmp = null;
                    	modeTmp = DISPLAY_ALL;
                    	// 在显示全部的模式下不需要左右滑动
                    	listenerTmp = null;
                    	break;
                    }
                }
            })
            .setPositiveButton(Strings.okBtnText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	categoryQuery = categoryTmp;
                	changeInfoBar(modeTmp);
                	switcher.getCurrentView().setOnTouchListener(listenerTmp);
                	// 进行过滤动作
                	doFilter();
                }
            })
            .setNegativeButton(Strings.cancelBtnText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            })
           .create();
    	case DIALOG_STATE:
    		return new AlertDialog.Builder(Main.this)
            .setTitle(Strings.todo_stateDialog_title)
            .setSingleChoiceItems(Strings.todo_stateDialog_items, 0, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // 具体参考Strings.todo_stateDialog_items
                	switch(which) {
                    case 0: // 显示全部
                    	stateTmp = -1; break;
                    case 1: // 显示已经完成的项目
                    	stateTmp = Todolist.STATE_DONE; break;         
                    case 2: // 显示未完成的项目
                    	stateTmp = Todolist.STATE_UNDONE; break;
                	}
                }
            })
            .setPositiveButton(Strings.okBtnText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	stateQuery = stateTmp;
                	doFilter();
                }
            })
            .setNegativeButton(Strings.cancelBtnText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            })
           .create();
    	case DIALOG_SORT:
    		return new AlertDialog.Builder(Main.this)
            .setTitle(Strings.todo_sortDialog_title)
            .setSingleChoiceItems(Strings.todo_sortDialog_items, 0, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	// 具体参考Strings.todo_sortDialog_title
                	switch(which) {
                    case 0: // 按时间升序排列
                    	sortTmp = Todolist.BEGIN_TIME_LONG + " DESC"; break;         
                    case 1: // 根据标签来显示
                    	sortTmp = Todolist.IMPORTANCE + " DESC"; break;
                	}
                }
            })
            .setPositiveButton(Strings.okBtnText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	sortQuery = sortTmp;
                	doFilter();
                }
            })
            .setNegativeButton(Strings.cancelBtnText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            })
           .create();
    	case DIALOG_TAG:
    		// 重新更新Tag
        	Tag.getInstance().reloadTag();
    		final String[] tags = new String[Tag.getInstance().getTagList().size()];
    		Tag.getInstance().getTagList().toArray(tags);
    		return new AlertDialog.Builder(Main.this)
            .setTitle(Strings.todo_tagDialog_title)
            .setSingleChoiceItems(tags, 0, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	// 重设当前标签
                	Tag.getInstance().setCurrentTag(tags[which]);
                	//updateInfoButton();
                	updateInfoCategory();
                }
            })
            .setPositiveButton(Strings.okBtnText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	doFilter();
                }
            })
            .setNegativeButton(Strings.cancelBtnText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            })
           .create();
    	}
		return null;
    }
    
    // 改变顶部标签栏的显示
    public static final int DISPLAY_DATE = 0;
    public static final int DISPLAY_TAG = 1;
    public static final int DISPLAY_ALL = 2;
    
    // 过滤内容需要经过以下三步：
    // 1.changeInfoBar改变顶部InfoBar的布局
    // 2.updateInfoButton更新InfoBar内部信息按钮显示的文字
    // 3.doFilter执行实际的过滤操作
    
    /** 改变顶部InfoBar提示文字以及Button的显示 */
    private void changeInfoBar(int barType) {
    	switch(barType) {
    	case DISPLAY_DATE:
    		dateBtn.setVisibility(View.VISIBLE);
    		tagBtn.setVisibility(View.INVISIBLE);
    		infoText.setText(Time.getCurrentDate());
        	break;
    	case DISPLAY_TAG:
    		dateBtn.setVisibility(View.INVISIBLE);
    		tagBtn.setVisibility(View.VISIBLE);
    		infoText.setText(Tag.getInstance().getCurrentTag());
        	break;
    	case DISPLAY_ALL:
    		dateBtn.setVisibility(View.INVISIBLE);
    		tagBtn.setVisibility(View.VISIBLE);
    		infoText.setText(Strings.todo_infoBarText_all);
    		break;
    	}
    }
    
    /** 更新顶部InfoBar按钮上的文字 */
	private void updateInfoButton() {
		// 反正不该显示的按钮也是隐藏的所以要更新就一起更新吧
		//infoText.setText(Time.getCurrentDate());
		//infoText.setText(Tag.getInstance().getCurrentTag());
	}
	/** 更新顶部InfoBar标签信息 */
	private void updateInfoCategory(){
		infoText.setText(Tag.getInstance().getCurrentTag());
	}
	/** 更新顶部InfoBar标签信息 */
	private void updateInfoDate(){
		infoText.setText(Time.getCurrentDate());
	}
	
    
	/** Windows下的换行符 */
    private static final String NL = "\r\n";
    // 排序查询用的几个String
    private String categoryQuery = Todolist.BEGIN_DATE;
    private int stateQuery = -1; // 显示全部
    private String sortQuery = Todolist.BEGIN_TIME_LONG + " ASC"; // 默认按照时间升序排列
    
    private void doFilter() {
    	((MyExpandableList)switcher.getCurrentView()).doFilter();
    }
    
    // 定义这个FilterQueryProvider来提供各种排序和显示功能
    // 和adapter.getFilter().filter(constraint)配合来完成过滤行为
    class TodoFilterQueryProvider implements FilterQueryProvider {
		@Override
		public Cursor runQuery(CharSequence constraint) {
			String category = null;
			String state = null;
			String sort = null;
			// 参考doFilter函数，进行反向解析
			String[] a = constraint.toString().split(NL);
			for(int i = 0; i < a.length; i++) {
				String[] b = a[i].split(":");
				if(b[0].equals("category")) {
					category = b[1];
				}else if(b[0].equals("state")) {
					state = b[1];
				}else if(b[0].equals("sort")) {
					sort = b[1];
				}
			}
			
			// 构建查询字符串
			StringBuffer where = new StringBuffer();
			if(category != null) {
				if(category.equals(Todolist.BEGIN_DATE)) {
					where.append(category).
						  append("=").
						  append("'"). // 不要忘了在字符串外面加''
						  append(Time.getCurrentDate()). 
						  append("'");
				}else if(category.equals(Todolist.TAG)) {
					where.append(category).
						  append("=").
						  append("'").
						  append(Tag.getInstance().getCurrentTag()).
						  append("'");
				}
			}
			if(state != null) {
				if(category != null)
					where.append(" and ");
				where.append(Todolist.STATE).
					  append("=").
					  append(state);
			}
			
			return db.query(groupProjection, where.toString(), null, sort);
		}
    }
    
    // 菜单项的ID定义
    private static final int MENU_MONTH   = 0;
    private static final int MENU_NEW 	  = 1;
    private static final int MENU_TODAY   = 2;
    private static final int MENU_SYNC 	  = 3;
    private static final int MENU_USER 	  = 4;
    private static final int MENU_SETTING = 5;
    private static final int MENU_SEARCH  = 6;
    private static final int MENU_NOTE    = 7;
    	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MENU_MONTH, 0, Strings.todo_optionsMenu_month);
    	menu.add(0, MENU_NEW, 0, Strings.todo_optionsMenu_new);
    	menu.add(0, MENU_TODAY, 0, Strings.todo_optionsMenu_today);
    	menu.add(0, MENU_SYNC, 0, Strings.todo_optionsMenu_sync);
    	menu.add(0, MENU_USER, 0, Strings.todo_optionsMenu_user);
    	menu.add(0, MENU_SETTING, 0, Strings.todo_optionsMenu_setting);
    	menu.add(0, MENU_NOTE, 0, Strings.todo_optionsMenu_note);
    	return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
    	// 只有在日期显示模式下才显示回到今天菜单项
    	if(categoryQuery != null &&  // 在显示全部的模式下categoryQuery是null的，所以要检查一下
    			categoryQuery.equals(Todolist.BEGIN_DATE))
    		menu.getItem(MENU_TODAY).setVisible(true);
    	else
    		menu.getItem(MENU_TODAY).setVisible(false);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_NEW:
        	Intent newItem = new Intent(this, New.class);
        	startActivityForResult(newItem, REQUEST_CODE_NEW);
            return true;
        case MENU_TODAY: 
        	Time.backToToday();
        	changeInfoBar(DISPLAY_DATE);
        	//updateInfoButton();
        	updateInfoDate();
        	doFilter();
            return true;
        case MENU_MONTH:
        	Intent intent = new Intent(Main.this, Month.class);
			startActivityForResult(intent, REQUEST_CODE_MONTH);
        	return true;
        case MENU_NOTE:
        	Intent note = new Intent(Main.this,NoteActivity.class);
        	startActivity(note);
        	return true;
        case MENU_SYNC:
        	Intent sync = new Intent(Main.this,SyncActivity.class);
        	startActivity(sync);
        	return true;
        case MENU_SETTING:
        	Intent preferences = new Intent(Main.this,MCSPreferences.class);
        	startActivity(preferences);
        	return true;
        }
        return false;
    }
    
    private static final int CONTEXT_MENU_EDIT = 0;
    private static final int CONTEXT_MENU_DEL = 1;
    
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, CONTEXT_MENU_EDIT, 0, Strings.todo_contextMenu_edit);
		menu.add(0, CONTEXT_MENU_DEL, 0, Strings.todo_contextMenu_delete);
	}
	
    @Override
	public boolean onContextItemSelected(MenuItem item) {
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case CONTEXT_MENU_EDIT:
			Intent editItem = new Intent(this, Edit.class);
			editItem.putExtra(Todolist._ID, info.id);
        	startActivityForResult(editItem, REQUEST_CODE_EDIT);
			return true;
		case CONTEXT_MENU_DEL:
			// 可见info.id直接对应的就是_ID
			db.delete(Todolist._ID + "=" + info.id, null);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
    
    // fire new activity with those ids.
    private static final int REQUEST_CODE_NEW = 1;
    private static final int REQUEST_CODE_EDIT = 2;
    private static final int REQUEST_CODE_MONTH = 3;
   

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case REQUEST_CODE_NEW:
			if(resultCode == RESULT_OK) {
				// 插入数据库中
				db.insert(intent2ContentValues(data, true));
				//如果是新建代办，手动计算插入记录的id
				Cursor cur = db.query(groupProjection, null, null, null);
				startManagingCursor(cur);
				cur.moveToLast();
				alarmId = (int) cur.getLong(0);
				//插入后立即创建提醒服务
				createAlert();
				// 新建完成以后也是要重新载入标签的
				String tag = data.getStringExtra(Todolist.TAG);
				Tag.getInstance().reloadTag();
				Tag.getInstance().setCurrentTag(tag);
				//updateInfoButton();
				updateInfoCategory();
				doFilter();
			}
			break;
		case REQUEST_CODE_EDIT:
			if(resultCode == RESULT_OK) {
				long id = data.getLongExtra(Todolist._ID, -1);
				alarmId = (int) id;
				db.update(intent2ContentValues(data, false), Todolist._ID + "=" + id, null);
				//修改也要重新创建提醒服务
				createAlert();
				// 如果在标签模式下修改了待办的标签，就必须刷新标签列表
				String tag = data.getStringExtra(Todolist.TAG);
				Tag.getInstance().reloadTag();
				Tag.getInstance().setCurrentTag(tag);
				//updateInfoButton();
				updateInfoCategory();
				doFilter();
			}
			break;
		case REQUEST_CODE_MONTH:
			if(resultCode == RESULT_OK) {
				categoryQuery = Todolist.BEGIN_DATE;
				changeInfoBar(DISPLAY_DATE);
				//updateInfoButton();
				updateInfoDate();
				doFilter();
			}
			break;
		}
    }
    
    /**
     * 获取ContentValues，以便于数据库操作，
     * 新建待办的时候需要插入这个创建时间，
     * 编辑待办的时候就不需要了
     * @param data 包含数据的intent
     * @param putCrtTime true则插入crtTime，false则不插入crtTime
     */
    private ContentValues intent2ContentValues(Intent data, boolean putCrtTime) {
    	String title = data.getStringExtra(Todolist.TITLE);
		String content = data.getStringExtra(Todolist.CONTENT);
		int state = Todolist.STATE_UNDONE; 
		int importance = data.getIntExtra(Todolist.IMPORTANCE, 0);
		String beginDate = data.getStringExtra(Todolist.BEGIN_DATE);
		String beginTime = data.getStringExtra(Todolist.BEGIN_TIME);
		long beginTimeLong = data.getLongExtra(Todolist.BEGIN_TIME_LONG, 0);
		String endDate = data.getStringExtra(Todolist.END_DATE);
		String endTime = data.getStringExtra(Todolist.END_TIME);
		long endTimeLong = data.getLongExtra(Todolist.END_TIME_LONG, 0);
		long alertTime = data.getLongExtra(Todolist.ALERT_TIME, 0);
		String warnFreq = data.getStringExtra(Todolist.WARN_FREQUENCY);
		String tag = data.getStringExtra(Todolist.TAG);
		
		ContentValues cv = new ContentValues();
		cv.put(Todolist.TITLE, title);
		cv.put(Todolist.CONTENT, content);
		cv.put(Todolist.STATE, state);
		cv.put(Todolist.IMPORTANCE, importance);
		cv.put(Todolist.BEGIN_DATE, beginDate);
		cv.put(Todolist.BEGIN_TIME, beginTime);
		cv.put(Todolist.BEGIN_TIME_LONG, beginTimeLong);
		cv.put(Todolist.END_DATE, endDate);
		cv.put(Todolist.END_TIME, endTime);
		cv.put(Todolist.END_TIME_LONG, endTimeLong);
		cv.put(Todolist.ALERT_TIME, alertTime);
		cv.put(Todolist.WARN_FREQUENCY, warnFreq);
		cv.put(Todolist.TAG, tag);
		
		// 新建待办的时候需要插入这个创建时间，编辑待办的时候就不需要了
		if(putCrtTime) {
			long crtTime = data.getLongExtra(Todolist.CRT_TIME, 0);
			Log.i("new crtTime", String.valueOf(crtTime));
			cv.put(Todolist.CRT_TIME, crtTime);			
		}
		// 赋值
	    alarmTitle = title;
	    alarmContent = content;
	    alarmBeginTimeLong = beginTimeLong;
	    alarmAlertTime = alertTime;
	    alarmWarnFreq = warnFreq;
				
		return cv;
    }
    
    
    /**
     * 创建提醒服务
     */
    public void createAlert(){
    	
    	long triggerAtTime = alarmBeginTimeLong - alarmAlertTime;
    	// TODO 改变服务的配置
    	Intent mIntent = new Intent(Main.this,MCSService.class);
    	mIntent.setAction(Strings.ALERT_SERVICE_ACTION);
    	mIntent.putExtra("alarmId", alarmId);
    	mIntent.putExtra("alarmTitle", alarmTitle);
    	mIntent.putExtra("alarmContent", alarmContent); 
    	mIntent.putExtra("alarmWarnFreq", alarmWarnFreq);
    	PendingIntent sender = PendingIntent.getService(Main.this, 1, mIntent, alarmId);
    	//am.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime, interval, sender);
    	am.set(AlarmManager.RTC_WAKEUP, triggerAtTime, sender);
    }
    class TodoAdapter extends CursorTreeAdapter {
    	// findViewById用来获得具体的空间，LayoutInflater用来导入xml文件
    	private LayoutInflater inflater;
        
		public TodoAdapter(Context context, Cursor cursor) {
			super(cursor, context);
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
			return inflater.inflate(R.layout.expandgroup, parent, false);
		}

		@Override
		protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
			return inflater.inflate(R.layout.expandchild, parent, false);
		}

		@Override
		protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
			TextView timeText = (TextView)view.findViewById(R.id.group_time);
			TextView titleText = (TextView)view.findViewById(R.id.group_title);
			RatingBar importanceRB = (RatingBar)view.findViewById(R.id.group_importance);
			TextView stateText = (TextView)view.findViewById(R.id.group_state);
			
			String time = cursor.getString(cursor.getColumnIndex(Todolist.BEGIN_TIME));
			String title = cursor.getString(cursor.getColumnIndex(Todolist.TITLE));
			int rate = cursor.getInt(cursor.getColumnIndex(Todolist.IMPORTANCE));
			int state = cursor.getInt(cursor.getColumnIndex(Todolist.STATE));
			
			timeText.setText(time);
			titleText.setText(title);
			importanceRB.setRating(rate);
			// 根据状态设置GroupView的状态文字
			switch(state) {
			case Todolist.STATE_DONE:
				stateText.setText(String.valueOf(Strings.state_done));
				break;
			case Todolist.STATE_UNDONE:
				stateText.setText(String.valueOf(Strings.state_undone));
				break;
			}
			
			/** 根据待办的开始时间与现在的差值决定待办的颜色 */
			long todo = cursor.getLong(cursor.getColumnIndex(Todolist.END_TIME_LONG));
			long now = System.currentTimeMillis();
			long alert = cursor.getLong(cursor.getColumnIndex(Todolist.ALERT_TIME));
			if(todo < now) {
				timeText.setBackgroundColor(0xff444444);
			}else if((todo - now) < alert) {
				timeText.setBackgroundColor(0xffa01b2c);
			}else {
				timeText.setBackgroundColor(0xff859df3);
			}
		}
		
		@Override
		protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
			TextView contentText = (TextView)view.findViewById(R.id.child_content);
			TextView beginTimeText = (TextView)view.findViewById(R.id.child_begintime);
			TextView endTimeText = (TextView)view.findViewById(R.id.child_endtime);
			TextView alertTimeText = (TextView)view.findViewById(R.id.child_alerttime);
			TextView warnFreqText = (TextView)view.findViewById(R.id.child_warnfreq);
			TextView tagText = (TextView)view.findViewById(R.id.child_tag);
			
			String content = cursor.getString(cursor.getColumnIndex(Todolist.CONTENT));
			String beginDate = cursor.getString(cursor.getColumnIndex(Todolist.BEGIN_DATE));
			String beginTime = cursor.getString(cursor.getColumnIndex(Todolist.BEGIN_TIME));
			String endDate = cursor.getString(cursor.getColumnIndex(Todolist.END_DATE));
			String endTime = cursor.getString(cursor.getColumnIndex(Todolist.END_TIME));
			long alertTime = cursor.getLong(cursor.getColumnIndex(Todolist.ALERT_TIME));
			String warnFreq = cursor.getString(cursor.getColumnIndex(Todolist.WARN_FREQUENCY));
			String tag = cursor.getString(cursor.getColumnIndex(Todolist.TAG));
			
			contentText.setText(content);
			beginTimeText.setText(beginDate + " " + beginTime);
			endTimeText.setText(endDate + " " + endTime);
			alertTimeText.
				setText("提前" + Time.getTimeString(Time.getAlertHour(alertTime), Time.getAlertMinute(alertTime), "HH小时mm分钟"));
			warnFreqText.setText(warnFreq);
			tagText.setText(tag);
		}

		@Override
		protected Cursor getChildrenCursor(Cursor groupCursor) {
			int id = groupCursor.getInt(groupCursor.getColumnIndexOrThrow(Todolist._ID));
			Cursor cur = db.query(null, Todolist._ID + "=" + id, null, null);
			Main.this.startManagingCursor(cur);
			return cur;
		}
    }
}
