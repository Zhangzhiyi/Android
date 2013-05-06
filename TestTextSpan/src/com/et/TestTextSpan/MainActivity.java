package com.et.TestTextSpan;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText edit;
	private TextView tv1;
	private TextView tv2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		edit = (EditText) findViewById(R.id.EditText01);
		tv1 = (TextView) findViewById(R.id.TextView01);
		tv2 = (TextView) findViewById(R.id.textView1);
		edit.setText("Styling the content of an editText dynamically");   
	    Spannable sp = edit.getText();   
	    //３代表从第几个字符开始变颜色，注意第一个字符序号是０．８代表变色到第几个字符．
	    //sp.setSpan(new BackgroundColorSpan(Color.RED), 1, 8,   
	    //Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
	    sp.setSpan(new BackgroundColorSpan(Color.RED), 3, 8,   
	    	    Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
	    
	    sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 0, 7,   
	                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    
	    ClickableSpan click = new ClickableSpan(){

			@Override
			public void onClick(View widget) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "TEST", Toast.LENGTH_LONG).show();
				Log.i("tag", "tag");
			}
	    	
	    };
	    sp.setSpan(click, 0, sp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);	    
	    tv1.setMovementMethod(new LinkMovementMethod().getInstance());//一定要有这句
	    tv1.setLinksClickable(true);
	    /**设置链接TextView的颜色**/
	    tv1.setLinkTextColor(Color.GREEN);	 
	    tv1.setText(sp);
	    
	    SpannableStringBuilder ss = null;
	    ss= new SpannableStringBuilder("红色打电话斜体删除线绿色下划线背景");
	       ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 2,
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	       ss.setSpan(new URLSpan("tel:4155551212"), 2, 5,
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	       ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 5, 7,
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	       ss.setSpan(new StrikethroughSpan(), 7, 10,
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	       ss.setSpan(new UnderlineSpan(), 10, 16,
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	       ss.setSpan(new ForegroundColorSpan(Color.GREEN), 10, 15,
	               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	       ss.setSpan(new  BackgroundColorSpan(Color.rgb(124, 156, 215)), 15, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	       tv2.setText(ss);
	       
	       tv2.setLinksClickable(true);
	       tv2.setMovementMethod(new LinkMovementMethod().getInstance());//一定要有这句
	}
}