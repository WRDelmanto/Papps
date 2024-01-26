package com.wrdelmanto.papps.utils

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.RESTART
import android.animation.ValueAnimator.REVERSE
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.LinearInterpolator
import androidx.core.view.isVisible
import com.wrdelmanto.papps.HALF_SECOND_IN_MILLIS
import com.wrdelmanto.papps.ONE_SECOND_IN_MILLIS
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Start blinking animation.
 *
 * @param view
 */
fun startBlinkingAnimation(view: View) {
    val animation =
        AlphaAnimation(0.0f, 1.0f).apply {
            duration = ONE_SECOND_IN_MILLIS
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
fun stopBlinkingAnimation(
    view: View,
    shouldStillBeVisible: Boolean = false,
) {
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
    ObjectAnimator.ofFloat(view, "rotation", 0f, 10f, 0f, -10f, 0f).apply {
        duration = HALF_SECOND_IN_MILLIS
        start()
    }

    view.isVisible = true
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
        duration = 2 * ONE_SECOND_IN_MILLIS
        repeatMode = RESTART
        repeatCount = INFINITE
        start()
    }

    view.isVisible = true
}

/**
 * Change color gradually.
 *
 * @param initialColor
 * @param finalColor
 * @param view
 */
fun changeColorGradually(
    initialColor: ColorDrawable,
    finalColor: ColorDrawable,
    view: View,
) {
    val transitionDrawable = TransitionDrawable(arrayOf(initialColor, finalColor))

    transitionDrawable.startTransition(HALF_SECOND_IN_MILLIS.toInt())

    view.apply {
        background = transitionDrawable
        isVisible = true
    }
}

/**
 * Change color gradually and then return to the original color.
 *
 * @param initialColor
 * @param middleColor
 * @param view
 */
fun changeColorGraduallyAndRollback(
    initialColor: ColorDrawable,
    middleColor: ColorDrawable,
    view: View,
) {
    MainScope().launch {
        changeColorGradually(initialColor, middleColor, view)
        delay(HALF_SECOND_IN_MILLIS)
        changeColorGradually(middleColor, initialColor, view)
    }
}
