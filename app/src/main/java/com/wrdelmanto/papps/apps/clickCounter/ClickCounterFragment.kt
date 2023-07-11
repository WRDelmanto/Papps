package com.wrdelmanto.papps.apps.clickCounter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wrdelmanto.papps.MainActivity
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.databinding.FragmentClickCounterBinding
import com.wrdelmanto.papps.utils.startBlinkingAnimation
import com.wrdelmanto.papps.utils.stopBlinkingAnimation

class ClickCounterFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentClickCounterBinding

    private val clickCounterViewModel: ClickCounterViewModel by viewModels()

    private lateinit var clickAnywhereButton: Button
    private lateinit var clickAnywhereMessage: TextView
    private lateinit var resetButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentClickCounterBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity?)?.updateAppBarTitle(getString(R.string.app_name_click_counter))

        binding.clickCounterViewModel = clickCounterViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        clickAnywhereButton = binding.clickCounterClickAnywhereButton
        clickAnywhereMessage = binding.clickCounterClickAnywhereMessage
        resetButton = binding.clickCounterResetButton

        initiateListeners()
    }

    override fun onResume() {
        super.onResume()

        clickCounterViewModel.resetUi(context)

//        startBlinkingAnimation(clickAnywhereMessage)
    }

    override fun onDestroy() {
        disableListeners()

        super.onDestroy()
    }

    private fun initiateListeners() {
        clickAnywhereButton.setOnClickListener {
            stopBlinkingAnimation(clickAnywhereMessage)
            clickCounterViewModel.increaseCounter(context)
        }

        resetButton.setOnClickListener {
            startBlinkingAnimation(clickAnywhereMessage)
            clickCounterViewModel.resetUi(context)
        }
    }

    private fun disableListeners() {
        clickAnywhereButton.setOnClickListener(null)
        resetButton.setOnClickListener(null)
    }
}