package chroya.fun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CustomClickActivity extends Activity {
	
	private Button mLongClick;
	private Button mDoubleClick;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		mLongClick = (Button) findViewById(R.id.longclick);
		mDoubleClick = (Button) findViewById(R.id.doubleclick);
		
		mLongClick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(CustomClickActivity.this, LongPressActivity.class);
				startActivity(mIntent);
			}
		});
		
		mDoubleClick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(CustomClickActivity.this, DoubleClickActivity.class);
				startActivity(mIntent);
			}
		});
	}
}
