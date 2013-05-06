package com.et.TestMediaScanner;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class MyAsyncTask extends AsyncTask<String, Integer, Integer> {
	
	HashMap<String, SoftReference<Bitmap>> imageCache;
	GridViewAdapter mAdapter;
	public MyAsyncTask(HashMap<String, SoftReference<Bitmap>> imageCache, GridViewAdapter mAdapter) {
		// TODO Auto-generated constructor stub
		this.imageCache = imageCache;
		this.mAdapter = mAdapter;
	}
	@Override
	protected Integer doInBackground(String... params) {
		// TODO Auto-generated method stub
		String path = params[0];		
		BitmapFactory.Options options = new BitmapFactory.Options();
		//options.inTempStorage = new byte[1024*1024*5]; //5MB的临时存储空间
		//压缩读取，节省内存
		options.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
    	imageCache.put(path, new SoftReference<Bitmap>(bitmap));
    	publishProgress(0);
		return null;
	}
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		/**一定要在主线程刷新UI,这个onProgressUpdate方法是在主线程运行的**/
		//mAdapter.notifyDataSetInvalidated();//这种方法刷新有时滚动条会跳到开始位置
		mAdapter.notifyDataSetChanged();
		super.onProgressUpdate(values);
	}

}
