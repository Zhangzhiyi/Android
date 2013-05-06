package com.et.TestDownloadManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.HashMap;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestDownloadManager extends Activity {
	
	DownloadManager mDownloadManager;
	
	private Button downBtn;
	private Button reasonBtn;
	private Button writeBtn;
	private Button readBtn;
	private File file;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);    
        
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downBtn = (Button) findViewById(R.id.button1);
        reasonBtn = (Button) findViewById(R.id.button2);
        writeBtn = (Button) findViewById(R.id.button3);
        readBtn = (Button) findViewById(R.id.button4);
        
        /**测试路径**/
        // /mnt/sdcard (SD卡跟目录)
        file = Environment.getExternalStorageDirectory();
        Log.i("getExternalStorageDirectory()", file.getAbsolutePath());
        
     // /mnt/sdcard/Pictures (这个是SD上的公共目录)
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Log.i("getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)", file.getAbsolutePath());
        
        // /mnt/sdcard/Android/data/com.et.TestDownloadManager/files 
        //(在SD卡上应用程序私有目录，媒体扫描器并不会扫描此目录文件，要自己调用MediaScannerConnection.scanFile扫描)
        file = getExternalFilesDir(null);
        Log.i("getExternalFilesDir(null)", file.getAbsolutePath());
        
        // /mnt/sdcard/Android/data/com.et.TestDownloadManager/files/Pictures
        file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.i("getExternalFilesDir(Environment.DIRECTORY_PICTURES)", file.getAbsolutePath());
        
        // /data/data/com.et.TestDownloadManager/files
        file = getFilesDir();
        Log.i("getFilesDir()", file.getAbsolutePath());
        
        // /data/data/com.et.TestDownloadManager/app_gostore
        file = getDir("gostore", MODE_PRIVATE);
        Log.i("getDir(gostore, MODE_PRIVATE)", file.getAbsolutePath());
        
        ///data/data/com.et.TestDownloadManager/cache   手机里的cache文件
        file = getCacheDir();
        Log.i("getCacheDir()", file.getAbsolutePath());
        
        ///mnt/sdcard/Android/data/com.et.TestDownloadManager/cache  SD卡里的cache文件
        file = getExternalCacheDir();
        Log.i("getExternalCacheDir()", file.getAbsolutePath());
        try {
        	// /data/data/com.et.TestDownloadManager/files/dest.jpg
        	//存储在/data/data应用程序内部的文件目录
			FileOutputStream fileOutStream = openFileOutput("ET", MODE_PRIVATE);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//写文件如果文件不存在也不会抛出异常,系统会自动创建一个空文件
			Log.i("openFileOutput", "文件不存在");
			e.printStackTrace();
		}
		try {
			FileInputStream fileInputStream = openFileInput("ET111");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//读文件如果文件不存在也会抛出异常
			Log.i("openFileInput", "文件不存在");
			e.printStackTrace();
		}
		
//		Bitmap bitmap = BitmapFactory.decodeFile("/res/drawable-hdpi/icon.png");
//		Log.i("bitmap", bitmap.toString());
		
        downBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent mIntent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
				//startActivity(mIntent);
				
				Uri uri = Uri.parse("http://www.eoeandroid.com/z/images/noavatar_middle.gif");
				DownloadManager.Request request = new DownloadManager.Request(uri);
				request.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.15) Gecko/2009101601 Firefox/3.0.15 (.NET CLR 3.5.30729)");
				request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
				request.setAllowedOverRoaming(false);
				request.setVisibleInDownloadsUi(true);
				request.setShowRunningNotification(true);
				//request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "noavatar_middle.gif");
				request.setDestinationInExternalFilesDir(TestDownloadManager.this, null, "noavatar_middle.gif");
				mDownloadManager.enqueue(request);
			}
		});
        reasonBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DownloadManager.Query query = new DownloadManager.Query();
				query.setFilterByStatus(DownloadManager.STATUS_FAILED);
				Cursor cursor = mDownloadManager.query(query);
				cursor.moveToFirst();
				int error = cursor.getInt(cursor.getColumnIndex(mDownloadManager.COLUMN_REASON));
				Log.i("error", String.valueOf(error));
			}
		});
        writeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (android.os.Environment.getExternalStorageState()
						.equals(android.os.Environment.MEDIA_MOUNTED)){
					String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "note_data1";
					File file = new File(path);
					if (!file.exists()) {
						file.mkdirs();
					}
					String objectPath = file.getAbsolutePath() + File.separator + "temp";
//					HashMap<String,String> map = new HashMap<String,String>();  
//			        map.put("name", "foolfish");
					Person person = new Person();
					person.id = 1;
					try {
						FileOutputStream fileOutputStream = new FileOutputStream(objectPath);
						ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
						objectOutputStream.writeObject(person);
						objectOutputStream.flush();
						objectOutputStream.close();
						fileOutputStream.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
        readBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (android.os.Environment.getExternalStorageState()
						.equals(android.os.Environment.MEDIA_MOUNTED)){
					String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/note_data1/temp";
					try {
						FileInputStream fileInputStream = new FileInputStream(fileName);
						ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);  
//						HashMap<String,String> map = (HashMap<String, String>) objectInputStream.readObject();
						Person person = (Person) objectInputStream.readObject();
						String name = person.getName();
						objectInputStream.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (StreamCorruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
    }
    public static class Person implements Serializable{

		private static final long serialVersionUID = 1L;
		int id;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
    }
}