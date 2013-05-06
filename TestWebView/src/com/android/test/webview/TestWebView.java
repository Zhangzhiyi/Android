package com.android.test.webview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class TestWebView extends Activity {
	
	private WebView mWebView;
	private Handler mHandler = new Handler();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.webviewlayout);
        
        mWebView = (WebView) findViewById(R.id.myWebView);
        //WebSettings webSettings = mWebView.getSettings();
        //webSettings.setJavaScriptEnabled(true);
        
        /*mWebView.addJavascriptInterface(new Object(){
        	public void clickOnAndroid(){
        		mHandler.post(new Runnable(){
        			public void run(){
        				mWebView.loadUrl("javascript:wave()");
        			}
        		});
        	}
        }, "demo");*/ 
        mWebView.loadUrl("file:///android_asset/zh/tutorial_zh_page3.html");   
        
    }
}