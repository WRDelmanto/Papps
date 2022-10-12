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
 *
 * @param context
 * @param text
 * @param duration
 */
fun showErrorToast(context: Context, text: CharSequence, duration: Int = LENGTH_SHORT) {
    configToast()
    Toasty.error(context, text, duration, true).show()
}

/**
 * Error Toast
 *
 * @param context
 * @param resId
 * @param duration
 */
fun showErrorToast(context: Context, resId: Int, duration: Int = LENGTH_SHORT) =
    showErrorToast(context, context.resources.getText(resId), duration)

/**
 * Success Toast
 *
 * @param context
 * @param text
 * @param duration
 */
fun showSuccessToast(context: Context, text: CharSequence, duration: Int = LENGTH_SHORT) {
    configToast()
    Toasty.success(context, text, duration, true).show()
}

/**
 * Success Toast
 *
 * @param context
 * @param resId
 * @param duration
 */
fun showSuccessToast(context: Context, resId: Int, duration: Int = LENGTH_SHORT) =
    showSuccessToast(context, context.resources.getText(resId), duration)

/**
 * Info Toast
 *
 * @param context
 * @param text
 * @param duration
 */
fun showInfoToast(context: Context, text: CharSequence, duration: Int = LENGTH_SHORT) {
    configToast()
    Toasty.info(context, text, duration, true).show()
}

/**
 * Info Toast
 *
 * @param context
 * @param resId
 * @param duration
 */
fun showInfoToast(context: Context, resId: Int, duration: Int = LENGTH_SHORT) =
    showInfoToast(context, context.resources.getText(resId), duration)

/**
 * Warning Toast
 *
 * @param context
 * @param text
 * @param duration
 */
fun showWarningToast(context: Context, text: CharSequence, duration: Int = LENGTH_SHORT) {
    configToast()
    Toasty.warning(context, text, duration, true).show()
}

/**
 * Warning Toast
 *
 * @param context
 * @param resId
 * @param duration
 */
fun showWarningToast(context: Context, resId: Int, duration: Int = LENGTH_SHORT) =
    showWarningToast(context, context.resources.getText(resId), duration)

/**
 * Normal Toast w/o icon
 *
 * @param context
 * @param text
 * @param duration
 */
fun showNormalToast(context: Context, text: CharSequence, duration: Int = LENGTH_SHORT, icon: Drawable? = null) {
    configToast()
    Toasty.normal(context, text, duration).show()
}

/**
 * Normal Toast w/o icon
 *
 * @param context
 * @param resId
 * @param duration
 */
fun showNormalToast(context: Context, resId: Int, duration: Int = LENGTH_SHORT) =
    showNormalToast(context, context.resources.getText(resId), duration)

/**
 * Custom Toast
 *
 * @param context
 * @param text
 * @param icon
 * @param tintColor
 * @param duration
 * @param withIcon
 * @param shouldTint
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

/**
 * Custom Toast
 *
 * @param context
 * @param resId
 * @param icon
 * @param tintColor
 * @param duration
 * @param withIcon
 * @param shouldTint
 */
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

/**
 * Configure Toast.
 */
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