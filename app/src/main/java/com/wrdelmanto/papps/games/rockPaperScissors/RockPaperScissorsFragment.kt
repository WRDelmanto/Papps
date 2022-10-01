package com.wrdelmanto.papps.games.rockPaperScissors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.logD
import java.util.Random

class RockPaperScissorsFragment : Fragment() {
    private lateinit var appChoiceResultImage: ImageView
    private lateinit var selfChoiceResultImage: ImageView
    private lateinit var appScore: TextView
    private lateinit var selfScore: TextView
    private lateinit var selfChoiceResult: TextView
    private lateinit var selfChoiceRock: ImageView
    private lateinit var selfChoicePaper: ImageView
    private lateinit var selfChoiceScissors: ImageView
    private lateinit var resetButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_rock_paper_scissors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appChoiceResultImage = view.findViewById(R.id.rock_paper_scissors_app_choice_result)
        selfChoiceResultImage = view.findViewById(R.id.rock_paper_scissors_self_choice)
        appScore = view.findViewById(R.id.rock_paper_scissors_app_score)
        selfScore = view.findViewById(R.id.rock_paper_scissors_self_score)
        selfChoiceResult = view.findViewById(R.id.rock_paper_scissors_choice)
        selfChoiceRock = view.findViewById(R.id.rock_paper_scissors_choice_rock)
        selfChoicePaper = view.findViewById(R.id.rock_paper_scissors_choice_paper)
        selfChoiceScissors = view.findViewById(R.id.rock_paper_scissors_choice_scissors)
        resetButton = view.findViewById(R.id.rock_paper_scissors_reset_button)

        initiateListeners()
    }

    private fun initiateListeners() {
        selfChoiceRock.setOnClickListener { onChoice("Rock") }
        selfChoicePaper.setOnClickListener { onChoice("Paper") }
        selfChoiceScissors.setOnClickListener { onChoice("Scissors") }
        resetButton.setOnClickListener { resetScore() }
    }

    private fun onChoice(selfChoice: String) {
        when (selfChoice) {
        "Rock" -> selfChoiceResultImage.setImageResource(R.drawable.rock_paper_scissors_rock)
        "Paper" -> selfChoiceResultImage.setImageResource(R.drawable.rock_paper_scissors_paper)
        "Scissors" -> selfChoiceResultImage.setImageResource(R.drawable.rock_paper_scissors_scissors)
        }

        val appChoice = generetaAppChoice()

        checkResults(selfChoice, appChoice)
    }

    private fun generetaAppChoice(): String {
        val options = arrayOf("Rock", "Paper", "Scissors")
        val appChoice = options[Random().nextInt(3)]

        when (appChoice) {
            "Rock" -> appChoiceResultImage.setImageResource(R.drawable.rock_paper_scissors_rock)
            "Paper" -> appChoiceResultImage.setImageResource(R.drawable.rock_paper_scissors_paper)
            "Scissors" -> appChoiceResultImage.setImageResource(R.drawable.rock_paper_scissors_scissors)
        }

        return appChoice
    }

    private fun checkResults(selfChoice: String, appChoice: String) {
        if (selfChoice == "Scissors" && appChoice == "Paper"
            || selfChoice == "Paper" && appChoice == "Rock"
            || selfChoice == "Rock" && appChoice == "Scissors"
        ) { // User Wins
                val result = 1 + selfScore.text.toString().toInt()
                selfScore.text = result.toString()
        } else if (selfChoice === "Scissors" && appChoice === "Rock"
            || selfChoice === "Paper" && appChoice === "Scissors"
            || selfChoice === "Rock" && appChoice === "Paper"
        ) { // App Wins
                val result = 1 + appScore.text.toString().toInt()
                appScore.text = result.toString()
            }

        logD { "selfChoice=$selfChoice, appChoice=$appChoice" }
    }

    private fun resetScore() {
        appScore.text = "0"
        selfScore.text = "0"
        appChoiceResultImage.setImageResource(R.drawable.ic_empty)
        selfChoiceResultImage.setImageResource(R.drawable.ic_empty)

        logD { "resetScore" }
    }
}