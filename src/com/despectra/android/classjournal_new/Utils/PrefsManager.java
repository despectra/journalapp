package com.despectra.android.classjournal_new.Utils;

/**
 * Created by Dmirty on 17.02.14.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class PrefsManager {
    public static final String USER_DATA_PREFS = "com.despectra.android.classjournal_new.USER_DATA_PREFS";

    public static void clearPreferences(Context context, String[] keys) {
        SharedPreferences.Editor prefsEditor = context.getSharedPreferences(USER_DATA_PREFS, context.MODE_PRIVATE).edit();
        for(String key : keys) {
            prefsEditor.remove(key);
        }
        prefsEditor.commit();
    }

    public static void inflatePreferencesFromIntentExtras(Context context, String[] keys, Bundle intentExtras) {
        SharedPreferences.Editor prefsEditor = context.getSharedPreferences(USER_DATA_PREFS, context.MODE_PRIVATE).edit();
        for(String key : keys) {
            prefsEditor.putString(key, intentExtras.getString(key));
        }
        prefsEditor.commit();
    }


}
