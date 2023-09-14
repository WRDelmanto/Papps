package com.wrdelmanto.papps.apps.dice

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.THREE_QUARTERS_SECOND_IN_MILLIS
import com.wrdelmanto.papps.utils.SP_D_DICE_HISTORY
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putSharedPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DicesViewModel : ViewModel() {
    private val _diceImage = MutableLiveData<Drawable>()
    val diceImage: LiveData<Drawable> = _diceImage

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

    private lateinit var diceHistory: String

    private var isFirstTime = true

    fun resetUi(context: Context) {
        diceHistory = SP_D_DICE_HISTORY.let {
            val lh = getSharedPreferences(context, it, String)
            lh ?: "*****"
        }.toString()

        updateDiceHistory()

        logD { "resetUi" }
    }

    fun rollDice(context: Context) {
        val diceResult = (1..6).random()

        if (diceHistory.length >= 5) diceHistory = diceHistory.dropLast(1)
        diceHistory = diceResult.toString() + diceHistory

        putSharedPreferences(context, SP_D_DICE_HISTORY, diceHistory)

        viewModelScope.launch {
            if (!isFirstTime) delay(THREE_QUARTERS_SECOND_IN_MILLIS)
            isFirstTime = false

            _diceImage.value = when (diceResult) {
                1 -> ResourcesCompat.getDrawable(context.resources, R.drawable.dice_1, null)
                2 -> ResourcesCompat.getDrawable(context.resources, R.drawable.dice_2, null)
                3 -> ResourcesCompat.getDrawable(context.resources, R.drawable.dice_3, null)
                4 -> ResourcesCompat.getDrawable(context.resources, R.drawable.dice_4, null)
                5 -> ResourcesCompat.getDrawable(context.resources, R.drawable.dice_5, null)
                else -> ResourcesCompat.getDrawable(context.resources, R.drawable.dice_6, null)
            }
        }

        logD { "diceResult=$diceResult, diceHistory=$diceHistory" }

        updateDiceHistory()
    }

    private fun updateDiceHistory() {
        _historyFirst.value =
            if (diceHistory[0].toString() == "*") "" else diceHistory[0].toString()
        _historySecond.value =
            if (diceHistory[1].toString() == "*") "" else diceHistory[1].toString()
        _historyThird.value =
            if (diceHistory[2].toString() == "*") "" else diceHistory[2].toString()
        _historyFourth.value =
            if (diceHistory[3].toString() == "*") "" else diceHistory[3].toString()
        _historyFifth.value =
            if (diceHistory[4].toString() == "*") "" else diceHistory[4].toString()
    }
}