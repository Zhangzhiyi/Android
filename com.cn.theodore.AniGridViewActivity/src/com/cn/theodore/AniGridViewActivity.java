package com.cn.theodore;

import java.util.ArrayList;

import com.cn.theodore.util.DateAdapter;
import com.cn.theodore.util.DragGrid;



import android.app.Activity;
import android.os.Bundle;
import android.view.animation.TranslateAnimation;

public class AniGridViewActivity extends Activity {
	/** GridView. */
	private DragGrid gridView;
	TranslateAnimation left, right;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		com.cn.theodore.util.Configure.init(this);
		gridView = (DragGrid) findViewById(R.id.gridview);
		ArrayList<String> l = new ArrayList<String>();
		for (int i = 0; i < 8; i++) {
			l.add("" + i);
		}
		DateAdapter adapter = new DateAdapter(AniGridViewActivity.this, l);
		gridView.setAdapter(adapter);
	}
}