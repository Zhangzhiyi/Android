package com.mcs.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {
	
	private EditText userNameEdit;
	private EditText passwordEdit;
	private Button   loginBtn;
	private Button   cancelBtn;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        userNameEdit = (EditText)findViewById(R.id.edit_user_name);
        passwordEdit = (EditText)findViewById(R.id.edit_password);
        loginBtn     = (Button)  findViewById(R.id.btn_login);
        cancelBtn    = (Button)  findViewById(R.id.btn_cancel);
        
        loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View btn) {
				// Start default activity.
				Intent defAct = new Intent(Login.this, Main.class);
				startActivity(defAct);
			}
        });
        cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Quit login activity.
				Login.this.finish();
			}
        });
    }
}