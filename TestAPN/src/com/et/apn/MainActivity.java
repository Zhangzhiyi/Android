package com.et.apn;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Uri uri = Uri.parse("content://telephony/carriers");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        
        
    }
}