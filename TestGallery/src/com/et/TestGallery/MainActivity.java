package com.et.TestGallery;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	private CustomGallery customGallery;
	private ImageAdapter mAdapter;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        customGallery = (CustomGallery) findViewById(R.id.gallery);
        mAdapter = new ImageAdapter(this);
        customGallery.setAdapter(mAdapter);
        customGallery.setSelection(2);
        
    }
}