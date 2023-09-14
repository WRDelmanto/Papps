package com.wrdelmanto.papps.apps.tip

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wrdelmanto.papps.databinding.FragmentTipBinding
import com.wrdelmanto.papps.utils.SP_T_TIP_PERCENTAGE
import com.wrdelmanto.papps.utils.SP_T_TIP_SWITCH
import com.wrdelmanto.papps.utils.SP_T_TOTAL_SWITCH
import com.wrdelmanto.papps.utils.hideKeyboard
import com.wrdelmanto.papps.utils.putSharedPreferences

class TipFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentTipBinding

    private val tipViewModel: TipViewModel by viewModels()

    private lateinit var roundUpTip: SwitchCompat
    private lateinit var roundUpTotal: SwitchCompat
    private lateinit var value: EditText
    private lateinit var tipPercentageSeekBar: SeekBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTipBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tipViewModel = tipViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        roundUpTip = binding.tipRoundUpTipSwitch
        roundUpTotal = binding.tipRoundUpTotalSwitch
        value = binding.tipValueInput
        tipPercentageSeekBar = binding.tipPercentageSeekBar

        initiateListeners()
        initiateObservers()
    }

    override fun onResume() {
        super.onResume()

        tipViewModel.resetUi(context)
    }

    override fun onDestroy() {
        disableListeners()

        super.onDestroy()
    }

    private fun initiateObservers() {
        tipViewModel.roundUpTip.observe(viewLifecycleOwner) {
            tipViewModel.calculateTotalOutput(context)
        }

        tipViewModel.roundUpTotal.observe(viewLifecycleOwner) {
            tipViewModel.calculateTotalOutput(context)
        }

        tipViewModel.valueInput.observe(viewLifecycleOwner) {
            tipViewModel.calculateTotalOutput(context)
        }

        tipViewModel.tipPercentage.observe(viewLifecycleOwner) {
            tipViewModel.calculateTotalOutput(context)
        }
    }

    private fun initiateListeners() {
        roundUpTip.setOnCheckedChangeListener { _, isChecked ->
            tipViewModel.updateroundUpTip(isChecked)
            putSharedPreferences(context, SP_T_TIP_SWITCH, roundUpTip.isChecked)
        }

        roundUpTotal.setOnCheckedChangeListener { _, isChecked ->
            tipViewModel.updateroundUpTotal(isChecked)
            putSharedPreferences(context, SP_T_TOTAL_SWITCH, roundUpTotal.isChecked)
        }

        value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                tipViewModel.updateValueInput(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })

        tipPercentageSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                tipViewModel.updateTipPercentageSeekBar(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                hideKeyboard()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                putSharedPreferences(context, SP_T_TIP_PERCENTAGE, tipPercentageSeekBar.progress)
            }

        })
    }

    private fun disableListeners() {
        roundUpTip.addTextChangedListener(null)
        roundUpTotal.addTextChangedListener(null)
        value.addTextChangedListener(null)
        tipPercentageSeekBar.setOnSeekBarChangeListener(null)
    }
}