package com.et.TestIntentServiceAndResultReceiver;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class TaskService extends IntentService {

	/**Service的构造方法一定要没有参数，否则会启动不了**/
	public TaskService() {
		super("TaskService");
	}
	/**IntentService执行完onHandleIntent会杀掉自己**/
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getExtras() != null) {
			/***创建主线程的Handler*,否则发消息不能发回去，因为这是在子线程执行*/
			Handler handler = new Handler(getMainLooper());
			final ResultReceiver receiver = intent.getParcelableExtra("ResultReceiver");
			final Bundle resultData = new Bundle();
			resultData.putString("result", "success");
//			receiver.send(Activity.RESULT_OK, resultData);
			/**延迟3秒发送执行成功消息**/
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					receiver.send(Activity.RESULT_OK, resultData);
				}
			}, 3000);
			
		}
	}

}
