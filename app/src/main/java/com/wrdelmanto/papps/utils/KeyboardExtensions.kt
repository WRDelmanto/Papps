package com.wrdelmanto.papps.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

/**
 * Hide soft keyboard after a dialog.
 */
private fun Context.hideKeyboard(view: View) {
    if (!view.isKeyboardOpen()) return

    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

    logD { "hideKeyboard" }
}

fun Fragment.hideKeyboard() = view?.let { activity?.hideKeyboard(it) }

fun Activity.hideKeyboard() = hideKeyboard(currentFocus ?: View(this))

/**
 * Open soft keyboard after a dialog.
 */
@Suppress("DEPRECATION")
private fun Context.openKeyboard(view: View) {
    if (view.isKeyboardOpen()) return

    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)

    logD { "openKeyboard" }
}

fun Fragment.openKeyboard() = view?.let { activity?.openKeyboard(it) }

fun Activity.openKeyboard() = openKeyboard(currentFocus ?: View(this))

/**
 * Check if soft keyboard is open.
 */
fun View.isKeyboardOpen(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        WindowInsetsCompat.toWindowInsetsCompat(rootWindowInsets).isVisible(WindowInsetsCompat.Type.ime())
    else true
}