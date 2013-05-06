package com.et.TestTypeValue;

import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.util.DebugUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

public class MainActivity extends Activity {
	
	//尝试不同单位 R.dimen.px;  R.dimen.sp;
	//int dimens = R.dimen.dip; 
	int dimens = R.dimen.px; 
	//int dimens = R.dimen.sp; 
	
	boolean TRACEVIEW_BOOLEAN = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //调试查看效率
        if (TRACEVIEW_BOOLEAN) {
        	Debug.startMethodTracing();
		}
        float dimention = getResources().getDimension(dimens);
        Log.i("getDimension", "" + dimention);
        float value = TypedValue.complexToFloat((int)dimention);
        Log.i("tag", "" + value);
       // value = TypedValue.c
        
        //不变
        value = getResources().getDimension(dimens);
        Log.i("getDimension", "" + value);
        
        //小数取最小整数
        value = getResources().getDimensionPixelOffset(dimens);
        Log.i("getDimensionPixelOffset", "" + value);
        
        //小数点后四舍五入
        value = getResources().getDimensionPixelSize(dimens);
        Log.i("getDimensionPixelSize", "" + value);
        
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        value = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimention, displayMetrics);
        Log.i("dip 转 dip", "" + value);
        
        /**如果百分数值带有p结尾，就用第三个参数相乘**/
        float base = getResources().getFraction(R.fraction.base, 2, 3);
        float parent = getResources().getFraction(R.fraction.parent, 2, 3);
        Log.i("base fraction", "" + base);
        Log.i("parent fraction", "" + parent);
        
        int i = 0;
        while (i<100000) {
			i++;
		}
        if (TRACEVIEW_BOOLEAN) {
			Debug.stopMethodTracing();
		}
    }
}