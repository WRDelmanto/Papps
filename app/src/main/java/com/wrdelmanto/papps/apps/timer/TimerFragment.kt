package com.wrdelmanto.papps.apps.timer

import android.content.Context
import android.media.RingtoneManager
import android.media.RingtoneManager.TYPE_ALARM
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wrdelmanto.papps.ONE_SECOND_IN_MILLIS
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.databinding.FragmentTimerBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class TimerFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentTimerBinding

    private val timerViewModel: TimerViewModel by viewModels()

    private lateinit var hourNumberPicker: NumberPicker
    private lateinit var minuteNumberPicker: NumberPicker
    private lateinit var secondNumberPicker: NumberPicker

    private lateinit var plusFive: TextView
    private lateinit var minusFive: TextView
    private lateinit var plusTen: TextView
    private lateinit var minusTen: TextView

    private lateinit var playButton: ImageView
    private lateinit var pauseButton: ImageView
    private lateinit var stopButton: ImageView

    private lateinit var timertime: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(layoutInflater)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.timerViewModel = timerViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        hourNumberPicker = binding.timerNumberPickerHours
        minuteNumberPicker = binding.timerNumberPickerMinutes
        secondNumberPicker = binding.timerNumberPickerSeconds

        plusFive = binding.timerFastPlusFive
        minusFive = binding.timerFastMinusFive
        plusTen = binding.timerFastPlusTen
        minusTen = binding.timerFastMinusTen

        playButton = binding.timerPlayButton
        pauseButton = binding.timerPauseButton
        stopButton = binding.timerStopButton

        timertime = binding.timerTime

        setupNumberPickers()

        initiateListeners()
        initiateObservers()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onDestroy() {
        disableListeners()
        disableObservers()

        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun initiateListeners() {
        hourNumberPicker.setOnValueChangedListener { _, _, newNumber ->
            timerViewModel.updateHoursInput(newNumber.toString())
        }
        minuteNumberPicker.setOnValueChangedListener { _, _, newNumber ->
            timerViewModel.updateMinutesInput(newNumber.toString())
        }
        secondNumberPicker.setOnValueChangedListener { _, _, newNumber ->
            timerViewModel.updateSecondsInput(newNumber.toString())
        }
        plusFive.setOnClickListener { timerViewModel.addFiveMinutes() }
        minusFive.setOnClickListener { timerViewModel.minusFiveMinutes() }
        plusTen.setOnClickListener { timerViewModel.addTenMinutes() }
        minusTen.setOnClickListener { timerViewModel.minusTenMinutes() }
        playButton.setOnClickListener { timerViewModel.startTimer() }
        pauseButton.setOnClickListener { timerViewModel.pauseTimer() }
        stopButton.setOnClickListener { timerViewModel.stopTimer() }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun initiateObservers() {
        timerViewModel.hasStarted.observe(viewLifecycleOwner) { hasStarted ->
            if (hasStarted) {
                stopButton.visibility = VISIBLE
                hourNumberPicker.visibility = GONE
                minuteNumberPicker.visibility = GONE
                secondNumberPicker.visibility = GONE
                timertime.visibility = VISIBLE
                plusFive.visibility = GONE
                minusFive.visibility = GONE
                plusTen.visibility = GONE
                minusTen.visibility = GONE
            } else {
                stopButton.visibility = GONE
                hourNumberPicker.visibility = VISIBLE
                minuteNumberPicker.visibility = VISIBLE
                secondNumberPicker.visibility = VISIBLE
                timertime.visibility = GONE
                plusFive.visibility = VISIBLE
                minusFive.visibility = VISIBLE
                plusTen.visibility = VISIBLE
                minusTen.visibility = VISIBLE
            }
        }
        timerViewModel.isRunning.observe(viewLifecycleOwner) { isRunning ->
            if (isRunning) {
                playButton.visibility = GONE
                pauseButton.visibility = VISIBLE
            } else {
                playButton.visibility = VISIBLE
                pauseButton.visibility = GONE
            }
        }
        timerViewModel.hasFinished.observe(viewLifecycleOwner) { hasFinished ->
            if (hasFinished) {
                MainScope().launch {
                    val beep = RingtoneManager.getRingtone(
                        context, RingtoneManager.getDefaultUri(TYPE_ALARM)
                    )
                    beep.play()

                    delay(ONE_SECOND_IN_MILLIS)

                    beep.stop()
                }
            }
        }
        timerViewModel.hours.observe(viewLifecycleOwner) { hours ->
            hourNumberPicker.value = hours.toInt()
        }
        timerViewModel.minutes.observe(viewLifecycleOwner) { minutes ->
            minuteNumberPicker.value = minutes.toInt()
        }
        timerViewModel.seconds.observe(viewLifecycleOwner) { seconds ->
            secondNumberPicker.value = seconds.toInt()
        }
    }

    private fun disableListeners() {
        hourNumberPicker.setOnValueChangedListener(null)
        minuteNumberPicker.setOnValueChangedListener(null)
        secondNumberPicker.setOnValueChangedListener(null)
        plusFive.setOnClickListener(null)
        plusFive.setOnClickListener(null)
        plusTen.setOnClickListener(null)
        plusTen.setOnClickListener(null)
        playButton.setOnClickListener(null)
        pauseButton.setOnClickListener(null)
        stopButton.setOnClickListener(null)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun disableObservers() {
        timerViewModel.isRunning.observe(viewLifecycleOwner) { }
        timerViewModel.isRunning.observe(viewLifecycleOwner) { }
        timerViewModel.hasFinished.observe(viewLifecycleOwner) { }
        timerViewModel.hours.observe(viewLifecycleOwner) { }
        timerViewModel.minutes.observe(viewLifecycleOwner) { }
        timerViewModel.seconds.observe(viewLifecycleOwner) { }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setupNumberPickers() {
        hourNumberPicker.apply {
            setFormatter {
                String.format("%02d", it)
            }
            textSize = context.resources.getDimension(R.dimen.font_size_large)
            minValue = 0
            maxValue = 23
            value = 0
            wrapSelectorWheel = true
        }
        minuteNumberPicker.apply {
            setFormatter {
                String.format("%02d", it)
            }
            textSize = context.resources.getDimension(R.dimen.font_size_large)
            minValue = 0
            maxValue = 59
            value = 30
            wrapSelectorWheel = true
        }
        secondNumberPicker.apply {
            setFormatter {
                String.format("%02d", it)
            }
            textSize = context.resources.getDimension(R.dimen.font_size_large)
            minValue = 0
            maxValue = 59
            value = 0
            wrapSelectorWheel = true
        }
    }
}