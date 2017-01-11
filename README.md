# color-picker-dialog

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
