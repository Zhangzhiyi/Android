package com.et.TestJavaReflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestJavaReflect extends Activity implements OnClickListener{
	
	private Button mPrivateFieldBtn;
	private Button mPublicFieldBtn;
	private Button mPublicMethodBtn;
	private Button mPrivateMethodBtn;
	private TextView mTextView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mPrivateFieldBtn = (Button) findViewById(R.id.button1);
        mPrivateFieldBtn.setOnClickListener(this);
        mPublicFieldBtn = (Button) findViewById(R.id.button2);
        mPublicFieldBtn.setOnClickListener(this);      
        mPrivateMethodBtn = (Button) findViewById(R.id.button3);
        mPrivateMethodBtn.setOnClickListener(this);
        mPublicMethodBtn = (Button) findViewById(R.id.button4);
        mPublicMethodBtn.setOnClickListener(this);
        mTextView = (TextView) findViewById(R.id.textView1);
       
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
			case R.id.button1:
				getPrivateField();
				break;
			case R.id.button2:
				getPublicField();
				break;
			case R.id.button3:
				getPrivateMethod();
				break;
			case R.id.button4:
				getPublicMethod();
				break;
		}
	}
	public void getPrivateField(){
		
		try {
			//获取类对象
			Class<?> classObject = Class.forName("com.et.TestJavaReflect.Person");
			//获取类的实例对象
			Object instanceObject = classObject.newInstance();
			//获取私有属性要用declared
			Field privateField = classObject.getDeclaredField("age");
			//设置不进行语言访问检查
			privateField.setAccessible(true);
			int age = privateField.getInt(instanceObject);
			mTextView.setText(String.valueOf(age));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//创建类的实例对象
		catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void getPublicField(){
		
		try {
			//获取类对象
			Class<?> classObject = Class.forName("com.et.TestJavaReflect.Person");
			//获取类的实例对象
			Object instanceObject = classObject.newInstance();
			
			Field publicField = classObject.getField("name");
			String name = publicField.get(instanceObject).toString();
			mTextView.setText(name);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void getPrivateMethod(){
		try {
			//获取类对象
			Class<?> classObject = Class.forName("com.et.TestJavaReflect.Person");
			//获取类的实例对象
			Object instanceObject = classObject.newInstance();
			//获取私有方法要用declared
			Method method = classObject.getDeclaredMethod("priDisplay", new Class[]{});
			//设置不进行语言访问检查
			method.setAccessible(true);
			Object result = method.invoke(instanceObject, new Object[]{});
			mTextView.setText(result.toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getPublicMethod(){
		try {
			//获取类对象
			Class<?> classObject = Class.forName("com.et.TestJavaReflect.Person");
			//获取类的实例对象
			Object instanceObject = classObject.newInstance();
			Method method = classObject.getMethod("pubDisplay", new Class[]{});
			Object result = method.invoke(instanceObject, new Object[]{});
			mTextView.setText(result.toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}