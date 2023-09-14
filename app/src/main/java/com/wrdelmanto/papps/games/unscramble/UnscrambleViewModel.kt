package com.wrdelmanto.papps.games.unscramble

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wrdelmanto.papps.utils.SP_U_CURRENT_WORD
import com.wrdelmanto.papps.utils.SP_U_HIGH_SCORE
import com.wrdelmanto.papps.utils.SP_U_SCRAMBLED_WORD
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putSharedPreferences

class UnscrambleViewModel : ViewModel() {
    private val _highScore = MutableLiveData(0)
    val highScore: LiveData<Int> = _highScore

    private val _currentScore = MutableLiveData(0)
    val currentScore: LiveData<Int> = _currentScore

    private val _scrambledWord = MutableLiveData("")
    val scrambledWord: LiveData<String> = _scrambledWord

    private lateinit var currentWord: String

    val isAnswerCorrect = MutableLiveData(false)

    fun resetUi(context: Context, shouldResetCurrentWord: Boolean = false) {
        _highScore.value = SP_U_HIGH_SCORE.let {
            val hs = getSharedPreferences(context, it, Int)
            hs ?: 0
        }.toString().toInt()

        if (shouldResetCurrentWord) generateNextWord(context)
        else {
            currentWord = SP_U_CURRENT_WORD.let {
                val hs = getSharedPreferences(context, it, String)
                hs ?: ""
            }.toString()

            if (currentWord.isBlank()) generateNextWord(context)
            else _scrambledWord.value =
                getSharedPreferences(context, SP_U_SCRAMBLED_WORD, String).toString()

            logD { "currentWord=${currentWord}, scrambledWord=${scrambledWord.value}" }
        }

        logD { "resetUi" }
    }

    fun checkAnswer(context: Context, answer: String) {
        logD { "currentWord=${currentWord}, scrambledWord=${scrambledWord.value}, answer=$answer" }

        if (answer == currentWord) {
            isAnswerCorrect.value = true

            _currentScore.value = _currentScore.value?.plus(1)

            if (_currentScore.value!! > _highScore.value!!) {
                _highScore.value = _currentScore.value
                putSharedPreferences(context, SP_U_HIGH_SCORE, _highScore.value!!)
            }

            generateNextWord(context)
        } else isAnswerCorrect.value = false
    }

    private fun generateNextWord(context: Context) {
        currentWord = allWordsList.random().uppercase()

        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while (String(tempWord) == currentWord) {
            tempWord.shuffle()
        }

        _scrambledWord.value = String(tempWord)

        putSharedPreferences(context, SP_U_CURRENT_WORD, currentWord)
        putSharedPreferences(context, SP_U_SCRAMBLED_WORD, _scrambledWord.value.toString())

        logD { "nextWord=${currentWord}, scrambledWord=${scrambledWord.value}" }
    }
}