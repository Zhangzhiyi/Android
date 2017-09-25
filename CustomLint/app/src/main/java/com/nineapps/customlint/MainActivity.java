package com.nineapps.customlint;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.util.Timer;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("eeee");
        Log.e("111", "1111");
        Message message = new Message();
        if (message != null) {

        }

        Timer timer = new Timer();
        MyFragment myFragment = new MyFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
