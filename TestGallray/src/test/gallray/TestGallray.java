package test.gallray;

import android.app.Activity;
import android.os.Bundle;

import com.et.TestGallery.CustomGallery;
import com.et.TestGallery.ImageAdapter;

public class TestGallray extends Activity {
	
	private CustomGallery customGallery;
	private ImageAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RocateLayout layout = new RocateLayout(this);
		customGallery = new CustomGallery(this);
		mAdapter = new ImageAdapter(this);
        customGallery.setAdapter(mAdapter);
        customGallery.setSelection(2);
        
        layout.addView(customGallery);
        layout.setBackgroundResource(R.drawable.background);
        setContentView(layout);
		
	}
}