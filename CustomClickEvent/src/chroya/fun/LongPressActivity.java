package chroya.fun;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

public class LongPressActivity extends Activity {
	private static final String LOG_TAG = "LongPressActivity";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   
        View v = new LongPressView2(this);
        v.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Log.d(LOG_TAG, "onLongClick");
				Toast.makeText(LongPressActivity.this, "LongPress", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
        setContentView(v);
	}
}
