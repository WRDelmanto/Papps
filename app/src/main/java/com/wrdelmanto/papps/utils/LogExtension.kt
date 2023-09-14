package com.wrdelmanto.papps.utils

import android.os.Build
import android.util.Log
import com.wrdelmanto.papps.BuildConfig
import java.util.regex.Pattern

/**
 * Check if this is a production build.
 */
private val isProductBuild = Build.TYPE == "user"

/**
 * Flag to be used fot the most verbose log level.
 */
const val LEVEL_VERBOSE = 1

/**
 * Flag to be used fot the debug log level.
 */
const val LEVEL_DEBUG = 10

/**
 * Flag to be used fot the user build log level.
 */
const val LEVEL_USER = 100

/**
 * Tag prefix to be used across the application.
 */
private const val TAG_PREFIX = "WFX."

/**
 * Default tag.
 */
private const val DEFAULT_TAG = TAG_PREFIX

/**
 * Within a stacktrace, our class appears in this position.
 */
private const val CLASS_STACK_INDEX = 3

/**
 * Regex to match anonymous class
 */
private val anonymousClassPattern = Pattern.compile("(\\$\\d+)+$")

/**
 * Get current log level.
 */
@Suppress("KotlinConstantConditions")
val logLevel: Int = when {
    isProductBuild -> LEVEL_USER
    BuildConfig.BUILD_TYPE == "release" -> LEVEL_USER
    BuildConfig.BUILD_TYPE == "debug" -> LEVEL_DEBUG
    BuildConfig.BUILD_TYPE == "verbose" -> LEVEL_VERBOSE
    // Unknown build type
    else -> 0
}

/**
 * Returns a TAG for the caller class. Tag format is : MM.classname
 *
 * @return the Tag to be used for logging
 */
fun getTag(): String {
    return if (logLevel <= LEVEL_DEBUG) {
        var tag = Thread.currentThread().stackTrace[CLASS_STACK_INDEX].className
        val matcher = anonymousClassPattern.matcher(tag)

        if (matcher.find()) tag = matcher.replaceAll("")

        tag = TAG_PREFIX + tag.substring(tag.lastIndexOf('.') + 1)
        tag
    } else DEFAULT_TAG
}

/**
 * Inline function for the standard [Log.VERBOSE] message log.
 *
 * @param tag Used to identify the source of the log message
 * @param desc The message to be logged
 */
inline fun logV(tag: String = getTag(), desc: () -> String) {
    if (logLevel <= LEVEL_VERBOSE) Log.v(tag, desc())
}

/**
 * Inline function for the standard [Log.DEBUG] message log.
 *
 * @param tag Used to identify the source of the log message
 * @param desc The message to be logged
 */
inline fun logD(tag: String = getTag(), desc: () -> String) {
    if (logLevel <= LEVEL_DEBUG) Log.d(tag, desc())
}

/**
 * Inline function for the standard [Log.WARN] message log.
 *
 * @param tag Used to identify the source of the log message
 * @param desc The message to be logged
 */
inline fun logW(tag: String = getTag(), desc: () -> String) {
    if (logLevel <= LEVEL_DEBUG) Log.w(tag, desc())
}

/**
 * Inline function for the standard [Log.WARN] message log.
 *
 * @param tag Used to identify the source of the log message
 * @param desc The message to be logged
 */
inline fun logW(tag: String = getTag(), tr: Throwable , desc: () -> String) {
    if (logLevel <= LEVEL_DEBUG) Log.w(tag, desc(), tr)
}

/**
 * Inline function for the standard [Log.ERROR] message log.
 *
 * @param tag Used to identify the source of the log message
 * @param desc The message to be logged
 */
inline fun logE(tag: String = getTag(), desc: () -> String) = Log.e(tag, desc())

/**
 * Inline function for the standard [Log.ERROR] message log.
 *
 * @param tag Used to identify the source of the log message
 * @param desc The message to be logged
 */
inline fun logE(tag: String = getTag(), tr: Throwable , desc: () -> String) = Log.e(tag, desc(), tr)

/**
 * Inline function for the standard [Log.INFO] message log.
 *
 * @param tag Used to identify the source of the log message
 * @param desc The message to be logged
 */
inline fun logI(tag: String = getTag(), desc: () -> String) = Log.i(tag, desc())

/**
 * Inline function for the standard [Log.INFO] message log.
 *
 * @param tag Used to identify the source of the log message
 * @param desc The message to be logged
 */
inline fun logI(tag: String = getTag(), tr: Throwable , desc: () -> String) = Log.i(tag, desc(), tr)