package com.wrdelmanto.papps.apps.timer

import android.os.Build
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wrdelmanto.papps.ONE_HOUR_IN_MILLIS
import com.wrdelmanto.papps.ONE_MINUTE_IN_MILLIS
import com.wrdelmanto.papps.ONE_SECOND_IN_MILLIS
import com.wrdelmanto.papps.utils.logD
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.S)
class TimerViewModel : ViewModel() {
    private val _timer = MutableLiveData<String>()
    val timer: LiveData<String> = _timer

    private val _timerProgress = MutableLiveData<Int>()
    val timerProgress: LiveData<Int> = _timerProgress

    private val _hours = MutableLiveData("00")
    val hours: LiveData<String> = _hours

    private val _minutes = MutableLiveData("30")
    val minutes: LiveData<String> = _minutes

    private val _seconds = MutableLiveData("00")
    val seconds: LiveData<String> = _seconds

    private val _hasStarted = MutableLiveData(false)
    val hasStarted: LiveData<Boolean> = _hasStarted

    private val _isRunning = MutableLiveData(false)
    val isRunning: LiveData<Boolean> = _isRunning

    private val _hasFinished = MutableLiveData(false)
    val hasFinished: LiveData<Boolean> = _hasFinished

    private var countDownTimer: Long = 0
    private var initialTimer: Long = 1800000
    private var lastTimer: String = ""

    private lateinit var countDownTimerObject: CountDownTimer

    init {
        resetUi()
    }

