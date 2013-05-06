package com.ray.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Test extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }
    
    class MyView extends SurfaceView implements SurfaceHolder.Callback{

    	private SurfaceHolder mHolder;
    	private Canvas canvas;
		public MyView(Context context) {
			super(context);
			mHolder = getHolder();
			mHolder.addCallback(this);
		}
		
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			canvas = mHolder.lockCanvas();
			Paint mPaint = new Paint();
			mPaint.setColor(Color.BLUE);
			canvas.drawRect(100, 200, 200, 300, mPaint);
			
			/**注销canvas.save();和canvas.restore();看看效果  注销后蓝色没有旋转，
			 * 而红色和绿色都旋转了，画布进行操作后对后面的代码有效**/			
			canvas.save();
			canvas.rotate(45);
			mPaint.setColor(Color.RED);
			canvas.drawRect(150, 10, 200, 60, mPaint);
			canvas.restore();
			
			mPaint.setColor(Color.GREEN);    
			canvas.drawRect(200, 10, 250, 100, mPaint);   
			
			canvas.save();
			canvas.translate(20, 0);
			canvas.drawRect(0, 0, 10, 10, mPaint);
			canvas.restore();
			
			canvas.drawRect(0, 0, 10, 10, mPaint);
			
			mHolder.unlockCanvasAndPost(canvas);
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
		}
    	
    }
}