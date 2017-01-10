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
 * Raw
 */

/*
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

package com.jrummyapps.android.colorpicker.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.jrummyapps.android.colorpicker.R;
import com.jrummyapps.android.colorpicker.adapter.ColorPaletteAdapter;
import com.jrummyapps.android.colorpicker.view.ColorPanelView;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ColorPresetsDialogFragment extends DialogFragment {

  private static final String ARG_ID = "id";
  private static final String ARG_COLOR = "color";
  private static final String ARG_COLORS = "colors";
  private static final String ARG_ALPHA = "alpha";

  /* material design colors */
  static final int[] DEFAULT_PRESET = {
      0xFFF44336, // RED 500
      0xFFE91E63, // PINK 500
      0xFF9C27B0, // PURPLE 500
      0xFF673AB7, // DEEP PURPLE 500
      0xFF3F51B5, // INDIGO 500
      0xFF2196F3, // BLUE 500
      0xFF03A9F4, // LIGHT BLUE 500
      0xFF00BCD4, // CYAN 500
      0xFF009688, // TEAL 500
      0xFF4CAF50, // GREEN 500
      0xFF8BC34A, // LIGHT GREEN 500
      0xFFCDDC39, // LIME 500
      0xFFFFEB3B, // YELLOW 500
      0xFFFFC107, // AMBER 500
      0xFFFF9800, // ORANGE 500
      0xFF795548, // BROWN 500
      0xFF9E9E9E, // GREY 500
      0xFF607D8B  // BLUE GREY 500
  };

  public static ColorPresetsDialogFragment newInstance(int dialogId,
                                                       @ColorInt int color) {
    return newInstance(dialogId, color, DEFAULT_PRESET);
  }

  public static ColorPresetsDialogFragment newInstance(int dialogId,
                                                       @ColorInt int color,
                                                       @NonNull int[] colors) {
    return newInstance(dialogId, color, colors, false);
  }

  public static ColorPresetsDialogFragment newInstance(int dialogId,
                                                       @ColorInt int color,
                                                       @NonNull int[] colors,
                                                       boolean showAlpha) {
    ColorPresetsDialogFragment fragment = new ColorPresetsDialogFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_ID, dialogId);
    args.putIntArray(ARG_COLORS, colors);
    args.putInt(ARG_COLOR, color);
    args.putBoolean(ARG_ALPHA, showAlpha);
    fragment.setArguments(args);
    return fragment;
  }

  private static int shadeColor(@ColorInt int color, double percent) {
    String hex = String.format("#%06X", (0xFFFFFF & color));
    long f = Long.parseLong(hex.substring(1), 16);
    double t = percent < 0 ? 0 : 255;
    double p = percent < 0 ? percent * -1 : percent;
    long R = f >> 16;
    long G = f >> 8 & 0x00FF;
    long B = f & 0x0000FF;
    int red = (int) (Math.round((t - R) * p) + R);
    int green = (int) (Math.round((t - G) * p) + G);
    int blue = (int) (Math.round((t - B) * p) + B);
    return Color.rgb(red, green, blue);
  }

  /*package*/ ColorPickerDialogFragment.ColorPickerDialogListener colorPickerDialogListener;
  /*package*/ ColorPaletteAdapter adapter;
  /*package*/ View view;
  /*package*/ int[] colors;
  /*package*/ int color;
  /*package*/ int dialogId;

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      colorPickerDialogListener = (ColorPickerDialogFragment.ColorPickerDialogListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException("Parent activity must implement ColorPickerDialogListener to receive result.");
    }
  }

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    colors = getArguments().getIntArray(ARG_COLORS);

    if (savedInstanceState == null) {
      color = getArguments().getInt(ARG_COLOR);
    } else {
      color = savedInstanceState.getInt(ARG_COLOR);
    }
    colors = unshiftIfNotExists(colors, color);
    dialogId = getArguments().getInt(ARG_ID);

    LayoutInflater inflater = LayoutInflater.from(getActivity());
    //noinspection all
    view = inflater.inflate(R.layout.colorpickerview__dialog_presets, null);
    GridView gridView = (GridView) view.findViewById(R.id.gridView);

    createColorShades(color);

    adapter = new ColorPaletteAdapter(new ColorPaletteAdapter.OnColorSelectedListener() {
      @Override public void onColorSelected(int color) {
        ColorPresetsDialogFragment.this.color = color;
        createColorShades(color);
      }
    }, colors, getSelectedItemPosition());

    gridView.setAdapter(adapter);

    return new AlertDialog.Builder(getActivity())
        .setTitle(R.string.colorpickerview__default_title)
        .setView(view)
        .setNeutralButton("Custom", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            boolean showAlpha = getArguments().getBoolean(ARG_ALPHA, false);
            ColorPickerDialogFragment.newInstance(dialogId, color, showAlpha)
                .show(getFragmentManager(), "color-picker");
          }
        })
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            colorPickerDialogListener.onColorSelected(dialogId, color);
          }
        })
        .create();
  }

  @Override public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    colorPickerDialogListener.onDialogDismissed(dialogId);
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    outState.putInt(ARG_COLOR, color);
    super.onSaveInstanceState(outState);
  }

  /*package*/ void createColorShades(@ColorInt int color) {
    final LinearLayout shadesLayout = (LinearLayout) view.findViewById(R.id.shades_layout);
    shadesLayout.removeAllViews();

    final int horizontalPadding =
        shadesLayout.getResources().getDimensionPixelSize(R.dimen.colorpickerview__item_horizontal_padding);

    int[] shades = getColorShades(color);
    for (final int shade : shades) {
      final ColorPanelView colorPanelView = new ColorPanelView(shadesLayout.getContext());
      int size = shadesLayout.getResources().getDimensionPixelSize(R.dimen.colorpickerview__item_size);
      FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size, size, Gravity.CENTER);
      layoutParams.leftMargin = layoutParams.rightMargin = horizontalPadding;
      colorPanelView.setLayoutParams(layoutParams);
      colorPanelView.setClickable(true);
      colorPanelView.setColor(shade);
      final ImageView iv = new ImageView(shadesLayout.getContext());
      iv.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER));
      colorPanelView.addView(iv);
      shadesLayout.addView(colorPanelView);
      colorPanelView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (v.getTag() instanceof Boolean && (Boolean) v.getTag()) {
            return; // already selected
          }
          ColorPresetsDialogFragment.this.color = shade;
          adapter.selectNone();
          for (int i = 0; i < shadesLayout.getChildCount(); i++) {
            ColorPanelView cpv = (ColorPanelView) shadesLayout.getChildAt(i);
            ImageView imageView = (ImageView) shadesLayout.getChildAt(0);
            imageView.setImageResource(cpv == v ? R.drawable.colorpickerview__preset_checked : 0);
            cpv.setTag(cpv == v);
          }
        }
      });
      colorPanelView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override public boolean onLongClick(View v) {
          colorPanelView.showHint();
          return true;
        }
      });
    }
  }

  private int[] getColorShades(@ColorInt int color) {
    return new int[]{
        shadeColor(color, 0.9),
        shadeColor(color, 0.7),
        shadeColor(color, 0.5),
        shadeColor(color, 0.333),
        shadeColor(color, 0.166),
        shadeColor(color, -0.125),
        shadeColor(color, -0.25),
        shadeColor(color, -0.375),
        shadeColor(color, -0.5),
        shadeColor(color, -0.675),
        shadeColor(color, -0.7),
        shadeColor(color, -0.775),
    };
  }

  private int[] unshiftIfNotExists(int[] array, int value) {
    boolean present = false;
    for (int i : array) {
      if (i == value) {
        present = true;
        break;
      }
    }
    if (!present) {
      int[] newArray = new int[array.length + 1];
      newArray[0] = value;
      System.arraycopy(array, 0, newArray, 1, newArray.length - 1);
      return newArray;
    }
    return array;
  }

  private int getSelectedItemPosition() {
    for (int i = 0; i < colors.length; i++) {
      if (colors[i] == color) {
        return i;
      }
    }
    return -1;
  }

}
