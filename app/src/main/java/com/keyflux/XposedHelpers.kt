package com.keyflux

import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

object XposedHelpers {
    class ClassNotFoundError(cause: Throwable) : Exception(cause)

    fun findClass(className: String, classLoader: ClassLoader): Class<*> {
        return try {
            Class.forName(className, false, classLoader)
        } catch (e: ClassNotFoundException) {
            throw ClassNotFoundError(e)
        }
    }

    fun findMethodExact(clazz: Class<*>, methodName: String, vararg parameterTypes: Class<*>): Method {
        return clazz.getDeclaredMethod(methodName, *parameterTypes).apply {
            isAccessible = true
        }
    }

    fun callMethod(obj: Any, methodName: String, vararg args: Any?): Any? {
        val clazz = obj.javaClass
        val methods = clazz.methods + clazz.declaredMethods
        val method = methods.firstOrNull { it.name == methodName && it.parameterTypes.size == args.size }
            ?: throw NoSuchMethodError("Method $methodName not found in ${clazz.name}")
        method.isAccessible = true
        return method.invoke(obj, *args)
    }

    fun getObjectField(obj: Any, fieldName: String): Any? {
        var clazz: Class<*>? = obj.javaClass
        while (clazz != null) {
            try {
                val field = clazz.getDeclaredField(fieldName)
                field.isAccessible = true
                return field.get(obj)
            } catch (e: NoSuchFieldException) {
                clazz = clazz.superclass
            }
        }
        throw NoSuchFieldError("Field $fieldName not found in ${obj.javaClass.name}")
    }

    fun setObjectField(obj: Any, fieldName: String, value: Any?) {
        var clazz: Class<*>? = obj.javaClass
        while (clazz != null) {
            try {
                val field = clazz.getDeclaredField(fieldName)
                field.isAccessible = true
                field.set(obj, value)
                return
            } catch (e: NoSuchFieldException) {
                clazz = clazz.superclass
            }
        }
        throw NoSuchFieldError("Field $fieldName not found in ${obj.javaClass.name}")
    }
    
    fun getIntField(obj: Any, fieldName: String): Int {
        return getObjectField(obj, fieldName) as Int
    }
    
    fun getBooleanField(obj: Any, fieldName: String): Boolean {
        return getObjectField(obj, fieldName) as Boolean
    }
}
