package com.wrdelmanto.papps.apps.random.randomLetter

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.SP_RL_LETTER_HISTORY
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putSharedPreferences
import com.wrdelmanto.papps.utils.randomString

class RandomLetterViewModel : ViewModel() {
    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    private val _historyFirst = MutableLiveData("")
    val historyFirst: LiveData<String> = _historyFirst

    private val _historySecond = MutableLiveData("")
    val historySecond: LiveData<String> = _historySecond

    private val _historyThird = MutableLiveData("")
    val historyThird: LiveData<String> = _historyThird

    private val _historyFourth = MutableLiveData("")
    val historyFourth: LiveData<String> = _historyFourth

    private val _historyFifth = MutableLiveData("")
    val historyFifth: LiveData<String> = _historyFifth

    private lateinit var letterHistory: String

    fun resetUi(context: Context) {
        _result.value = context.resources.getString(R.string.click_anywhere)

        letterHistory = SP_RL_LETTER_HISTORY.let {
            val lh = getSharedPreferences(context, it, String)
            lh ?: "*****"
        }.toString()

        updateLetterHistory()

        logD { "resetUi" }
    }

    fun generateRandomLetter(context: Context) {
        val randomLetter = ('A'..'Z').randomString(1)

        _result.value = randomLetter

        if (letterHistory.length >= 5) letterHistory = letterHistory.dropLast(1)
        letterHistory = randomLetter + letterHistory

        putSharedPreferences(context, SP_RL_LETTER_HISTORY, letterHistory)

        updateLetterHistory()

        logD { "randomLetter=$randomLetter, letterHistory=$letterHistory" }
    }

    private fun updateLetterHistory() {
        _historyFirst.value =
            if (letterHistory[0].toString() == "*") "" else letterHistory[0].toString()
        _historySecond.value =
            if (letterHistory[1].toString() == "*") "" else letterHistory[1].toString()
        _historyThird.value =
            if (letterHistory[2].toString() == "*") "" else letterHistory[2].toString()
        _historyFourth.value =
            if (letterHistory[3].toString() == "*") "" else letterHistory[3].toString()
        _historyFifth.value =
            if (letterHistory[4].toString() == "*") "" else letterHistory[4].toString()
    }
}