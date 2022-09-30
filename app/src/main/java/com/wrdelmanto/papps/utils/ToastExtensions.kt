package com.wrdelmanto.papps.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast.LENGTH_SHORT
import es.dmoral.toasty.Toasty

/**
 * Different types of Toasts
 * More info: https://github.com/GrenderG/Toasty
 */

/**
 * Error Toast
 */
fun showErrorToast(context: Context, text: CharSequence, duration: Int = LENGTH_SHORT) {
    configToast()
    Toasty.error(context, text, duration, true).show()
}

fun showErrorToast(context: Context, resId: Int, duration: Int = LENGTH_SHORT) =
    showErrorToast(context, context.resources.getText(resId), duration)

/**
 * Success Toast
 */
fun showSuccessToast(context: Context, text: CharSequence, duration: Int = LENGTH_SHORT) {
    configToast()
    Toasty.success(context, text, duration, true).show()
}

fun showSuccessToast(context: Context, resId: Int, duration: Int = LENGTH_SHORT) =
    showSuccessToast(context, context.resources.getText(resId), duration)

/**
 * Info Toast
 */
fun showInfoToast(context: Context, text: CharSequence, duration: Int = LENGTH_SHORT) {
    configToast()
    Toasty.info(context, text, duration, true).show()
}

fun showInfoToast(context: Context, resId: Int, duration: Int = LENGTH_SHORT) =
    showInfoToast(context, context.resources.getText(resId), duration)

/**
 * Warning Toast
 */
fun showWarningToast(context: Context, text: CharSequence, duration: Int = LENGTH_SHORT) {
    configToast()
    Toasty.warning(context, text, duration, true).show()
}

fun showWarningToast(context: Context, resId: Int, duration: Int = LENGTH_SHORT) =
    showWarningToast(context, context.resources.getText(resId), duration)

/**
 * Normal Toast w/o icon
 */
fun showNormalToast(context: Context, text: CharSequence, duration: Int = LENGTH_SHORT, icon: Drawable? = null) {
    configToast()
    Toasty.normal(context, text, duration).show()
}

fun showNormalToast(context: Context, resId: Int, duration: Int = LENGTH_SHORT) =
    showNormalToast(context, context.resources.getText(resId), duration)

/**
 * Custom Toast
 */
fun showCustomToast(
    context: Context,
    text: CharSequence,
    icon: Int,
    tintColor: Int,
    duration: Int = LENGTH_SHORT,
    withIcon: Boolean,
    shouldTint: Boolean
) {
    configToast()
    Toasty.custom(
        context,
        text,
        icon,
        tintColor,
        duration,
        withIcon,
        shouldTint
    ).show()
}

fun showCustomToast(
    context: Context,
    resId: Int,
    icon: Int,
    tintColor: Int,
    duration: Int = LENGTH_SHORT,
    withIcon: Boolean,
    shouldTint: Boolean
) =
    showCustomToast(
        context,
        context.resources.getText(resId),
        icon,
        tintColor,
        duration,
        withIcon,
        shouldTint
    )

private fun configToast() {
    Toasty.Config.getInstance()
        .allowQueue(false)
        .apply()

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
}