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

package com.jrummyapps.android.colorpicker.demo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class AboutDialog extends AlertDialog {

  private TextView mAppNameText;
  private TextView mAboutText;
  private TextView mVersionText;

  public AboutDialog(Context context) {
    super(context);
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View layout = inflater.inflate(R.layout.dialog_about, null);

    mAboutText = (TextView) layout.findViewById(android.R.id.text2);
    mVersionText = (TextView) layout.findViewById(android.R.id.text1);
    mAppNameText = (TextView) layout.findViewById(android.R.id.title);

    setView(layout);
    loadAbout();
    setTitle("About");
    setButton(DialogInterface.BUTTON_POSITIVE, getContext().getString(android.R.string.ok), new OnClickListener() {

      @Override public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
  }

  private void loadAbout() {
    try {
      PackageInfo pi = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
      mAppNameText.setText(R.string.app_name);
      mVersionText.setText(pi.versionName);
      String html = "<p><strong>Developed by:</strong></p>Daniel Nilsson<br>Jared Rummler";
      mAboutText.setText(Html.fromHtml(html));
    } catch (NameNotFoundException ignored) {
      // WTF
    }
  }

}
