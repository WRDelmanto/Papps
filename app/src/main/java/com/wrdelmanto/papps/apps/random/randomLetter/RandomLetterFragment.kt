package com.wrdelmanto.papps.apps.random.randomLetter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.MainActivity
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.databinding.FragmentRandomLetterBinding
import com.wrdelmanto.papps.utils.SP_RL_LETTER_HISTORY
import com.wrdelmanto.papps.utils.checkKeySharedPreferences
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putSharedPreferences
import com.wrdelmanto.papps.utils.randomString
import com.wrdelmanto.papps.utils.startBlinkingAnimation
import com.wrdelmanto.papps.utils.stopBlinkingAnimation

class RandomLetterFragment : Fragment() {
    private lateinit var binding: FragmentRandomLetterBinding

    private lateinit var result: TextView
    private lateinit var randomizerButton: Button

    private lateinit var letterHistory: String

    private lateinit var firstHistory: TextView
    private lateinit var secondHistory: TextView
    private lateinit var thirdHistory: TextView
    private lateinit var fourthHistory: TextView
    private lateinit var fifthHistory: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRandomLetterBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        result = binding.randomLetterResult
        randomizerButton = binding.randomLetterClickHere

        firstHistory = binding.randomLetterHistoryFirst
        secondHistory = binding.randomLetterHistorySecond
        thirdHistory = binding.randomLetterHistoryThird
        fourthHistory = binding.randomLetterHistoryFourth
        fifthHistory = binding.randomLetterHistoryFifth
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

    private fun initiateListeners() = randomizerButton.setOnClickListener {
        stopBlinkingAnimation(result)
        generateRandomLetter()
    }

    private fun disableListeners() = randomizerButton.setOnClickListener(null)

    @Suppress("DEPRECATION")
    private fun resetUi() {
        (activity as MainActivity?)?.updateAppBarTitle(getString(R.string.app_name_random_letter))
        result.apply {
            text = getString(R.string.click_anywhere)
            textSize = 16F
            setTextColor(resources.getColor(R.color.defaul_text_color))
        }

        letterHistory =
            if (context?.let { checkKeySharedPreferences(it, SP_RL_LETTER_HISTORY) } == true) {
                context?.let { getSharedPreferences(it, SP_RL_LETTER_HISTORY, String) } as String
            } else "*****"

        updateLetterHistory()

        startBlinkingAnimation(result)
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
        firstHistory.text =
            if (letterHistory[0].toString() == "*") "" else letterHistory[0].toString()
        secondHistory.text =
            if (letterHistory[1].toString() == "*") "" else letterHistory[1].toString()
        thirdHistory.text =
            if (letterHistory[2].toString() == "*") "" else letterHistory[2].toString()
        fourthHistory.text =
            if (letterHistory[3].toString() == "*") "" else letterHistory[3].toString()
        fifthHistory.text =
            if (letterHistory[4].toString() == "*") "" else letterHistory[4].toString()
    }
}