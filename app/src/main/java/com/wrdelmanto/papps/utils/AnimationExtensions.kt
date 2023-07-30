package com.wrdelmanto.papps.utils

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.core.view.isVisible

/**
 * Start blinking animation.
 *
 * @param view
 */
fun startBlinkingAnimation(view: View) {
    val animation = AlphaAnimation(0.0f, 1.0f)

    animation.duration = 1000
    animation.startOffset = 20
    animation.repeatMode = Animation.REVERSE
    animation.repeatCount = Animation.INFINITE

    view.isVisible = true
    view.startAnimation(animation)
}

/**
 * Stop blinking animation.
 *
 * @param view
 * @param shouldStillBeVisible
 */
fun stopBlinkingAnimation(view: View, shouldStillBeVisible: Boolean = false) {
    view.clearAnimation()
    view.isVisible = shouldStillBeVisible
}

/**
 * Start tilting animation.
 *
 * @param view
 */
fun startTiltingAnimation(view: View) {
    val degrees = 10f

    val animation = ObjectAnimator.ofFloat(view, "rotation", 0f, degrees, 0f, -degrees, 0f)
    animation.duration = 1000

    view.isVisible = true
    animation.start()
}