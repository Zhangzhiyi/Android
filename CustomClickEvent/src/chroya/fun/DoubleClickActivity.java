package chroya.fun;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

public class DoubleClickActivity extends Activity {
	private static final String LOG_TAG = "DoubleClickActivity";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DoubleClickView v = new DoubleClickView(this);
        v.setOnDoubleClickListener(new DoubleClickView.OnDoubleClickListener() {
			
			@Override
			public void onDoubleClick(View v) {
				// TODO Auto-generated method stub
				Log.d(LOG_TAG, "DoubleClick");
				Toast.makeText(DoubleClickActivity.this, "DoubleClick", Toast.LENGTH_SHORT).show();
			}
		});
        setContentView(v);
	}
}
