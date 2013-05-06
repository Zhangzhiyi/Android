package com.et.TestImageMove;

import android.app.Activity;
import android.app.Service;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

public class TestImageMove extends Activity {
    /** Called when the activity is first created. */
	
	private ImageView image;
	private AlphaAnimation alpha;
	private int width,height;
	private int startX,startY;
	
	private Vibrator mVibrator;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /**下面一种情况是有加上一个图片视图，测试image移动该视图是否会被遮挡，
         * 证实是会被新加入的图片视图遮挡住，这种情况的解决办法是
         * 用WindowManager来在屏幕上加入和更新移动image，
         * 注意WindowManager.addView(View view, ViewGroup.LayoutParams params)
         * 的第二个参数一定要用WindowManager.LayoutParams*/
        //setContentView(R.layout.test);
        
        mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        
        image=(ImageView) findViewById(R.id.ruhua);
        width=image.getWidth();
        height=image.getHeight();
        image.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int x=(int)event.getRawX();//注意这里用getRaw
				int y=(int)event.getRawY();
				//v.setLayoutParams(new AbsoluteLayout.LayoutParams(width,height, x-width/2, y-height/2));
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					{
						
							Log.i("down", "down");
							//mVibrator.vibrate(new long[]{0,50,0,0}, -1);							
							/**以图片左上角为原点，计算图片点击点到左上角原点的纵横距离**/
							startX = (int)event.getX();
							startY = y - v.getTop();
				 		
						
						return true;
					}
				if(event.getAction()==MotionEvent.ACTION_MOVE)
				{
					Log.i("move", String.valueOf(event.getRawX()));
					//v.setLayoutParams(new AbsoluteLayout.LayoutParams(v.getWidth(),v.getHeight(), x-v.getWidth()/2, y-v.getHeight()/2));
					v.layout(x - startX, y - startY, x + v.getWidth() - startX, y - startY + v.getHeight());
					
					/**不可以加这句.否则view会通知父布局来重新布局的,
					 * 现在的布局是LinearLayout,LinearLayout会安照它的方式布局**/
					//v.requestLayout();
					return true;
				}
				
				return false;
				
			}
		});
        
    }
   
}