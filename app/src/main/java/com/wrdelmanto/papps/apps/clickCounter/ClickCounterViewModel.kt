package com.wrdelmanto.papps.apps.clickCounter

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wrdelmanto.papps.utils.SP_CC_HIGH_SCORE
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putSharedPreferences

class ClickCounterViewModel: ViewModel() {
    private val _counter = MutableLiveData(0)
    val counter: MutableLiveData<Int>
        get() = _counter

    private val _highScore = MutableLiveData(0)
    val highScore: MutableLiveData<Int>
        get() = _highScore

    fun resetUi(context: Context) {
        _counter.value = 0

        _highScore.value = SP_CC_HIGH_SCORE.let {
            val hs = getSharedPreferences(context, it, Int)
            hs ?: 0
        }.toString().toInt()
    }

    fun increaseCounter(context: Context) {
        _counter.value = _counter.value?.plus(1)

        if (_counter.value!! > _highScore.value!!) {
            _highScore.value = _counter.value
            putSharedPreferences(context, SP_CC_HIGH_SCORE, _counter.value!!)
        }

        logD { "clicks=${_counter.value}" }
    }
}