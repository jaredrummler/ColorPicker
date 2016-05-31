/*
 * Copyright (C) 2016 Jared Rummler <jared.rummler@gmail.com>
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
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.jrummyapps.android.colorpicker.R;
import com.jrummyapps.android.colorpicker.view.ColorPanelView;
import com.jrummyapps.android.colorpicker.view.ColorPickerView;
import com.jrummyapps.android.colorpicker.view.ColorPickerView.OnColorChangedListener;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ColorPickerDialogFragment extends DialogFragment {

  private ColorPickerDialogListener colorPickerDialogListener;
  private ColorPickerView colorPicker;
  private ColorPanelView newColorPanel;
  private int dialogId = -1;

  public static ColorPickerDialogFragment newInstance(int dialogId, int initialColor) {
    return newInstance(dialogId, null, null, initialColor, false);
  }

  public static ColorPickerDialogFragment newInstance(int dialogId, String title, String btnText, int color, boolean showAlpha) {
    ColorPickerDialogFragment fragment = new ColorPickerDialogFragment();
    Bundle args = new Bundle();
    args.putInt("id", dialogId);
    args.putString("title", title);
    args.putString("ok_button", btnText);
    args.putBoolean("alpha", showAlpha);
    args.putInt("init_color", color);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    dialogId = getArguments().getInt("id");
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
    Dialog d = super.onCreateDialog(savedInstanceState);
    d.requestWindowFeature(Window.FEATURE_NO_TITLE);
    d.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
    return d;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.colorpickerview__dialog_color_picker, container);

    TextView titleView = (TextView) v.findViewById(android.R.id.title);

    colorPicker = (ColorPickerView) v.findViewById(R.id.colorpickerview__color_picker_view);
    ColorPanelView oldColorPanel = (ColorPanelView) v.findViewById(R.id.colorpickerview__color_panel_old);
    newColorPanel = (ColorPanelView) v.findViewById(R.id.colorpickerview__color_panel_new);
    Button okButton = (Button) v.findViewById(android.R.id.button1);

    colorPicker.setOnColorChangedListener(new OnColorChangedListener() {

      @Override public void onColorChanged(int newColor) {
        newColorPanel.setColor(newColor);
      }
    });

    okButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        colorPickerDialogListener.onColorSelected(dialogId, colorPicker.getColor());
        getDialog().dismiss();
      }

    });

    String title = getArguments().getString("title");

    if (title != null) {
      titleView.setText(title);
    } else {
      titleView.setVisibility(View.GONE);
    }

    if (savedInstanceState == null) {
      colorPicker.setAlphaSliderVisible(getArguments().getBoolean("alpha"));

      String ok = getArguments().getString("ok_button");
      if (ok != null) {
        okButton.setText(ok);
      }

      int initColor = getArguments().getInt("init_color");

      oldColorPanel.setColor(initColor);
      colorPicker.setColor(initColor, true);
    }

    return v;
  }

  @Override public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    colorPickerDialogListener.onDialogDismissed(dialogId);
  }

  public interface ColorPickerDialogListener {

    void onColorSelected(int dialogId, int color);

    void onDialogDismissed(int dialogId);

  }

}
