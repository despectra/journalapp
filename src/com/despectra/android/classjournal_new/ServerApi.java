package com.despectra.android.classjournal_new;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

public class ServerApi {
	
	public static final String[] ERROR_KEYS = new String[]{"success", "error_code", "error_message"};
	public static final String[] LOGIN_SUCCESS_KEYS = new String[]{"success", "token"};
	public static final String[] GETMINPROFILE_SUCCESS_KEYS = new String[]{"uid", "name", "middlename", "surname", "level"};
	public static final String[] CHECKTOKEN_SUCCESS_KEYS = new String[]{"success"};
	
	public static final String AVATAR_FILENAME = "user_avatar";
	
	public static class Auth {
		public static JSONObject login(String login, String password) throws Exception {
			String response = doQuery(
					"http://j292427.myjino.ru/api/index.php?auth.login",
					"POST",
					String.format("login=%s&passwd=%s", login, password));
			return processApiResponse(
					response,
					LOGIN_SUCCESS_KEYS,
					ERROR_KEYS,
					new JSONPredicate() {
						@Override
						public boolean check(JSONObject json) throws Exception {
							return json.getInt("success") != 0;
						}
					},
					null);
		}
		
		public static JSONObject checkToken(Context ctx, String token) throws Exception {
			JSONObject json = new JSONObject();
			json.put("token", token);
			String response = doQuery(
					String.format("http://j292427.myjino.ru/api/index.php?auth.checktoken=%s", json.toString()),
					"GET",
					"");
			return processApiResponse(
					response,
					CHECKTOKEN_SUCCESS_KEYS,
					ERROR_KEYS,
					new JSONPredicate() {
						@Override
						public boolean check(JSONObject json) throws Exception {
							return json.getInt("success") == 1;
						}
					},
					null);
		}
	};
	
	public static class Profile {
		public static JSONObject getMinProfile(final Context ctx, String token) throws Exception {
			JSONObject json = new JSONObject();
			json.put("token", token);
			String response = doQuery(
					String.format("http://j292427.myjino.ru/api/index.php?profile.getminprofile=%s", json.toString()),
					"GET",
					"");
			return processApiResponse(
					response,
					GETMINPROFILE_SUCCESS_KEYS,
					ERROR_KEYS,
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
							FileOutputStream fos = ctx.openFileOutput(AVATAR_FILENAME, Context.MODE_PRIVATE);
							int b;
							while((b = in.read()) != -1) {
								fos.write(b);
							}
							in.close();
							fos.close();
						}
					});
		}
	};
	
	private static String doQuery(String host, String method, String queryBody) throws IOException {
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
	
	private static JSONObject processApiResponse(
			String response,
			String[] keysIfSuccess,
			String[] keysIfError,
			JSONPredicate success,
			ApiCallback callback) throws Exception {
		JSONObject json = null;
		try {
			json = new JSONObject(response);
			boolean isSuccess = success.check(json);
			String[] keys = isSuccess ? keysIfSuccess : keysIfError;
			if(isSuccess && callback != null) {
				callback.apiSuccess(json);
			}
			return json;
		} catch(Exception ex) {
			throw ex;
		}
	}
	
	private interface JSONPredicate {
		public boolean check(JSONObject json) throws Exception;
	}
	
	private interface ApiCallback {
		public void apiSuccess(JSONObject data) throws Exception;
	}
}
