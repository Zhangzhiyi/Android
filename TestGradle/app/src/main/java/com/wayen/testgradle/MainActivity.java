package com.wayen.testgradle;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends Activity implements View.OnClickListener {

	private TextView mTextView;
	CallbackManager callbackManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();
		setContentView(R.layout.activity_main);

		mTextView = (TextView) findViewById(R.id.produce_flavors);
		findViewById(R.id.button).setOnClickListener(this);
		mTextView.setText(Constants.NAME);
		// Log.i("", BuildConfig.SERVER_URL);

		LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setReadPermissions("email", "user_friends");
		loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				Log.i("FacebookCallback", "onSuccess");
				final AccessToken accessToken = loginResult.getAccessToken();
				GraphRequestAsyncTask requestAsyncTask =
						new GraphRequestAsyncTask(GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
						String email = jsonObject.optString("email");
						Log.i("FacebookCallback", "email:" + email);
					}
				}));
				requestAsyncTask.execute();
			}

			@Override
			public void onCancel() {
				Log.i("FacebookCallback", "onCancel");
			}

			@Override
			public void onError(FacebookException e) {
				Log.i("FacebookCallback", "FacebookException");
			}
		});

		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (PackageManager.NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}



	}

	static String getE2E() {
		JSONObject e2e = new JSONObject();
		try {
			e2e.put("init", System.currentTimeMillis());
		} catch (JSONException e) {
		}
		return e2e.toString();
	}

	public void facebookLogin() {
		Intent intent = new Intent().setClassName("com.facebook.katana", "com.facebook.katana.ProxyAuth").putExtra("client_id", "809032719161576");
		intent.putExtra("scope", TextUtils.join(",", new String[] {"email", "public_profile", "user_friends"}));
		intent.putExtra("e2e", getE2E());
		intent.putExtra("response_type", "token,signed_request");
		intent.putExtra("return_scopes", "true");
		startActivityForResult(intent, 0);
	}

	private String getError(Bundle extras) {
		String error = extras.getString("error");
		if (error == null) {
			error = extras.getString("error_type");
		}
		return error;
	}

	private String getErrorMessage(Bundle extras) {
		String errorMessage = extras.getString("error_message");
		if (errorMessage == null) {
			errorMessage = extras.getString("error_description");
		}
		return errorMessage;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			Bundle extras = data.getExtras();
			String error = getError(extras);
			String errorCode = extras.getString("error_code");
			String errorMessage = getErrorMessage(extras);
			if (error == null && errorCode == null && errorMessage == null) {
				String token = extras.getString(AccessToken.ACCESS_TOKEN_KEY);
                String signed_request = extras.getString("signed_request");
				String userId = getUserIDFromSignedRequest(signed_request);
			}
		} else if (resultCode == Activity.RESULT_CANCELED) {

		}
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	private static String getUserIDFromSignedRequest(String signedRequest) {
		try {
			String[] signatureAndPayload = signedRequest.split("\\.");
			if (signatureAndPayload.length == 2) {
				byte[] data = Base64.decode(signatureAndPayload[1], Base64.DEFAULT);
				String dataStr = new String(data, "UTF-8");
				JSONObject jsonObject = new JSONObject(dataStr);
				return jsonObject.getString("user_id");
			}
		} catch (UnsupportedEncodingException ex) {
		} catch (JSONException ex) {
		}
		throw new FacebookException("Failed to retrieve user_id from signed_request");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button:
				facebookLogin();
				break;
		}
	}
}
