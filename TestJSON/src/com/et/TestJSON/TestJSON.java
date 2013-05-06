package com.et.TestJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TestJSON extends Activity {
	TextView textView ;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        textView = (TextView) findViewById(R.id.textview);
        
        JSONObject jsonObject = new JSONObject();        
        JSONArray array = new JSONArray();
        
        
        
        try {
        	String name = "Tom";
			jsonObject.put("name", name);
			array.put(true);
	        array.put(1);
	        array.put(2.0);
	        jsonObject.put("array", array);
	        String str = jsonObject.toString();
	        JSONObject otherObject = new JSONObject(str);
	        Log.i("JSON", otherObject.toString());
	        name = otherObject.getString("name");
	        Log.i("name", name);
	        
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		textView.setText(jsonObject.toString());
        
    }
}