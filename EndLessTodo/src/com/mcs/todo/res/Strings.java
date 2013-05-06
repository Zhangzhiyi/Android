package com.mcs.todo.res;

public class Strings {
	// 命名规则：界面名_控件名_名称[_用途]
	// 各个元素内部遵循一般的大写方式
	
	
	// ------------------------TodoActivity--------------------
	
    // 底部按钮弹出的SingleChoiceDialog中的各个项
    public static String[] todo_categoryDialog_items = {
    		"日期",
    		"标签",
    		"全部"
    };
    
    public static String[] todo_stateDialog_items = {
    		"全部",
    		"完成",
    		"未完成"
    };
    
    public static String[] todo_sortDialog_items = {
    		"开始时间",
    		"重要性"
    };
    
    // 几个Dialog的标题
    public static String todo_categoryDialog_title = "选择显示的方式";
    public static String todo_stateDialog_title = "选择待办的状态";
    public static String todo_sortDialog_title = "选择排序的方式";
    public static String todo_tagDialog_title = "选择要显示的标签";
    
    public static String okBtnText = "确定";
    public static String cancelBtnText = "取消";
    
    // 顶部信息栏
    public static String todo_infoBarText_date = "日期：";
    public static String todo_infoBarText_tag = "标签：";
    public static String todo_infoBarText_all = "所有待办";
    
    // OptionsMenu的菜单文字
    public static String todo_optionsMenu_month = "月视图";
    public static String todo_optionsMenu_new = "新建待办";
    public static String todo_optionsMenu_today = "回到今天";
    public static String todo_optionsMenu_sync = "同步";
    public static String todo_optionsMenu_user = "切换用户";
    public static String todo_optionsMenu_setting = "设置";
    public static String todo_optionsMenu_search = "搜索";
    public static String todo_optionsMenu_note = "备忘";
    
    // ContextMenu的菜单文字
    public static String todo_contextMenu_edit = "编辑待办";
    public static String todo_contextMenu_delete = "删除待办";
    
    // ------------------------------TodoNew------------------------
    // 两个Spinner
    public static String[] todoNew_spinner_warnFreq = {
    	"一次",
    	"每天",
    	"每个工作日",
    	"每周",
    	"每月",
    	"每年"
    };
    
    
    
                                                   
    
    public static String tag_def = "无标签";
    
    public static String state_undone = "未完成";
    public static String state_done = "完成";
    
    // -----------------------------WIDGET----------------------------
    public static String widget_none = "无待办事项";
    public static String[] widget_conf_modes = {
    	"今日待办",
    	"标签"
    };
    //----------------------action----------------------
    public static final String ALERT_SERVICE_ACTION = "com.mcs.todo.alertservice";
    public static final String SEARCH_TODO_ACTION = "com.mcs.todo.search";
    public static final String WIDGET_DISPLAY_ACTION = "com.mcs.todo.display";
    public static final String ADD_NOTE_ACTION = "com.mcs.todo.add";
    public static final String EDIT_NOTE_ACTION = "com.mcs.todo.edit";
    public static final String NOTIFIACATION_TODO_ACTION = "com.mcs.todo.notification";
    
}
