package com.wrdelmanto.papps.apps.moneyConverter

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wrdelmanto.papps.MainActivity
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.databinding.FragmentMoneyConverterBinding
import com.wrdelmanto.papps.utils.SP_MC_PRIMARY_CONVERSION
import com.wrdelmanto.papps.utils.SP_MC_SECONDARY_CONVERSION
import com.wrdelmanto.papps.utils.putSharedPreferences

class MoneyConverterFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentMoneyConverterBinding

    private val moneyConverterViewModel: MoneyConverterViewModel by viewModels()

    private lateinit var primaryInput: EditText
    private lateinit var secondaryInput: EditText
    private lateinit var primaryConversion: EditText
    private lateinit var secondaryConversion: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoneyConverterBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.moneyConverterViewModel = moneyConverterViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        (activity as MainActivity?)?.updateAppBarTitle(getString(R.string.app_name_money_converter))

        primaryInput = binding.moneyConverterPrimaryInput
        secondaryInput = binding.moneyConverterSecondaryInput
        primaryConversion = binding.moneyConverterConversionPrimaryInput
        secondaryConversion = binding.moneyConverterConversionSecondaryInput

        initiateViewModelObservers()
    }

    override fun onResume() {
        super.onResume()

        moneyConverterViewModel.resetUi(context)
    }

    override fun onDestroy() {
        disableListeners()

        super.onDestroy()
    }

    private fun initiateViewModelObservers() {
        moneyConverterViewModel.state.observe(viewLifecycleOwner) {
            changeViewState(it.moneyConverterState)
        }
    }

    private fun changeViewState(viewState: MoneyConverterState) {
        when (viewState) {
            MoneyConverterState.LOADING -> {
                // Do nothing
            }

            MoneyConverterState.LOADED -> {
                initiateListeners()
                moneyConverterViewModel.setNormalState()
                moneyConverterViewModel.calculateExchange("primaryInput")
            }

            MoneyConverterState.NORMAL -> {
                // Do nothing
            }

            MoneyConverterState.CALCULATING -> {
                // Do nothing
            }
        }
    }

    private fun initiateListeners() {
        primaryInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                moneyConverterViewModel.primaryInput.value = s.toString()
                moneyConverterViewModel.calculateExchange("primaryInput")
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })
        secondaryInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                moneyConverterViewModel.secondaryInput.value = s.toString()
                moneyConverterViewModel.calculateExchange("secondaryInput")
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })
        primaryConversion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                putSharedPreferences(context, SP_MC_PRIMARY_CONVERSION, s.toString())
                moneyConverterViewModel.primaryConversion.value = s.toString()
                moneyConverterViewModel.calculateExchange("conversion")
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })
        secondaryConversion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                putSharedPreferences(context, SP_MC_SECONDARY_CONVERSION, s.toString())
                moneyConverterViewModel.secondaryConversion.value = s.toString()
                moneyConverterViewModel.calculateExchange("conversion")
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })
    }

    private fun disableListeners() {
        primaryInput.removeTextChangedListener(null)
        secondaryInput.removeTextChangedListener(null)
        primaryConversion.removeTextChangedListener(null)
        secondaryConversion.removeTextChangedListener(null)
    }
}