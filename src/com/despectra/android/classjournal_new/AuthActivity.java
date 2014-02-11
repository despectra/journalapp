package com.despectra.android.classjournal_new;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class AuthActivity extends Activity implements OnClickListener, OnEditorActionListener {

	public static final String BACKGROUND_FRAGMENT_TAG = "login_fragment";
	
	private String mToken;
	private Button mLoginBtn;
	private EditText mLoginEdit;
	private EditText mPassEdit;
	private TextView mResponseText;
	private ProgressDialog mLoggingDialog;
	private BackgroundServiceReceiver mLoginReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		mLoginReceiver = new BackgroundServiceReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(BackgroundService.ACTION_LOGIN);
		filter.addAction(BackgroundService.ACTION_GET_PROFILE);
		
		registerReceiver(mLoginReceiver, filter);
		SharedPreferences prefs = getSharedPreferences(PreferencesManager.USER_DATA_PREFS, MODE_PRIVATE);
		if(prefs.contains("token") && !prefs.getString("token", "").equals("")) {
			
		}
	}
	
	private void initUi() {
		setContentView(R.layout.activity_auth);
		mLoginEdit = (EditText)findViewById(R.id.login_edit);
		mLoginEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		mPassEdit = (EditText)findViewById(R.id.password_edit);
		mPassEdit.setImeActionLabel("Login", KeyEvent.KEYCODE_ENTER);
		mPassEdit.setOnEditorActionListener(this);
		mLoginBtn = (Button)findViewById(R.id.login_btn);
		mResponseText = (TextView)findViewById(R.id.response_text);
		
		mLoggingDialog = new ProgressDialog(this);
		mLoggingDialog.setTitle("Log in");
		mLoggingDialog.setMessage("Logging inprocess");
		mLoggingDialog.setCancelable(false);
		
		mLoginBtn.setOnClickListener(this);
	}
	
	private void skipLogging() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		return;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e("TAG", "onDestroy");
		if(mLoginReceiver != null) {
			unregisterReceiver(mLoginReceiver);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.auth, menu);
		return true;
	}

	@Override
	public void onClick(View clickedView) {
		performLogin();
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == EditorInfo.IME_ACTION_DONE) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			performLogin();
		}
		return true;
	}
	
	private void performLogin() {
		String login = mLoginEdit.getText().toString();
		String pass = mPassEdit.getText().toString();
		if(login.isEmpty() || pass.isEmpty()) {
			return;
		}
		mResponseText.setText(null);
		Intent intent = new Intent(this, BackgroundService.class);
		intent.setAction(BackgroundService.ACTION_LOGIN)
		      .putExtra(BackgroundService.DATA_API_METHOD, ApiMethodCodes.AUTH_LOGIN)
		      .putExtra(BackgroundService.DATA_LOGIN, login)
		      .putExtra(BackgroundService.DATA_PASSWORD, pass);
		startService(intent);
		
		mLoggingDialog.show();
	}
	
	private class BackgroundServiceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent responseIntent) {
			Bundle data = responseIntent.getExtras();
			if(responseIntent.getAction() == BackgroundService.ACTION_LOGIN) {
				int success = Integer.valueOf(data.getString("success"));
				switch(success) {
				case -1:
					mLoggingDialog.dismiss();
					mResponseText.setText(data.getString("exception"));
					break;
				case 0:
					mLoggingDialog.dismiss();
					mResponseText.setText(data.getString("error_message"));
					break;
				case 1:
					mLoggingDialog.setMessage("Retrieving user data");
					String token = data.getString("token");
					PreferencesManager.inflatePreferencesFromIntentExtras(AuthActivity.this,
							new String[]{"token"},
							data);
					Intent intent = new Intent(AuthActivity.this, BackgroundService.class);
					intent.setAction(BackgroundService.ACTION_GET_PROFILE)
					  	  .putExtra(BackgroundService.DATA_API_METHOD, ApiMethodCodes.PROFILE_GET)
						  .putExtra(BackgroundService.DATA_TOKEN, token);
					startService(intent);
					break;
				}
			} else if(responseIntent.getAction() == BackgroundService.ACTION_GET_PROFILE) {
				//TODO start main activity;
				mLoggingDialog.dismiss();
				if(data.containsKey("success") && data.getString("success").equals("0")) {
					mResponseText.setText(data.getString("error_message"));
					return;
				}
				PreferencesManager.inflatePreferencesFromIntentExtras(AuthActivity.this,
						new String[]{"uid", "name", "surname", "middlename", "level", "avatar"},
						data);
				Intent intent = new Intent(AuthActivity.this, MainActivity.class);					  
				startActivity(intent);
			}
		}	
	}
}
