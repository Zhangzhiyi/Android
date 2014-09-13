package com.et.testapplication;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		textView = (TextView) findViewById(R.id.textView1);

		MyApplication myApplication = (MyApplication) getApplication();
		textView.setText(myApplication.getName());
		
		List<PackageInfo> allList = getPackageManager().getInstalledPackages(0);
		HashMap<String , PackageInfo> temp = new HashMap<String, PackageInfo>();
		for (PackageInfo item : allList) {
			ApplicationInfo appInfo = item.applicationInfo;
			// 判断是否为非系统预装的应用程序
			if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0 || (item.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) > 0) {//FLAG_UPDATED_SYSTEM_APP表示是系统程序，但用户更新过，也算是用户安装的程序
				temp.put(item.packageName, item);
			} else if ((item.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0 && item.applicationInfo.uid > 10000) { // 判断是否为非系统预装的应用程序
				
			}
		}
		MyApplication.setUPDATE_APP_MAP(temp);
	}
}
