package com.wrdelmanto.papps.utils

import android.os.CountDownTimer

private var isTimerRunning = false

/**
 * Check if a timer is running.
 *
 * @return isTimerStillRunning
 */
fun isTimerRunning(): Boolean = isTimerRunning

/**
 * Setup timer.
 *
 * @param milliseconds
 */
fun startTimer(milliseconds: Long) {
    isTimerRunning = true
    object : CountDownTimer(milliseconds, 10) {
        override fun onTick(millisUntilFinished: Long) {
            val percentage = ((milliseconds - millisUntilFinished) * 100 / milliseconds).toString()
            val millis = milliseconds - millisUntilFinished
            logD { "$percentage % - $millis/$milliseconds" }
        }
        override fun onFinish() {
            isTimerRunning = false
            logD { "100 % - $milliseconds/$milliseconds" }
        }
    }.start()
}