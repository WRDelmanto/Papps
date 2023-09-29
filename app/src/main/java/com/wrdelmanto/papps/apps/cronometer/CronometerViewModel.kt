package com.wrdelmanto.papps.apps.cronometer

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

@RequiresApi(Build.VERSION_CODES.S)
class CronometerViewModel : ViewModel() {
    private val _cronometer = MutableLiveData<String>()
    val cronometer: LiveData<String> = _cronometer

    private val _hasStarted = MutableLiveData(false)
    val hasStarted: LiveData<Boolean> = _hasStarted

    private val _isRunning = MutableLiveData(false)
    val isRunning: LiveData<Boolean> = _isRunning

    private val _lapList = MutableLiveData(listOf<String>().toMutableList())
    val lapList: LiveData<MutableList<String>> = _lapList

    private var startTime: Long = 0
    private var elapsedTime: Long = 0

    private val handler = Handler(Looper.getMainLooper())

    private val chronometerRunnable: Runnable by lazy {
        Runnable {
            elapsedTime = SystemClock.elapsedRealtime() - startTime
            updateCronometerTime(elapsedTime)
            handler.post(chronometerRunnable)
        }
    }

    init {
        resetUi()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun resetUi() {
        startTime = 0
        elapsedTime = 0
        _lapList.value?.clear()
        // So that the observer can see that the list has changed
        this._lapList.value = _lapList.value
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

        if (!_lapList.value?.contains(newLap)!!) {
            _lapList.value?.add(newLap)
            _lapList.value!!.sortDescending()
            // So that the observer can see that the list has changed
            this._lapList.value = _lapList.value

            logD { "New lap added=$newLap" }
            logD { "Lap list=${_lapList.value}" }
        }
    }

    fun stopCronometer() {
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
        val milliseconds = duration.toMillisPart() / 10

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
        else String.format(HOURS_MINUTES_SECONDS_FORMAT, hours, minutes, seconds)
    }

    private companion object {
        const val HOURS_MINUTES_SECONDS_FORMAT = "%d:%02d:%02d"
        const val HOURS_MINUTES_SECONDS_MILLISECONDS_FORMAT = "%d:%02d:%02d.%02d"
        const val MINUTES_SECONDS_MILLISECONDS_FORMAT = "%02d:%02d.%02d"
    }
}