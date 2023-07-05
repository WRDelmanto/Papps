package com.wrdelmanto.papps.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

private lateinit var editor: SharedPreferences.Editor
private lateinit var sharedPreferences: SharedPreferences

// Shared preferences variables
// Click counter
const val SP_CC_HIGH_SCORE = "SHARED_PREFERENCES_CLICK_COUNTER_HIGH_SCORE"

// Random letter
const val SP_RL_LETTER_HISTORY = "SHARED_PREFERENCES_RANDOM_LETTER_LETTER_HISTORY"

// Random number
const val SP_RN_NUMBER_HISTORY = "SHARED_PREFERENCES_RANDOM_NUMBER_NUMBER_HISTORY"
const val SP_RN_MIN = "SHARED_PREFERENCES_RANDOM_NUMBER_MIN"
const val SP_RN_MAX = "SHARED_PREFERENCES_RANDOM_NUMBER_MAX"

// Tip
const val SP_T_TIP_SWITCH = "SHARED_PREFERENCES_TIP_TIP_SWITCH"
const val SP_T_TOTAL_SWITCH = "SHARED_PREFERENCES_TIP_TOTAL_SWITCH"
const val SP_T_TIP_PERCENTAGE = "SHARED_PREFERENCES_TIP_TIP_PERCENTAGE"

// Easter egg
const val SP_EASTER_EGG = "SHARED_PREFERENCES_EASTER_EGG"

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

    return if (checkKeySharedPreferences(context, key)) {
        when (valueType) {
            String -> sharedPreferences.getString(key, "")
            Int -> sharedPreferences.getInt(key, 0)
            Boolean -> sharedPreferences.getBoolean(key, false)
            Long -> sharedPreferences.getLong(key, 0L)
            Float -> sharedPreferences.getFloat(key, 0F)
            else -> error("Only primitive types can be returned from SharedPreferences")
        }
    } else null
}

/**
 * Check if a key exists at Shared Preferences.
 *
 * @param context
 * @param key
 *
 * @return True if a specified Key exists at Shared Preferences.
 */
fun checkKeySharedPreferences(context: Context, key: String): Boolean {
    configSharedPreferences(context)
    return sharedPreferences.contains(key)
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