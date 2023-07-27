package com.wrdelmanto.papps.apps.tip

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.SP_T_TIP_PERCENTAGE
import com.wrdelmanto.papps.utils.SP_T_TIP_SWITCH
import com.wrdelmanto.papps.utils.SP_T_TOTAL_SWITCH
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.roundTo2Decimals
import kotlin.math.roundToInt

class TipViewModel : ViewModel() {
    private val _roundUpTip = MutableLiveData(false)
    val roundUpTip: MutableLiveData<Boolean>
        get() = _roundUpTip

    private val _roundUpTotal = MutableLiveData(false)
    val roundUpTotal: MutableLiveData<Boolean>
        get() = _roundUpTotal

    private val _tipPercentage = MutableLiveData(10)
    val tipPercentage: MutableLiveData<Int>
        get() = _tipPercentage

    private val _tipPercentageSeekBar = MutableLiveData(10)
    val tipPercentageSeekBar: MutableLiveData<Int>
        get() = _tipPercentageSeekBar

    private val _valueInput = MutableLiveData<String>()
    val valueInput: MutableLiveData<String>
        get() = _valueInput

    private val _tipOutput = MutableLiveData<String>()
    val tipOutput: MutableLiveData<String>
        get() = _tipOutput

    private val _totalOutput = MutableLiveData<String>()
    val totalOutput: MutableLiveData<String>
        get() = _totalOutput

    fun resetUi(context: Context) {
        _roundUpTip.value = SP_T_TIP_SWITCH.let {
            val rut = getSharedPreferences(context, it, Boolean)
            rut ?: false
        }.toString().toBoolean()

        _roundUpTotal.value = SP_T_TOTAL_SWITCH.let {
            val rut = getSharedPreferences(context, it, Boolean)
            rut ?: false
        }.toString().toBoolean()

        _tipPercentage.value = SP_T_TIP_PERCENTAGE.let {
            val p = getSharedPreferences(context, it, Int)
            p ?: 10
        }.toString().toInt()

        _tipPercentageSeekBar.value = _tipPercentage.value!!.toInt()
    }

    fun calculateTotalOutput(context: Context) {
        if (_valueInput.value.isNullOrBlank()) {
            tipOutput.value = context.resources.getString(R.string.zero_decimal)
            totalOutput.value = context.resources.getString(R.string.zero_decimal)

            logD { "valueInput=Empty" }
            return
        }

        val tip = if (_roundUpTip.value == true) {
            roundTo2Decimals(
                _valueInput.value?.toDouble()?.times(_tipPercentage.value!!)
                    ?.times(TRANSFORM_TO_PERCENTAGE)!!.roundToInt().toDouble()
            )
        } else {
            roundTo2Decimals(
                _valueInput.value?.toDouble()?.times(_tipPercentage.value?.toDouble()!!)
                    ?.times(TRANSFORM_TO_PERCENTAGE)!!
            )
        }

        _tipOutput.value =
            String.format(context.resources.getString(R.string.value_with_cipher), tip)

        val total = if (_roundUpTotal.value == true) {
            roundTo2Decimals(
                (valueInput.value?.toDouble()?.plus(tip.toDouble()))!!.roundToInt().toDouble()
            )
        } else {
            roundTo2Decimals(valueInput.value?.toDouble()?.plus(tip.toDouble())!!)
        }

        _totalOutput.value =
            String.format(context.resources.getString(R.string.value_with_cipher), total)

        logD { "valueInput=${_valueInput.value}, tipPercentage=${_tipPercentage.value}, tip=${_tipOutput.value}, total=${_totalOutput.value}" }
    }

    companion object {
        const val TRANSFORM_TO_PERCENTAGE = 0.01
    }
}