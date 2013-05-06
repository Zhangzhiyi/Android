package com.mcs.todo.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import android.app.Activity;
import android.database.Cursor;

import com.mcs.todo.db.DBAdapter;
import com.mcs.todo.db.DBConstant;
import com.mcs.todo.db.Todolist;
import com.mcs.todo.res.Strings;

/**
 * 标签管理类
 * @author dbds
 */
public class Tag {
	
	private ArrayList<String> tags = new ArrayList<String>();
	private DBAdapter db;
	private String[] projection = {Todolist.TAG};
	
	private static Activity activity = null;
	private static Tag instance = null;
	
	private static int currentPos = 0;
	
	public static final Tag getInstance() {
		if(instance == null && activity != null) {
			instance = new Tag();
		}
		return instance;
	}
	
	/** 使用之前，请先初始化 */
	public static void init(Activity a) {
		activity = a;
	}
	
	private Tag() {
		db = new DBAdapter(activity, DBConstant.TABLE_TODOLIST);
		reloadTag();
	}
	
	public void reloadTag() {
		Cursor cur = db.query(projection, null, null, null);
		if (cur != null) {
			if (cur.moveToFirst()) {
				HashSet<String> tagSet = new HashSet<String>();
				while (!cur.isAfterLast()) {
					tagSet.add(cur.getString(cur.getColumnIndex(Todolist.TAG)));
					cur.moveToNext();
				}
				Iterator<String> iter = tagSet.iterator();
				tags.clear();
				while (iter.hasNext()) {
					tags.add(iter.next());
				}
				cur.close();
				currentPos = 0;
			}
		}
	}
	
	public void putTag(String tag) {
		tags.add(tag);
	}
	
	public void rollToNext() {
		currentPos++;
		if(currentPos >= tags.size())
			currentPos = 0;
	}
	
	public void rollToPrevious() {
		currentPos--;
		if(currentPos < 0)
			currentPos = tags.size() - 1;
	}
	
	public String getCurrentTag() {
		if(tags.size() == 0)
			return Strings.tag_def;
		else 
			return tags.get(currentPos);
	}
	
	public void setCurrentTag(String tag) {
		currentPos = tags.indexOf(tag);
	}
	
	public ArrayList<String> getTagList() {
		return tags;
	}

}
