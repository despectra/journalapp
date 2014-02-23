package com.despectra.android.classjournal_new.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.*;
import com.despectra.android.classjournal_new.Background.BackgroundService;
import com.despectra.android.classjournal_new.R;

import java.util.List;

/**
 * Created by Андрей on 23.02.14.
 */
public class MainPreferencesActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MainPreferencesFragment())
                .commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateSummary(sharedPreferences, key);
        Intent intent = new Intent(BackgroundService.ACTION_SET_SERVER_HOST, null, this, BackgroundService.class);
        startService(intent);
    }

    private void updateSummary(SharedPreferences sharedPreferences, String key) {
        //TODO
    }


    private static class MainPreferencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            Preference hostPreference = preferenceScreen.findPreference("settings_host");
            hostPreference.setSummary(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("settings_host", ""));
        }
    }


}
