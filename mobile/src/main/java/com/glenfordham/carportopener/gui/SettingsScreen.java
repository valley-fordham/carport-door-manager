package com.glenfordham.carportopener.gui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.glenfordham.carportopener.R;

public class SettingsScreen extends AppCompatActivity {

    // Setting keys
    public static final String KEY_PREF_HOST_URL = "host_url";
    public static final String KEY_PREF_DOOR_RESPONSE = "door_response";
    public static final String KEY_PREF_STATUS_INTERVAL = "status_interval";
    public static final String KEY_PREF_SERVICE_INTERVAL = "service_interval";
    public static final String KEY_PREF_TRIGGER_PARAM = "trigger_param";
    public static final String KEY_PREF_STATUS_PARAM = "status_param";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}