# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

### Added
- **Experimental & AI Features Section**: Added a new settings category in Gboard containing 5 new toggles.
  - **Inline Suggestions**: Toggle `enable_inline_suggestions_on_decoder_side`.
  - **Proactive Emoji Kitchen**: Toggle `enable_proactive_emoji_kitchen` and `enable_expression_moment`.
  - **Clipboard Action Chips**: Toggle `enable_clipboard_action_chips`, `enable_clipboard_entity_extraction`, and `enable_copy_to_reply`.
  - **TFLite Neural Engine**: Toggle `enable_nwp_tflite_engine` and `enable_emoji_predictor_tflite_engine`.
  - **Fast Access Bar**: Toggle `enable_fast_access_bar`, `keyboard_redesign_google_sans`, and `keyboard_redesign_forbid_key_shadows`.
- **Localization**: Added full translation support for AR, EN, FA, UR, ES, FR, DE, RU, and TR for the new experimental settings.

### Changed
- **Performance**: Integrated Kotlin Coroutines for heavy background operations. `DexKit` scanning is now executed on `Dispatchers.Default` preventing Main Thread blocks.
- **Error Handling**: Implemented standardized error logging across all empty or silent `catch (t: Throwable)` blocks without exposing sensitive data.
- **Flags Management**: Improved conditional mapping in `FlagsManager.kt` to allow conflict-free toggling of overlapping Gboard flags.

### Fixed
- **Privacy Leak**: Fixed an issue in `ClipboardHooker.kt` where sensitive clipboard data might be leaked to Logcat. Extended `isSensitiveText` patterns to prevent logging passwords, emails, and sensitive URLs even when debugging is enabled.

## [1.0.0] - 2026-06-15
### Added
- Initial Release.
- Clipboard History limitation bypass (keep forever or specific days).
- Native Gboard settings injection (`PreferenceHooker`).
- Dynamic layout injection using DexKit (`PluginEntry`).
- Basic Flags overriding engine (`FlagsManager`).
