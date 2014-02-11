package com.despectra.android.classjournal_new;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.Intent;

public class ServerApi {
	
	public static class Auth {
		public static void login(ReceivableActivity activity, String login, String password, Callback callback) throws IOException {
			String response = doQuery("http://j292427.myjino.ru/api/index.php?auth.login",
					"POST",
					String.format("login=%s&passwd=%s", login, password));
		}
		
		public static void checkToken(Context ctx, String token, Callback callback) {
			
		}
	};
	
	public static class Profile {
		public static void getMinProfile(Context ctx, String token, Callback callback) {
		
		}
	};
	
	public interface Callback {
		public void onApiQueryCompleted();
	}
	
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
}
