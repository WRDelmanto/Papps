package com.wrdelmanto.papps.apps.dice

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wrdelmanto.papps.HALF_SECOND_IN_MILLIS
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.SP_D_DICE_HISTORY
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putSharedPreferences
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Random

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

    private var updateDiceHistoryJob: Job? = null

    fun resetUi(context: Context) {
        diceHistory =
            SP_D_DICE_HISTORY.let {
                val lh = getSharedPreferences(context, it, String)
                lh ?: "*****"
            }.toString()

        updateDiceHistory()

        logD { "resetUi" }
    }

    fun rollDice(context: Context) {
        val diceResult = Random().nextInt(DICE_SIDES) + 1

        if (diceHistory.length >= DICE_HISTORY_SIZE) diceHistory = diceHistory.dropLast(1)
        diceHistory = diceResult.toString() + diceHistory

        putSharedPreferences(context, SP_D_DICE_HISTORY, diceHistory)

        viewModelScope.launch {
            if (!isFirstTime) delay(HALF_SECOND_IN_MILLIS)
            isFirstTime = false

            _diceImage.value =
                when (diceResult) {
                    DICE_SIDE_ONE ->
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.dice_1,
                            null,
                        )

                    DICE_SIDE_TWO ->
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.dice_2,
                            null,
                        )

                    DICE_SIDE_THREE ->
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.dice_3,
                            null,
                        )

                    DICE_SIDE_FOUR ->
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.dice_4,
                            null,
                        )

                    DICE_SIDE_FIVE ->
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.dice_5,
                            null,
                        )

                    DICE_SIDE_SIX ->
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.dice_6,
                            null,
                        )

                    else -> ResourcesCompat.getDrawable(context.resources, R.drawable.dice_6, null)
                }
        }

        logD { "diceResult=$diceResult, diceHistory=$diceHistory" }

        updateDiceHistoryJob?.cancel()
        updateDiceHistoryJob =
            viewModelScope.launch {
                delay(HALF_SECOND_IN_MILLIS)
                updateDiceHistory()
            }
    }

    private fun updateDiceHistory() {
        _historyFirst.value =
            if (diceHistory[DICE_FIRST_HISTORY_RESULT].toString() == "*") {
                ""
            } else {
                diceHistory[DICE_FIRST_HISTORY_RESULT].toString()
            }
        _historySecond.value =
            if (diceHistory[DICE_SECOND_HISTORY_RESULT].toString() == "*") {
                ""
            } else {
                diceHistory[DICE_SECOND_HISTORY_RESULT].toString()
            }
        _historyThird.value =
            if (diceHistory[DICE_THIRD_HISTORY_RESULT].toString() == "*") {
                ""
            } else {
                diceHistory[DICE_THIRD_HISTORY_RESULT].toString()
            }
        _historyFourth.value =
            if (diceHistory[DICE_FOURTH_HISTORY_RESULT].toString() == "*") {
                ""
            } else {
                diceHistory[DICE_FOURTH_HISTORY_RESULT].toString()
            }
        _historyFifth.value =
            if (diceHistory[DICE_FIFTH_HISTORY_RESULT].toString() == "*") {
                ""
            } else {
                diceHistory[DICE_FIFTH_HISTORY_RESULT].toString()
            }
    }

    private companion object {
        const val DICE_SIDES = 6
        const val DICE_SIDE_ONE = 1
        const val DICE_SIDE_TWO = 2
        const val DICE_SIDE_THREE = 3
        const val DICE_SIDE_FOUR = 4
        const val DICE_SIDE_FIVE = 5
        const val DICE_SIDE_SIX = 6

        const val DICE_HISTORY_SIZE = 5
        const val DICE_FIRST_HISTORY_RESULT = 0
        const val DICE_SECOND_HISTORY_RESULT = 1
        const val DICE_THIRD_HISTORY_RESULT = 2
        const val DICE_FOURTH_HISTORY_RESULT = 3
        const val DICE_FIFTH_HISTORY_RESULT = 4
    }
}
