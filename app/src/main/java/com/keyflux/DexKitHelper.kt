package com.keyflux

import org.luckypray.dexkit.DexKitBridge

object DexKitHelper {

    // --- Interfaces ---

    fun getBzfClass(bridge: DexKitBridge): String? {
        val fields = bridge.findField {
            matcher { declaredClass("androidx.preference.Preference") }
        }
        for (field in fields) {
            val type = field.typeName
            if (type.startsWith("defpackage.")) {
                val methods = bridge.findMethod { matcher { declaredClass(type) } }
                if (methods.size == 1 && methods[0].returnTypeName == "boolean" && methods[0].paramTypeNames.size == 1 && methods[0].paramTypeNames[0] == "java.lang.Object") {
                    return type
                }
            }
        }
        return "defpackage.bzf"
    }

    fun getBzgClass(bridge: DexKitBridge): String? {
        val fields = bridge.findField {
            matcher { declaredClass("androidx.preference.Preference") }
        }
        for (field in fields) {
            val type = field.typeName
            if (type.startsWith("defpackage.")) {
                val methods = bridge.findMethod { matcher { declaredClass(type) } }
                if (methods.size == 1 && methods[0].returnTypeName == "boolean" && methods[0].paramTypeNames.size == 1 && methods[0].paramTypeNames[0] == "androidx.preference.Preference") {
                    return type
                }
            }
        }
        return "defpackage.bzg"
    }

    // --- PreferenceGroup Methods ---

    fun getAddPreferenceMethodName(bridge: DexKitBridge): String {
        val methods = bridge.findMethod {
            matcher {
                declaredClass("androidx.preference.PreferenceGroup")
                paramTypes("androidx.preference.Preference")
                returnType("void")
                usingStrings("Found duplicated key: \"")
            }
        }
        return methods.firstOrNull()?.name ?: "am"
    }

    fun getGetPreferenceCountMethodName(bridge: DexKitBridge): String {
        val methods = bridge.findMethod {
            matcher {
                declaredClass("androidx.preference.PreferenceGroup")
                paramTypes()
                returnType("int")
            }
        }
        return methods.firstOrNull()?.name ?: "k"
    }

    fun getGetPreferenceMethodName(bridge: DexKitBridge): String {
        val methods = bridge.findMethod {
            matcher {
                declaredClass("androidx.preference.PreferenceGroup")
                paramTypes("int")
                returnType("androidx.preference.Preference")
            }
        }
        return methods.firstOrNull()?.name ?: "o"
    }

    fun getFindPreferenceMethodName(bridge: DexKitBridge): String {
        val methods = bridge.findMethod {
            matcher {
                declaredClass("androidx.preference.PreferenceGroup")
                paramTypes("java.lang.CharSequence")
                returnType("androidx.preference.Preference")
            }
        }
        return methods.firstOrNull()?.name ?: "l"
    }

    // --- Preference Methods ---

    fun getSetKeyMethodName(bridge: DexKitBridge): String {
        val methods = bridge.findMethod {
            matcher {
                declaredClass("androidx.preference.Preference")
                paramTypes("java.lang.String")
                returnType("void")
                usingStrings("Preference does not have a key assigned.")
            }
        }
        return methods.firstOrNull()?.name ?: "O"
    }

    fun getSetSummaryMethodName(bridge: DexKitBridge): String {
        val methods = bridge.findMethod {
            matcher {
                declaredClass("androidx.preference.Preference")
                paramTypes("java.lang.CharSequence")
                returnType("void")
                usingStrings("Preference already has a SummaryProvider set.")
            }
        }
        return methods.firstOrNull()?.name ?: "n"
    }

    fun getSetTitleMethodName(bridge: DexKitBridge): String {
        val summaryName = getSetSummaryMethodName(bridge)
        val methods = bridge.findMethod {
            matcher {
                declaredClass("androidx.preference.Preference")
                paramTypes("java.lang.CharSequence")
                returnType("void")
            }
        }
        return methods.firstOrNull { it.name != summaryName }?.name ?: "U"
    }

    fun getSetCheckedMethodName(bridge: DexKitBridge): String {
        val methods = bridge.findMethod {
            matcher {
                declaredClass("androidx.preference.TwoStatePreference")
                paramTypes("boolean")
                returnType("void")
            }
        }
        return methods.firstOrNull { it.name == "k" }?.name ?: "k"
    }

    fun getNotifyChangedMethodName(bridge: DexKitBridge): String {
        val methods = bridge.findMethod {
            matcher {
                declaredClass("androidx.preference.Preference")
                paramTypes()
                returnType("void")
            }
        }
        return methods.firstOrNull { it.name == "v" }?.name ?: "v"
    }

    // --- Fragment Methods ---

    fun getPreferenceScreenMethodName(bridge: DexKitBridge, fragmentClass: String): String {
        val methods = bridge.findMethod {
            matcher {
                declaredClass(fragmentClass)
                paramTypes()
                returnType("androidx.preference.PreferenceScreen")
            }
        }
        return methods.firstOrNull()?.name ?: "o"
    }

    fun getContextMethodName(bridge: DexKitBridge, fragmentClass: String): String {
        val methods = bridge.findMethod {
            matcher {
                declaredClass(fragmentClass)
                paramTypes()
                returnType("android.content.Context")
            }
        }
        return methods.firstOrNull()?.name ?: "x"
    }

    fun getBbMethodName(bridge: DexKitBridge, fragmentClass: String): String {
        val methods = bridge.findMethod {
            matcher {
                declaredClass(fragmentClass)
                paramTypes("int")
                returnType("void")
            }
        }
        return methods.firstOrNull { it.name == "bb" }?.name ?: "bb"
    }

    fun getBcMethodName(bridge: DexKitBridge, fragmentClass: String): String {
        val methods = bridge.findMethod {
            matcher {
                declaredClass(fragmentClass)
                paramTypes("int", "androidx.preference.PreferenceGroup")
                returnType("void")
            }
        }
        return methods.firstOrNull()?.name ?: "bc"
    }

    // --- Fields ---

    fun getIntentFieldName(bridge: DexKitBridge): String {
        val fields = bridge.findField {
            matcher {
                declaredClass("androidx.preference.Preference")
                type("android.content.Intent")
            }
        }
        return fields.firstOrNull()?.name ?: "s"
    }

    fun getKeyFieldName(bridge: DexKitBridge): String {
        val fields = bridge.findField {
            matcher {
                declaredClass("androidx.preference.Preference")
                type("java.lang.String")
            }
        }
        return fields.firstOrNull { it.name == "r" }?.name ?: "r"
    }

    fun getFlagNameField(bridge: DexKitBridge, clazzName: String): String {
        val fields = bridge.findField {
            matcher {
                declaredClass(clazzName)
                type("java.lang.String")
            }
        }
        return fields.firstOrNull { it.name == "a" }?.name ?: "a"
    }

    fun getMapField(bridge: DexKitBridge): String {
        // DexKit cannot search Android framework bootclasspath classes like java.util.HashSet.
        // It's a standard JVM field.
        return "map"
    }
}
