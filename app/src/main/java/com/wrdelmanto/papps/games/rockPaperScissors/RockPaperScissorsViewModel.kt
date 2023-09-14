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
    private val _primaryChoice = MutableLiveData<Drawable>()
    val primaryChoice: MutableLiveData<Drawable>
        get() = _primaryChoice

    private val _secondaryChoice = MutableLiveData<Drawable>()
    val secondaryChoice: MutableLiveData<Drawable>
        get() = _secondaryChoice

    private val _primaryScore = MutableLiveData("0")
    val primaryScore: MutableLiveData<String>
        get() = _primaryScore

    private val _secondaryScore = MutableLiveData("0")
    val secondaryScore: MutableLiveData<String>
        get() = _secondaryScore

    private val _modeButton = MutableLiveData<String>()
    val modeButton: MutableLiveData<String>
        get() = _modeButton

    private var primaryChoiceTemp = ""
    private var secondaryChoiceTemp = ""

    var isTwoPlayersModeEnabled = false

    fun resetUi(context: Context) {
        _primaryChoice.value =
            ResourcesCompat.getDrawable(context.resources, R.drawable.ic_empty, null)
        _secondaryChoice.value =
            ResourcesCompat.getDrawable(context.resources, R.drawable.ic_empty, null)
        _primaryScore.value = "0"
        _secondaryScore.value = "0"

        clearTempVariable()

        updateGameMode(context, true)

        logD { "resetUi" }
    }

    fun updateGameMode(context: Context, isFirstTime: Boolean = false) {
        if (!isFirstTime) isTwoPlayersModeEnabled = !isTwoPlayersModeEnabled

        if (!isTwoPlayersModeEnabled) {
            _modeButton.value = context.resources.getString(R.string.one_player_mode)
        } else {
            _modeButton.value = context.resources.getString(R.string.two_player_mode)
        }

        logD { "updateGameMode: isTwoPlayersModeEnabled=$isTwoPlayersModeEnabled" }
    }

    fun primaryChoice(context: Context, selfChoice: String) {
        if (!isTwoPlayersModeEnabled) {
            _primaryChoice.value = when (selfChoice) {
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
                    ResourcesCompat.getDrawable(context.resources, R.drawable.ic_empty, null)
                }
            }

            checkResults(selfChoice, generateSecondaryChoice(context))
        } else {
            if (secondaryChoiceTemp != "") {
                _primaryChoice.value = when (selfChoice) {
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
                        ResourcesCompat.getDrawable(context.resources, R.drawable.ic_empty, null)
                    }
                }

                _secondaryChoice.value = when (secondaryChoiceTemp) {
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
                        ResourcesCompat.getDrawable(context.resources, R.drawable.ic_empty, null)
                    }
                }

                checkResults(selfChoice, secondaryChoiceTemp)
            } else {
                primaryChoiceTemp = selfChoice
            }
        }
    }

    fun secondaryChoice(context: Context, selfChoice: String) {
        if (primaryChoiceTemp != "") {
            _primaryChoice.value = when (primaryChoiceTemp) {
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
                    ResourcesCompat.getDrawable(context.resources, R.drawable.ic_empty, null)
                }
            }

            _secondaryChoice.value = when (selfChoice) {
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
                    ResourcesCompat.getDrawable(context.resources, R.drawable.ic_empty, null)
                }
            }

            checkResults(primaryChoiceTemp, selfChoice)
        } else {
            secondaryChoiceTemp = selfChoice
        }
    }

    private fun generateSecondaryChoice(context: Context): String {
        val options = arrayOf("Rock", "Paper", "Scissors")
        val appChoice = options[Random().nextInt(options.size)]

        _secondaryChoice.value = when (appChoice) {
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
                ResourcesCompat.getDrawable(context.resources, R.drawable.ic_empty, null)
            }
        }

        return appChoice
    }

    private fun checkResults(primaryChoice: String, secondaryChoice: String) {
        if (primaryChoice == "Scissors" && secondaryChoice == "Paper" || primaryChoice == "Paper" && secondaryChoice == "Rock" || primaryChoice == "Rock" && secondaryChoice == "Scissors") {
            // User 1 Wins
            _primaryScore.value = (_primaryScore.value?.toInt()?.plus(1)).toString()
        } else if (primaryChoice === "Scissors" && secondaryChoice === "Rock" || primaryChoice === "Paper" && secondaryChoice === "Scissors" || primaryChoice === "Rock" && secondaryChoice === "Paper") {
            // App/User 2 Wins
            _secondaryScore.value = (_secondaryScore.value?.toInt()?.plus(1)).toString()
        }

        clearTempVariable()

        logD { "primaryChoice=$primaryChoice, secondaryChoice=$secondaryChoice, primaryScore=${_primaryScore.value}, secondaryScore=${_secondaryScore.value}, isTwoPlayersModeEnabled=$isTwoPlayersModeEnabled" }
    }

    private fun clearTempVariable() {
        primaryChoiceTemp = ""
        secondaryChoiceTemp = ""
    }
}