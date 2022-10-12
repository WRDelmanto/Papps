package com.wrdelmanto.papps.utils

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView

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

    textview.startAnimation(anim)
}

/**
 * Stop blinking animation.
 *
 * @param textview
 */
fun stopBlinkingAnimation(textview: TextView) = textview.clearAnimation()