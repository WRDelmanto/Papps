package com.wrdelmanto.papps.apps.random.randomLetter

import android.content.Context
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
    val result: MutableLiveData<String>
        get() = _result

    private val _historyFirst = MutableLiveData("")
    val historyFirst: MutableLiveData<String>
        get() = _historyFirst

    private val _historySecond = MutableLiveData("")
    val historySecond: MutableLiveData<String>
        get() = _historySecond

    private val _historyThird = MutableLiveData("")
    val historyThird: MutableLiveData<String>
        get() = _historyThird

    private val _historyFourth = MutableLiveData("")
    val historyFourth: MutableLiveData<String>
        get() = _historyFourth

    private val _historyFifth = MutableLiveData("")
    val historyFifth: MutableLiveData<String>
        get() = _historyFifth

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