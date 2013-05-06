package com.et.TestIntentServiceAndResultReceiver;

import com.example.testintentservice2resultreceiver.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity{

	private TextView mShowText;
	private Button mTaskButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mShowText = (TextView) findViewById(R.id.textView1);
		mTaskButton = (Button) findViewById(R.id.button1);
		mTaskButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, TaskService.class);
				CallBackResult callBackResult = new CallBackResult(new Handler());
				intent.putExtra("ResultReceiver", callBackResult);
				startService(intent);
				mShowText.setText("开始执行任务");
			}
		});
	}
/** 1.    调用startService。
	2.    service中开始操作处理，并且通过消息告诉activity处理已经开始。
	3.    activity处理消息并且显示进度条
	4.    service完成处理并且返回给activity需要的数据。
	5.    activity处理数据。
	6.    service通过消息告诉activity处理完成，并且kill掉自己。
	7.    activity取得消息并且结束掉进度条。**/
	class CallBackResult extends ResultReceiver{
		
		public CallBackResult(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}
		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			// TODO Auto-generated method stub
			if (resultCode == RESULT_OK) {
				String result = resultData.getString("result");
				mShowText.setText(result);
			}
		}
	}
}
