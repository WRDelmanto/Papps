package com.wrdelmanto.papps.games.rockPaperScissors

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.logD
import java.util.Random

class RockPaperScissorsViewModel : ViewModel() {
    private val _appChoice = MutableLiveData<Drawable>()
    val appChoice: MutableLiveData<Drawable>
        get() = _appChoice

    private val _selfChoice = MutableLiveData<Drawable>()
    val selfChoice: MutableLiveData<Drawable>
        get() = _selfChoice

    private val _selfScore = MutableLiveData("0")
    val selfScore: MutableLiveData<String>
        get() = _selfScore

    private val _appScore = MutableLiveData("0")
    val appScore: MutableLiveData<String>
        get() = _appScore

    fun resetUi(context: Context) {
        _appChoice.value = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_empty, null)
        _selfChoice.value =
            ResourcesCompat.getDrawable(context.resources, R.drawable.ic_empty, null)
        _selfScore.value = "0"
        _appScore.value = "0"
    }

    fun onChoice(context: Context, selfChoice: String) {
        _selfChoice.value = when (selfChoice) {
            "Rock" -> ResourcesCompat.getDrawable(
                context.resources, R.drawable.rock_paper_scissors_rock, null
            )

            "Paper" -> ResourcesCompat.getDrawable(
                context.resources, R.drawable.rock_paper_scissors_paper, null
            )

            "Scissors" -> ResourcesCompat.getDrawable(
                context.resources, R.drawable.rock_paper_scissors_scissors, null
            )

            else -> {
                ResourcesCompat.getDrawable(
                    context.resources, R.drawable.rock_paper_scissors_rock, null
                )
            }
        }

        val appChoice = generetaAppChoice(context)

        checkResults(context, selfChoice, appChoice)
    }

    private fun generetaAppChoice(context: Context): String {
        val options = arrayOf("Rock", "Paper", "Scissors")
        val appChoice = options[Random().nextInt(3)]

        _appChoice.value = when (appChoice) {
            "Rock" -> ResourcesCompat.getDrawable(
                context.resources, R.drawable.rock_paper_scissors_rock, null
            )

            "Paper" -> ResourcesCompat.getDrawable(
                context.resources, R.drawable.rock_paper_scissors_paper, null
            )

            "Scissors" -> ResourcesCompat.getDrawable(
                context.resources, R.drawable.rock_paper_scissors_scissors, null
            )

            else -> {
                ResourcesCompat.getDrawable(
                    context.resources, R.drawable.rock_paper_scissors_rock, null
                )
            }
        }

        return appChoice
    }

    private fun checkResults(context: Context, selfChoice: String, appChoice: String) {
        if (selfChoice == "Scissors" && appChoice == "Paper" || selfChoice == "Paper" && appChoice == "Rock" || selfChoice == "Rock" && appChoice == "Scissors") {
            // User Wins
            _selfScore.value = (_selfScore.value?.toInt()?.plus(1)).toString()
        } else if (selfChoice === "Scissors" && appChoice === "Rock" || selfChoice === "Paper" && appChoice === "Scissors" || selfChoice === "Rock" && appChoice === "Paper") {
            // App Wins
            _appScore.value = (_appScore.value?.toInt()?.plus(1)).toString()
        }

        logD { "selfChoice=$selfChoice, appChoice=$appChoice" }
    }
}