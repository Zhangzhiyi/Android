package com.TestUI;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
public class IntentActivity extends Activity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		LinearLayout layout=new LinearLayout(this);
		/*//setContentView(R.layout.test_layout);
		ImageView image=new ImageView(this);
		//image.setImageResource(R.drawble.left); 为什么在这里用这个方法不行？？
		image.setImageDrawable(getResources().
                getDrawable(R.drawable.right));
		layout.addView(image);
		//setContentView(layout);*/
		
		Spinner spinner=new Spinner(this);
//		spinner.setLayoutParams(new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
//			      LinearLayout.LayoutParams.WRAP_CONTENT));//设置控件大小
		final String[] country={"China" ,"Russia", "Germany",
		"Ukraine", "Belarus", "USA" };
		ArrayAdapter<String> array=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,country);
		spinner.setAdapter(array);
		array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		layout.addView(spinner);
		setContentView(layout);
		
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{
			 
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				setTitle(country[position]);
			}
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}