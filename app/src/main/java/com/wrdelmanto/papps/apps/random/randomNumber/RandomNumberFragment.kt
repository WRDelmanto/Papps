package com.wrdelmanto.papps.apps.random.randomNumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.checkKeySharedPreferences
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.hideKeyboard
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putSharedPreferences
import com.wrdelmanto.papps.utils.showNormalToast
import com.wrdelmanto.papps.utils.startBlinkingAnimation
import com.wrdelmanto.papps.utils.stopBlinkingAnimation

class RandomNumberFragment : Fragment() {
    private lateinit var result: TextView
    private lateinit var randomizerButton: Button
    private lateinit var minInput: EditText
    private lateinit var maxInput: EditText

    private var numberHistory = "*.*.*.*"
    private var numberHistoryList = listOf("*", "*", "*", "*")

    private lateinit var firstHistory: TextView
    private lateinit var secondHistory: TextView
    private lateinit var thirdHistory: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_random_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        result = view.findViewById(R.id.random_number_result)
        randomizerButton = view.findViewById(R.id.random_number_click_here)
        minInput = view.findViewById(R.id.random_number_min_input)
        maxInput = view.findViewById(R.id.random_number_max_input)

        firstHistory = view.findViewById(R.id.random_number_history_first)
        secondHistory = view.findViewById(R.id.random_number_history_second)
        thirdHistory = view.findViewById(R.id.random_number_history_third)

        if (context?.let { checkKeySharedPreferences(it, SP_RN_NUMBER_HISTORY) } == true) {
            numberHistory = context?.let { getSharedPreferences(it, SP_RN_NUMBER_HISTORY, String) } as String
            numberHistoryList = numberHistory.split(".")
        }

        updateNumberHistory(true)

        startBlinkingAnimation(result)
    }

    override fun onResume() {
        super.onResume()

        initiateListeners()
    }

    override fun onPause() {
        disableListeners()

        super.onPause()
    }

    private fun initiateListeners() = randomizerButton.setOnClickListener {
        hideKeyboard()
        stopBlinkingAnimation(result)
        generateRandomNumber()
    }

    private fun disableListeners() = randomizerButton.setOnClickListener(null)

    @Suppress("DEPRECATION")
    private fun generateRandomNumber() {
        val min = minInput.text.toString()
        val max = maxInput.text.toString()

        if (min == "" || max == "") context?.let { showNormalToast(it, R.string.random_number_no_input_found); return }

        if (min != "0" && min.first() == '0') minInput.setText(min.toInt().toString(), TextView.BufferType.EDITABLE)
        if (max != "0" && max.first() == '0') maxInput.setText(max.toInt().toString(), TextView.BufferType.EDITABLE)

        if (min.toInt() <= max.toInt()) {
            val randomNumber = (min.toInt()..max.toInt()).random()

            result.apply {
                text = randomNumber.toString()
                textSize = 128F
                setTextColor(resources.getColor(R.color.color_secondary))
            }

            if (numberHistoryList.size >= 4) numberHistoryList = numberHistoryList.dropLast(1)
            numberHistory = randomNumber.toString() + "." + numberHistoryList.joinToString(".")
            numberHistoryList = numberHistory.split(".")

            context?.let { putSharedPreferences(it, SP_RN_NUMBER_HISTORY, numberHistory) }

            updateNumberHistory(false)

            logD { "min=$min, max=$max, randomNumber=$randomNumber" }
        } else context?.let { showNormalToast(it, R.string.random_number_min_higher_than_max) }
    }

    private fun updateNumberHistory(firstTime: Boolean) {
        if (firstTime) {
            firstHistory.text = if (numberHistoryList[0] == "*") "" else numberHistoryList[0]
            secondHistory.text = if (numberHistoryList[1] == "*") "" else numberHistoryList[1]
            thirdHistory.text = if (numberHistoryList[2] == "*") "" else numberHistoryList[2]
        } else {
            firstHistory.text = if (numberHistoryList[1] == "*") "" else numberHistoryList[1]
            secondHistory.text = if (numberHistoryList[2] == "*") "" else numberHistoryList[2]
            thirdHistory.text = if (numberHistoryList[3] == "*") "" else numberHistoryList[3]
        }
    }

    private companion object {
        const val SP_RN_NUMBER_HISTORY = "SHARED_PREFERENCES_RANDOM_NUMBER_NUMBER_HISTORY"
    }
}