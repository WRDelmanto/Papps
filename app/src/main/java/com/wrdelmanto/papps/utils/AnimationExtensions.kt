package com.wrdelmanto.papps.utils

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.core.view.isVisible

/**
 * Start blinking animation.
 *
 * @param textview
 */
fun startBlinkingAnimation(textview: TextView) {
    val anim: Animation = AlphaAnimation(0.0f, 1.0f)

    anim.duration = 1000
    anim.startOffset = 20
    anim.repeatMode = Animation.REVERSE
    anim.repeatCount = Animation.INFINITE

    textview.isVisible = true
    textview.startAnimation(anim)
}

/**
 * Stop blinking animation.
 *
 * @param textview
 */
fun stopBlinkingAnimation(textview: TextView) {
    textview.clearAnimation()
    textview.isVisible = false
}