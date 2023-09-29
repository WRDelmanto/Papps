package com.wrdelmanto.papps.apps.cronometer

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wrdelmanto.papps.databinding.FragmentCronometerBinding

class CronometerFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentCronometerBinding

    private val cronometerViewModel: CronometerViewModel by viewModels()

    private lateinit var playButton: ImageView
    private lateinit var pauseButton: ImageView
    private lateinit var lapButton: ImageView
    private lateinit var stopButton: ImageView

    private lateinit var lapListRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCronometerBinding.inflate(layoutInflater)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cronometerViewModel = cronometerViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        playButton = binding.cronometerPlayButton
        pauseButton = binding.cronometerPauseButton
        lapButton = binding.cronometerLapButton
        stopButton = binding.cronometerStopButton

        lapListRecyclerView = binding.cronometerLaps

        lapListRecyclerView.layoutManager = LinearLayoutManager(context)
        lapListRecyclerView.adapter =
            CronometerAdapter(cronometerViewModel.lapList.value?.toTypedArray()!!)

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
        playButton.setOnClickListener { cronometerViewModel.startCronometer() }
        pauseButton.setOnClickListener { cronometerViewModel.pauseCronometer() }
        lapButton.setOnClickListener { cronometerViewModel.addNewLap() }
        stopButton.setOnClickListener { cronometerViewModel.stopCronometer() }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun initiateObservers() {
        cronometerViewModel.hasStarted.observe(viewLifecycleOwner) { hasStarted ->
            if (hasStarted) {
                lapButton.visibility = VISIBLE
                stopButton.visibility = VISIBLE
            } else {
                lapButton.visibility = GONE
                stopButton.visibility = GONE
            }
        }
        cronometerViewModel.isRunning.observe(viewLifecycleOwner) { isRunning ->
            if (isRunning) {
                playButton.visibility = GONE
                pauseButton.visibility = VISIBLE
            } else {
                playButton.visibility = VISIBLE
                pauseButton.visibility = GONE
            }
        }
        cronometerViewModel.lapList.observe(viewLifecycleOwner) { lapList ->
            lapListRecyclerView.adapter = CronometerAdapter(lapList.toTypedArray())
        }
    }

    private fun disableListeners() {
        playButton.setOnClickListener(null)
        pauseButton.setOnClickListener(null)
        lapButton.setOnClickListener(null)
        stopButton.setOnClickListener(null)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun disableObservers() {
        cronometerViewModel.isRunning.observe(viewLifecycleOwner) { }
    }
}