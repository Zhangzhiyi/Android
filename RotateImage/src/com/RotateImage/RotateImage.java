package com.RotateImage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;


public class RotateImage extends Activity {
	
	int transX = 10;
    @Override
    /**3种利用Matrix来实现图片各种效果**/
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* LinearLayout layout=new LinearLayout(this);
        
        Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.ruhua);//加载图片
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        
        Matrix matrix=new Matrix();
        Log.i("Matrix", matrix.toString());
        //matrix.postRotate(90); //旋转90度
        matrix.setScale(-1, -1);
      //发现移动没有效果~ 因为用addView方法父布局Linearlayout会调用用requestLayout()强制重新布局
        matrix.preTranslate(100, 20);
        matrix.preRotate(30); 
        Log.i("Matrix", matrix.toString());
        
        Bitmap newbitmap=Bitmap.createBitmap(bitmap, 0, 0, width, height,matrix,true);//创建新图片
        //BitmapDrawable bitmapdrawable=new BitmapDrawable(newbitmap);//将上面创建的Bitmap转换成Drawable对象，使得其可以使用在ImageView, ImageButton中 
        ImageView image=new ImageView(this);
        image.setImageBitmap(newbitmap);//设置显示上面的新图片（两种方法）
        //image.setImageDrawable(bitmapdrawable);//设置显示上面的新图片
        
        //image.setScaleType(ScaleType.CENTER);//设置居中显示
          
        layout.addView(image,new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        setContentView(layout);*/
       
        
        /***注意要在xml设置ImageView的android:scaleType="matrix",以及大小占整个屏幕,否则限定自身大小会看不到效果
         * 因为Matrix变换的效果可能回超出是在View所占的空间,而父布局只会绘画子view所占空间里面的内容,所以还要计算移动,
         * 会比较麻烦; 如果ImageView应用Amination是可以超出自己所占空间的**/
        setContentView(R.layout.main);
        final ImageView image = (ImageView) findViewById(R.id.imageView1);
        /*Matrix matrix=new Matrix();
        matrix.setScale(-1, -1);
        matrix.postTranslate(150, 150);
        image.setImageMatrix(matrix);*/
        
        image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Matrix matrix=new Matrix();
				transX = transX + 10;
				/**postTranslate(float dx, float dy)的参数指的是坐标，不是距离哦**/
		        matrix.postTranslate(10, 0);
		        image.setImageMatrix(matrix);
			}
		});
        
       // setContentView(new MyView(this));
        
    }
    
    //实现在屏幕画图片
    //由于是自定义view,所以不需要调用Layout文件
    
    public class MyView extends View{
    	Bitmap bitmap;
    	Paint paint;
    	Drawable image;
    	Matrix matrix;
    	public MyView(Context context){
    		super(context);    		
            
            //设置图片要画的宽高  (还是不太明白，还是用Matrix好用)
            image = getResources().getDrawable(R.drawable.ruhua);
            /**创建一个位图，设置好宽高**/
            bitmap=Bitmap.createBitmap(80,80, Config.ARGB_8888);
            /**创建此为图的画布**/
            Canvas canvas = new Canvas(bitmap);
            /**将drawable画上位图的画布**/
            image.setBounds(0, 0, 80,80);
            image.draw(canvas);
           
            
            
           // bitmap=Bitmap.createBitmap(bitmap, 20, 20, 50, 50);//重新设置要画图片的那一部分
            
            paint = new Paint();
            matrix = new Matrix();
            //matrix.setRotate(30,40,40);
            matrix.setScale(-1, -1);
          //注意矩阵乘法交换是不对等的.post是前乘，pre是后乘;如果用pre输出matrix看到与Translate有关的是-80，-160;
            matrix.postTranslate(80, 160);
            Log.i("Matrix", matrix.toString());
            
    	}
    	//在自定义VIEW时，必须实现此方法
    	public void onDraw(Canvas canvas){
    		super.onDraw(canvas);
    		canvas.drawBitmap(bitmap, 0, 0, paint);
    		canvas.drawBitmap(bitmap, matrix, paint);
    		
    		
    	}
    }
}