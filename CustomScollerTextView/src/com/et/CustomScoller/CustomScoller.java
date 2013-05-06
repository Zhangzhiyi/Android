package com.et.CustomScoller;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.et.CustomTextView.*;


public class CustomScoller extends Activity {
    
	private CustomTextView textView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        textView =  (CustomTextView)findViewById(R.id.textview);
        textView.startScroll();
       
        
    }
}