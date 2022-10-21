package com.wrdelmanto.papps.apps.random.randomLetter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.checkKeySharedPreferences
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putSharedPreferences
import com.wrdelmanto.papps.utils.randomString
import com.wrdelmanto.papps.utils.startBlinkingAnimation
import com.wrdelmanto.papps.utils.stopBlinkingAnimation

class RandomLetterFragment : Fragment() {
    private lateinit var result: TextView
    private lateinit var randomizerButton: Button

    private var letterHistory = "*****"
    private lateinit var firstHistory: TextView
    private lateinit var secondHistory: TextView
    private lateinit var thirdHistory: TextView
    private lateinit var fourthHistory: TextView
    private lateinit var fifthHistory: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_random_letter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        result = view.findViewById(R.id.random_letter_result)
        randomizerButton = view.findViewById(R.id.random_letter_click_here)

        firstHistory = view.findViewById(R.id.random_letter_history_first)
        secondHistory = view.findViewById(R.id.random_letter_history_second)
        thirdHistory = view.findViewById(R.id.random_letter_history_third)
        fourthHistory = view.findViewById(R.id.random_letter_history_fourth)
        fifthHistory = view.findViewById(R.id.random_letter_history_fifth)

        if (context?.let { checkKeySharedPreferences(it, SP_RL_LETTER_HISTORY) } == true) {
            letterHistory = context?.let { getSharedPreferences(it, SP_RL_LETTER_HISTORY, String) } as String
            updateLetterHistory()
        }

        startBlinkingAnimation(result)

        initiateListeners()
    }

    private fun initiateListeners() = randomizerButton.setOnClickListener {
        stopBlinkingAnimation(result)
        generateRandomLetter()
    }

    @Suppress("DEPRECATION")
    private fun generateRandomLetter() {
        val randomLetter = ('A'..'Z').randomString(1)

        result.apply {
            text = randomLetter
            textSize = 128F
            setTextColor(resources.getColor(R.color.color_secondary))
        }

        if (letterHistory.length >= 5) letterHistory = letterHistory.dropLast(1)
        letterHistory = randomLetter + letterHistory

        context?.let { putSharedPreferences(it, SP_RL_LETTER_HISTORY, letterHistory) }

        updateLetterHistory()

        logD { "randomLetter=$randomLetter" }
    }

    private fun updateLetterHistory() {
        firstHistory.text = if (letterHistory[0].toString() == "*") "" else letterHistory[0].toString()
        secondHistory.text = if (letterHistory[1].toString() == "*") "" else letterHistory[1].toString()
        thirdHistory.text = if (letterHistory[2].toString() == "*") "" else letterHistory[2].toString()
        fourthHistory.text = if (letterHistory[3].toString() == "*") "" else letterHistory[3].toString()
        fifthHistory.text = if (letterHistory[4].toString() == "*") "" else letterHistory[4].toString()
    }

    private companion object {
        const val SP_RL_LETTER_HISTORY = "SHARED_PREFERENCES_RANDOM_LETTER_LETTER_HISTORY"
    }
}