    private fun countDownTimer(timerRemaining: Long, isStarting: Boolean = false) {
        if (isStarting) {
            countDownTimerObject = object : CountDownTimer(timerRemaining, 1) {
                override fun onTick(millisUntilFinished: Long) {
                    countDownTimer = millisUntilFinished
                    logD { countDownTimer.toString() }
                    _timerProgress.value = (100 * millisUntilFinished / initialTimer).toInt()
                    updateTimer()
                }

                override fun onFinish() {
                    _isRunning.value = false
                    _hasFinished.value = true

                    logD { "onFinish - countDownTimer=${_timer.value}" }
                    resetUi()
                }
            }.start()
        } else try {
            countDownTimerObject.cancel()
            updateTimer()
        } catch (exception: Exception) {
            logD { exception.toString() }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun updateTimer() {
        val duration = Duration.ofMillis(countDownTimer)

        val tempHours = duration.toHoursPart()
        val tempMinutes = duration.toMinutesPart()
        var tempSeconds = duration.toSecondsPart()

        _hours.value = tempHours.toString()
        _minutes.value = tempMinutes.toString()
        _seconds.value = tempSeconds.toString()

        if (_hasStarted.value == true) tempSeconds += 1

        _timer.value =
            String.format(HOURS_MINUTES_SECONDS_FORMAT, tempHours, tempMinutes, tempSeconds)
        updateCountDownTimer()

        if (_timer.value != lastTimer) {
            lastTimer = _timer.value.toString()

            logD {
                "timer=${
                    String.format(
                        HOURS_MINUTES_SECONDS_FORMAT, tempHours, tempMinutes, tempSeconds
                    )
                }"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun resetUi() {
        _isRunning.value = false
        _hasStarted.value = false
        _hasFinished.value = false

        logD { "resetUi" }

        countDownTimer = initialTimer
        countDownTimer(countDownTimer, isStarting = false)
    }

    fun startTimer() {
        if (countDownTimer == 0L) {
            logD { countDownTimer.toString() }
            return
        }

        lastTimer = ""
        _isRunning.value = true

        // TODO: pause resume time between
        // TODO: 0 time start

        if (_hasStarted.value == false) {
            logD { "startTimer" }

            _hasStarted.value = true
            updateCountDownTimer()
            countDownTimer(countDownTimer, isStarting = true)
            initialTimer = countDownTimer
        } else {
            logD { "resumeTimer" }

            countDownTimer(countDownTimer, isStarting = true)
        }
    }

    fun pauseTimer() {
        logD { "pauseTimer" }

        _isRunning.value = false
        countDownTimer(countDownTimer, isStarting = false)
    }

    fun stopTimer() {
        logD { "stopTimer" }

        resetUi()
    }

    fun updateHoursInput(newHoursInput: String) {
        _hours.value = newHoursInput

        updateCountDownTimer()
        updateTimer()
    }

    fun updateMinutesInput(newMinutesInput: String) {
        _minutes.value = newMinutesInput

        updateCountDownTimer()
        updateTimer()
    }

    fun updateSecondsInput(newSecondsInput: String) {
        _seconds.value = newSecondsInput

        updateCountDownTimer()
        updateTimer()
    }

    fun addFiveMinutes() {
        if (_minutes.value?.toInt()!! >= FIFTY_FIVE_MINUTES) {
            _minutes.value = (_minutes.value!!.toInt().minus(FIFTY_FIVE_MINUTES)).toString()

            if (_hours.value?.toInt()!! == TWENTY_THREE_HOURS) _hours.value =
                (hours.value!!.toInt().minus(TWENTY_THREE_HOURS)).toString()
            else _hours.value = (hours.value!!.toInt().plus(ONE_HOUR)).toString()
        } else _minutes.value = (_minutes.value?.toInt()?.plus(FIVE_MINUTES)).toString()

        updateCountDownTimer()
        updateTimer()
    }

    fun minusFiveMinutes() {
        if (_minutes.value?.toInt()!! < FIVE_MINUTES) {
            _minutes.value = (_minutes.value!!.toInt().plus(FIFTY_FIVE_MINUTES)).toString()

            if (_hours.value?.toInt()!! < ONE_HOUR) _hours.value =
                (hours.value!!.toInt().plus(TWENTY_THREE_HOURS)).toString()
            else _hours.value = (hours.value!!.toInt().minus(ONE_HOUR)).toString()
        } else _minutes.value = (_minutes.value?.toInt()?.minus(FIVE_MINUTES)).toString()

        updateCountDownTimer()
        updateTimer()
    }

    fun addTenMinutes() {
        if (_minutes.value?.toInt()!! >= FIFTY_MINUTES) {
            _minutes.value = (_minutes.value!!.toInt().minus(FIFTY_MINUTES)).toString()

            if (_hours.value?.toInt()!! == TWENTY_THREE_HOURS) _hours.value =
                (hours.value!!.toInt().minus(TWENTY_THREE_HOURS)).toString()
            else _hours.value = (hours.value!!.toInt().plus(ONE_HOUR)).toString()
        } else _minutes.value = (_minutes.value?.toInt()?.plus(TEN_MINUTES)).toString()

        updateCountDownTimer()
        updateTimer()
    }

    fun minusTenMinutes() {
        if (_minutes.value?.toInt()!! < TEN_MINUTES) {
            _minutes.value = (_minutes.value!!.toInt().plus(FIFTY_MINUTES)).toString()

            if (_hours.value?.toInt()!! < ONE_HOUR) _hours.value =
                (hours.value!!.toInt().plus(TWENTY_THREE_HOURS)).toString()
            else _hours.value = (hours.value!!.toInt().minus(ONE_HOUR)).toString()
        } else _minutes.value = (_minutes.value?.toInt()?.minus(TEN_MINUTES)).toString()

        updateCountDownTimer()
        updateTimer()
    }

    private fun updateCountDownTimer() {
        val tempHoursInMillis = _hours.value?.toLong()?.times(ONE_HOUR_IN_MILLIS) ?: 0L
        val tempMinutesInMillis = _minutes.value?.toLong()?.times(ONE_MINUTE_IN_MILLIS) ?: 0L
        val tempSecondsInMillis = _seconds.value?.toLong()?.times(ONE_SECOND_IN_MILLIS) ?: 0L

        countDownTimer = tempHoursInMillis + tempMinutesInMillis + tempSecondsInMillis
    }

    private companion object {
        const val HOURS_MINUTES_SECONDS_FORMAT = "%02d:%02d:%02d"
        const val TWENTY_THREE_HOURS = 23
        const val ONE_HOUR = 1
        const val FIFTY_FIVE_MINUTES = 55
        const val FIFTY_MINUTES = 50
        const val TEN_MINUTES = 10
        const val FIVE_MINUTES = 5
    }
}