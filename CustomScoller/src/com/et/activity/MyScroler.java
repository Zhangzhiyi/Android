package com.et.activity;

import android.app.Activity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.view.View.OnClickListener;

import android.widget.Button;

import android.widget.LinearLayout;

import android.widget.Scroller;

 

public class MyScroler extends Activity {

    /** Called when the activity is first created. */

    LinearLayout lay1,lay2,lay;

     private Scroller mScroller;

     private boolean s1,s2;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mScroller = new Scroller(this);
        
         lay1 = new LinearLayout(this){

             @Override 
             public void computeScroll() { 
            	 //super.computeScroll();
            	 /**这里要用mScroller.computeScrollOffset(),在里面计算当前Scroller位置，具体看源码**/
                 if (mScroller.computeScrollOffset()) { 
                	 /**View都可以设置滚动，可以参考SDK和源码**/
                	 Log.i("S1:mScroller.getCurrX()", String.valueOf(mScroller.getCurrX()));
                	 scrollTo(mScroller.getCurrX(), 0); 
                     postInvalidate(); 
                 }
             } 

         };
         
         lay2 = new LinearLayout(this){

             @Override 

             public void computeScroll() { 
            	 //super.computeScroll();
                 if (mScroller.computeScrollOffset()) { 
                	 Log.i("S2:mScroller.getCurrX()", String.valueOf(mScroller.getCurrX()));
                    // mScrollX = mScroller.getCurrX(); 
                	 /*if(s2)
                		 scrollTo(mScroller.getCurrX(), 0); */

                     postInvalidate(); 

                 } 

             } 

         };

      lay1.setBackgroundColor(this.getResources().getColor(android.R.color.darker_gray));

        lay2.setBackgroundColor(this.getResources().getColor(android.R.color.white));

        lay = new LinearLayout(this);

        lay.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams p0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);     

        this.setContentView(lay, p0);

        

        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);     

        p1.weight=1;

        lay.addView(lay1,p1);

        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);     

        p2.weight=1;

        lay.addView(lay2,p2);

        Button tx = new Button(this);

        Button tx2 = new Button(this);

        tx.setText("Button1");  

        tx2.setText("Button2");

        tx.setOnClickListener(new OnClickListener(){

            @Override

            public void onClick(View v) {

                if(!s1){

                    mScroller.startScroll(-100, 0, 100, 10, 5000);

                    s1 = true;

                }else{

                    mScroller.startScroll(30, 0, -50, -10,5000);

                    s1 = false;

                }

            }

            

        });

        tx2.setOnClickListener(new OnClickListener(){

            @Override

            public void onClick(View v) {

                if(!s2){

                    mScroller.startScroll(0, 0, 50, 20,5000);

                    s2=true;

                }else{

                    mScroller.startScroll(-20, -20, -50, -20,5000);

                    s2=false;

                }

            }

        });

        lay1.addView(tx);
        Button btn = new Button(this);
        btn.setText("11111111");
        lay1.addView(btn);
        lay2.addView(tx2);

    }

}

