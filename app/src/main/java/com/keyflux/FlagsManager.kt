package com.keyflux

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import org.luckypray.dexkit.wrap.DexMethod

object FlagsManager {
    fun hook(plugin: PluginEntry, classLoader: ClassLoader, dexMethod: DexMethod) {
        plugin.apply {
            val methodName = dexMethod.name
            val className = dexMethod.className
            val tag = "$className#$methodName"
            log("Hooking ReadConfig method: $tag")
            tryHook(tag) {
                findAndHookMethod(
                    className, classLoader, methodName,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            try {
                                val name = getFlagName(param.thisObject)
                                if (name == null) {
                                    log("Flag name field is null or unresolved")
                                    return
                                }

                                val overrideTrueVal = if (name.endsWith("_language_tags") || name.endsWith("_locales") || name.endsWith("_countries")) "*" else true
                                val overrideFalseVal = if (name.endsWith("_language_tags") || name.endsWith("_locales") || name.endsWith("_countries")) "" else false

                                // Default clipboard entity extraction overrides
                                if (!enableClipboardChips && (
                                    name == "enable_clipboard_entity_extraction" ||
                                    name == "enable_clipboard_query_refactoring"
                                )) {
                                    param.result = overrideFalseVal
                                    return
                                }

                                if (enablePrivacy) {
                                    if (name == "disable_correction_storage" ||
                                        name == "disable_content_capture_for_input_view" ||
                                        name == "deprecate_native_log_event"
                                    ) {
                                        param.result = overrideTrueVal
                                        if (logSwitch) log("Privacy: Overrode flag $name to $overrideTrueVal")
                                        return
                                    }
                                    if (name == "always_log_speed_stats" ||
                                        name == "enable_logging_for_emoji_search_query" ||
                                        name == "enable_internal_speech_enhancement_pii_logging" ||
                                        name == "enable_report_from_training_cache" ||
                                        name == "enable_chinese_training_cache" ||
                                        name == "enable_spell_checker_training_cache" ||
                                        name == "enable_training_cache_metrics_processors" ||
                                        name == "enable_conversation_id_in_training_cache" ||
                                        name == "enable_auto_correction_stats" ||
                                        name == "enable_metric_counts_stats" ||
                                        name == "enable_spatial_stats" ||
                                        name == "enable_spell_checker_stats" ||
                                        name == "enable_typo_stats" ||
                                        name == "voice_donation_promo_banner" ||
                                        name == "voice_donation_confirm_banner"
                                    ) {
                                        param.result = overrideFalseVal
                                        if (logSwitch) log("Privacy: Overrode flag $name to $overrideFalseVal")
                                        return
                                    }
                                }

                                // Overrides
                                if (enableAi && (
                                    name == "enable_ai_core_llm" ||
                                    name == "enable_ai_core_smart_reply" ||
                                    name == "enable_emojify" ||
                                    name == "enable_emojify_settings_option" ||
                                    name == "enable_smart_reply" ||
                                    name == "enable_smart_compose" ||
                                    name == "enable_smart_compose_inline_suggestions" ||
                                    name == "enable_inline_suggestions" ||
                                    name == "enable_inline_suggestions_on_all_apps" ||
                                    name == "enable_custom_sticker_tab" ||
                                    name == "enable_custom_sticker_lol_fix" ||
                                    name == "enable_custom_sticker_naive_prompt_expander" ||
                                    name == "enable_sticker_predictions_while_typing" ||
                                    name == "enable_animated_emoji_content_suggestions" ||
                                    name == "show_animated_emoji_in_expression_moment" ||
                                    name == "enable_emojify_language_tags" ||
                                    name == "enable_emojify_model_language_tags" ||
                                    name == "enable_expression_moment_language_tags" ||
                                    name == "enable_expression_moment_proactive_emoji_kitchen_language_tags" ||
                                    name == "enable_dynamic_art_language_tags" ||
                                    name == "enable_tenor_trending_term_v2_for_language_tags"
                                )) {
                                    param.result = overrideTrueVal
                                    if (logSwitch) log("Overrode flag $name to $overrideTrueVal")
                                    return
                                }

                                if (enableGrammar && (
                                    name == "enable_grammar_checker" ||
                                    name == "enable_on_device_proofread" ||
                                    name == "enable_llm_based_grammar_checker" ||
                                    name == "enable_writing_tools_cooperative_mode" ||
                                    name == "enable_text_conversion" ||
                                    name == "enable_highlight_voice_reconversion_composing_text" ||
                                    name == "nga_enable_undo_delete" ||
                                    name == "enable_proofread" ||
                                    name == "enable_pk_auto_correction_locales"
                                )) {
                                    param.result = overrideTrueVal
                                    if (logSwitch) log("Overrode flag $name to $overrideTrueVal")
                                    return
                                }

                                if (enableMultilingual && (
                                    name == "enable_multilingual_typing" ||
                                    name == "enable_crank_for_first_supported_locale_in_multilingual" ||
                                    name == "enable_crank_for_primary_locale_in_multilingual" ||
                                    name == "enable_more_candidates_view_for_multilingual" ||
                                    name == "enable_auto_multi_lang_on_all_pixel_devices" ||
                                    name == "enable_speech_enhancement_for_multilang_users"
                                )) {
                                    param.result = overrideTrueVal
                                    if (logSwitch) log("Overrode flag $name to $overrideTrueVal")
                                    return
                                }

                                if (enableFloating && (
                                    name == "enable_auto_float_keyboard_in_landscape" ||
                                    name == "enable_auto_float_keyboard_in_multi_window" ||
                                    name == "enable_auto_float_keyboard_in_freeform" ||
                                    name == "enable_split_keyboard_on_tablet_large" ||
                                    name == "enable_dynamic_font_size_slider" ||
                                    name == "enable_split_keyboard" ||
                                    name == "enable_tablet_split_keyboard" ||
                                    name == "enable_enter_exit_animation"
                                )) {
                                    param.result = overrideTrueVal
                                    if (logSwitch) log("Overrode flag $name to $overrideTrueVal")
                                    return
                                }

                                if (enableEmojiKitchen && (
                                    name == "enable_emoji_kitchen_browse" ||
                                    name == "enable_emoji_kitchen_browse_entry_point_v2" ||
                                    name == "enable_emoji_kitchen_for_zero_state_emojis" ||
                                    name == "enable_embedded_photo_picker" ||
                                    name == "enable_emoji_search_v2" ||
                                    name == "enable_emoji_recommendations" ||
                                    name == "enable_play_emoji_kitchen_mix_animation"
                                )) {
                                    param.result = overrideTrueVal
                                    if (logSwitch) log("Overrode flag $name to $overrideTrueVal")
                                    return
                                }

                                if (enableAccessPoint && (
                                    name == "enable_access_points_menu_redesign" ||
                                    name == "enable_access_point_keyboard" ||
                                    name == "use_silk_theme_by_default" ||
                                    name == "use_system_font" ||
                                    name == "enable_custom_themes" ||
                                    name == "enable_silk_theme" ||
                                    name == "enable_candidates_access_points_switching_animation" ||
                                    name == "keyboard_redesign_google_sans" ||
                                    name == "keyboard_redesign_forbid_key_shadows"
                                )) {
                                    param.result = overrideTrueVal
                                    if (logSwitch) log("Overrode flag $name to $overrideTrueVal")
                                    return
                                }

                                if (meteredDownloads && (
                                    name == "allow_language_pack_downloads_on_metered_connections" ||
                                    name == "allow_metered_network_to_download_langid_model" ||
                                    name == "allow_metered_small_speech_pack_downloads" ||
                                    name == "force_speech_language_pack_updates"
                                )) {
                                    param.result = overrideTrueVal
                                    if (logSwitch) log("Overrode flag $name to $overrideTrueVal")
                                    return
                                }

                                if (enableInlineSuggestions && (
                                    name == "enable_inline_suggestions_on_decoder_side" ||
                                    name == "enable_multiword_predictions_as_inline_from_crank_cifg"
                                )) {
                                    param.result = overrideTrueVal
                                    if (logSwitch) log("Overrode flag $name to $overrideTrueVal")
                                    return
                                }

                                if (enableProactiveEmoji && (
                                    name == "enable_proactive_emoji_kitchen" ||
                                    name == "enable_expression_moment"
                                )) {
                                    param.result = overrideTrueVal
                                    if (logSwitch) log("Overrode flag $name to $overrideTrueVal")
                                    return
                                }

                                if (enableClipboardChips && (
                                    name == "enable_clipboard_action_chips" ||
                                    name == "enable_clipboard_entity_extraction" ||
                                    name == "enable_copy_to_reply"
                                )) {
                                    param.result = overrideTrueVal
                                    if (logSwitch) log("Overrode flag $name to $overrideTrueVal")
                                    return
                                }

                                if (enableTfliteEngine && (
                                    name == "enable_nwp_tflite_engine" ||
                                    name == "enable_emoji_predictor_tflite_engine"
                                )) {
                                    param.result = overrideTrueVal
                                    if (logSwitch) log("Overrode flag $name to $overrideTrueVal")
                                    return
                                }

                                if (enableFastAccess && (
                                    name == "enable_fast_access_bar" ||
                                    name == "keyboard_redesign_google_sans"
                                )) {
                                    param.result = overrideTrueVal
                                    if (logSwitch) log("Overrode flag $name to $overrideTrueVal")
                                    return
                                }

                            } catch (t: Throwable) {
                                log("Error evaluating flag override: ${t.message}")
                            }
                        }
                    }
                )
            }
        }
    }
}
