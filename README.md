# color-picker-dialog

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jrummyapps/color-picker-dialog/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.jrummyapps/color-picker-dialog)
[![License](http://img.shields.io/:license-apache-blue.svg)](LICENSE)
[![API](https://img.shields.io/badge/API-14%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=14) 
[![Twitter Follow](https://img.shields.io/twitter/follow/jrummy16.svg?style=social)](https://twitter.com/jrummy16)

A color picker is something that has always been missing from the standard set of components which developers can build their user interface in Android with. This is a color picker which Daniel Nilsson wrote and we improved on. It includes a simple dialog for choosing a color as well as a `ColorPreference` for usage in a `PreferenceFragment`.

### Screenshots
<img src="art/screenshot1.png" width="370" alt="Screenshot">
<img src="art/screenshot3.png" width="370" alt="Screenshot">

### How to use

Show a `ColorPickerDialog`:

```java
ColorPickerDialog.newBuilder()
  .setDialogId(DIALOG_ID)
  .setColor(color)
  .show(getActivity());
```

The activity that shows the dialog should implement `ColorPickerDialogListener`.

For further doumentation about how to use the library, check the [demo](demo) app included in this project.

### Download

Download [the latest AAR](https://repo1.maven.org/maven2/com/jrummyapps/color-picker-dialog/1.0.0/color-picker-dialog-1.0.0.aar) or grab via Gradle:

```groovy
compile 'com.jrummyapps:color-picker-dialog:1.0.0'
```
or Maven:
```xml
<dependency>
  <groupId>com.jrummyapps</groupId>
  <artifactId>color-picker-dialog</artifactId>
  <version>1.0.0</version>
  <type>aar</type>
</dependency>
```

### LICENSE

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
