package com.wrdelmanto.papps.apps.random.randomLetter

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.databinding.FragmentRandomLetterBinding
import com.wrdelmanto.papps.utils.startBlinkingAnimation
import com.wrdelmanto.papps.utils.stopBlinkingAnimation

class RandomLetterFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentRandomLetterBinding

    private val randomLetterViewModel: RandomLetterViewModel by viewModels()

    private lateinit var result: TextView
    private lateinit var clickAnywhereButton: Button

    private lateinit var resultLetter: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRandomLetterBinding.inflate(layoutInflater)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.randomLetterViewModel = randomLetterViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        result = binding.randomLetterResult
        clickAnywhereButton = binding.randomLetterClickAnywhereButton

        resultLetter = binding.randomLetterResult

        initiateListeners()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()

        randomLetterViewModel.resetUi(context)

        result.apply {
            textSize = 16F
            setTextColor(resources.getColor(R.color.defaul_text_color, null))
        }

        startBlinkingAnimation(result)
    }

    override fun onDestroy() {
        disableListeners()

        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initiateListeners() = clickAnywhereButton.setOnClickListener {
        stopBlinkingAnimation(result, true)
        randomLetterViewModel.generateRandomLetter(context)

        result.apply {
            textSize = 128F
            setTextColor(resources.getColor(R.color.color_secondary, null))
        }
    }

    private fun disableListeners() = clickAnywhereButton.setOnClickListener(null)
}