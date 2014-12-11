package com.et.TestIntentServiceAndResultReceiver;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.SlidingDrawer;

public class TaskService extends IntentService {
	public static final String TAG = "TaskService";

	/** Service的构造方法一定要没有参数，否则会启动不了 **/
	public TaskService() {
		super("TaskService");
	}

	/** IntentService执行完onHandleIntent会杀掉自己 **/
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getExtras() != null) {
			/*** 创建主线程的Handler*,否则发消息不能发回去，因为这是在子线程执行 */
			Handler handler = new Handler(getMainLooper());
			final ResultReceiver receiver = intent.getParcelableExtra("ResultReceiver");
			final Bundle resultData = new Bundle();
			resultData.putString("result", "success");
			// receiver.send(Activity.RESULT_OK, resultData);
			/** 延迟3秒发送执行成功消息 **/
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					receiver.send(Activity.RESULT_OK, resultData);
				}
			}, 3000);

		}
		/**最好不要在IntentService的onHandler方法里面使用AsynTask，可能会出现IntentService: Sending Message to a handler on dead thread**/
		new TestAsynTask().execute();
		new Thread() {
			public void run() {
				int count = 0;
				try {
					while (!isInterrupted()) {
						Log.i(TAG, "" + count);
						count++;
						sleep(1000);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}
 static class TestAsynTask extends AsyncTask<Void, Integer, Boolean>{

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {
			Log.i(TAG, "Sleep Start");
			TimeUnit.MILLISECONDS.sleep(5000);
			Log.i(TAG, "Sleep End");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
 }
}
