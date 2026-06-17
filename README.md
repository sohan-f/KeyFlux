# KeyFlux

KeyFlux is an advanced LSPosed/Xposed module designed to enhance and customize the Google Keyboard (Gboard) experience. 

It unlocks hidden features, bypasses certain limitations, and integrates new capabilities directly into the native Gboard settings interface.

## 🚀 Features

- **Enable Hidden Flags**: Easily toggle Gboard's internal Phenotype flags directly from the settings.
- **Experimental & AI Features**: Unlock Google's latest hidden capabilities:
  - 🧠 **TFLite Neural Engine**: Accelerate local AI response speed (`enable_nwp_tflite_engine`).
  - ✨ **Proactive Emoji Kitchen**: Get smart emoji combinations on the fly (`enable_proactive_emoji_kitchen`, `enable_expression_moment`).
  - 🪄 **Inline Smart Suggestions**: See multi-word predictions inline (`enable_inline_suggestions_on_decoder_side`).
  - ⚡ **Fast Access Bar & Material Silk Design**: Enable the new UI redesign without key shadows and using Google Sans (`keyboard_redesign_google_sans`, `enable_fast_access_bar`).
  - 📋 **Clipboard Action Chips**: Extract entities and get smart action chips for copied text (`enable_clipboard_action_chips`).
- **Enhanced Clipboard History**: Bypass default clipboard limits. Customize the clipboard history retention duration (select specific days or keep forever).
- **Multilingual Typing**: Enable concurrent multilingual typing support.
- **Floating Keyboard Layouts**: Unlock hidden floating keyboard options and window layouts.
- **Native Integration**: All new options are injected directly into Gboard's native settings screens with matching layouts and icons for a seamless experience.
- **Performance Optimized**: Uses Kotlin Coroutines and background thread scanning (`DexKit`) to prevent UI freezes and ensure maximum performance.
- **Privacy Focused**: No sensitive data (like Clipboard content) is ever printed to Logcat.

## 📱 Compatibility

- **Android Versions**: Android 10 (API 29) through Android 17 (API 37).
- **Architecture**: `arm64-v8a`, `x86_64`
- **Hook Framework**: 
  - [Vector](https://github.com/NawafCode/Vector) (Recommended)
  - LSPosed 1.8.x - 1.9.x
- **API Support**: 100 / 101 / 102 (Hot Reloading).
- **Target App**: Google Gboard 13.x - 17.x.

## 🛠 Installation

1. Install [LSPosed](https://github.com/LSPosed/LSPosed) on your rooted device.
2. Download the latest KeyFlux APK from the [Releases](#) page and install it.
3. Open the LSPosed Manager app and enable the **KeyFlux** module.
4. Make sure **Gboard** is selected in the scope of the module.
5. Force stop Gboard to apply changes.
6. Open Gboard settings to configure KeyFlux options.

## 🏗 Build Instructions

To build the project locally, you need Android Studio and Gradle 8.5+.

```bash
# Compile and build the debug APK
./gradlew assembleDebug

# Build the release APK
./gradlew assembleRelease
```

## 📄 License

This project is licensed under the MIT License.
