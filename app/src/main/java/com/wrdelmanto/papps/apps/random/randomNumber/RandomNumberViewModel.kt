package com.wrdelmanto.papps.apps.random.randomNumber

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.SP_RN_MAX
import com.wrdelmanto.papps.utils.SP_RN_MIN
import com.wrdelmanto.papps.utils.SP_RN_NUMBER_HISTORY
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putSharedPreferences
import com.wrdelmanto.papps.utils.showNormalToast

class RandomNumberViewModel : ViewModel() {
    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    private val _historyFirst = MutableLiveData("")
    val historyFirst: LiveData<String> = _historyFirst

    private val _historySecond = MutableLiveData("")
    val historySecond: LiveData<String> = _historySecond

    private val _historyThird = MutableLiveData("")
    val historyThird: LiveData<String> = _historyThird

    private val _minInput = MutableLiveData<String>()
    val minInput: LiveData<String> = _minInput

    private val _maxInput = MutableLiveData<String>()
    val maxInput: LiveData<String> = _maxInput

    private lateinit var numberHistory: String
    private lateinit var numberHistoryList: List<String>

    fun resetUi(context: Context) {
        _result.value = context.resources.getString(R.string.click_anywhere)

        numberHistory = SP_RN_NUMBER_HISTORY.let {
            val nh = getSharedPreferences(context, it, String)
            nh ?: "*.*.*.*"
        }.toString()
        numberHistoryList = numberHistory.split(".")

        updateNumberHistory(true)

        _minInput.value = SP_RN_MIN.let {
            val mi = getSharedPreferences(context, it, String)
            mi ?: "1"
        }.toString()

        _maxInput.value = SP_RN_MAX.let {
            val mi = getSharedPreferences(context, it, String)
            mi ?: "10"
        }.toString()

        logD { "resetUi" }
    }

    fun generateRandomNumber(context: Context) {
        val min = _minInput.value.toString()
        val max = _maxInput.value.toString()

        if (min == "" || max == "") {
            showNormalToast(context, R.string.random_number_no_input_found)
            return
        }

        if (min != "0" && min.first() == '0') _minInput.value = min.toInt().toString()

        if (max != "0" && max.first() == '0') _maxInput.value = max.toInt().toString()

        if (min.toInt() <= max.toInt()) {
            val randomNumber = (min.toInt()..max.toInt()).random()

            _result.value = randomNumber.toString()

            if (numberHistoryList.size >= 4) numberHistoryList = numberHistoryList.dropLast(1)

            numberHistory = randomNumber.toString() + "." + numberHistoryList.joinToString(".")
            numberHistoryList = numberHistory.split(".")

            putSharedPreferences(context, SP_RN_NUMBER_HISTORY, numberHistory)

            updateNumberHistory(false)

            logD { "min=$min, max=$max, randomNumber=$randomNumber, numberHistory=$numberHistory" }
        } else showNormalToast(context, R.string.random_number_min_higher_than_max)
    }

    private fun updateNumberHistory(firstTime: Boolean) {
        if (firstTime) {
            _historyFirst.value = if (numberHistoryList[0] == "*") "" else numberHistoryList[0]
            _historySecond.value = if (numberHistoryList[1] == "*") "" else numberHistoryList[1]
            _historyThird.value = if (numberHistoryList[2] == "*") "" else numberHistoryList[2]
        } else {
            _historyFirst.value = if (numberHistoryList[1] == "*") "" else numberHistoryList[1]
            _historySecond.value = if (numberHistoryList[2] == "*") "" else numberHistoryList[2]
            _historyThird.value = if (numberHistoryList[3] == "*") "" else numberHistoryList[3]
        }
    }
}