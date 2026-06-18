## 🚀 KeyFlux v1.0.4 (The Ultimate Compatibility Update)

This update represents a complete overhaul of how KeyFlux hooks into Gboard, combining the best of all previous attempts into one incredibly robust solution!

### 🛠️ What's Changed
- **Dual Context Hooking:** To bypass Samsung OneUI 8 (Android 16) restrictions, KeyFlux now uses both `Application#onCreate` AND `Instrumentation#callApplicationOnCreate`. This guarantees the module loads properly on all OEM ROMs without failing.
- **Fail-Proof Preference Scanner:** The dynamic preference scanner no longer relies on ANY obfuscated letters (`o`, `x`, `e`). Instead, it scans the entire Gboard app's internal structure at runtime, checking return types and object instances (e.g., `androidx.preference.PreferenceGroup`).
- **Direct Argument Hooking:** If the settings screen is intercepted mid-creation, KeyFlux intercepts the `PreferenceScreen` object directly from the method arguments (`param.args[1]`), completely bypassing the need for reflection!
- **Header Fragment Support:** Fixed a silent failure where KeyFlux would abort if it encountered a settings category (like a Header Fragment) that didn't have preferences.

### 📥 Installation
1. Download the `KeyFlux_release_v1.0.4_...apk` below.
2. Install the update.
3. Force stop Gboard.
4. Enjoy!
