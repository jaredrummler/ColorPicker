/*
 * Copyright (C) 2017 JRummy Apps Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jrummyapps.android.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

/**
 * A Preference to select a color
 */
public class ColorPreference extends Preference {

  /*package*/ OnShowDialogListener onShowDialogListener;
  /*package*/ int color = 0xFF000000;

  public ColorPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public ColorPreference(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(attrs);
  }

  private void init(AttributeSet attrs) {
    setPersistent(true);
    TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.cpv_ColorPickerView);
    int shape = a.getInt(R.styleable.cpv_ColorPickerView_colorShape, ColorShape.CIRCLE);
    if (shape == ColorShape.CIRCLE) {
      setWidgetLayoutResource(R.layout.cpv_preference_circle);
    } else {
      setWidgetLayoutResource(R.layout.cpv_preference_square);
    }
    a.recycle();
    setOnPreferenceClickListener(new OnPreferenceClickListener() {

      @Override public boolean onPreferenceClick(Preference preference) {
        if (onShowDialogListener != null) {
          onShowDialogListener.onShowColorPickerDialog((String) getTitle(), color);
          return true;
        } else {
          throw new IllegalArgumentException(
              "You must first call setOnShowDialogListener() and handle showing the ColorPickerDialog yourself.");
        }
      }
    });
  }

  /**
   * The listener used for showing the {@link ColorPickerDialog}.
   * Call {@link #saveValue(int)} after the user chooses a color.
   *
   * @param listener
   *     The listener to show the dialog
   */
  public void setOnShowDialogListener(OnShowDialogListener listener) {
    onShowDialogListener = listener;
  }

  @Override protected void onBindView(View view) {
    super.onBindView(view);
    ColorPanelView preview = (ColorPanelView) view.findViewById(R.id.cpv_preference_preview_color_panel);
    if (preview != null) {
      preview.setColor(color);
    }

  }

  @Override protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
    if (restorePersistedValue) {
      color = getPersistedInt(0xFF000000);
    } else {
      color = (Integer) defaultValue;
      persistInt(color);
    }
  }

  @Override protected Object onGetDefaultValue(TypedArray a, int index) {
    return a.getInteger(index, 0xFF000000);
  }

  public void saveValue(int color) {
    this.color = color;
    persistInt(this.color);
    notifyChanged();
  }

  public interface OnShowDialogListener {

    void onShowColorPickerDialog(String title, int currentColor);
  }

}
