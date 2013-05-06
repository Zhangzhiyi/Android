package com.et.TestMediaScanner;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class AsyncLoadDrawable {
	
	/**这个类直接用线程读取图片,每读取一张图片就开一个线程
	 * 当图片多了开的线程就会很多,最后还是崩溃了,线程多了很难管理
	 * 所以放弃此方法,改用Android的异步任务AsyncTask,性能很好也很稳定**/
	public HashMap<String, SoftReference<Drawable>> imageCache;
	public Vector<String> vector;
	
	public AsyncLoadDrawable() {
		// TODO Auto-generated constructor stub
		imageCache = new HashMap<String, SoftReference<Drawable>>();
		vector = new Vector<String>();
		
	}
	
	public Drawable loadDrawable(final String path){
		if(imageCache.containsKey(path)){
			SoftReference<Drawable> softReference = imageCache.get(path);
			Drawable drawable = softReference.get();
			return drawable;
		}
		if(!vector.contains(path)){
			new Thread(){
	        	public void run() {
	        		Drawable drawable = Drawable.createFromPath(path);
	        		imageCache.put(path, new SoftReference<Drawable>(drawable));        			        		
	        	};
	        }.start();
	        vector.add(path);
		}
		
		return null;
	}
}
