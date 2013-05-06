package com.et.CustomAnimation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class CustomAnimation extends Animation {
	
	Camera camera = new Camera();  
	int mCenterX; 
    int mCenterY;  
	
	/**Animation 类及其子类
	Animation 类及其子类是动画的核心模块，它实现了各种动画效果，如平移、缩 放、旋转、改变透明度等。
	Tween 动画的每一桢都根据 Interpolator 对 view 的内容做一次图形变换，因此 Animation 的核心工作是做变换(transformation)。
	Aniamtion 是基类，他记录了动画的通用属性和方法。主要的属性包括动画持续时间、重复次数、interpolator 等。
	动画里最重要的方法是 getTransformation (currentTime, outTransformation)，该方法根据当前时间 (currentTime) 和 interpolator，
	计算当前的变换，在 outTransformation 中返回。
	TranslateAnimation、RotateAnimation、AlphaAnimation 等是 Animation 的 子类，分别实现了平移、旋转、改变 Alpha 值等动画。
	每个动画都重载了父类的 applyTransformation 方法，这个方法会被父类的 getTransformation 方法调用。另外每个动画还有个 initialize 方法，完成初始化工作。**/
	public CustomAnimation() {
		// TODO Auto-generated constructor stub
	}	
	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		// TODO Auto-generated method stub
		   super.initialize(width, height, parentWidth, parentHeight);
		   Log.i("width", String.valueOf(width));
		   Log.i("height", String.valueOf(height));
		   Log.i("parentWidth", String.valueOf(parentWidth));
		   Log.i("parentHeight", String.valueOf(parentHeight));
		   
		   mCenterX = width/2;   
		   mCenterY = height/2;  
		   setDuration(5000);  
		   //设置当动画结束是否保持当前效果，false为恢复原状
		   setFillAfter(true);
		   //设置动画重复次数
		   //setRepeatCount(Animation.INFINITE);
		   setInterpolator(new LinearInterpolator()); 
	}	
	/**在动画效果绘制的过程中会不断调用此方法，该方法根据当前时间 (currentTime) 和 interpolator，计算当前的变换，
	 * 在 outTransformation 中返回**/
	@Override
	public boolean getTransformation(long currentTime,
			Transformation outTransformation) {
		// TODO Auto-generated method stub
		Log.i("getTransformation", "getTransformation");
		return super.getTransformation(currentTime, outTransformation);
	}
	/**在绘制动画的过程中会反复的调用applyTransformation函数，每次调用参数interpolatedTime值都会变化，
	 * 该参数从0渐 变为1，当该参数为0时表明动画开始，1时表明动画结束。通过参数Transformation 来获取变换的矩阵（matrix），
	 * 通过改变矩阵就可以实现各种复杂的效果。*/
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		// TODO Auto-generated method stub
		/**用上Camera可以有3D的效果**/
		final Matrix matrix = t.getMatrix();  
	   camera.save();  
	   camera.translate(0.0f, 0.0f, (5000 - 4500.0f * interpolatedTime));  
	   camera.rotateY(360 * interpolatedTime);
	   camera.rotateX(360*interpolatedTime);
	   camera.getMatrix(matrix);  
	   matrix.preTranslate(-mCenterX, -mCenterY);  
	   matrix.postTranslate(mCenterX, mCenterY);  
	   camera.restore();  
	}
}
