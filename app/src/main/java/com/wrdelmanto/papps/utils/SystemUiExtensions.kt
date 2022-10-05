package com.wrdelmanto.papps.utils

import android.content.Context
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import com.wrdelmanto.papps.R

@Suppress("DEPRECATION")
fun hideSystemUiii(window: Window) {
    window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
}

@Suppress("DEPRECATION")
fun showSystemUIii(window: Window) {
    window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
}

@Suppress("DEPRECATION")
fun hideSystemUi(window: Window) {
    // Hide navigation bar  = SYSTEM_UI_FLAG_HIDE_NAVIGATION and SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    // Hide status bar      = SYSTEM_UI_FLAG_FULLSCREEN
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
}

fun switchNavBarColor(context: Context, window: Window) {
    window.navigationBarColor = ContextCompat.getColor(context, R.color.navigation_bar_color)
}

fun switchStatusBarColor(context: Context, window: Window) {
    window.statusBarColor = ContextCompat.getColor(context, R.color.status_bar_color)
}