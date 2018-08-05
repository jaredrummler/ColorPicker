package com.jaredrummler.android.colorpicker.demo;

import android.os.Bundle;
import android.util.Log;

import com.jaredrummler.android.colorpicker.ColorPreference;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class DemoFragment extends PreferenceFragmentCompat {

    private static final String TAG = "DemoFragment";

    private static final String KEY_DEFAULT_COLOR = "default_color";

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        setPreferencesFromResource(R.xml.main, rootKey);
        // Example showing how we can get the new color when it is changed:
        ColorPreference colorPreference = (ColorPreference) findPreference(KEY_DEFAULT_COLOR);
        colorPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (KEY_DEFAULT_COLOR.equals(preference.getKey())) {
                    String newDefaultColor = Integer.toHexString((int) newValue);
                    Log.d(TAG, "New default color is: #" + newDefaultColor);
                }
                return true;
            }
        });
    }

}
