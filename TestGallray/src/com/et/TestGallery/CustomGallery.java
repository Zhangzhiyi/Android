package com.et.TestGallery;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class CustomGallery extends Gallery {
	
	private Camera mCamera = new Camera();
	private float mMaxZTranslation = -180f;
	
	private float perXaxis = 20;
	private float perYaxis = 20;
	private float perZaxis = 50;
	
	private int mCenterOfGallery;
	public CustomGallery(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		Log.i("tag", "1");
		setStaticTransformationsEnabled(true);
		setSpacing(0);
	}
	public CustomGallery(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
		Log.i("tag", "2");
		setStaticTransformationsEnabled(true);
		setSpacing(0);
	}
	
	/**计算Gallery控件宽度中间值，ItemView滑到中间ItemView宽度中间值也会等于此值**/
	public int getCenterOfGallery(){
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
        + getPaddingLeft();
	}
	/**获取现时ItemView的宽度中间值**/
	public int getCenterOfItemView(View view){
		int id = view.getId();
		Log.i(id+":view.getLeft()", String.valueOf(view.getLeft()));
        return view.getLeft() + view.getWidth() / 2;
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		 mCenterOfGallery = getCenterOfGallery();
		super.onSizeChanged(w, h, oldw, oldh);
	}
	/**每一个ItemView滑入屏幕时每一刻都会调用此方法**/
	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		// TODO Auto-generated method stub
		
		int childCenter = getCenterOfItemView(child);
		int childWidth = child.getWidth();
		/**获取child的Id打印Log调试**/
		int id = child.getId();
        //Log.i("childWidth:", String.valueOf(childWidth));
        Log.i(id+":childCenter", String.valueOf(childCenter));
        float x = 0;
        float y = 0;
        float z = 0;
        
        t.clear();
        t.setTransformationType(Transformation.TYPE_MATRIX);
        if(childCenter ==  mCenterOfGallery){
        	transformImageBitmap((ImageView) child, t, 0.0f, 0.0f, mMaxZTranslation);
        }
        else{
        	float gap = mCenterOfGallery - childCenter;
        	//x = Math.abs(gap)/childWidth*perXaxis;
        	//y = Math.abs(gap)/childWidth*perYaxis;
        	//z = Math.abs(gap)/childWidth*perZaxis;
        	z = mMaxZTranslation + Math.abs(gap);
        	transformImageBitmap((ImageView) child, t, x, y, z);
        	Log.i("Z-id:" + id, String.valueOf(z));
        	/*if(gap > 0){
        		transformImageBitmap((ImageView) child, t, x, y, z);
        	} 
        	else{
        		transformImageBitmap((ImageView) child, t, x, y, z);
        	}*/
        	
        }
		return true;
	}
	
	private void transformImageBitmap(ImageView child, Transformation t,
	            float x, float y, float z) {
	    mCamera.save();
	    final Matrix imageMatrix = t.getMatrix();
	    final int imageHeight = child.getHeight();//child.getLayoutParams().height;
	    final int imageWidth = child.getWidth();//child.getLayoutParams().width;
	
	    // 在Z轴上正向移动camera的视角，实际效果为放大图片。
	    // 如果在Y轴上移动，则图片上下移动；X轴上对应图片左右移动。
	    mCamera.translate(x, y, z);
	    
	    mCamera.getMatrix(imageMatrix);
	    //以图片的中心点为旋转中心,如果不加这两句，就是以（0,0）点为旋转中心
	    imageMatrix.postRotate(-90);
	    imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
	    imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
	    mCamera.restore();
	}
	
}
