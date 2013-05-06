package org.pskink.animseekbar;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

public class AnimSeekBarTest extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setup((SeekBar) findViewById(R.id.seek_bar0), 25);
        setup((SeekBar) findViewById(R.id.seek_bar1), 50);
        setup((SeekBar) findViewById(R.id.seek_bar2), 75);
        
    }
    
	private void setup(SeekBar seekBar, int v) {
		Resources res = getResources();
		int max = seekBar.getMax();
		Log.i("max", String.valueOf(max));
		boolean flag = v < seekBar.getMax() / 2;
		Log.i("boolean", String.valueOf(flag));
		Drawable d = new AnimSeekBarDrawable(res, v < seekBar.getMax() / 2);
		seekBar.setProgressDrawable(d);
		seekBar.setProgress(v);
	}
}