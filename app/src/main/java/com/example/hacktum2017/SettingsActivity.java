package com.example.hacktum2017;

        import android.content.pm.ActivityInfo;
        import android.os.Bundle;
        import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_gen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
