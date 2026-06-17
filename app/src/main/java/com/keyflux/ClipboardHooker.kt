package com.keyflux

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ClipboardHooker {
    fun hook(plugin: PluginEntry, classLoader: ClassLoader) {
        plugin.apply {
            tryHook("com.google.android.apps.inputmethod.libs.clipboard.ClipboardContentProvider#onCreate") { _ ->
                findAndHookMethod(
                    "com.google.android.apps.inputmethod.libs.clipboard.ClipboardContentProvider",
                    classLoader,
                    "onCreate",
                    object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam) {
                            try {
                                val context = XposedHelpers.callMethod(param.thisObject, "getContext") as? Context ?: return
                                initializeKeyFlux(context, classLoader)
                            } catch (t: Throwable) {
                                logAlways("Error during ClipboardContentProvider#onCreate hook execution: ${t.message}")
                            }
                        }
                    }
                )
            }

            tryHook("com.google.android.apps.inputmethod.libs.clipboard.ClipboardContentProvider#query") { _ ->
                findAndHookMethod(
                    "com.google.android.apps.inputmethod.libs.clipboard.ClipboardContentProvider",
                    classLoader,
                    "query",
                    Uri::class.java,
                    Array<String>::class.java,
                    String::class.java,
                    Array<String>::class.java,
                    String::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            try {
                                if (param.args.size < 5) return

                                val arg2 = param.args[2]?.toString() ?: ""
                                val arg3 = param.args[3] as? Array<String>
                                val arg4 = param.args[4]?.toString()

                                val indexOf = arg2.indexOf("timestamp >= ?")
                                if (indexOf != -1) {
                                    var indexOfWen = 0
                                    StringBuilder(arg2).forEachIndexed { index, c ->
                                        if (index >= indexOf) return@forEachIndexed
                                        if (c == '?') {
                                            indexOfWen++
                                        }
                                    }

                                    val textTime = clipboardTextTime
                                    val afterTimeStamp = if (textTime < 0L) 0L else (System.currentTimeMillis() - textTime)
                                    arg3?.let {
                                        if (indexOfWen < it.size) {
                                            it[indexOfWen] = afterTimeStamp.toString()
                                            param.args[3] = it
                                            log(
                                                "Modifying time limit, " + if (textTime < 0L) "Forever" else SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ROOT).format(Date(afterTimeStamp))
                                            )
                                        }
                                    }
                                }
                                if (arg4 == "timestamp DESC limit 5") {
                                    param.args[4] = "timestamp DESC limit $clipboardTextSize"
                                    log("Modifying size limit, $clipboardTextSize")
                                }
                            } catch (t: Throwable) {
                                log("Error in ClipboardContentProvider query hook: ${t.message}")
                            }
                        }

                        override fun afterHookedMethod(param: MethodHookParam) {
                            try {
                                val cursor = param.result as? Cursor
                                log("query end, size=${cursor?.count ?: "null"}")
                            } catch (t: Throwable) {
                                log("Error in ClipboardContentProvider query afterHookedMethod: ${t.message}")
                            }
                        }
                    }
                )
            }

            tryHook("com.google.android.apps.inputmethod.libs.clipboard.ClipboardContentProvider#insert") { _ ->
                findAndHookMethod(
                    "com.google.android.apps.inputmethod.libs.clipboard.ClipboardContentProvider",
                    classLoader,
                    "insert",
                    Uri::class.java,
                    ContentValues::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            try {
                                if (!secureClipboard) return
                                val values = param.args[1] as? ContentValues ?: return
                                if (isCurrentFieldSecure) {
                                    log("Blocking clipboard insertion: Copied from secure/password field")
                                    param.result = null
                                    return
                                }
                                for (key in values.keySet()) {
                                    val valueStr = values.getAsString(key) ?: continue
                                    if (isSensitiveText(valueStr)) {
                                        log("Blocking clipboard insertion: Matches OTP/sensitive text pattern")
                                        param.result = null
                                        return
                                    }
                                }
                            } catch (t: Throwable) {
                                log("Error in ClipboardContentProvider insert hook: ${t.message}")
                            }
                        }
                    }
                )
            }

            tryHook("InputMethodService#onStartInput") {
                findAndHookMethod(
                    "android.inputmethodservice.InputMethodService",
                    classLoader,
                    "onStartInput",
                    android.view.inputmethod.EditorInfo::class.java,
                    Boolean::class.javaPrimitiveType,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            try {
                                val editorInfo = param.args[0] as? android.view.inputmethod.EditorInfo ?: return
                                val inputType = editorInfo.inputType
                                val isPassword = (inputType and android.text.InputType.TYPE_MASK_CLASS) == android.text.InputType.TYPE_CLASS_TEXT &&
                                    ((inputType and android.text.InputType.TYPE_MASK_VARIATION) == android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                                     (inputType and android.text.InputType.TYPE_MASK_VARIATION) == android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ||
                                     (inputType and android.text.InputType.TYPE_MASK_VARIATION) == android.text.InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD)
                                val isNumberPassword = (inputType and android.text.InputType.TYPE_MASK_CLASS) == android.text.InputType.TYPE_CLASS_NUMBER &&
                                    (inputType and android.text.InputType.TYPE_MASK_VARIATION) == android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD
                                isCurrentFieldSecure = isPassword || isNumberPassword
                                if (forceIncognito) {
                                    editorInfo.imeOptions = editorInfo.imeOptions or android.view.inputmethod.EditorInfo.IME_FLAG_NO_PERSONALIZED_LEARNING
                                }
                            } catch (t: Throwable) {
                                log("Error in onStartInput hook: ${t.message}")
                            }
                        }
                    }
                )
            }

            tryHook("HashSet") { _ ->
                findAndHookMethod(
                    HashSet::class.java,
                    "size",
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            try {
                                val set = param.thisObject as? HashSet<*> ?: return
                                if (set.isEmpty()) return
                                val first = set.iterator().run { if (hasNext()) next() else null }
                                val firstClassName = first?.javaClass?.name
                                if (firstClassName == "j$.time.Instant" || firstClassName == "java.time.Instant") {
                                    val map = XposedHelpers.getObjectField(set, "map") as? Map<*, *>
                                    if (map != null && map.size <= clipboardTextSize) {
                                        param.result = 5
                                    }
                                }
                            } catch (t: Throwable) {
                                log("Error in HashSet.size hook: ${t.message}")
                            }
                        }
                    }
                )
            }
        }
    }
}
