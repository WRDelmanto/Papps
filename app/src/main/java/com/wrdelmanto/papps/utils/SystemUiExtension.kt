package com.wrdelmanto.papps.utils

import android.content.Context
import android.os.Build
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.Window
import androidx.core.content.ContextCompat
import com.wrdelmanto.papps.R

/**
 * Setup navigation and status bar.
 *
 * @param context
 * @param window
 */
fun setupNavigationAndStatusBar(context: Context, window: Window) {
    setupStatusBar(context, window)
    setupNavBar(context, window)
}

/**
 * Setup navigation and status bar for full screen.
 *
 * @param context
 * @param window
 */
fun setupNavigationAndStatusBarFullScreen(context: Context, window: Window) {
    setupStatusBarFullScreen(context, window)
    setupNavBarFullScreen(window)
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

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) window.decorView.systemUiVisibility =
        SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
}

/**
 * Setup status bar for full screen.
 *
 * @param context
 * @param window
 */
@Suppress("DEPRECATION")
private fun setupStatusBarFullScreen(context: Context, window: Window) {
    changeStatusBarColorFullScreen(context, window)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) window.decorView.systemUiVisibility =
        SYSTEM_UI_FLAG_FULLSCREEN
}

/**
 * Setup navigation bar.
 *
 * @param context
 * @param window
 */
private fun setupNavBar(context: Context, window: Window) = changeNavBarColor(context, window)

/**
 * Setup navigation bar for full screen.
 *
 * @param window
 */
@Suppress("DEPRECATION")
private fun setupNavBarFullScreen(window: Window) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) window.decorView.systemUiVisibility =
        (SYSTEM_UI_FLAG_HIDE_NAVIGATION or SYSTEM_UI_FLAG_IMMERSIVE)

}

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
 * Change status bar color for full screen.
 *
 * @param context
 * @param window
 */
private fun changeStatusBarColorFullScreen(context: Context, window: Window) {
    window.statusBarColor = ContextCompat.getColor(context, R.color.black)
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