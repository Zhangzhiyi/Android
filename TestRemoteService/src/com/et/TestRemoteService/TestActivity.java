package com.et.TestRemoteService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.et.TestRemoteService.R;

public class TestActivity extends Activity {

	private TextView processText;
	private Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout);
		setTitle("TestActivity");
		processText = (TextView) findViewById(R.id.process);
		button = (Button) findViewById(R.id.Button01);
		
		int pid = Process.myPid();		
		Log.i("TestActivity Process ID", String.valueOf(pid));
		 processText.setText("进程号："+String.valueOf(pid));
		 
		 button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/**跳转回MainActiviy,无论这个activity当初是MainActiviy还是service跳转过来,
				 * 这个activity始终与MainActiviy保持在同一进程**/
				Intent mIntent = new Intent(TestActivity.this,MainActivity.class);
				startActivity(mIntent);
			}
		});
	}
	
	
}
