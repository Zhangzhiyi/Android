package com.et.TestInputFilter;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.DateTimeKeyListener;
import android.text.method.DigitsKeyListener;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private EditText edit1;
	private EditText edit2;
	private EditText edit3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        edit1 = (EditText) findViewById(R.id.editText1);
        edit2 = (EditText) findViewById(R.id.editText2);
        edit3 = (EditText) findViewById(R.id.editText3);
        /**创建过滤数据，因为一个EditText可以关联多个过滤器**/
        InputFilter[] filters1 = new InputFilter[2];
        filters1[0] = new InputFilter.LengthFilter(8){
        	@Override
        	public CharSequence filter(CharSequence source, int start, int end,
        			Spanned dest, int dstart, int dend) {
        		// TODO Auto-generated method stub
        		Log.i("CharSequence length", String.valueOf(source.length()));
        		Log.i("Spanned lenght", String.valueOf(dest.length()));
        		if(dest.length() == 8){
        			Toast.makeText( MainActivity.this, "输入长度为8位", Toast.LENGTH_SHORT).show();
        		}
        		return super.filter(source, start, end, dest, dstart, dend);
        	}
        };
        filters1[1] = new InputFilter.AllCaps();       
        edit1.setFilters(filters1);
        edit2.setKeyListener(new NumberKeyListener() {
			
			@Override
			public int getInputType() {
				// TODO Auto-generated method stub
				return InputType.TYPE_MASK_VARIATION;
			}
			
			@Override
			protected char[] getAcceptedChars() {
				// TODO Auto-generated method stub
				return new char[]{'a','b','c'};
			}
		});
        edit3.setKeyListener(new DigitsKeyListener());
        
    }
}