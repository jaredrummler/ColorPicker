/*
 * Copyright (C) 2016 Jared Rummler <jared.rummler@gmail.com>
 * Copyright (C) 2015 Daniel Nilsson
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
 *
 */

package com.jrummyapps.android.colorpicker.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.jrummyapps.android.colorpicker.R;
import com.jrummyapps.android.colorpicker.view.ColorPanelView;
import com.jrummyapps.android.colorpicker.view.ColorPickerView;
import com.jrummyapps.android.colorpicker.view.ColorPickerView.OnColorChangedListener;

/**
 * A dialog to pick a color
 */
public class ColorPickerDialogFragment extends DialogFragment implements TextWatcher, OnTouchListener, OnColorChangedListener {

  private static final String ARG_ID = "id";
  private static final String ARG_COLOR = "color";
  private static final String ARG_ALPHA = "alpha";

  private ColorPickerDialogListener colorPickerDialogListener;
  private ColorPickerView colorPicker;
  private ColorPanelView newColorPanel;
  private EditText hexEditText;
  private boolean showAlphaSlider;
  private boolean fromEditText;
  private int dialogId;

  /**
   * Create a new {@link ColorPickerDialogFragment}. By default, the alpha slider will not be shown.
   *
   * @param dialogId The ID sent to the {@link OnColorChangedListener}
   * @param color The initial color
   * @return The {@link ColorPickerDialogFragment}
   */
  public static ColorPickerDialogFragment newInstance(int dialogId, int color) {
    return newInstance(dialogId, color, false);
  }

