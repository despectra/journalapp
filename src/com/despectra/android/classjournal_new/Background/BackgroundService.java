package com.despectra.android.classjournal_new.Background;

/**
 * Created by Dmirty on 17.02.14.
 */


import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.despectra.android.classjournal_new.App.JournalApplication;
import com.despectra.android.classjournal_new.Utils.PrefsManager;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

public class BackgroundService extends IntentService {

    public static final String PACKAGE = JournalApplication.PACKAGE;

    public static final String DATA_LOGIN = "login";
    public static final String DATA_PASSWORD = "passwd";
    public static final String DATA_API_METHOD = "method";
    public static final String DATA_TOKEN = "token";

    public static final String ACTION_LOGIN = PACKAGE + ".LOGIN";
    public static final String ACTION_LOGOUT = PACKAGE + ".LOGOUT";
    public static final String ACTION_CHECK_TOKEN = PACKAGE + ".CHECK_TOKEN";
    public static final String ACTION_GET_PROFILE = PACKAGE + ".GET_PROFILE";
    public static final String ACTION_GET_AVATAR = PACKAGE + ".GET_AVATAR";
    public static final String ACTION_SET_SERVER_HOST = PACKAGE + ".SET_HOST";
    public static final String AVATAR_FILENAME = "user_avatar";
    private static final String TAG = "BACKGROUND_SERVICE";

    private static final ArrayDeque<String> API_ACTIONS = new ArrayDeque<String>(Arrays.asList(
            ACTION_LOGIN,
            ACTION_LOGOUT,
            ACTION_CHECK_TOKEN,
            ACTION_GET_PROFILE,
            ACTION_GET_AVATAR));

    public static final ArrayDeque<String> INTERNAL_ACTIONS = new ArrayDeque<String>(Arrays.asList(
            ACTION_SET_SERVER_HOST
    ));

    private static ServerApi mServer;

    public BackgroundService() {
        super("background-service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mServer = ServerApi.instantiate(getApplicationContext(), getHostFromPreferences());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (API_ACTIONS.contains(action)) {
            processApiAction(intent);
        } else if (INTERNAL_ACTIONS.contains(action)) {
            processInternalAction(intent);
        }
    }

    private void processApiAction(Intent intent) {
        try {
            Bundle intentData = intent.getExtras();
            int apiMethodCode = intentData.getInt(DATA_API_METHOD);
            JSONObject response = new JSONObject();
            switch(apiMethodCode) {
                case ServerApi.API_AUTH_LOGIN:
                    response = mServer.login(intentData.getString(DATA_LOGIN), intentData.getString(DATA_PASSWORD));
                    break;
                case ServerApi.API_PROFILE_GET:
                    response = mServer.getMinProfile(intentData.getString(DATA_TOKEN));
                    break;
                case ServerApi.API_AUTH_CHECK_TOKEN:
                    response = mServer.checkToken(intentData.getString(DATA_TOKEN));
                    break;
            }
            Intent responseIntent = fillIntentFromJson(new Intent(intent.getAction()), response);
            sendBroadcast(responseIntent);
        } catch(Exception ex) {
            Intent responseIntent = new Intent(intent.getAction());
            responseIntent.putExtra("success", "-1")
                    .putExtra("exception", ex.getMessage());
            sendBroadcast(responseIntent);
        }
    }

    private void processInternalAction(Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_SET_SERVER_HOST)) {
            mServer.setHost(getHostFromPreferences());
        }
    }

    private String getHostFromPreferences(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return preferences.getString(PrefsManager.KEY_HOST, "");
    }

    private Intent fillIntentFromJson(Intent emptyIntent, JSONObject jsonObject) throws JSONException {
        Intent intent = emptyIntent;
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = jsonObject.getString(key);
            intent.putExtra(key, value);
        }
        return intent;
    }
}

