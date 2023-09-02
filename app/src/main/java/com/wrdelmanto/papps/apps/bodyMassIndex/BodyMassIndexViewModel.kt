package com.wrdelmanto.papps.apps.bodyMassIndex

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wrdelmanto.papps.utils.SP_BMI_HEIGHT
import com.wrdelmanto.papps.utils.SP_BMI_WEIGHT
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.isNumeric
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.roundTo2Decimals

class BodyMassIndexViewModel : ViewModel() {
    private val _height = MutableLiveData<String>()
    val height: MutableLiveData<String>
        get() = _height

    private val _weight = MutableLiveData<String>()
    val weight: MutableLiveData<String>
        get() = _weight

    private val _bmi = MutableLiveData<String>()
    val bmi: MutableLiveData<String>
        get() = _bmi

    fun resetUi(context: Context) {
        _height.value = SP_BMI_HEIGHT.let {
            val hs = getSharedPreferences(context, it, Int)
            hs ?: "1.75"
        }.toString()

        _weight.value = SP_BMI_WEIGHT.let {
            val hs = getSharedPreferences(context, it, Int)
            hs ?: "75"
        }.toString()

        calculateBMI()
    }

    fun calculateBMI() {
        if (!isNumeric(_height.value.toString()) || !isNumeric(_weight.value.toString())) return

        _bmi.value = roundTo2Decimals(
            _weight.value?.toDouble()!!.div(
                _height.value!!.toDouble().times(_height.value!!.toDouble())
            )
        )

        logD { "Height=${_height.value}, Weight=${_weight.value}, BMI=${_bmi.value}" }
    }
}