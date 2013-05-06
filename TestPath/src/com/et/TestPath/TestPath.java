package com.et.TestPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class TestPath extends Activity {
	
	private File file;
	private Uri uri;
	public static final String WORKDIRECTORY_NAME = "TestPath";
	public String mWorkDirectoryPath;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /**测试路径**/
        try {
        	//创建文件pictures存储在/data/data/com.et.TestPath/files
			FileOutputStream  mFileOutputStream = openFileOutput("pictures", MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // /data/data/com.et.TestPath/files/pictures
        file = getFileStreamPath("pictures");
        Log.i("file path", file.getAbsolutePath());
        
        // /data/data/com.et.TestPath/files
    	file = getFilesDir();
    	Log.i("file path", file.getAbsolutePath());
    
       // /mnt/sdcard/Android/data/com.et.TestDownloadManager/files 
       //(在SD卡上应用程序私有目录，媒体扫描器并不会扫描此目录文件，要自己调用MediaScannerConnection.scanFile扫描)
       file = getExternalFilesDir(null);
       Log.i("file path", file.getAbsolutePath());
       
       // /mnt/sdcard/Android/data/com.et.TestDownloadManager/files/Pictures
       file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
       Log.i("file path", file.getAbsolutePath());
       
       // /mnt/sdcard/Pictures (这个是SD上的公共目录)
       file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
       Log.i("file path", file.getAbsolutePath());
       
       // /data
       file = Environment.getDataDirectory();
       Log.i("file path", file.getAbsolutePath());
       
       // /mnt/sdcard
       file = Environment.getExternalStorageDirectory();
       Log.i("file path", file.getAbsolutePath()  + "---|---" + file.getPath());
       
       // /system
       file = Environment.getRootDirectory();
       Log.i("file path", file.getAbsolutePath());
       
       String path = getPackageCodePath();
       Log.i("getPackageCodePath()", path);
    		   
       path = getPackageResourcePath();
       Log.i("getPackageResourcePath()", path);
       
       uri = Uri.parse("content://com.example.diarycontentprovider/diaries/1"); 
       
       // 0:diaries   1:1
       List<String> list = uri.getPathSegments(); 
       for(int i=0;i<list.size();i++) 
       Log.i("uri path", list.get(i));
       
       // /diaries/1
       path = uri.getEncodedPath(); 
       Log.i("uri path", path);
       
       // 1
       path = uri.getLastPathSegment(); 
       Log.i("uri path", path);
       
       //com.example.diarycontentprovider
       path = uri.getAuthority(); 
       Log.i("uri path", path);
       
       //com.example.diarycontentprovider
       path = uri.getHost(); 
       Log.i("uri path", path);
       
       // content
       path = uri.getScheme(); 
       Log.i("uri path", path); 
       
       // /diaries/1
       path = uri.getPath(); 
       Log.i("uri path", path);
       
       if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    	   mWorkDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + WORKDIRECTORY_NAME + File.separator;
//    	   File file = new File(mWorkDirectoryPath);
//			if (!file.exists()) {
//				file.mkdirs();
//			}
			String[] fileLists;
		    try {
			    fileLists = getAssets().list("");
			    Log.i("uri path", path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	   CopyAssets("", mWorkDirectoryPath);
       }
       
    }
    private void CopyAssets(String assetDir,String dir) {
		String[] files;    
	     try    
	     {    
	         files = this.getResources().getAssets().list(assetDir);    
	     }    
	     catch (IOException e1)    
	     {    
	         return;    
	     }    
	     File mWorkingPath = new File(dir);
	     //if this directory does not exists, make one. 
	     if(!mWorkingPath.exists())    
	     {    
	    	 mWorkingPath.mkdirs();
	     }    
	     
	     for(int i = 0; i < files.length; i++)    
	     {    
	         try    
	         {    
	             String fileName = files[i]; 
//	             if (fileName.equals(object)) {
//					
//				 }
	             //we make sure file name not contains '.' to be a folder. 
	             if(!fileName.contains("."))
	             {
	            	 if(0==assetDir.length())
	            	 {
	            		 CopyAssets(fileName,dir+fileName + "/");
	            	 }
	            	 else
	            	 {
	            		 CopyAssets(assetDir+"/"+fileName,dir+fileName+"/");
	            	 }
	            	 continue;
	             }
	             File outFile = new File(mWorkingPath, fileName);    
	             if(outFile.exists()) 
	            	 outFile.delete();
	             InputStream in =null;
	             if(0!=assetDir.length())
	            	 in = getAssets().open(assetDir+"/"+fileName);    
	             else
	            	 in = getAssets().open(fileName);
	             OutputStream out = new FileOutputStream(outFile);    
	     
	             // Transfer bytes from in to out   
	             byte[] buf = new byte[1024];    
	             int len;    
	             while ((len = in.read(buf)) > 0)    
	             {    
	                 out.write(buf, 0, len);    
	             }    
	     
	             in.close();    
	             out.close();    
	         }    
	         catch (FileNotFoundException e)    
	         {    
	             e.printStackTrace();    
	         }    
	         catch (IOException e)    
	         {    
	             e.printStackTrace();    
	         }         
	    }
    }

}