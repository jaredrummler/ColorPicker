/*
 * Copyright (C) 2017 Jared Rummler
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

package com.jaredrummler.android.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;

/**
 * A Preference to select a color
 */
public class ColorPreferenceSupport extends android.support.v7.preference.DialogPreference  implements ColorPickerDialogListener {

  private static final int SIZE_NORMAL = 0;
  private static final int SIZE_LARGE = 1;

  private OnShowDialogListener onShowDialogListener;
  private int color = Color.BLACK;
  private boolean showDialog;
  @ColorPickerDialog.DialogType
  private int dialogType;
  private int colorShape;
  private boolean allowPresets;
  private boolean allowCustom;
  private boolean showAlphaSlider;
  private boolean showColorShades;
  private int previewSize;
  private int[] presets;
  private int dialogTitle;
  private ColorPanelView preview;
  private Bundle args;

  public ColorPreferenceSupport(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public ColorPreferenceSupport(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(attrs);
  }

  private void init(AttributeSet attrs) {
    setPersistent(true);
    TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ColorPreference);
    showDialog = a.getBoolean(R.styleable.ColorPreference_cpv_showDialog, true);
    //noinspection WrongConstant
    dialogType = a.getInt(R.styleable.ColorPreference_cpv_dialogType, ColorPickerDialog.TYPE_PRESETS);
    colorShape = a.getInt(R.styleable.ColorPreference_cpv_colorShape, ColorShape.CIRCLE);
    allowPresets = a.getBoolean(R.styleable.ColorPreference_cpv_allowPresets, true);
    allowCustom = a.getBoolean(R.styleable.ColorPreference_cpv_allowCustom, true);
    showAlphaSlider = a.getBoolean(R.styleable.ColorPreference_cpv_showAlphaSlider, false);
    showColorShades = a.getBoolean(R.styleable.ColorPreference_cpv_showColorShades, true);
    previewSize = a.getInt(R.styleable.ColorPreference_cpv_previewSize, SIZE_NORMAL);
    final int presetsResId = a.getResourceId(R.styleable.ColorPreference_cpv_colorPresets, 0);
    dialogTitle = a.getResourceId(R.styleable.ColorPreference_cpv_dialogTitle, R.string.cpv_default_title);
    if (presetsResId != 0) {
      presets = getContext().getResources().getIntArray(presetsResId);
    } else {
      presets = ColorPickerDialog.MATERIAL_COLORS;
    }
    if (colorShape == ColorShape.CIRCLE) {
      setWidgetLayoutResource(
          previewSize == SIZE_LARGE ? R.layout.cpv_preference_circle_large : R.layout.cpv_preference_circle);
    } else {
      setWidgetLayoutResource(
          previewSize == SIZE_LARGE ? R.layout.cpv_preference_square_large : R.layout.cpv_preference_square
      );
    }
    args=new Bundle();
    int dialogId=a.getInt(R.styleable.ColorPreference_cpv_dialogID, 0);
    int presetsButtonText = a.getResourceId(R.styleable.ColorPreference_cpv_presetsButtonText, R.string.cpv_default_title);
    int customButtonText = a.getResourceId(R.styleable.ColorPreference_cpv_customButtonText, R.string.cpv_default_title);
    int selectedButtonText = a.getResourceId(R.styleable.ColorPreference_cpv_selectedButtonText, R.string.cpv_default_title);

    args.putInt(ColorPickerDialogPreference.ARG_ID, dialogId);
    args.putInt(ColorPickerDialogPreference.ARG_TYPE, dialogType);
    args.putInt(ColorPickerDialogPreference.ARG_COLOR, color);
    args.putIntArray(ColorPickerDialogPreference.ARG_PRESETS, presets);
    args.putBoolean(ColorPickerDialogPreference.ARG_ALPHA, showAlphaSlider);
    args.putBoolean(ColorPickerDialogPreference.ARG_ALLOW_CUSTOM, allowCustom);
    args.putBoolean(ColorPickerDialogPreference.ARG_ALLOW_PRESETS, allowPresets);
    args.putInt(ColorPickerDialogPreference.ARG_DIALOG_TITLE, dialogTitle);
    args.putBoolean(ColorPickerDialogPreference.ARG_SHOW_COLOR_SHADES, showColorShades);
    args.putInt(ColorPickerDialogPreference.ARG_COLOR_SHAPE, colorShape);
    args.putInt(ColorPickerDialogPreference.ARG_PRESETS_BUTTON_TEXT, presetsButtonText);
    args.putInt(ColorPickerDialogPreference.ARG_CUSTOM_BUTTON_TEXT, customButtonText);
    args.putInt(ColorPickerDialogPreference.ARG_SELECTED_BUTTON_TEXT, selectedButtonText);
    a.recycle();

  }


  @Override
  public void onBindViewHolder(PreferenceViewHolder view) {
    super.onBindViewHolder(view);
    preview = (ColorPanelView) view.findViewById(R.id.cpv_preference_preview_color_panel);
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
    if (preview != null) {
      preview.setColor(color);
    }
  }

  @Override protected Object onGetDefaultValue(TypedArray a, int index) {
    return a.getInteger(index, Color.BLACK);
  }

  @Override public void onColorSelected(int dialogId, @ColorInt int color) {
    saveValue(color);
  }

  @Override public void onDialogDismissed(int dialogId) {
    // no-op
  }

  /**
   * Set the new color
   *
   * @param color
   *     The newly selected color
   */
  public void saveValue(@ColorInt int color) {
    this.color = color;
    persistInt(this.color);
    notifyChanged();
    callChangeListener(color);
  }

  /**
   * Set the colors shown in the {@link ColorPickerDialog}.
   *
   * @param presets An array of color ints
   */
  public void setPresets(@NonNull int[] presets) {
    this.presets = presets;
  }

  /**
   * Get the colors that will be shown in the {@link ColorPickerDialog}.
   *
   * @return An array of color ints
   */
  public int[] getPresets() {
    return presets;
  }

  /**
   * The listener used for showing the {@link ColorPickerDialog}.
   * Call {@link #saveValue(int)} after the user chooses a color.
   * If this is set then it is up to you to show the dialog.
   *
   * @param listener
   *     The listener to show the dialog
   */
  public void setOnShowDialogListener(OnShowDialogListener listener) {
    onShowDialogListener = listener;
  }

  /**
   * The tag used for the {@link ColorPickerDialog}.
   *
   * @return The tag
   */
  public String getFragmentTag() {
    return "color_" + getKey();
  }

  public Bundle getArgs() {
    return args;
  }

  public int getColor() {
    return getPersistedInt(Color.BLACK);
  }

  public interface OnShowDialogListener {

    void onShowColorPickerDialog(String title, int currentColor);
  }

}
