package com.et.objecttran;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
public class ObjectTranDemo1 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView mTextView = new TextView(this);
        Person mPerson = (Person)getIntent().getSerializableExtra(ObjectTranDemo.SER_KEY);
        mTextView.setText("You name is: " + mPerson.getName() + "\n"+
        		"You age is: " + mPerson.getAge());
        
        setContentView(mTextView);
    }
}