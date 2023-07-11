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
    val highScore: MutableLiveData<Int>
        get() = _highScore

    private val _currentScore = MutableLiveData(0)
    val currentScore: LiveData<Int>
        get() = _currentScore

    private val _scrambledWord = MutableLiveData("")
    val scrambledWord: LiveData<String>
        get() = _scrambledWord

    private val currentWord = MutableLiveData("")

    val isAnswerCorrect = MutableLiveData(false)

    fun resetUi(context: Context) {
        _highScore.value = SP_U_HIGH_SCORE.let {
            val hs = getSharedPreferences(context, it, Int)
            hs ?: 0
        } as Int

        _currentScore.value = 0

        currentWord.value = SP_U_CURRENT_WORD.let {
            val hs = getSharedPreferences(context, it, String)
            hs ?: ""
        } as String

        if (currentWord.value.isNullOrBlank()) generateNextWord(context)
        else _scrambledWord.value =
            getSharedPreferences(context, SP_U_SCRAMBLED_WORD, String) as String
    }

    fun checkAnswer(context: Context, answer: String) {
        if (answer == currentWord.value) {
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
        currentWord.value = allWordsList.random().uppercase()

        currentWord.value?.let { putSharedPreferences(context, SP_U_CURRENT_WORD, it) }
        _scrambledWord.value = currentWord.value?.reversed()

        logD { "currentWord=${currentWord.value}, scrambledWord=${scrambledWord.value}" }
    }

    fun reset(context: Context) {
        _currentScore.value = 0
        generateNextWord(context)
    }
}