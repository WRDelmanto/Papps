package com.wrdelmanto.papps.apps.speedTest

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wrdelmanto.papps.databinding.FragmentSpeedTestBinding

class SpeedTestFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentSpeedTestBinding

    private val speedTestViewModel: SpeedTestViewModel by viewModels()

    private lateinit var resetButton: AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpeedTestBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.speedTestViewModel = speedTestViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        resetButton = binding.speedTestResetButton

        initiateListeners()
    }

    override fun onResume() {
        super.onResume()

        speedTestViewModel.resetUi(context)
    }

    override fun onDestroy() {
        disableListeners()

        super.onDestroy()
    }

    private fun initiateListeners() {
        resetButton.setOnClickListener { speedTestViewModel.resetUi(context) }
    }

    private fun disableListeners() {
        resetButton.setOnClickListener(null)
    }
}