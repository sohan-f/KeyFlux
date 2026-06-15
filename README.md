# KeyFlux

KeyFlux is an advanced LSPosed/Xposed module designed to enhance and customize the Google Keyboard (Gboard) experience. 

It unlocks hidden features, bypasses certain limitations, and integrates new capabilities directly into the native Gboard settings interface.

## Features

- **Enable Hidden Flags**: Easily toggle Gboard's internal Phenotype flags directly from the settings.
- **Multilingual Typing**: Enable concurrent multilingual typing support.
- **Floating Keyboard Layouts**: Unlock hidden floating keyboard options and window layouts.
- **Enhanced Clipboard History**: Bypass default clipboard limits. Customize the clipboard history retention duration (select specific days or keep forever).
- **Native Integration**: All new options are injected directly into Gboard's native settings screens with matching layouts and icons for a seamless experience.

## Compatibility

- **Android Versions**: Android 11 (API 30) through Android 15 (API 35).
- **Architecture**: `arm64-v8a`
- **Hook Framework**: LSPosed 1.8.x - 1.9.x (API 100/101 compatibility).
- **Target App**: Google Gboard.

## Installation

1. Install [LSPosed](https://github.com/LSPosed/LSPosed) on your rooted device.
2. Download the latest KeyFlux APK from the [Releases](#) page and install it.
3. Open the LSPosed Manager app and enable the **KeyFlux** module.
4. Make sure **Gboard** is selected in the scope of the module.
5. Force stop Gboard to apply changes.
6. Open Gboard settings to configure KeyFlux options.

## Build Instructions

To build the project locally, you need Android Studio and Gradle 8.5+.

```bash
# Compile and build the debug APK
./gradlew assembleDebug

# Build the release APK
./gradlew assembleRelease
```

## License

This project is licensed under the MIT License.
