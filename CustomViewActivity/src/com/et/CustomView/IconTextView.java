package com.et.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;


public class IconTextView extends TextView{
	 //  命名空间的值
	private final String nameSpace = "http://net.blogjava.mobile";
	 //  保存图像资源ID的变量
	private int resourceId = 0;
	private Bitmap bitmap;
	public IconTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		 //  getAttributeResourceValue 方用来获得组件属性的值，在本例中需要通过该方法的第1个参数指
		 //  定命名空间的值。该方法的第2个参数表示组件属性名（包括命名空间名称），第3个参数表示默
		 //  认值，也就是如果该属性不存在，则返回第3个参数指定的值
		resourceId = attrs.getAttributeResourceValue(nameSpace, "iconSrc",0);
		  //  如果 成功获得图像资源的ID，装载这个图像资源，并创建Bitmap对象
		if (resourceId > 0)
			bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
			//super.onDraw(canvas);   这句放在这里文本没有向右移动
			if (bitmap != null){
			//  从原图上截取图像的区域，在本例中为整个图像
			Rect src = new Rect();
			//  将截取的图像复制到bitmap上的目标区域，在本例中与复制区域相同
			Rect target = new Rect();
			src.left = 0;
			src.top = 0;
			src.right = bitmap.getWidth();
			src.bottom = bitmap.getHeight();
			
			int textHeight = (int) getTextSize();
			target.left = 0;
			 //  计算图像复制到目标区域的纵坐标。由于 TextView组件的文本内容并不是
			 //  从最顶端开始绘制的，因此，需要重新计算绘制图像的纵坐标
			target.top = (int) ((getMeasuredHeight() - getTextSize()) / 2) + 1;
			target.bottom = target.top + textHeight;
			//  为了保证图像不变形，需要根据图像高度重新计算图像的宽度
			target.right = (int) (textHeight * (bitmap.getWidth() / (float) bitmap.getHeight()));
			//  开始绘制图像
			canvas.drawBitmap(bitmap, src, target, getPaint());
			//  将TextView中的文本向右移动一定的距离（在本例中移动了图像宽度加2个象素点的位置）
			canvas.translate(target.right + 2, 0);
		}
			super.onDraw(canvas);//没有这句会效果只会有图片，没有文本。而且这一句必须放在这里，否则文本没有向右移动
	}
	

	
}