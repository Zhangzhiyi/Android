package com.et.TestMediaScanner;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;

public class MainActivity extends Activity {
	
	public Button scanBtn;
	public Cursor cursor;
	
	public GridView mGridView;
	
	/**设置读取图片的路径**/
	//public String path = "/sdcard/DCIM/100MEDIA";
	/**存储缩略图文件夹**/
	public String path = "/sdcard/DCIM/.thumbnails";
	/**指定目录**/
	//public String path = "/sdcard/ch14_systemcamera";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mGridView = (GridView) findViewById(R.id.gridView1);
        
        scanBtn = (Button) findViewById(R.id.scan);
        scanBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, 
				//		MediaStore.Images.Media.DATA + " like '" + path +"%'" , null, null);
				/**查询缩略图**/
				cursor = getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, null, 
						MediaStore.Images.Media.DATA + " like '" + path +"%.jpg'" , null, null);
				
				Log.i("--------count----------", String.valueOf(cursor.getCount()));
				GridViewAdapter mAdapter = new GridViewAdapter(MainActivity.this, cursor);
				mGridView.setAdapter(mAdapter);
			}
		});
          
        
    }
}