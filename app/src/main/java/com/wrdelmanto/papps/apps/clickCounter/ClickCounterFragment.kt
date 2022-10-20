package com.wrdelmanto.papps.apps.clickCounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putSharedPreferences
import com.wrdelmanto.papps.utils.startBlinkingAnimation
import com.wrdelmanto.papps.utils.stopBlinkingAnimation

class ClickCounterFragment : Fragment() {
    private lateinit var counter: TextView
    private lateinit var highScoreOutput: TextView
    private lateinit var additionButton: Button
    private lateinit var clickAnywhere: TextView
    private lateinit var resetButton: Button

    private var highScore = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_click_counter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        counter = view.findViewById(R.id.click_counter_counter)
        highScoreOutput = view.findViewById(R.id.click_counter_high_score_output)
        additionButton = view.findViewById(R.id.click_counter_addition_button)
        clickAnywhere = view.findViewById(R.id.click_counter_click_anywhere)
        resetButton = view.findViewById(R.id.click_counter_reset_button)

        startBlinkingAnimation(clickAnywhere)

        highScore = context?.let { getSharedPreferences(it, SP_CC_HIGH_SCORE, Int) } as Int
        highScoreOutput.text = highScore.toString()

        initiateListeners()
    }

    private fun initiateListeners() {
        additionButton.setOnClickListener { stopBlinkingAnimation(clickAnywhere); addition() }
        resetButton.setOnClickListener { startBlinkingAnimation(clickAnywhere); resetCounter() }
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

    private companion object {
        const val SP_CC_HIGH_SCORE = "SHARED_PREFERENCES_CLICK_COUNTER_HIGH_SCORE"
    }
}