package com.et.TestJavaReflect;

public class Person {
	
	private int age = 23;
	public String name = "ET";
	
	public String pubDisplay(){
		return "通过反射访问公共方法";
		
	}
	
	private String priDisplay(){
		return "通过反射访问私有方法";
		
	}
}
