package com.wrdelmanto.papps.apps.random.randomLetter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.randomString
import com.wrdelmanto.papps.utils.startBlinkingAnimation
import com.wrdelmanto.papps.utils.stopBlinkingAnimation

class RandomLetterFragment : Fragment() {
    private lateinit var result: TextView
    private lateinit var randomizerButton: Button

    private var letterHistoryMultableList = mutableListOf("", "", "", "", "", "")
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
        updateLetterHistory(randomLetter)

        result.apply {
            text = randomLetter
            textSize = 128F
            setTextColor(resources.getColor(R.color.color_secondary))
        }

        logD { "randomLetter=$randomLetter" }
    }

    private fun updateLetterHistory(randomLetter: String) {
        if (letterHistoryMultableList.size >= 5) letterHistoryMultableList.removeLast()
        letterHistoryMultableList.add(0, randomLetter)

        firstHistory.text = letterHistoryMultableList[1]
        secondHistory.text = letterHistoryMultableList[2]
        thirdHistory.text = letterHistoryMultableList[3]
        fourthHistory.text = letterHistoryMultableList[4]
        fifthHistory.text = letterHistoryMultableList[5]
    }
}