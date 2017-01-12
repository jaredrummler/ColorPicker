# Color Picker

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jrummyapps/colorpicker/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.jrummyapps/colorpicker)
[![License](http://img.shields.io/:license-apache-blue.svg)](LICENSE)
[![API](https://img.shields.io/badge/API-14%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=14) 
[![Twitter Follow](https://img.shields.io/twitter/follow/jrummy16.svg?style=social)](https://twitter.com/jrummy16)

A color picker is something that has always been missing from the standard set of components which developers can build their user interface in Android with. This is a color picker which Daniel Nilsson wrote and we improved on. It includes a simple dialog for choosing a color as well as a `ColorPreference` for usage in a `PreferenceFragment`.

## Screenshots
![GIF](art/demo.gif)
&nbsp;&nbsp;
<img src="art/screenshot3.png" width="300" alt="Screenshot">


## Usage

Add the `ColorPreference` to your preference XML:

```xml
<PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android"
                  xmlns:app="http://schemas.android.com/apk/res-auto">

  <PreferenceCategory>

    <com.jrummyapps.android.colorpicker.ColorPreference
        android:defaultValue="@color/color_default"
        android:key="default_color"
        android:summary="@string/color_default_summary"
        android:title="@string/color_default_title"/>

    ...

  </PreferenceCategory>

</PreferenceScreen>
```

You can add attributes to customize the `ColorPreference`:

| name            | type      | documentation                                                                         |
|-----------------|-----------|---------------------------------------------------------------------------------------|
| dialogType      | enum      | "custom" to show the color picker, "preset" to show pre-defined colors                |
| showAlphaSlider | boolean   | Show a slider for changing the alpha of a color (adding transparency)                 |
| colorShape      | enum      | "square" or "circle" for the shape of the color preview                               |
| colorPresets    | reference | An int-array of pre-defined colors to show in the dialog                              |
| dialogTitle     | reference | The string resource id for the dialog title. By default the title is "Select a Color" |
| showColorShades | boolean   | true to show different shades of the selected color                                   |
| allowPresets    | boolean   | true to add a button to toggle to the custom color picker                             |
| allowCustom     | boolean   | true to add a button to toggle to the presets color picker                            |
| showDialog      | boolean   | true to let the ColorPreference handle showing the dialog                             |

You can also show a `ColorPickerDialog` without using the `ColorPreference`:

```java
ColorPickerDialog.newBuilder().setColor(color).show(activity);
```

All the attributes above can also be applied to the `ColorPickerDialog`. The activity that shows the dialog should implement `ColorPickerDialogListener` to get a callback when a color is selected.

For further doumentation about how to use the library, check the [demo](demo) app included in this project.

## Download

Download [the latest AAR](https://repo1.maven.org/maven2/com/jrummyapps/colorpicker/2.0.0/colorpicker-2.0.0.aar) or grab via Gradle:

```groovy
compile 'com.jrummyapps:colorpicker:2.0.0'
```
or Maven:
```xml
<dependency>
  <groupId>com.jrummyapps</groupId>
  <artifactId>colorpicker</artifactId>
  <version>2.0.0</version>
  <type>aar</type>
</dependency>
```

## License

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
