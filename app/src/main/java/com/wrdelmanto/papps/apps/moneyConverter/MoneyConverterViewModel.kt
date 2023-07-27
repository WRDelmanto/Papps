package com.wrdelmanto.papps.apps.moneyConverter

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wrdelmanto.papps.TEN_MILLIS
import com.wrdelmanto.papps.utils.SP_MC_PRIMARY_CONVERSION
import com.wrdelmanto.papps.utils.SP_MC_SECONDARY_CONVERSION
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.isNumeric
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.roundTo2Decimals
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MoneyConverterViewModel : ViewModel() {
    private val _primaryInput = MutableLiveData<String>()
    val primaryInput: MutableLiveData<String>
        get() = _primaryInput

    private val _secondaryInput = MutableLiveData<String>()
    val secondaryInput: MutableLiveData<String>
        get() = _secondaryInput

    private val _primaryConversion = MutableLiveData<String>()
    val primaryConversion: MutableLiveData<String>
        get() = _primaryConversion

    private val _secondaryConversion = MutableLiveData<String>()
    val secondaryConversion: MutableLiveData<String>
        get() = _secondaryConversion

    private val _state = MutableLiveData<MoneyConverterViewModelState>()
    val state: MutableLiveData<MoneyConverterViewModelState>
        get() = _state

    private lateinit var moneyConverterState: MoneyConverterState

    private fun setLoadingState() {
        moneyConverterState = MoneyConverterState.LOADING
        postState()
    }

    private fun setLoadedState() {
        moneyConverterState = MoneyConverterState.LOADED
        postState()
    }

    fun setNormalState() {
        moneyConverterState = MoneyConverterState.NORMAL
        postState()
    }

    private fun setCalculatingState() {
        moneyConverterState = MoneyConverterState.CALCULATING
        postState()
    }

    private fun postState() {
        val newState = MoneyConverterViewModelState(moneyConverterState)
        _state.postValue(newState)
    }

    fun calculateExchange(whatChanged: String) {
        if (moneyConverterState != MoneyConverterState.NORMAL) return

        setCalculatingState()
        var shouldDelay = false

        viewModelScope.launch {
            when (whatChanged) {
                "primaryInput" -> {
                    shouldDelay = true

                    if (shouldCalculateExchange()) calculateSecondaryInput()
                    else return@launch
                }

                "secondaryInput" -> {
                    shouldDelay = true

                    if (shouldCalculateExchange()) calculatePrimaryInput()
                    else return@launch
                }

                "primaryConversion" -> {
                    shouldDelay = false

                    if (shouldCalculateExchange()) calculateSecondaryInput()
                    else return@launch
                }

                "secondaryConversion" -> {
                    shouldDelay = false

                    if (shouldCalculateExchange()) calculateSecondaryInput()
                    else return@launch
                }
            }

            logD { "primaryInput=${primaryInput.value}, secondaryInput=${secondaryInput.value}, primaryConversion=${primaryConversion.value}, secondaryConversion=${secondaryConversion.value}" }
            if (shouldDelay) delay(TEN_MILLIS)

            setNormalState()
        }
    }

    private fun calculatePrimaryInput() {
        val newPrimaryInput = roundTo2Decimals(
            _secondaryInput.value?.toDouble()?.times(
                _primaryConversion.value!!.toDouble()
            )?.div(_secondaryConversion.value!!.toDouble())!!
        )

        if (!_primaryInput.value.equals(newPrimaryInput)) _primaryInput.value = newPrimaryInput
    }

    private fun calculateSecondaryInput() {
        val newSecondaryInput = roundTo2Decimals(
            _primaryInput.value?.toDouble()?.times(
                _secondaryConversion.value!!.toDouble()
            )?.div(_primaryConversion.value!!.toDouble())!!
        )

        if (!_secondaryInput.value.equals(newSecondaryInput)) _secondaryInput.value =
            newSecondaryInput
    }

    private fun shouldCalculateExchange(): Boolean {
        if (!isNumeric(_primaryInput.value.toString())) return false
        if (!isNumeric(_secondaryInput.value.toString())) return false
        if (!isNumeric(_primaryConversion.value.toString())) return false
        if (!isNumeric(_secondaryConversion.value.toString())) return false

        return true
    }

    fun resetUi(context: Context) {
        setLoadingState()

        _primaryConversion.value = SP_MC_PRIMARY_CONVERSION.let {
            val hs = getSharedPreferences(context, it, String)
            hs ?: "1.00"
        } as String

        _secondaryConversion.value = SP_MC_SECONDARY_CONVERSION.let {
            val hs = getSharedPreferences(context, it, String)
            hs ?: "2.00"
        } as String

        _primaryInput.value = "1000.00"
        _secondaryInput.value = "1.00"

        setLoadedState()
    }
}