  /**
   * Create a new {@link ColorPickerDialogFragment}.
   *
   * @param dialogId The ID sent to the {@link OnColorChangedListener}
   * @param color The initial color
   * @param showAlpha {@code true} to show the alpha sliding
   * @return The {@link ColorPickerDialogFragment}
   */
  public static ColorPickerDialogFragment newInstance(int dialogId, int color, boolean showAlpha) {
    ColorPickerDialogFragment fragment = new ColorPickerDialogFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_ID, dialogId);
    args.putBoolean(ARG_ALPHA, showAlpha);
    args.putInt(ARG_COLOR, color);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      colorPickerDialogListener = (ColorPickerDialogListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException("Parent activity must implement ColorPickerDialogListener to receive result.");
    }
  }

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    View contentView = View.inflate(getActivity(), R.layout.colorpickerview__dialog_color_picker, null);

    colorPicker = (ColorPickerView) contentView.findViewById(R.id.colorpickerview__color_picker_view);
    ColorPanelView oldColorPanel = (ColorPanelView) contentView.findViewById(R.id.colorpickerview__color_panel_old);
    newColorPanel = (ColorPanelView) contentView.findViewById(R.id.colorpickerview__color_panel_new);
    ImageView arrowRight = (ImageView) contentView.findViewById(R.id.colorpickerview__arrow_right);
    hexEditText = (EditText) contentView.findViewById(R.id.colorpickerview__hex);
    dialogId = getArguments().getInt(ARG_ID);
    showAlphaSlider = getArguments().getBoolean(ARG_ALPHA);

    try {
      TypedValue typedValue = new TypedValue();
      getActivity().getTheme().resolveAttribute(android.R.attr.textColorSecondary, typedValue, true);
      int arrowColor = typedValue.data;
      arrowRight.setColorFilter(arrowColor);
    } catch (Exception ignored) {
    }

    if (savedInstanceState == null) {
      colorPicker.setAlphaSliderVisible(showAlphaSlider);
      int initColor = getArguments().getInt(ARG_COLOR);
      oldColorPanel.setColor(initColor);
      colorPicker.setColor(initColor, true);
      setHex(initColor);
    }

    if (!showAlphaSlider) {
      hexEditText.setFilters(new InputFilter[]{new LengthFilter(6)});
    }

    contentView.setOnTouchListener(this);
    colorPicker.setOnColorChangedListener(this);
    hexEditText.addTextChangedListener(this);

    return new AlertDialog.Builder(getActivity())
        .setView(contentView)
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

          @Override public void onClick(DialogInterface dialog, int which) {
            colorPickerDialogListener.onColorSelected(dialogId, colorPicker.getColor());
          }
        }).create();
  }

  @Override public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    colorPickerDialogListener.onDialogDismissed(dialogId);
  }

  @Override public void onColorChanged(int newColor) {
    newColorPanel.setColor(newColor);
    if (!fromEditText) {
      setHex(newColor);
      if (hexEditText.hasFocus()) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(hexEditText.getWindowToken(), 0);
        hexEditText.clearFocus();
      }
    }
    fromEditText = false;
  }

  @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override public void afterTextChanged(Editable s) {
    if (hexEditText.isFocused()) {
      int color = parseColorString(s.toString());
      if (color != colorPicker.getColor()) {
        fromEditText = true;
        colorPicker.setColor(color, true);
      }
    }
  }

  @Override public boolean onTouch(View v, MotionEvent event) {
    if (v != hexEditText && hexEditText.hasFocus()) {
      hexEditText.clearFocus();
      InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(hexEditText.getWindowToken(), 0);
      hexEditText.clearFocus();
      return true;
    }
    return false;
  }

  private void setHex(int color) {
    String hex = Integer.toHexString(color);
    if (showAlphaSlider) {
      hexEditText.setText(hex);
    } else {
      hexEditText.setText(hex.substring(2));
    }
  }

  private int parseColorString(String colorString) throws NumberFormatException {
    int a, r, g, b = 0;
    if (colorString.startsWith("#")) {
      colorString = colorString.substring(1);
    }
    if (colorString.length() == 0) {
      r = 0;
      a = 255;
      g = 0;
    } else if (colorString.length() <= 2) {
      a = 255;
      r = 0;
      b = Integer.parseInt(colorString, 16);
      g = 0;
    } else if (colorString.length() == 3) {
      a = 255;
      r = Integer.parseInt(colorString.substring(0, 1), 16);
      g = Integer.parseInt(colorString.substring(1, 2), 16);
      b = Integer.parseInt(colorString.substring(2, 3), 16);
    } else if (colorString.length() == 4) {
      a = 255;
      r = Integer.parseInt(colorString.substring(0, 2), 16);
      g = r;
      r = 0;
      b = Integer.parseInt(colorString.substring(2, 4), 16);
    } else if (colorString.length() == 5) {
      a = 255;
      r = Integer.parseInt(colorString.substring(0, 1), 16);
      g = Integer.parseInt(colorString.substring(1, 3), 16);
      b = Integer.parseInt(colorString.substring(3, 5), 16);
    } else if (colorString.length() == 6) {
      a = 255;
      r = Integer.parseInt(colorString.substring(0, 2), 16);
      g = Integer.parseInt(colorString.substring(2, 4), 16);
      b = Integer.parseInt(colorString.substring(4, 6), 16);
    } else if (colorString.length() == 7) {
      a = Integer.parseInt(colorString.substring(0, 1), 16);
      r = Integer.parseInt(colorString.substring(1, 3), 16);
      g = Integer.parseInt(colorString.substring(3, 5), 16);
      b = Integer.parseInt(colorString.substring(5, 7), 16);
    } else if (colorString.length() == 8) {
      a = Integer.parseInt(colorString.substring(0, 2), 16);
      r = Integer.parseInt(colorString.substring(2, 4), 16);
      g = Integer.parseInt(colorString.substring(4, 6), 16);
      b = Integer.parseInt(colorString.substring(6, 8), 16);
    } else {
      b = -1;
      g = -1;
      r = -1;
      a = -1;
    }
    return Color.argb(a, r, g, b);
  }

  public interface ColorPickerDialogListener {

    void onColorSelected(int dialogId, int color);

    void onDialogDismissed(int dialogId);

  }

}
