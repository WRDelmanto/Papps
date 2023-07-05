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
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.MainActivity
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.databinding.FragmentTipBinding
import com.wrdelmanto.papps.utils.SP_T_TIP_PERCENTAGE
import com.wrdelmanto.papps.utils.SP_T_TIP_SWITCH
import com.wrdelmanto.papps.utils.SP_T_TOTAL_SWITCH
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.hideKeyboard
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putSharedPreferences
import com.wrdelmanto.papps.utils.roundTo2Decimals
import com.wrdelmanto.papps.utils.showNormalToast
import kotlin.math.round

class TipFragment : Fragment() {
    private lateinit var binding: FragmentTipBinding

    private lateinit var value: EditText
    private lateinit var tipPercentage: TextView
    private lateinit var tipPercentageSeekBar: SeekBar
    private lateinit var tipOutput: TextView
    private lateinit var totalOutput: TextView
    private lateinit var roundUpTip: SwitchCompat
    private lateinit var roundUpTotal: SwitchCompat

    private var percentage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTipBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        value = binding.tipValueInput
        tipPercentage = binding.tipPercentage
        tipPercentageSeekBar = binding.tipPercentageSeekBar
        tipOutput = binding.tipTipOutput
        totalOutput = binding.tipTotalOutput
        roundUpTip = binding.tipRoundUpTipSwitch
        roundUpTotal = binding.tipRoundUpTotalSwitch
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
        roundUpTip.setOnCheckedChangeListener { _, _ ->
            context?.let {
                putSharedPreferences(
                    it, SP_T_TIP_SWITCH, roundUpTip.isChecked
                )
            }
            calculateTotalOutput(true)
        }

        roundUpTotal.setOnCheckedChangeListener { _, _ ->
            context?.let { putSharedPreferences(it, SP_T_TOTAL_SWITCH, roundUpTotal.isChecked) }
            calculateTotalOutput(true)
        }

        value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                calculateTotalOutput(true)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        tipPercentageSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                percentage = progress
                tipPercentage.text = String.format(
                    getString(R.string.value_with_percentage),
                    tipPercentageSeekBar.progress.toString()
                )
                calculateTotalOutput(true)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                hideKeyboard()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                context?.let {
                    putSharedPreferences(
                        it, SP_T_TIP_PERCENTAGE, tipPercentageSeekBar.progress
                    )
                }
            }
        })
    }

    private fun disableListeners() {
        value.addTextChangedListener(null)
        tipPercentageSeekBar.setOnSeekBarChangeListener(null)
    }

    private fun resetUi() {
        (activity as MainActivity?)?.updateAppBarTitle(getString(R.string.app_name_tip))

        roundUpTip.isChecked = context?.let {
            val rut = getSharedPreferences(it, SP_T_TIP_SWITCH, Boolean)
            rut ?: false
        } as Boolean

        roundUpTotal.isChecked = context?.let {
            val rut = getSharedPreferences(it, SP_T_TOTAL_SWITCH, Boolean)
            rut ?: false
        } as Boolean

        tipPercentageSeekBar.progress = 10

        percentage = context?.let {
            val p = getSharedPreferences(it, SP_T_TIP_PERCENTAGE, Int)
            p ?: 10
        } as Int

        tipPercentage.text =
            String.format(getString(R.string.value_with_percentage), percentage.toString())
        tipPercentageSeekBar.progress = percentage

        calculateTotalOutput(false)
    }

    private fun calculateTotalOutput(shouldCalculateOutput: Boolean) {
        val valueInput = value.text.toString()

        if (valueInput.isNotEmpty()) {
            val tip =
                if (roundUpTip.isChecked) round(valueInput.toDouble() * percentage.toDouble() * TRANSFORM_TO_PERCENTAGE)
                else valueInput.toDouble() * percentage.toDouble() * TRANSFORM_TO_PERCENTAGE

            val total = if (roundUpTotal.isChecked) round(valueInput.toDouble() + tip)
            else valueInput.toDouble() + tip

            tipOutput.text = getString(R.string.value_with_cipher, roundTo2Decimals(tip))
            totalOutput.text = getString(R.string.value_with_cipher, roundTo2Decimals(total))

            logD { "valueInput=$valueInput, tipPercentage=$percentage, tip=${tipOutput.text}, total=${totalOutput.text}" }
        } else if (shouldCalculateOutput) {
            tipOutput.setText(R.string.zero_decimal)
            totalOutput.setText(R.string.zero_decimal)

            context?.let { showNormalToast(it, R.string.tip_no_input_found) }

            logD { "valueInput=Empty" }
        }
    }

    companion object {
        const val TRANSFORM_TO_PERCENTAGE = 0.01
    }
}