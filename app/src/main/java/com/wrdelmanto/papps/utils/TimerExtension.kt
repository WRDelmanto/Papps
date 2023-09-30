package com.wrdelmanto.papps.utils

import android.os.CountDownTimer

private var isTimeRunning = false

/**
 * Check if a time is running.
 *
 * @return isTime1StillRunning
 */
fun isTimeRunning(): Boolean = isTimeRunning

/**
 * Setup time.
 *
 * @param milliseconds
 */
fun startTime(milliseconds: Long) {
    isTimeRunning = true
    object : CountDownTimer(milliseconds, 10) {
        override fun onTick(millisUntilFinished: Long) {
            val percentage = ((milliseconds - millisUntilFinished) * 100 / milliseconds).toString()
            val millis = milliseconds - millisUntilFinished
            logD { "$percentage % - $millis/$milliseconds" }
        }
        override fun onFinish() {
            isTimeRunning = false
            logD { "100 % - $milliseconds/$milliseconds" }
        }
    }.start()
}