package com.wrdelmanto.papps.apps.tip

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.hideKeyboard
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.showNormalToast

class TipFragment : Fragment() {
    private lateinit var value: EditText
    private lateinit var tipPercentage: TextView
    private lateinit var tipPercentageSeekBar: SeekBar
    private lateinit var tipOutput: TextView
    private lateinit var totalOutput: TextView

    private var percentage = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_tip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        value = view.findViewById(R.id.tip_value_input)
        tipPercentage = view.findViewById(R.id.tip_percentage)
        tipPercentageSeekBar = view.findViewById(R.id.tip_percentage_seek_bar)
        tipOutput = view.findViewById(R.id.tip_tip_output)
        totalOutput = view.findViewById(R.id.tip_total_output)

        tipPercentage.text = getString(R.string.value_with_percentage, tipPercentageSeekBar.progress)
        percentage = tipPercentageSeekBar.progress

        initiateListeners()
    }

    private fun initiateListeners() {
        value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) { calculateTotalOutput() }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        tipPercentageSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                percentage = progress
                tipPercentage.text = getString(R.string.value_with_percentage, progress)
                calculateTotalOutput()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) { hideKeyboard() }
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun calculateTotalOutput() {
        val valueInput = value.text.toString()

        if (valueInput.isNotEmpty()) {
            val tip = valueInput.toDouble() * percentage.toDouble() * TRANSFORM_TO_PERCENTAGE
            val total = valueInput.toDouble() + tip

            tipOutput.text = getString(R.string.value_with_cipher, tip.toString())
            totalOutput.text = getString(R.string.value_with_cipher, total.toString())

            logD { "valueInput=$valueInput, tipPercentage=$percentage, tip=$tip, total=$total" }
        } else {
            tipOutput.setText(R.string.zero)
            totalOutput.setText(R.string.zero)

            context?.let { showNormalToast(it, R.string.tip_no_input_found) }

            logD { "valueInput=Empty" }
        }
    }

    companion object {
        const val TRANSFORM_TO_PERCENTAGE = 0.01
    }
}