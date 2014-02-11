package com.despectra.android.classjournal_new;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class BackgroundService extends IntentService {

	public static final String DATA_LOGIN = "login";
	public static final String DATA_PASSWORD = "passwd";
	public static final String DATA_API_METHOD = "method";
	public static final String DATA_TOKEN = "token";
	
	public static final String ACTION_LOGIN = "com.despectra.android.classjournal_new.LOGIN";
	public static final String ACTION_CHECK_TOKEN = "com.despectra.android.classjournal_new.CHECK_TOKEN";
	public static final String ACTION_GET_PROFILE = "com.despectra.android.classjournal_new.GET_PROFILE";
	public static final String ACTION_GET_AVATAR = "com.despectra.android.classjournal_new.GET_AVATAR";
	
	public static final String AVATAR_FILENAME = "user_avatar";
	
	public BackgroundService() {
		super("background-service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			Bundle intentData = intent.getExtras();
			int apiMethodCode = intentData.getInt(DATA_API_METHOD);
			switch(apiMethodCode) {
			case ApiMethodCodes.AUTH_LOGIN:
				//processLoginResponse(login(intentData.getString(DATA_LOGIN), intentData.getString(DATA_PASSWORD)));
				break;
			case ApiMethodCodes.PROFILE_GET:
				//processProfileResponse(getProfile(intentData.getString(DATA_TOKEN)));
				break;
			case ApiMethodCodes.AUTH_CHECK_TOKEN:
				//processTokenChecking(checkToken(intentData.getString(DATA_TOKEN)));
				break;
			}
		} catch(Exception ex) {
				Intent responseIntent = new Intent(intent.getAction());
				responseIntent.putExtra("success", -1)
				              .putExtra("exception", ex.getMessage());
				sendBroadcast(responseIntent);
		}
	}
}
