package com.wrdelmanto.papps.apps.clickCounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.MainActivity
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.databinding.FragmentClickCounterBinding
import com.wrdelmanto.papps.utils.SP_CC_HIGH_SCORE
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putSharedPreferences
import com.wrdelmanto.papps.utils.startBlinkingAnimation
import com.wrdelmanto.papps.utils.stopBlinkingAnimation

class ClickCounterFragment : Fragment() {
    private lateinit var binding: FragmentClickCounterBinding

    private lateinit var counter: TextView
    private lateinit var highScoreOutput: TextView
    private lateinit var additionButton: Button
    private lateinit var clickAnywhere: TextView
    private lateinit var resetButton: Button

    private var highScore = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentClickCounterBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        counter = binding.clickCounterCounter
        highScoreOutput = binding.clickCounterHighScoreOutput
        additionButton = binding.clickCounterAdditionButton
        clickAnywhere = binding.clickCounterClickAnywhere
        resetButton = binding.clickCounterResetButton

        startBlinkingAnimation(clickAnywhere)
    }

    override fun onResume() {
        super.onResume()

        resetUi()
        initiateListeners()
    }

    override fun onPause() {
        disableListeners()

        super.onPause()
    }

    private fun initiateListeners() {
        additionButton.setOnClickListener { stopBlinkingAnimation(clickAnywhere); addition() }
        resetButton.setOnClickListener { startBlinkingAnimation(clickAnywhere); resetCounter() }
    }

    private fun disableListeners() {
        additionButton.setOnClickListener(null)
        resetButton.setOnClickListener(null)
    }

    private fun resetUi() {
        (activity as MainActivity?)?.updateAppBarTitle(getString(R.string.app_name_click_counter))
        counter.text = getString(R.string.zero)

        highScore = context?.let {
            val hs = getSharedPreferences(it, SP_CC_HIGH_SCORE, Int)
            hs ?: 0
        } as Int
        highScoreOutput.text = highScore.toString()

        clickAnywhere.isVisible = true
        startBlinkingAnimation(clickAnywhere)
    }

    private fun addition() {
        clickAnywhere.isVisible = false

        val clicks = 1 + counter.text.toString().toInt()
        counter.text = clicks.toString()

        if (clicks > highScore) {
            context?.let { putSharedPreferences(it, SP_CC_HIGH_SCORE, clicks) }
            highScoreOutput.text = clicks.toString()
        }

        logD { "clicks=$clicks" }
    }

    private fun resetCounter() {
        clickAnywhere.isVisible = true
        counter.text = getString(R.string.zero)
        highScore = context?.let { getSharedPreferences(it, SP_CC_HIGH_SCORE, Int) } as Int

        logD { "resetCounter" }
    }
}