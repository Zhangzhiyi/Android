package com.et.objecttran;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
public class ObjectTranDemo extends Activity implements OnClickListener {
    
	private Button sButton,pButton;
	public  final static String SER_KEY = "com.tutor.objecttran.ser";
	public  final static String PAR_KEY = "com.tutor.objecttran.par";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);   
        setupViews();
        
    }
    
    //我的一贯作风呵呵
    public void setupViews(){
    	sButton = (Button)findViewById(R.id.button1);
    	pButton = (Button)findViewById(R.id.button2);
    	sButton.setOnClickListener(this);
    	pButton.setOnClickListener(this);
    }
    //Serializeable传递对象的方法
    public void SerializeMethod(){
    	Person mPerson = new Person();
    	mPerson.setName("frankie");
    	mPerson.setAge(25);
    	Intent mIntent = new Intent(this,ObjectTranDemo1.class);
    	Bundle mBundle = new Bundle();
    	mBundle.putSerializable(SER_KEY,mPerson);
    	mIntent.putExtras(mBundle);
    	
    	startActivity(mIntent);
    }
    //Pacelable传递对象方法
    public void PacelableMethod(){
    	Book mBook = new Book();
    	mBook.setBookName("Android Tutor");
    	mBook.setAuthor("Frankie");
    	mBook.setPublishTime(2010);
    	String s = "test";
    	mBook.setContent(s.getBytes());
    	Intent mIntent = new Intent(this,ObjectTranDemo2.class);
    	Bundle mBundle = new Bundle();
    	mBundle.putParcelable(PAR_KEY, mBook);
    	mIntent.putExtras(mBundle);
    	
    	startActivity(mIntent); 
    }
    //铵钮点击事件响应
	public void onClick(View v) {
		if(v == sButton){
			SerializeMethod();
		}else{
			PacelableMethod();
		}
	}
}