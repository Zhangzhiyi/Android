package com.et.asycntask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

	public ProgressBar mProgressBar;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
		/**线程规则二：execute(Params...)必须在UI线程上调用,
		 * 这里可以同时传入多个值,如:new DownloadFilesTask().execute(url1, url2, url3);**/
		new AsyncLoader().execute((Void)null);
	}

	//继承于AsyncTask
	class AsyncLoader extends AsyncTask<Void, Integer, Integer>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mProgressBar.setProgress(0);
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(Void... params) {
			
			for (int i = 0; i < 100; i++) {
				Log.i("i", String.valueOf(i));
			}
			//更新进度
			publishProgress(30);
			for (int i = 0; i < 100; i++) {
				Log.i("i", String.valueOf(i));
			}
			publishProgress(60);
			for (int i = 0; i < 100; i++) {
				Log.i("i", String.valueOf(i));
			}
			publishProgress(90);
			for (int i = 0; i < 1000; i++) {
				Log.i("i", String.valueOf(i));
			}
			return 100;
		}
		/**在doInBackground调用publishProgress就会调用此方法**/
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub			
			mProgressBar.setProgress(values[0]);
			super.onProgressUpdate(values);
		}
		/**onPostExecute的参数result是doInBackground最后的返回值**/
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if(result == 100){
				mProgressBar.setProgress(result);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent intent = new Intent(MainActivity.this, Double2.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
				
			super.onPostExecute(result);
		}
		
	}
}