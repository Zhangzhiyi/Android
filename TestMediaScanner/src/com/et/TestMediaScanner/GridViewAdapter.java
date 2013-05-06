package com.et.TestMediaScanner;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class GridViewAdapter extends BaseAdapter{
	
	private Context context;
	private ViewHolder viewHolder;
	private Cursor cursor;
	public HashMap<String, SoftReference<Bitmap>> imageCache;
	public AsyncLoadDrawable mAsyncLoadBitmap;
	
	public GridViewAdapter(Context context, Cursor cursor) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.cursor = cursor;
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
		//mAsyncLoadBitmap = new AsyncLoadDrawable();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cursor.getCount();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		/**教训：注意这里不要写成cursor.move(position); 否则刷新的过程中要出现CursorIndexOutOfBoundsException异常**/
		cursor.moveToPosition(position);
		return cursor.getInt(0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView==null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.gridview_listitem, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.imageView.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
		cursor.moveToPosition(position);
		String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
		Log.i(String.valueOf(position), path);
		Bitmap bitmap = null;
		if(imageCache.containsKey(path)){
			SoftReference<Bitmap> softReference = imageCache.get(path);
			/**如果获取的对象给回收了则为null,要重新启动异步任务读取图片**/
			bitmap = softReference.get();
		}
		/*else{
			new MyAsyncTask(imageCache, this).execute(path);
		}*/
		if(bitmap!=null){
			//viewHolder.imageView.setBackgroundDrawable(drawable);
			viewHolder.imageView.setImageBitmap(bitmap);
		}
		else{
			//viewHolder.imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.icon));
			viewHolder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon));
			/**启动异步任务读取图片**/
			new MyAsyncTask(imageCache, this).execute(path);
		}
		
		return convertView;
	}
	
	private class ViewHolder {
		ImageView imageView;
	}
}
