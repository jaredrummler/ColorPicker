package com.jaredrummler.android.colorpicker.demo;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import com.jaredrummler.android.colorpicker.ColorPreference;

public class DemoFragment extends PreferenceFragment {

  private static final String TAG = "DemoFragment";

  private static final String KEY_DEFAULT_COLOR = "default_color";

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.main);

    // Example showing how we can get the new color when it is changed:
    ColorPreference colorPreference = (ColorPreference) findPreference(KEY_DEFAULT_COLOR);
    colorPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
      @Override public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (KEY_DEFAULT_COLOR.equals(preference.getKey())) {
          String newDefaultColor = Integer.toHexString((int) newValue);
          Log.d(TAG, "New default color is: #" + newDefaultColor);
        }
        return true;
      }
    });
  }
}
