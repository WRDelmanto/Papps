package com.wrdelmanto.papps.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

private lateinit var editor: SharedPreferences.Editor
private lateinit var sharedPreferences: SharedPreferences

/**
 * Put a value at Shared Preferences.
 *
 * @param context
 * @param key
 * @param value
 */
fun putSharedPreferences(context: Context, key: String, value: Any) {
    configSharedPreferences(context)
    when (value) {
        is String -> editor.putString(key, value)
        is Int -> editor.putInt(key, value)
        is Boolean -> editor.putBoolean(key, value)
        is Long -> editor.putLong(key, value)
        is Float -> editor.putFloat(key, value)
        else -> error("Only primitive types can be stored in SharedPreferences")
    }
    editor.commit()
}

/**
 * Get a value from Shared Preferences.
 *
 * @param context
 * @param key
 * @param valueType
 *
 * @return The value of the specified Key at Shared Preferences.
 */
fun getSharedPreferences(context: Context, key: String, valueType: Any): Any? {
    configSharedPreferences(context)
    return when (valueType) {
        String -> sharedPreferences.getString(key, "")
        Int -> sharedPreferences.getInt(key, 0)
        Boolean -> sharedPreferences.getBoolean(key, false)
        Long -> sharedPreferences.getLong(key, 0L)
        Float -> sharedPreferences.getFloat(key, 0F)
        else -> error("Only primitive types can be returned from SharedPreferences")
    }
}

/**
 * Remove a key from Shared Preferences.
 *
 * @param context
 * @param key
 */
fun removeKeySharedPreferences(context: Context, key: String) {
    configSharedPreferences(context)
    editor.remove(key)
    editor.commit()
}

/**
 * Erase any data from Shared Preferences.
 *
 * @param context
 */
fun clearSharedPreferences(context: Context) {
    configSharedPreferences(context)
    editor.clear()
    editor.commit()
}

/**
 * Configure Shared Preferences.
 *
 * @param context
 */
private fun configSharedPreferences(context: Context) {
    sharedPreferences = context.getSharedPreferences("PappsDB", MODE_PRIVATE)
    editor = sharedPreferences.edit()
}