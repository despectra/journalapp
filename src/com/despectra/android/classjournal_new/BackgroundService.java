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
				processLoginResponse(login(intentData.getString(DATA_LOGIN), intentData.getString(DATA_PASSWORD)));
				break;
			case ApiMethodCodes.PROFILE_GET:
				processProfileResponse(getProfile(intentData.getString(DATA_TOKEN)));
				break;
			case ApiMethodCodes.AUTH_CHECK_TOKEN:
				processTokenChecking(checkToken(intentData.getString(DATA_TOKEN)));
				break;
			}
		} catch(Exception ex) {
				Intent responseIntent = new Intent(intent.getAction());
				responseIntent.putExtra("success", -1)
				              .putExtra("exception", ex.getMessage());
				sendBroadcast(responseIntent);
		}
	}
	
	private String login(String login, String password) throws IOException {
		return doQuery("http://j292427.myjino.ru/api/index.php?auth.login",
				"POST",
				String.format("login=%s&passwd=%s", login, password));
	}
	
	private String getProfile(String token) throws IOException, JSONException {
		JSONObject json = new JSONObject();
		json.put("token", token);
		return doQuery(String.format("http://j292427.myjino.ru/api/index.php?profile.getminprofile=%s", json.toString()),
				"GET",
				"");
	}
	
	private String checkToken(String token) throws IOException, JSONException {
		JSONObject json = new JSONObject();
		json.put("token", token);
		return doQuery(String.format("http://j292427.myjino.ru/api/index.php?auth.checktoken=%s", json.toString()),
				"GET",
				"");
	}
	
	private String doQuery(String host, String method, String queryBody) throws IOException {
		InputStream reader = null;
		OutputStream writer = null;
		String exeptionMessage = null;
		try {
			/*MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(password.getBytes());
			password = new String(md5.digest());*/
			
			URL url = new URL(host);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(method);
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(15000);
			connection.setDoOutput(true);
			connection.connect();
			writer = connection.getOutputStream();
			writer.write(queryBody.getBytes());
			reader = connection.getInputStream();
			byte[] data = new byte[2048];
			reader.read(data);
			return new String(data);
		} catch(Exception ex) {
			exeptionMessage = ex.getMessage();
		} finally {
			if(reader != null) {
				reader.close();
			}
			if(writer != null) {
				writer.close();
			}
		}
		return exeptionMessage;
	}
	
	private void processLoginResponse(String response) throws Exception {
		processApiResponse(
				ACTION_LOGIN,
				response,
				new String[]{"success", "token"},
				new String[]{"success", "error_code", "error_message"},
				new JSONPredicate() {
					@Override
					public boolean check(JSONObject json) throws JSONException {
						return json.getInt("success") != 0;
					}
				},
				null);
	}
	
	private void processProfileResponse(String response) throws Exception {
		processApiResponse(
				ACTION_GET_PROFILE,
				response,
				new String[]{"uid", "name", "middlename", "surname", "level"},
				new String[]{"success", "error_code", "error_message"},
				new JSONPredicate() {
					@Override
					public boolean check(JSONObject json) throws Exception {
						return !(json.has("success") && json.getInt("success") == 0);
					}
				},
				new ApiCallback() {
					@Override
					public void apiSuccess(JSONObject json) throws JSONException, IOException {
						String avatarUrl = "http://" + json.getString("avatar");
						InputStream in = (InputStream)new URL(avatarUrl).getContent();
						FileOutputStream fos = openFileOutput(AVATAR_FILENAME, Context.MODE_PRIVATE);
						int b;
						while((b = in.read()) != -1) {
							fos.write(b);
						}
						in.close();
						fos.close();
					}
				});
	}
	
	private void processTokenChecking(String response) throws Exception {
		processApiResponse(
				ACTION_CHECK_TOKEN,
				response,
				new String[]{"success"},
				new String[]{"success", "error_code", "error_message"},
				new JSONPredicate() {
					@Override
					public boolean check(JSONObject json) throws Exception {
						return json.getInt("success") == 1;
					}
				},
				null);
	}
	
	private void processApiResponse(
			String action,
			String response,
			String[] keysIfSuccess,
			String[] keysIfError,
			JSONPredicate success,
			ApiCallback callback) throws Exception {
		JSONObject jsonData = null;
		Intent responseIntent = new Intent(action);
		try {
			jsonData = new JSONObject(response);
			boolean isSuccess = success.check(jsonData);
			String[] keys = isSuccess ? keysIfSuccess : keysIfError;
			if(isSuccess && callback != null) {
				callback.apiSuccess(jsonData);
			}
			responseIntent = inflateIntentFromJson(responseIntent, jsonData, keys);
			sendBroadcast(responseIntent);
		} catch(Exception ex) {
			throw ex;
		}
	}
	
	private Intent inflateIntentFromJson(Intent intent, JSONObject json, String[] keys) throws JSONException {
		for(String key : keys) {
			if(json.has(key)) {
				intent.putExtra(key, json.getString(key));
			}
		}
		return intent;
	}
	
	private interface JSONPredicate {
		public boolean check(JSONObject json) throws Exception;
	}
	
	private interface ApiCallback {
		public void apiSuccess(JSONObject data) throws Exception;
	}
}
