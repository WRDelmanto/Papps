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
import com.wrdelmanto.papps.utils.hideKeyboard
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.showNormalToast

class RandomNumberFragment : Fragment() {
    private lateinit var result: TextView
    private lateinit var randomizerButton: Button
    private lateinit var minInput: EditText
    private lateinit var maxInput: EditText

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

        initiateListeners()
    }

    private fun initiateListeners() = randomizerButton.setOnClickListener { hideKeyboard(); generateRandomNumber() }

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

            logD { "min=$min, max=$max, randomNumber=$randomNumber" }
        } else context?.let { showNormalToast(it, R.string.random_number_min_higher_than_max) }
    }
}