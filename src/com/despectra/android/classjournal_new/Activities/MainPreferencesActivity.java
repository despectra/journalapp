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
public class MainPreferencesActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MainPreferencesFragment())
                .commit();
    }

    private static class MainPreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            Preference hostPreference = preferenceScreen.findPreference("settings_host");
            hostPreference.setSummary(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("settings_host", ""));
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            updateSummaries(getPreferenceScreen());
            Intent intent = new Intent(BackgroundService.ACTION_SET_SERVER_HOST, null, getActivity(), BackgroundService.class);
            getActivity().startService(intent);
        }

        private void updateSummaries(Preference item) {
            //TODO
            if (item instanceof PreferenceCategory) {
                PreferenceCategory category = (PreferenceCategory) item;
                for (int i = 0; i < category.getPreferenceCount(); i++) {
                    Preference categroryItem = category.getPreference(i);
                    if (categroryItem instanceof PreferenceCategory) {
                        updateSummaries(category.getPreference(i));
                    } else if (categroryItem instanceof EditTextPreference || categroryItem instanceof ListPreference) {
                        updateSimpleSummary(categroryItem);
                    }
                }
            }
        }

        private void updateSimpleSummary(Preference item) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            item.setSummary(sharedPreferences.getString(item.getKey(), ""));
        }
    }
}
