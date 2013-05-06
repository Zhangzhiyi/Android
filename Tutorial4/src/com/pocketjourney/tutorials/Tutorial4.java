package com.pocketjourney.tutorials;

import com.pocketjourney.view.ImageOnlyButton;
import com.pocketjourney.view.TextOnlyButton;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Tutorial4 extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.tutorial4);
        
        TextOnlyButton button = (TextOnlyButton) findViewById(R.id.text_only_button);
        button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Toast.makeText(Tutorial4.this, "Text Button Clicked",Toast.LENGTH_SHORT).show();
        }});

        ImageOnlyButton button2 = (ImageOnlyButton) findViewById(R.id.image_only_button);
        button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Toast.makeText(Tutorial4.this, "Image Button Clicked",Toast.LENGTH_SHORT).show();
        }});
        
        int color = getResources().getColor(R.color.white);
        int color1 = getResources().getColor(R.color.black);
    }
}