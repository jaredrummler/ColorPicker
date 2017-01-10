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

package com.jrummyapps.android.colorpicker.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.jrummyapps.android.animations.Rebound;
import com.jrummyapps.android.colorpicker.R;
import com.jrummyapps.android.colorpicker.view.ColorPanelView;
import com.jrummyapps.android.listeners.TouchReleaseListener;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ColorPaletteAdapter extends BaseAdapter {

  /*package*/ final OnColorSelectedListener listener;
  /*package*/ final int[] colors;
  /*package*/ int selectedPosition;

  public ColorPaletteAdapter(OnColorSelectedListener listener, int[] colors, int selectedPosition) {
    this.listener = listener;
    this.colors = colors;
    this.selectedPosition = selectedPosition;
  }

  @Override public int getCount() {
    return colors.length;
  }

  @Override public Object getItem(int position) {
    return colors[position];
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    final ViewHolder holder;
    if (convertView == null) {
      holder = new ViewHolder(parent.getContext());
      convertView = holder.layout;
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.colorPanelView.setColor(colors[position]);
    holder.imageView.setImageResource(selectedPosition == position ? R.drawable.colorpickerview__preset_checked : 0);
    holder.setOnClickListener(position);
    return convertView;
  }

  public void selectNone() {
    selectedPosition = -1;
    notifyDataSetChanged();
  }

  public interface OnColorSelectedListener {

    void onColorSelected(int color);
  }

  private final class ViewHolder {

    FrameLayout layout;
    ColorPanelView colorPanelView;
    ImageView imageView;

    ViewHolder(Context context) {
      layout = new FrameLayout(context);
      layout.setForegroundGravity(Gravity.CENTER);

      colorPanelView = new ColorPanelView(context);
      int size = context.getResources().getDimensionPixelSize(R.dimen.colorpickerview__item_size);
      colorPanelView.setLayoutParams(new FrameLayout.LayoutParams(size, size, Gravity.CENTER));
      colorPanelView.setClickable(true);
      layout.addView(colorPanelView);

      imageView = new ImageView(context);
      imageView.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER));
      layout.addView(imageView);

      layout.setTag(this);
    }

    void setOnClickListener(final int position) {
      colorPanelView.setOnTouchListener(new TouchReleaseListener() {
        @Override public void onTouched(View v) {
          Rebound.animate(0.25, v);
        }

        @Override public void onReleased(View v, boolean clicked) {
          Rebound.animate(0, v);
          if (clicked) {
            if (selectedPosition != position) {
              selectedPosition = position;
              layout.postDelayed(new Runnable() {
                @Override public void run() {
                  notifyDataSetChanged();
                  listener.onColorSelected(colors[position]);
                }
              }, 10);
            }
          }
        }
      });
    }

  }

}