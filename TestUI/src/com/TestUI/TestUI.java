package com.TestUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TestUI extends Activity implements OnClickListener {

	public static final String TAG = "TestUI";
	private LinearLayout linearLayout;
	private TextView text;
	private Button button;
	OnClickListener listener = null;
	private Button button1;
	private Button TestSpan;
	private Button TestLayout;
	private Button TestScroller;
	private Button mClipImageBtn;
	public mDeleteReceiver mDelete;
	PendingIntent pendingIntent1;
	PendingIntent pendingIntent2;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		int memoryClass = activityManager.getMemoryClass();
		Runtime rt = Runtime.getRuntime();
		long maxMemory = rt.maxMemory();
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		String imsi = telephonyManager.getSubscriberId();
		String androidId = Settings.Secure.getString(getContentResolver(), "android_id");
		int phoneType = telephonyManager.getPhoneType();
		String networkName = telephonyManager.getNetworkOperatorName();
		int networkType = telephonyManager.getNetworkType();
		String manufacturer = Build.MANUFACTURER;
		String brand = Build.BRAND;
		String model = Build.MODEL;
		
		long time1 = System.currentTimeMillis();
		Time time = new Time();
		time.setToNow();
		long time2 = time.toMillis(false);
		time.hour = time.hour + 1;
		long time3 = time.toMillis(true);
		// setContentView(R.layout.test_layout);
		linearLayout = (LinearLayout) findViewById(R.id.lindarLayout01);
		text = (TextView) findViewById(R.id.textview);
		button = (Button) findViewById(R.id.button);
		TestSpan = (Button) findViewById(R.id.TextSpan);
		TestSpan.setOnClickListener(this);
		TestLayout = (Button) findViewById(R.id.TestLayout);
		TestLayout.setOnClickListener(this);
		TestScroller = (Button) findViewById(R.id.TestScroller);
		TestScroller.setOnClickListener(this);
		mClipImageBtn = (Button) findViewById(R.id.cilpimage);
		mClipImageBtn.setOnClickListener(this);
		File file = getFilesDir();
		String path = file.getAbsolutePath();
		// data/data/com.TestUI/files
		Log.i("filepath", path);
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		Map<String, String> hash = System.getenv();
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// button.post(runnable);
				// runOnUiThread(runnable);
				Drawable drawable = button.getBackground();
				drawable.setAlpha(255);
				// button.setAlpha(0);
			}
		});
		LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) button.getLayoutParams();
		Log.i("X", String.valueOf(lp.width));
		Log.i("Y", String.valueOf(lp.height));

		copyFile("/data/data/com.TestUI/shared_prefs/com.TestUI_preferences.xml", "/mnt/sdcard/com.TestUI_preferences.xml");
		Uri uri = Uri.parse("http://www.baidu.com");
		Intent intent1 = new Intent(Intent.ACTION_VIEW);
		intent1.setData(uri);
		pendingIntent1 = PendingIntent.getActivity(this, 0, intent1, 0);
		Uri uri2 = Uri.parse("http://www.qq.com");
		Intent intent2 = new Intent(Intent.ACTION_VIEW);
		intent2.setData(uri);
		pendingIntent2 = PendingIntent.getActivity(this, 0, intent2, 0);

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPreferences.edit().putInt("default", 1000).commit();

		sharedPreferences = getSharedPreferences("TestUI", MODE_WORLD_READABLE);
		sharedPreferences.edit().putInt("TestUI", 100).commit();

		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					/**
					 * 无论显式或是隐式启动其它工程的Activity,目标Activity声明的IntentFilter一定要加入
					 * "android.intent.category.DEFAULT"
					 * ("android.intent.action.MAIN"
					 * 和"android.intent.category.LAUNCHER"的intent filter例外.
					 * 它们不需要"android.intent.category.DEFAULT".)
					 * 
					 * 隐式启动本工程的Activity,目标Activity声明的IntentFilter一定要加入
					 * "android.intent.category.DEFAULT"
					 * ("android.intent.action.MAIN"
					 * 和"android.intent.category.LAUNCHER"的intent filter例外.
					 * 它们不需要"android.intent.category.DEFAULT".)
					 **/
					Context outContext = createPackageContext("com.TestUI", Context.CONTEXT_IGNORE_SECURITY);
					Intent intent = new Intent("com.jb.gokeyboard.theme.android");
					// Intent intent = new Intent();
					// ComponentName componentName= new
					// ComponentName(outContext.getPackageName(),
					// "com.TestUI.GoPurchaseActivity");
					// intent.setComponent(componentName);
					// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// startActivity(intent);
					startActivityForResult(intent, 1);
					// /**修改不了其他工程默认的设置文件**/
					// SharedPreferences sharedPreferences =
					// PreferenceManager.getDefaultSharedPreferences(outContext);
					// int def = sharedPreferences.getInt("default", -1);
					// Log.i(TAG, "def:" + def);
					// /**可以读取赋了读取权限其他工程设置文件**/
					// sharedPreferences =
					// outContext.getSharedPreferences("TestUI",
					// MODE_WORLD_READABLE);
					// int test = sharedPreferences.getInt("TestUI", -1);
					// Log.i(TAG, "test:" + test);

				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			button.setText("在线程中更新UI");
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.TextSpan: {
			try {
				Context outContext = createPackageContext("com.jb.gokeyboard.theme.android", Context.CONTEXT_INCLUDE_CODE | CONTEXT_IGNORE_SECURITY);
				Class cla = outContext.getClassLoader().loadClass("com.android.vending.GoPurchaseActivity");
				Object obj = cla.newInstance();
				Method method = cla.getMethod("init", new Class[] { Context.class });
				method.invoke(obj, outContext);
				Method method1 = cla.getMethod("purchaseBillingInApp", null);
				method1.invoke(obj, null);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
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
			//
			break;
		case R.id.TestLayout:
			/** 通过action发起其他工程的服务 **/
			Intent serviceIntent = new Intent("com.et.MyService");
			startService(serviceIntent);
			break;
		case R.id.TestScroller:
			// copyFile("/mnt/sdcard/com.TestUI_preferences.xml",
			// "/data/data/com.TestUI/shared_prefs/com.TestUI_preferences.xml");
			// billingEnter.checkBillingSupported();
			Intent intent1 = new Intent("android.intent.action.test");
			startActivity(intent1);
			break;
		case R.id.cilpimage:
			Uri photoUri = Uri.fromFile(new File("/mnt/sdcard/ruhua.jpg"));
			Log.i("path", photoUri.toString());
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(photoUri, "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 80);
			intent.putExtra("outputY", 80);
			intent.putExtra("output", photoUri);
			intent.putExtra("outputFormat", "JPEG");
			startActivityForResult(intent, 1);
			break;

		}
	}

	/** 可以接受到其它工程Activity的返回值 **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {
			Log.i(TAG, "onActivityResult:RESULT_OK");
		} else if (resultCode == RESULT_CANCELED) {
			Log.i(TAG, "onActivityResult:RESULT_CANCELED");
		}
	}

	public void copyFile(String srcPath, String destPath) {
		try {
			FileInputStream fileInput = new FileInputStream(srcPath);
			File file = new File(destPath);
			if (!file.exists())
				file.createNewFile();
			FileOutputStream fileOutput = new FileOutputStream(file);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fileInput.read(bytes)) != -1) {
				fileOutput.write(bytes, 0, length);
			}
			fileInput.close();
			fileOutput.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class mDeleteReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Toast.makeText(context, "卸载", Toast.LENGTH_LONG).show();
			Log.i("11111", "22222222222");
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_DATA_CLEARED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		mDelete = new mDeleteReceiver();
		registerReceiver(mDelete, filter);
		super.onResume();
	}

	/*** 不能在onPause()注销这个广播，因为跳转到卸载画面时这个activity是pause状态 **/
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		// unregisterReceiver(mDelete);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(mDelete);
		super.onDestroy();
	}
}