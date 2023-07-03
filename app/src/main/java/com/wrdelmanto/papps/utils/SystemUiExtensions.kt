package com.wrdelmanto.papps.utils

import android.content.Context
import android.os.Build
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.Window
import androidx.core.content.ContextCompat
import com.wrdelmanto.papps.R

/**
 * Setup navigation and status bar
 *
 * @param context
 * @param window
 */
fun setupNavigationAndStatusBar(context: Context, window: Window) {
    setupStatusBar(context, window)
    setupNavBar(context, window)
}

/**
 * Setup status bar.
 *
 * @param context
 * @param window
 */
@Suppress("DEPRECATION")
private fun setupStatusBar(context: Context, window: Window) {
    changeStatusBarColor(context, window)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
}

/**
 * Setup navigation bar.
 *
 * @param context
 * @param window
 */
private fun setupNavBar(context: Context, window: Window) = changeNavBarColor(context, window)

/**
 * Change status bar color.
 *
 * @param context
 * @param window
 */
private fun changeStatusBarColor(context: Context, window: Window) {
    window.statusBarColor = ContextCompat.getColor(context, R.color.status_bar_color)
}

/**
 * Change navigation bar color.
 *
 * @param context
 * @param window
 */
private fun changeNavBarColor(context: Context, window: Window) {
    window.navigationBarColor = ContextCompat.getColor(context, R.color.navigation_bar_color)
}