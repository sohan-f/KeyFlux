package com.keyflux

import android.content.Context
import android.content.Intent
import android.net.Uri
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.findAndHookMethod

object PreferenceHooker {
    fun hook(plugin: PluginEntry, classLoader: ClassLoader) {
        plugin.apply {
            logAlways("PreferenceHooker.hook called!")
            try {
                hookPreferenceFragments(classLoader)
                logAlways("hookPreferenceFragments executed successfully")
            } catch (t: Throwable) {
                logAlways("Error in hookPreferenceFragments: ${t.message}")
            }
        }
    }
}
