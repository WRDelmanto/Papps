package com.wrdelmanto.papps.apps.dice

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wrdelmanto.papps.databinding.FragmentDicesBinding
import com.wrdelmanto.papps.utils.startBlinkingAnimation
import com.wrdelmanto.papps.utils.startTiltingAnimation
import com.wrdelmanto.papps.utils.stopBlinkingAnimation

class DicesFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentDicesBinding

    private val dicesViewModel: DicesViewModel by viewModels()

    private lateinit var clickAnywhereMessage: TextView
    private lateinit var clickAnywhereButton: Button

    private lateinit var resultImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDicesBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dicesViewModel = dicesViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        clickAnywhereMessage = binding.dicesClickAnywhereMessage
        clickAnywhereButton = binding.dicesClickAnywhereButton

        resultImage = binding.dicesResult

        initiateListeners()
    }

    override fun onResume() {
        super.onResume()

        dicesViewModel.resetUi(context)
        resultImage.isVisible = false

        startBlinkingAnimation(clickAnywhereMessage)
    }

    override fun onDestroy() {
        disableListeners()

        super.onDestroy()
    }

    private fun initiateListeners() = clickAnywhereButton.setOnClickListener {
        stopBlinkingAnimation(clickAnywhereMessage)
        startTiltingAnimation(resultImage)
        dicesViewModel.rollDice(context)
    }

    private fun disableListeners() = clickAnywhereButton.setOnClickListener(null)
}