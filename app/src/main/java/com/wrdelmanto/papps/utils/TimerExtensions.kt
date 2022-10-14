package com.wrdelmanto.papps.utils

import android.os.CountDownTimer

/**
 * Setup timer.
 *
 * @param milliseconds
 */
fun startTimer(milliseconds: Long) {
    object : CountDownTimer(milliseconds, 10) {
        override fun onTick(millisUntilFinished: Long) {
            val percentage = ((milliseconds - millisUntilFinished) * 100 / milliseconds).toString()
            val millis = milliseconds - millisUntilFinished
            logD { "$percentage % - $millis/$milliseconds" }
        }
        override fun onFinish() {logD { "100 % - $milliseconds/$milliseconds" }}
    }.start()
}