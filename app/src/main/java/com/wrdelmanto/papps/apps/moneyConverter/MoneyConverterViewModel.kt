package com.wrdelmanto.papps.apps.moneyConverter

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wrdelmanto.papps.ONE_QUARTER_SECOND_IN_MILLIS
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
    val primaryInput: LiveData<String> = _primaryInput

    private val _secondaryInput = MutableLiveData<String>()
    val secondaryInput: LiveData<String> = _secondaryInput

    private val _primaryConversion = MutableLiveData<String>()
    val primaryConversion: LiveData<String> = _primaryConversion

    private val _secondaryConversion = MutableLiveData<String>()
    val secondaryConversion: LiveData<String> = _secondaryConversion

    private val _state = MutableLiveData<MoneyConverterViewModelState>()
    val state: LiveData<MoneyConverterViewModelState> = _state

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

    private fun postState() = _state.postValue(MoneyConverterViewModelState(moneyConverterState))

    fun updatePrimaryInput(newPrimaryInput: String) {
        _primaryInput.value = newPrimaryInput
    }

    fun updateSecondaryInput(newSecondaryInput: String) {
        _secondaryInput.value = newSecondaryInput
    }

    fun updatePrimaryConversion(newPrimaryConversion: String) {
        _primaryConversion.value = newPrimaryConversion
    }

    fun updateSecondaryConversion(newSecondaryConversion: String) {
        _secondaryConversion.value = newSecondaryConversion
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

                "conversion" -> {
                    shouldDelay = false

                    if (shouldCalculateExchange()) calculateSecondaryInput()
                    else return@launch
                }
            }

            logD { "primaryInput=${primaryInput.value}, secondaryInput=${secondaryInput.value}, primaryConversion=${primaryConversion.value}, secondaryConversion=${secondaryConversion.value}" }
            if (shouldDelay) delay(ONE_QUARTER_SECOND_IN_MILLIS)

            setNormalState()
        }
    }

    private fun calculatePrimaryInput() {
        var newPrimaryInput = roundTo2Decimals(
            _secondaryInput.value?.toDouble()?.times(
                _primaryConversion.value!!.toDouble()
            )?.div(_secondaryConversion.value!!.toDouble())!!
        )

        if (!newPrimaryInput.toFloat().isFinite()) newPrimaryInput = "0.00"

        if (!_primaryInput.value.equals(newPrimaryInput)) _primaryInput.value = newPrimaryInput
    }

    private fun calculateSecondaryInput() {
        var newSecondaryInput = roundTo2Decimals(
            _primaryInput.value?.toDouble()?.times(
                _secondaryConversion.value!!.toDouble()
            )?.div(_primaryConversion.value!!.toDouble())!!
        )

        if (!newSecondaryInput.toFloat().isFinite()) newSecondaryInput = "0.00"

        if (!_secondaryInput.value.equals(newSecondaryInput)) _secondaryInput.value =
            newSecondaryInput
    }

    private fun shouldCalculateExchange(): Boolean {
        if (!isNumeric(_primaryInput.value.toString())) return false
        if (!isNumeric(_secondaryInput.value.toString())) return false
        if (!isNumeric(_primaryConversion.value.toString())) return false
        if (!_secondaryConversion.value!!.toFloat().isFinite()) return false

        return true
    }

    fun resetUi(context: Context) {
        setLoadingState()

        _primaryConversion.value = SP_MC_PRIMARY_CONVERSION.let {
            val hs = getSharedPreferences(context, it, String)
            hs ?: "1.00"
        }.toString()

        _secondaryConversion.value = SP_MC_SECONDARY_CONVERSION.let {
            val hs = getSharedPreferences(context, it, String)
            hs ?: "1.00"
        }.toString()

        _primaryInput.value = "1000.00"
        _secondaryInput.value = "1.00"

        logD { "resetUi" }

        setLoadedState()
    }
}