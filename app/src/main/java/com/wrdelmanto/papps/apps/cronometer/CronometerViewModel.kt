package com.wrdelmanto.papps.apps.cronometer

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wrdelmanto.papps.utils.logD
import java.time.Duration

class CronometerViewModel : ViewModel() {
    private val _cronometer = MutableLiveData<String>()
    val cronometer: LiveData<String> = _cronometer

    private val _hasStarted = MutableLiveData(false)
    val hasStarted: LiveData<Boolean> = _hasStarted

    private val _isRunning = MutableLiveData(false)
    val isRunning: LiveData<Boolean> = _isRunning

    private var startTime: Long = 0
    private var elapsedTime: Long = 0

    private var lapList = mutableListOf<String>()

    private val handler = Handler(Looper.getMainLooper())

    private val chronometerRunnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.S)
        override fun run() {
            elapsedTime = SystemClock.elapsedRealtime() - startTime
            updateCronometerTime(elapsedTime)
            handler.post(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun resetUi() {
        elapsedTime = 0
        startTime = 0
        lapList.clear()
        _isRunning.value = false
        _hasStarted.value = false

        logD { "resetUi" }

        updateCronometerTime(startTime)
    }

    fun startCronometer() {
        startTime = SystemClock.elapsedRealtime() - elapsedTime
        handler.post(chronometerRunnable)
        _isRunning.value = true
        _hasStarted.value = true

        logD { "startCronometer" }
    }

    fun pauseCronometer() {
        handler.removeCallbacks(chronometerRunnable)
        _isRunning.value = false

        logD { "pauseCronometer" }
    }

    fun addNewLap() {
        val newLap = _cronometer.value.toString()

        if (newLap !in lapList) {
            lapList.add(newLap)

            logD { "New lap added=$newLap" }
            logD { "Lap list=$lapList" }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun stopCronometer(context: Context) {
        handler.removeCallbacks(chronometerRunnable)

        logD { "stopCronometer" }

        resetUi()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun updateCronometerTime(clock: Long) {
        val duration = Duration.ofMillis(clock)

        val hours = duration.toHoursPart()
        val minutes = duration.toMinutesPart()
        val seconds = duration.toSecondsPart()
        val milliseconds = duration.toMillisPart()

        logD {
            "cronometer=${
                String.format(
                    HOURS_MINUTES_SECONDS_MILLISECONDS_FORMAT, hours, minutes, seconds, milliseconds
                )
            }"
        }

        _cronometer.value = if (hours == 0) String.format(
            MINUTES_SECONDS_MILLISECONDS_FORMAT, minutes, seconds, milliseconds
        )
        else String.format(
            HOURS_MINUTES_SECONDS_MILLISECONDS_FORMAT, hours, minutes, seconds, milliseconds
        )
    }

    private companion object {
        const val MINUTES_SECONDS_MILLISECONDS_FORMAT = "%02d:%02d.%02d"
        const val HOURS_MINUTES_SECONDS_MILLISECONDS_FORMAT = "%d:%02d:%02d.%02d"
    }
}