package com.despectra.android.classjournal_new.Background;

/**
 * Created by Dmirty on 17.02.14.
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.despectra.android.classjournal_new.Utils.PrefsManager;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class ServerApi {

    public static final int API_AUTH_LOGIN = 0;
    public static final int API_AUTH_CHECK_TOKEN = 1;
    public static final int API_PROFILE_GET = 2;

    public static final String[] ERROR_KEYS = new String[]{"success", "error_code", "error_message"};
    public static final String[] LOGIN_SUCCESS_KEYS = new String[]{"success", "token"};
    public static final String[] GETMINPROFILE_SUCCESS_KEYS = new String[]{"uid", "name", "middlename", "surname", "level"};
    public static final String[] CHECKTOKEN_SUCCESS_KEYS = new String[]{"success"};
    public static final String AVATAR_FILENAME = "user_avatar";

    private static final JSONPredicate SIMPLE_PREDICATE = new JSONPredicate() {
            @Override
            public boolean check(JSONObject json) throws Exception {
                return json.getInt("success") == 1;
            }
        };

    private static ServerApi mServerInstance;
    private Context mContext;
    private String mHost;

    private ServerApi(Context context, String host) {
        setHost(host);
        setContext(context);
    }

    public  static ServerApi instantiate(Context context, String host) {
        if (mServerInstance == null) {
            mServerInstance = new ServerApi(context, host);
        }
        return mServerInstance;
    }

    public void setHost(String host) {
        mHost = host;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public JSONObject login(String login, String password) throws Exception {
        String response = doQuery(
                String.format("%s/api/index.php?auth.login", mHost),
                "POST",
                String.format("login=%s&passwd=%s", login, password));
        return processApiResponse(
                response,
                LOGIN_SUCCESS_KEYS,
                ERROR_KEYS,
                SIMPLE_PREDICATE,
                null);
    }

    public JSONObject logout(String token) throws Exception {
        JSONObject json = new JSONObject();
        json.put("token", token);
        String response = doQuery(
                String.format("%s/api/index.php?auth.logout=%s", mHost, json.toString()),
                "GET",
                ""
        );
        return processApiResponse(
                response,
                CHECKTOKEN_SUCCESS_KEYS,
                ERROR_KEYS,
                SIMPLE_PREDICATE,
                null);
    }

    public JSONObject checkToken(String token) throws Exception {
        JSONObject json = new JSONObject();
        json.put("token", token);
        String response = doQuery(
                String.format("%s/api/index.php?auth.checktoken=%s", mHost, json.toString()),
                "GET",
                "");
        return processApiResponse(
                response,
                CHECKTOKEN_SUCCESS_KEYS,
                ERROR_KEYS,
                SIMPLE_PREDICATE,
                null);
    }

    public JSONObject getMinProfile(String token) throws Exception {
        JSONObject json = new JSONObject();
        json.put("token", token);
        String response = doQuery(
                String.format("%s/api/index.php?profile.getminprofile=%s", mHost, json.toString()),
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
                        InputStream in = (InputStream) new URL(avatarUrl).getContent();
                        FileOutputStream fos = mContext.openFileOutput(AVATAR_FILENAME, Context.MODE_PRIVATE);
                        int b;
                        while ((b = in.read()) != -1) {
                            fos.write(b);
                        }
                        in.close();
                        fos.close();
                    }
                }
        );
    }

    private String doQuery(String httpUrl, String httpMethod, String queryBody) throws Exception {
        if (mHost.isEmpty()) {
            throw new Exception("Server hostname is incorrect");
        }
        InputStream reader = null;
        OutputStream writer = null;
        String exeptionMessage = null;
        try {
            /*MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
			password = new String(md5.digest());*/

            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod);
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
        } catch (Exception ex) {
            exeptionMessage = ex.getMessage();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
        return exeptionMessage;
    }

    private JSONObject processApiResponse(
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
            if (isSuccess && callback != null) {
                callback.apiSuccess(json);
            }
            return json;
        } catch (Exception ex) {
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
