package com.keyflux

import android.content.Context
import kotlinx.coroutines.*
import org.luckypray.dexkit.DexKitBridge

object ObfuscationMap {
    var isInitialized = false

    var bzfClass = "defpackage.bzf"
    var bzgClass = "defpackage.bzg"

    var prefGroupAddPreference = "am"
    var prefGroupGetPreferenceCount = "k"
    var prefGroupGetPreference = "o"
    var prefGroupFindPreference = "l"

    var prefSetKey = "O"
    var prefSetSummary = "n"
    var prefSetTitle = "U"
    var prefNotifyChanged = "v"

    var switchSetChecked = "k"

    var prefIntentField = "s"
    var prefKeyField = "r"
    var hashSetMapField = "map"

    class FragMethods(
        val getPreferenceScreen: String,
        val getContext: String,
        val addPreferencesFromResource: String,
        val bcMethod: String
    )

    val fragMethods = mutableMapOf<String, FragMethods>()

    fun initAsync(context: Context, apkPath: String, prefFragmentClasses: List<String>) {
        if (isInitialized) return
        
        val prefs = context.getSharedPreferences("keyflux_dexkit_cache", Context.MODE_PRIVATE)
        val pm = context.packageManager
        val pi = pm.getPackageInfo(context.packageName, 0)
        val appVersion = pi.longVersionCode
        
        val cachedVersion = prefs.getLong("app_version", -1L)
        if (cachedVersion == appVersion) {
            bzfClass = prefs.getString("bzfClass", "defpackage.bzf")!!
            bzgClass = prefs.getString("bzgClass", "defpackage.bzg")!!
            prefGroupAddPreference = prefs.getString("prefGroupAddPreference", "am")!!
            prefGroupGetPreferenceCount = prefs.getString("prefGroupGetPreferenceCount", "k")!!
            prefGroupGetPreference = prefs.getString("prefGroupGetPreference", "o")!!
            prefGroupFindPreference = prefs.getString("prefGroupFindPreference", "l")!!
            prefSetKey = prefs.getString("prefSetKey", "O")!!
            prefSetSummary = prefs.getString("prefSetSummary", "n")!!
            prefSetTitle = prefs.getString("prefSetTitle", "U")!!
            prefNotifyChanged = prefs.getString("prefNotifyChanged", "v")!!
            switchSetChecked = prefs.getString("switchSetChecked", "k")!!
            prefIntentField = prefs.getString("prefIntentField", "s")!!
            prefKeyField = prefs.getString("prefKeyField", "r")!!
            hashSetMapField = prefs.getString("hashSetMapField", "map")!!
            
            for (frag in prefFragmentClasses) {
                fragMethods[frag] = FragMethods(
                    getPreferenceScreen = prefs.getString("frag_prefScreen_$frag", "o")!!,
                    getContext = prefs.getString("frag_context_$frag", "x")!!,
                    addPreferencesFromResource = prefs.getString("frag_addPref_$frag", "bb")!!,
                    bcMethod = prefs.getString("frag_bc_$frag", "bc")!!
                )
            }
            isInitialized = true
            return
        }

        CoroutineScope(Dispatchers.Default).launch {
            try {
                DexKitBridge.create(apkPath)?.use { bridge ->
                        val bzfClassDef = async { DexKitHelper.getBzfClass(bridge) ?: "defpackage.bzf" }
                    val bzgClassDef = async { DexKitHelper.getBzgClass(bridge) ?: "defpackage.bzg" }
                    val prefGroupAddPreferenceDef = async { DexKitHelper.getAddPreferenceMethodName(bridge) }
                    val prefGroupGetPreferenceCountDef = async { DexKitHelper.getGetPreferenceCountMethodName(bridge) }
                    val prefGroupGetPreferenceDef = async { DexKitHelper.getGetPreferenceMethodName(bridge) }
                    val prefGroupFindPreferenceDef = async { DexKitHelper.getFindPreferenceMethodName(bridge) }
                    val prefSetKeyDef = async { DexKitHelper.getSetKeyMethodName(bridge) }
                    val prefSetSummaryDef = async { DexKitHelper.getSetSummaryMethodName(bridge) }
                    val prefSetTitleDef = async { DexKitHelper.getSetTitleMethodName(bridge) }
                    val prefNotifyChangedDef = async { DexKitHelper.getNotifyChangedMethodName(bridge) }
                    val switchSetCheckedDef = async { DexKitHelper.getSetCheckedMethodName(bridge) }
                    val prefIntentFieldDef = async { DexKitHelper.getIntentFieldName(bridge) }
                    val prefKeyFieldDef = async { DexKitHelper.getKeyFieldName(bridge) }
                    val hashSetMapFieldDef = async { DexKitHelper.getMapField(bridge) }
                    
                    val fragMethodsDefs = prefFragmentClasses.map { frag ->
                        frag to async {
                            val m1 = DexKitHelper.getPreferenceScreenMethodName(bridge, frag)
                            val m2 = DexKitHelper.getContextMethodName(bridge, frag)
                            val m3 = DexKitHelper.getBbMethodName(bridge, frag)
                            val m4 = DexKitHelper.getBcMethodName(bridge, frag)
                            FragMethods(m1, m2, m3, m4)
                        }
                    }.toMap()

                    bzfClass = bzfClassDef.await()
                    bzgClass = bzgClassDef.await()
                    prefGroupAddPreference = prefGroupAddPreferenceDef.await()
                    prefGroupGetPreferenceCount = prefGroupGetPreferenceCountDef.await()
                    prefGroupGetPreference = prefGroupGetPreferenceDef.await()
                    prefGroupFindPreference = prefGroupFindPreferenceDef.await()
                    prefSetKey = prefSetKeyDef.await()
                    prefSetSummary = prefSetSummaryDef.await()
                    prefSetTitle = prefSetTitleDef.await()
                    prefNotifyChanged = prefNotifyChangedDef.await()
                    switchSetChecked = switchSetCheckedDef.await()
                    prefIntentField = prefIntentFieldDef.await()
                    prefKeyField = prefKeyFieldDef.await()
                    hashSetMapField = hashSetMapFieldDef.await()

                    val edit = prefs.edit()
                    edit.putLong("app_version", appVersion)
                    edit.putString("bzfClass", bzfClass)
                    edit.putString("bzgClass", bzgClass)
                    edit.putString("prefGroupAddPreference", prefGroupAddPreference)
                    edit.putString("prefGroupGetPreferenceCount", prefGroupGetPreferenceCount)
                    edit.putString("prefGroupGetPreference", prefGroupGetPreference)
                    edit.putString("prefGroupFindPreference", prefGroupFindPreference)
                    edit.putString("prefSetKey", prefSetKey)
                    edit.putString("prefSetSummary", prefSetSummary)
                    edit.putString("prefSetTitle", prefSetTitle)
                    edit.putString("prefNotifyChanged", prefNotifyChanged)
                    edit.putString("switchSetChecked", switchSetChecked)
                    edit.putString("prefIntentField", prefIntentField)
                    edit.putString("prefKeyField", prefKeyField)
                    edit.putString("hashSetMapField", hashSetMapField)

                    for ((frag, def) in fragMethodsDefs) {
                        val m = def.await()
                        fragMethods[frag] = m
                        edit.putString("frag_prefScreen_$frag", m.getPreferenceScreen)
                        edit.putString("frag_context_$frag", m.getContext)
                        edit.putString("frag_addPref_$frag", m.addPreferencesFromResource)
                        edit.putString("frag_bc_$frag", m.bcMethod)
                    }
                    
                    edit.apply()
                    isInitialized = true
            }
            } catch (t: Throwable) {
                de.robv.android.xposed.XposedBridge.log("KeyFlux: Failed to initialize ObfuscationMap: ${t.message}")
            }
        }
    }
}
