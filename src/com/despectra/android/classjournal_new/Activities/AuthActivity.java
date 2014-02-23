package com.despectra.android.classjournal_new.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.despectra.android.classjournal_new.*;
import com.despectra.android.classjournal_new.Background.BackgroundService;
import com.despectra.android.classjournal_new.Background.ServerApi;
import com.despectra.android.classjournal_new.Utils.PrefsManager;

public class AuthActivity extends Activity implements TextView.OnEditorActionListener {

    public static final String BACKGROUND_FRAGMENT_TAG = "login_fragment";

    private Button mLoginBtn;
    private ImageButton mSettingsButton;
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
        SharedPreferences prefs = getSharedPreferences(PrefsManager.USER_DATA_PREFS, MODE_PRIVATE);
        if(prefs.contains("token") && !prefs.getString("token", "").equals("")) {
            launchMainActivity();
        } else {
            initUi();
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
        mLoggingDialog.setMessage("Logging in process");
        mLoggingDialog.setCancelable(false);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickedView) {
                performLogin();
            }
        });
        mSettingsButton = (ImageButton) findViewById(R.id.settings_btn);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthActivity.this, MainPreferencesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                .putExtra(BackgroundService.DATA_API_METHOD, ServerApi.API_AUTH_LOGIN)
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
                        PrefsManager.inflatePreferencesFromIntentExtras(AuthActivity.this,
                                new String[]{"token"},
                                data);
                        Intent intent = new Intent(AuthActivity.this, BackgroundService.class);
                        intent.setAction(BackgroundService.ACTION_GET_PROFILE)
                                .putExtra(BackgroundService.DATA_API_METHOD, ServerApi.API_PROFILE_GET)
                                .putExtra(BackgroundService.DATA_TOKEN, token);
                        startService(intent);
                        break;
                }
            } else if(responseIntent.getAction() == BackgroundService.ACTION_GET_PROFILE) {
                mLoggingDialog.dismiss();
                if(data.containsKey("success") && data.getString("success").equals("0")) {
                    mResponseText.setText(data.getString("error_message"));
                    return;
                }
                PrefsManager.inflatePreferencesFromIntentExtras(AuthActivity.this,
                        new String[]{"uid", "name", "surname", "middlename", "level", "avatar"},
                        data);
                launchMainActivity();
            }
        }
    }
}
