package com.et.Activity;

import android.app.Activity;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ClipDrawableActivity extends Activity{
	
	private ImageView mImageView01;
	private ImageView mImageView02;
	private ImageView mImageView03;
	private Button mButton01;
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clipdrawable);
		mImageView01 = (ImageView) findViewById(R.id.imageView1);
		mImageView02 = (ImageView) findViewById(R.id.imageView2);
		mImageView03 = (ImageView) findViewById(R.id.imageView3);
		mButton01 = (Button) findViewById(R.id.button1);
        mButton01 = (Button) findViewById(R.id.button1);
        
        mButton01.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ClipDrawable drawable01 = (ClipDrawable) mImageView01.getDrawable();
				if(drawable01.getLevel()<10000)
					drawable01.setLevel(drawable01.getLevel()+1000);
				ClipDrawable drawable02 = (ClipDrawable) mImageView02.getDrawable();
				if(drawable02.getLevel()<10000)
					drawable02.setLevel(drawable02.getLevel()+1000);
				ClipDrawable drawable03 = (ClipDrawable) mImageView03.getDrawable();
				if(drawable03.getLevel()<10000)
					drawable03.setLevel(drawable03.getLevel()+1000);
				
			}
		});
	}
	
}
