package com.eric;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.widget.EditText;
public class main extends Activity {
	
	private EditText edittext;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        edittext = (EditText) findViewById(R.id.html);
        
        SmiliesEditText et=(SmiliesEditText)findViewById(R.id.EditText1);
        et.insertIcon(R.drawable.smile);
        
        
        
        /**通过html<img>标签获取图像**/
        Html.ImageGetter imageGetter = new ImageGetter() {
			
			@Override
			public Drawable getDrawable(String source) {
				// TODO Auto-generated method stub
				Drawable d = getResources().getDrawable(R.drawable.smile);
				d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
				return d;
			}
		};
		edittext.append(Html.fromHtml("<img/>", imageGetter, null));
        
        
    }
}