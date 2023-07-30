package com.wrdelmanto.papps.games.rockPaperScissors

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wrdelmanto.papps.databinding.FragmentRockPaperScissorsBinding

class RockPaperScissorsFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentRockPaperScissorsBinding

    private val rockPaperScissorsViewModel: RockPaperScissorsViewModel by viewModels()

    private lateinit var selfChoiceRock: ImageView
    private lateinit var selfChoicePaper: ImageView
    private lateinit var selfChoiceScissors: ImageView
    private lateinit var resetButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRockPaperScissorsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rockPaperScissorsViewModel = rockPaperScissorsViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        selfChoiceRock = binding.rockPaperScissorsChoiceRock
        selfChoicePaper = binding.rockPaperScissorsChoicePaper
        selfChoiceScissors = binding.rockPaperScissorsChoiceScissors
        resetButton = binding.rockPaperScissorsResetButton
    }

    override fun onResume() {
        super.onResume()

        resetUi()
        initiateListeners()
    }

    override fun onPause() {
        disableListeners()

        super.onPause()
    }

    private fun initiateListeners() {
        selfChoiceRock.setOnClickListener { rockPaperScissorsViewModel.onChoice(context, "Rock") }
        selfChoicePaper.setOnClickListener { rockPaperScissorsViewModel.onChoice(context, "Paper") }
        selfChoiceScissors.setOnClickListener {
            rockPaperScissorsViewModel.onChoice(
                context, "Scissors"
            )
        }
        resetButton.setOnClickListener { rockPaperScissorsViewModel.resetUi(context) }
    }

    private fun disableListeners() {
        selfChoiceRock.setOnClickListener(null)
        selfChoicePaper.setOnClickListener(null)
        selfChoiceScissors.setOnClickListener(null)
        resetButton.setOnClickListener(null)
    }

    private fun resetUi() = rockPaperScissorsViewModel.resetUi(context)
}