package com.et.TestNetWork;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestNetWork extends Activity {
	/**设置cmwap上网首先要切换DNS检查选项,在手机拨号界面输入*#*#4636#*#*进入设置界面,点手机信息选项,
	 * 点击切换DNS检查,变成0.0.0.0 allowed;  
	 * **/
	public static final String CMNET = "cmnet";
	public static final String CMWAP = "cmwap";
	/**每个接入点的id要根据不同的机器查询里面Telephony数据库**/
	public static final int CMNET_ID = 1;
	public static final int CMWAP_ID = 2;
	private int apnId = -1;
	private String curApnName;
	
	//存储所有APN数据库的uri
    Uri uri = Uri.parse("content://telephony/carriers");
    //记录当前选中的APN的id的uri,这个表只有一条记录，表的列数、字段名和carriers的一样
    Uri curUri = Uri.parse("content://telephony/carriers/preferapn");
	
   
    
	private TextView text;
	private TextView apn01;
	private TextView apn02;
	private Button btn01;
	private Button btn02;
	private Button sqlChangeApnBtn;
	private Button managerChangeApnBtn;
	private Button getWapBtn;
	private ConnectivityManager connec;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        text = (TextView) findViewById(R.id.text);
        apn01 = (TextView) findViewById(R.id.apn01);
        apn02 = (TextView) findViewById(R.id.apn02);
        btn01 = (Button) findViewById(R.id.Button01);
        btn02 = (Button) findViewById(R.id.Button02);
        getWapBtn = (Button) findViewById(R.id.getwap);
        sqlChangeApnBtn = (Button) findViewById(R.id.SqliteChangeAPN);
        managerChangeApnBtn = (Button) findViewById(R.id.ManagerChangeApn);
        connec =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        /**查询当前网络连接方式***/
        if (connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        	text.setText("wifi方式连接");
        if (connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
        	text.setText("GPRS方式连接");
        
        queryAPNfromConnective(); 
        
        
        btn01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				queryAPNfromConnective(); 
			}
		});
        btn02.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				queryAPNfromTable();
			}
		});
        /*****通过更改数据库方式更改现在的APN*****/    
        sqlChangeApnBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				setDefaultApn();
				//connectManagerSetApn();
			}
		});
        /*****通过ConnectiveManager更改现在的APN*****/ 
        managerChangeApnBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/***这种方式更改失败***/
				connectManagerSetApn();
			}
		});
        
        getWapBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 String url = "wap.gd.10086.cn/gotone_biz_portal.do?bizID=84";
				 HttpGet httpGet = new HttpGet(url);
				 HttpParams baseParams = new BasicHttpParams();
				 baseParams.setParameter("http.route.default-proxy", 
			                new HttpHost("10.0.0.172", 80)); 
			     HttpConnectionParams.setConnectionTimeout(baseParams, 30 * 1000); 
			     HttpConnectionParams.setSoTimeout(baseParams, 45 * 1000); 
			     DefaultHttpClient client = new DefaultHttpClient(baseParams); 
			     try {
					HttpResponse httpResponse = client.execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200)
					{
						
						String result = EntityUtils.toString(httpResponse
								.getEntity());
						result = result.replaceAll("\r", "");
						Log.i("wap", result);
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
    }
	/***通过ConnectivityManager测试到当前的APN**/
	public void queryAPNfromConnective(){
		NetworkInfo info = connec.getActiveNetworkInfo();
        if (info != null) {  
             curApnName = info.getExtraInfo();  
             if (curApnName == null) {  
                   curApnName = "取不到移动网络信息";  
                }  
         } else {  
                curApnName = "取不到网络信息";  
        }
        apn01.setText("通过ConnectivityManager来查询当前APN："+curApnName);
        sqlChangeApnBtn.setText(curApnName);
        managerChangeApnBtn.setText(curApnName);
	}
	/*****通过数据库方式查询现在的APN*****/
	public void queryAPNfromTable(){
		
        int curApnId = 0;
        Cursor cursor = getContentResolver().query(curUri, null, null, null, null);
        if(cursor!=null){
        	cursor.moveToFirst();
        	Log.i("cursorcount", String.valueOf(cursor.getCount()));
        	/**打印记录数目和列的字段名**/
        	int count = cursor.getColumnCount();
        	Log.i("count", String.valueOf(count));
        	/**不明白为什么单是打印列名有20列,如果连列值一起打印就没有20列**/
        	for(int i=0;i<count;i++){
        		Log.i(String.valueOf(i)+":", cursor.getColumnName(i));
        	}
        	/**如果连列值一起打印就没有20列**/
        	for(int i=0;i<count;i++){
        		Log.i(String.valueOf(i)+":", cursor.getColumnName(i));
        		String str = cursor.getString(i);
        		if(str!=null)
        			Log.i(cursor.getColumnName(i), cursor.getString(i));
        		else
        			Log.i(cursor.getColumnName(i), "null");	
        	}
        	
        	curApnId = cursor.getInt(cursor.getColumnIndex("_id"));
        	Log.i("curApnId", String.valueOf(curApnId));
        	curApnName = cursor.getString(cursor.getColumnIndex("apn"));
        	apn02.setText("通过APN数据库查询当前APN："+curApnName);
        }
        cursor.close();
        
        /**打印数据测试**/
        //很奇怪,在这里查询条件部分如果这样写"apn = cmwap" 就会出错
        /*cursor = getContentResolver().query(uri, null, "apn = ? and current = ?", new String[]{"cmwap","1"}, null);
        if(cursor!=null){
        	cursor.moveToFirst();
        	Log.i("cursorcount", String.valueOf(cursor.getCount()));
        	//打印记录数目和列的字段名
        	int count = cursor.getColumnCount();
        	Log.i("count", String.valueOf(count));
        	for(int i=0;i<count;i++){
        		Log.i("Column", cursor.getColumnName(i));
        	}
        	cursor.moveToFirst();
        	do{
        		Log.i("APN",cursor.getString(cursor.getColumnIndex("apn")));
        	}while(cursor.moveToNext());
        }*/
	}
	public void setDefaultApn(){
		if(curApnName.equals(CMNET)){
			setDefaultApn(CMWAP_ID);
		}
		else
			setDefaultApn(CMNET_ID);
		
	}
	public void setDefaultApn(int id) {
		ContentResolver cr = getContentResolver();
		ContentValues cv = new ContentValues();
		/**为什么preferapn表没有apn_id这个字段.....**/
		cv.put("apn_id",id);
		cr.update(curUri, cv, null, null);
	}
	
	public void connectManagerSetApn(){
		connec.startUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE, "wap");
	}
	
}