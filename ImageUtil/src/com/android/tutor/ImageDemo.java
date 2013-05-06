package com.android.tutor;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
public class ImageDemo extends Activity {
	private ImageView mImageView01,mImageView02;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupViews();
    }
   
    private void setupViews(){
    	mImageView01 = (ImageView)findViewById(R.id.image01);
    	mImageView02 = (ImageView)findViewById(R.id.image02);
    	
    	//获取壁纸返回值是Drawable
    	Drawable drawable = getWallpaper();
    	//将Drawable转化为Bitmap
    	Bitmap bitmap = ImageUtil.drawableToBitmap(drawable);
    	//缩放图片
    	Bitmap zoomBitmap = ImageUtil.zoomBitmap(bitmap, 100, 100);
    	//获取圆角图片
    	Bitmap roundBitmap = ImageUtil.getRoundedCornerBitmap(zoomBitmap, 20.0f);
    	//获取倒影图片
    	Bitmap reflectBitmap = ImageUtil.createReflectionImageWithOrigin(zoomBitmap);
    	//这里可以让Bitmap再转化为Drawable
//    	Drawable roundDrawable = new BitmapDrawable(roundBitmap);    	
//    	Drawable reflectDrawable = new BitmapDrawable(reflectBitmap);   	
//    	mImageView01.setBackgroundDrawable(roundDrawable);
//    	mImageView02.setBackgroundDrawable(reflectDrawable);
    	    	
    	mImageView01.setImageBitmap(roundBitmap);
    	mImageView02.setImageBitmap(reflectBitmap);
    }
      
       
}