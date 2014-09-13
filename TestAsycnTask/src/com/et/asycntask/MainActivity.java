package com.et.asycntask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.et.asycntask.MainActivity.AsyncTaskAdapter.ViewHolder;

public class MainActivity extends Activity {

	public ProgressBar mProgressBar;
	public ListView mListView;
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/**线程规则二：execute(Params...)必须在UI线程上调用,
		 * 这里可以同时传入多个值,如:new DownloadFilesTask().execute(url1, url2, url3);**/
		
		mListView = (ListView) findViewById(R.id.listView1);
		mListView.setAdapter(new AsyncTaskAdapter(this, 20));
	}

	//继承于AsyncTask
	public static class SimpleAsyncTask extends AsyncTask<Void, Integer, Integer>{
		private ViewHolder mViewHolder;
		private WeakReference<ProgressBar> mReference;
		private int value;
		public SimpleAsyncTask(ViewHolder viewHolder) {
			// TODO Auto-generated constructor stub
			mViewHolder = viewHolder;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(Void... params) {
			try {
				while (value <= 100) {
					value ++;
					publishProgress(value);
					TimeUnit.MILLISECONDS.sleep(100); 
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}
		/**在doInBackground调用publishProgress就会调用此方法**/
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			mViewHolder.progressBar.setProgress(values[0]);
		}
		/**onPostExecute的参数result是doInBackground最后的返回值**/
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public static class AsyncTaskAdapter extends BaseAdapter{
		private List<SimpleAsyncTask> asyncTasks;
		private Context context;
		private int mCount;
		public AsyncTaskAdapter(Context context, int count) {
			// TODO Auto-generated constructor stub
			this.context = context;
			asyncTasks = new ArrayList<MainActivity.SimpleAsyncTask>(count);
			mCount = count;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
//			if (asyncTasks != null) {
//				return asyncTasks.size();
//			}
			return mCount;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_layout, null);
				viewHolder.textView = (TextView) convertView.findViewById(R.id.textView1);
				viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar1);
				SimpleAsyncTask simpleAsyncTask = new SimpleAsyncTask(viewHolder);
//				simpleAsyncTask.execute((Void)null);
				simpleAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);
				asyncTasks.add(simpleAsyncTask);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.textView.setText("AsyncTask #" + position);
			return convertView;
		}
		public static class ViewHolder{
			TextView textView;
			ProgressBar progressBar;
		}
	}
}