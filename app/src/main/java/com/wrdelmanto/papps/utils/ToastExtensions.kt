package com.wrdelmanto.papps.utils

import android.content.Context
import android.widget.Toast.LENGTH_SHORT
import es.dmoral.toasty.Toasty

const val TOAST_ERROR_MESSAGE = "Only CharSequence and Int can be used at Toasts"

/**
 * Different types of Toasts
 * More info: https://github.com/GrenderG/Toasty
 */

/**
 * Error Toast
 *
 * @param context
 * @param value
 * @param duration
 */
fun showErrorToast(context: Context, value: Any, duration: Int = LENGTH_SHORT) {
    configToast()
    when (value) {
        is CharSequence -> Toasty.error(context, value, duration, true).show()
        is Int -> Toasty.error(context, context.resources.getText(value), duration, true).show()
        else -> error(TOAST_ERROR_MESSAGE)
    }
}

/**
 * Success Toast
 *
 * @param context
 * @param value
 * @param duration
 */
fun showSuccessToast(context: Context, value: Any, duration: Int = LENGTH_SHORT) {
    configToast()
    when (value) {
        is CharSequence -> Toasty.success(context, value, duration, true).show()
        is Int -> Toasty.success(context, context.resources.getText(value), duration, true).show()
        else -> error(TOAST_ERROR_MESSAGE)
    }
}

/**
 * Info Toast
 *
 * @param context
 * @param value
 * @param duration
 */
fun showInfoToast(context: Context, value: Any, duration: Int = LENGTH_SHORT) {
    configToast()
    when (value) {
        is CharSequence -> Toasty.info(context, value, duration, true).show()
        is Int -> Toasty.info(context, context.resources.getText(value), duration, true).show()
        else -> error(TOAST_ERROR_MESSAGE)
    }
}

/**
 * Warning Toast
 *
 * @param context
 * @param value
 * @param duration
 */
fun showWarningToast(context: Context, value: Any, duration: Int = LENGTH_SHORT) {
    configToast()
    when (value) {
        is CharSequence -> Toasty.warning(context, value, duration, true).show()
        is Int -> Toasty.warning(context, context.resources.getText(value), duration, true).show()
        else -> error(TOAST_ERROR_MESSAGE)
    }
}

/**
 * Normal Toast w/o icon
 *
 * @param context
 * @param value
 * @param duration
 */
fun showNormalToast(context: Context, value: Any, duration: Int = LENGTH_SHORT) {
    configToast()
    when (value) {
        is CharSequence -> Toasty.normal(context, value, duration).show()
        is Int -> Toasty.normal(context, context.resources.getText(value), duration).show()
        else -> error(TOAST_ERROR_MESSAGE)
    }
}

/**
 * Custom Toast
 *
 * @param context
 * @param value
 * @param icon
 * @param tintColor
 * @param duration
 * @param withIcon
 * @param shouldTint
 */
fun showCustomToast(
    context: Context,
    value: Any,
    icon: Int,
    tintColor: Int,
    duration: Int = LENGTH_SHORT,
    withIcon: Boolean,
    shouldTint: Boolean
) {
    configToast()
    when (value) {
        is CharSequence ->
            Toasty.custom(
                context,
                value,
                icon,
                tintColor,
                duration,
                withIcon,
                shouldTint
            ).show()
        is Int ->
            Toasty.custom(
                context,
                context.resources.getText(value),
                icon,
                tintColor,
                duration,
                withIcon,
                shouldTint
            ).show()
        else -> error(TOAST_ERROR_MESSAGE)
    }
}

/**
 * Configure Toast.
 */
private fun configToast() = Toasty.Config.getInstance().allowQueue(false).apply()

/**
Toasty.Config.getInstance()
.tintIcon(boolean tintIcon)                         // optional (apply textColor also to the icon)
.setToastTypeface(@NonNull Typeface typeface)       // optional
.setTextSize(int sizeInSp)                          // optional
.allowQueue(boolean allowQueue)                     // optional (prevents several Toastys from queuing)
.setGravity(int gravity, int xOffset, int yOffset)  // optional (set toast gravity, offsets are optional)
.supportDarkTheme(boolean supportDarkTheme)         // optional (whether to support dark theme or not)
.setRTL(boolean isRTL)                              // optional (icon is on the right)
.apply();                                           // required
 */