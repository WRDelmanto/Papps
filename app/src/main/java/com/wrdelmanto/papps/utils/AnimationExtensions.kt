package com.wrdelmanto.papps.utils

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.RESTART
import android.animation.ValueAnimator.REVERSE
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.LinearInterpolator
import androidx.core.view.isVisible

/**
 * Start blinking animation.
 *
 * @param view
 */
fun startBlinkingAnimation(view: View) {
    val animation = AlphaAnimation(0.0f, 1.0f)

    animation.apply {
        duration = 1000
        startOffset = 20
        repeatMode = REVERSE
        repeatCount = INFINITE
    }

    view.apply {
        isVisible = true
        startAnimation(animation)
    }
}

/**
 * Stop blinking animation.
 *
 * @param view
 * @param shouldStillBeVisible
 */
fun stopBlinkingAnimation(view: View, shouldStillBeVisible: Boolean = false) {
    view.apply {
        clearAnimation()
        isVisible = shouldStillBeVisible
    }
}

/**
 * Start tilting animation.
 *
 * @param view
 */
fun startTiltingAnimation(view: View) {
    view.isVisible = true

    ObjectAnimator.ofFloat(view, "rotation", 0f, 10f, 0f, -10f, 0f).apply {
        duration = 1000
        start()
    }
}

/**
 * Start rotating animation.
 *
 * @param view
 */
fun startRotatingAnimation(view: View) {
    ValueAnimator.ofFloat(0f, 360f).apply {
        addUpdateListener { view.rotation = it.animatedValue as Float }
        interpolator = LinearInterpolator()
        duration = 2000
        repeatMode = RESTART
        repeatCount = INFINITE
        start()
    }

    // TODO: view.startAnimation() test
}