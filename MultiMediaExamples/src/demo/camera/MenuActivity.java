package demo.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity {
	
	//private Button btn1,btn2,btn3,btn4,btn5,btn6,btn7;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
//		btn1 = (Button)this.findViewById(R.id.btn1);
//		btn2 = (Button)this.findViewById(R.id.btn2);
//		btn3 = (Button)this.findViewById(R.id.btn3);
//		btn4 = (Button)this.findViewById(R.id.btn4);
//		btn5 = (Button)this.findViewById(R.id.btn5);
//		btn6 = (Button)this.findViewById(R.id.btn6);
//		btn7 = (Button)this.findViewById(R.id.btn7);
	}
	
	
	public void onClick(View v){
		int id = v.getId();
		Intent intent = null;
		switch (id) {
		case R.id.btn1:
		    intent = new Intent(MenuActivity.this, MainActivity.class); 
			break;
		case R.id.btn2:
			intent = new Intent(MenuActivity.this,PhotoManager.class);
			break;
		case R.id.btn3:
			intent = new Intent(MenuActivity.this, MyCamera.class);
			break;
		case R.id.btn4:
			intent = new Intent(MenuActivity.this, PhotoProcess.class);
			break;
		case R.id.btn5:
			intent = new Intent(MenuActivity.this, AudioDemo.class);
			break;
		case R.id.btn6:
			intent = new Intent(MenuActivity.this, BackgroundAudioDemo.class);
			break;
		case R.id.btn7:
			intent = new Intent(MenuActivity.this, InternetAudioDemo.class);
			break;
		case R.id.btn8:
			intent = new Intent(MenuActivity.this, AudioRecordDemo.class);
			break;
		case R.id.btn9:
			intent = new Intent(MenuActivity.this, VideoDemo.class);
			break;
		default:
			break;
		}
		
		startActivity(intent);
	}

}
