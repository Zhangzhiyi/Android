package com.et.TestGallery;

import test.gallray.R;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	
	
	private Context context;
	public ImageAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		//return mImageIds.length;
		return Integer.MAX_VALUE;
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
		Log.i("position", String.valueOf(position));
		ImageView mImageView = new ImageView(context);
		mImageView.setId(position);
		if(position==0)
			position = mImageIds.length;
		mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
		mImageView.setImageResource(mImageIds[position%mImageIds.length]);
		mImageView.setLayoutParams(new Gallery.LayoutParams(80, 80));
		//mImageView.setBackgroundResource(0);
        
        return mImageView;
	}
	/*private int[] mImageIds = {
            R.drawable.gallery_photo_1,
            R.drawable.gallery_photo_2,
            R.drawable.gallery_photo_3,
            R.drawable.gallery_photo_4,
            R.drawable.gallery_photo_5,
            R.drawable.gallery_photo_6,
            R.drawable.gallery_photo_7,
            R.drawable.gallery_photo_8
    };*/
	private int[] mImageIds = {
			R.drawable.mobile_photo_0, 
			R.drawable.mobile_photo_1,
			R.drawable.mobile_photo_2,
			R.drawable.mobile_photo_3,
			R.drawable.mobile_photo_4,
			R.drawable.mobile_photo_5};
}
