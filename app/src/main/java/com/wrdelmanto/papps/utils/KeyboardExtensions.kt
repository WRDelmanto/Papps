package com.wrdelmanto.papps.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import kotlin.math.roundToInt

/**
 * Hide soft keyboard after a dialog.
 */
private fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

    logD { "hideKeyboard" }
}

/**
 * Hide soft keyboard after a dialog.
 */
fun Fragment.hideKeyboard() = view?.let { activity?.hideKeyboard(it) }

/**
 * Hide soft keyboard after a dialog.
 */
fun Activity.hideKeyboard() = hideKeyboard(currentFocus ?: View(this))

/**
 * Open soft keyboard after a dialog.
 */
private fun Context.openKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

    logD { "openKeyboard" }
}

/**
 * Open soft keyboard after a dialog.
 */
fun Fragment.openKeyboard() = view?.let { activity?.openKeyboard(it) }

/**
 * Open soft keyboard after a dialog.
 */
fun Activity.openKeyboard() = openKeyboard(currentFocus ?: View(this))

/**
 * Get root view.
 */
fun Activity.getRootView(): View = findViewById(android.R.id.content)

/**
 * Convert Dp To Px.
 */
fun Context.convertDpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        this.resources.displayMetrics
    )
}

/**
 * Check if keyboard is opened.
 */
fun Fragment.isKeyboardOpen(): Boolean? = view?.let { activity?.isKeyboardOpen() }

/**
 * Check if keyboard is opened.
 */
fun Activity.isKeyboardOpen(): Boolean {
    val visibleBounds = Rect()
    this.getRootView().getWindowVisibleDisplayFrame(visibleBounds)
    val heightDiff = getRootView().height - visibleBounds.height()
    val marginOfError = this.convertDpToPx(50F).roundToInt()
    return heightDiff > marginOfError
}