package com.et.CustomEditText.view;

import com.et.CustomEditTextActivity.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**自定义有下划线的EditText**/
public class LinedEditText extends EditText {
        private Rect mRect;
        private Paint mPaint;
         
        /** 记得还要设置gravity为top，因为默认光标是在中间的，画线是从中间画起的**/ 
        // we need this constructor for LayoutInflater
        public LinedEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
            
            mRect = new Rect();
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(R.color.gray);
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
            int count = getLineCount();       	
            Rect r = mRect;
            Paint paint = mPaint;
            int height = getHeight();
            int baseline = 0;
            for (int i = 0; i < count; i++) {
                baseline = getLineBounds(i, r);
                /*测试数据**/
                /*int bottom = r.bottom;
                int getBaseLine = getBaseline();
                int lineHeight = getLineHeight();

                Log.i("baseline", String.valueOf(baseline));
                Log.i("bottom", String.valueOf(bottom));
                Log.i("getBaseLine", String.valueOf(getBaseLine));
                Log.i("lineHeight", String.valueOf(lineHeight));*/
                Log.i("height", String.valueOf(height));
                
                canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
            }
            
            int lineHeight = getLineHeight();
            while (baseline < height){
            	baseline = baseline + lineHeight;
                canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
            }
           

            super.onDraw(canvas);
        	
        }
    }
		