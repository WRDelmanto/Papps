package com.wrdelmanto.papps.games.rockPaperScissors

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wrdelmanto.papps.databinding.FragmentRockPaperScissorsBinding

class RockPaperScissorsFragment(
    private val context: Context,
) : Fragment() {
    private lateinit var binding: FragmentRockPaperScissorsBinding

    private val rockPaperScissorsViewModel: RockPaperScissorsViewModel by viewModels()

    private lateinit var primaryChoiceRock: ImageView
    private lateinit var primaryChoicePaper: ImageView
    private lateinit var primaryChoiceScissors: ImageView

    private lateinit var modeButton: Button
    private lateinit var resetButton: Button

    private lateinit var secondaryChoiceResult: ImageView

    private lateinit var secondaryChoiceRock: ImageView
    private lateinit var secondaryChoicePaper: ImageView
    private lateinit var secondaryChoiceScissors: ImageView

    private lateinit var onePlayerModeGroup: Group
    private lateinit var twoPlayersModeGroup: Group

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRockPaperScissorsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.rockPaperScissorsViewModel = rockPaperScissorsViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        primaryChoiceRock = binding.rockPaperScissorsPrimaryChoiceRock
        primaryChoicePaper = binding.rockPaperScissorsPrimaryChoicePaper
        primaryChoiceScissors = binding.rockPaperScissorsPrimaryChoiceScissors

        resetButton = binding.rockPaperScissorsResetButton
        modeButton = binding.rockPaperScissorsModeButton

        secondaryChoiceResult = binding.rockPaperScissorsSecondaryChoiceResult

        secondaryChoiceRock = binding.rockPaperScissorsSecondaryChoiceRock
        secondaryChoicePaper = binding.rockPaperScissorsSecondaryChoicePaper
        secondaryChoiceScissors = binding.rockPaperScissorsSecondaryChoiceScissors

        onePlayerModeGroup = binding.rockPaperScissorsOnePlayerModeGroup
        twoPlayersModeGroup = binding.rockPaperScissorsTwoPlayersModeGroup

        initiateListeners()
        initiateObservers()
    }

    override fun onResume() {
        super.onResume()

        rockPaperScissorsViewModel.resetUi(context, true)
    }

    override fun onDestroy() {
        disableListeners()

        super.onDestroy()
    }

    private fun initiateListeners() {
        primaryChoiceRock.setOnClickListener {
            rockPaperScissorsViewModel.primaryChoice(
                context,
                "Rock",
            )
        }
        primaryChoicePaper.setOnClickListener {
            rockPaperScissorsViewModel.primaryChoice(
                context,
                "Paper",
            )
        }
        primaryChoiceScissors.setOnClickListener {
            rockPaperScissorsViewModel.primaryChoice(
                context,
                "Scissors",
            )
        }
        resetButton.setOnClickListener {
            rockPaperScissorsViewModel.resetUi(
                context,
            )
        }

        modeButton.setOnClickListener { rockPaperScissorsViewModel.updateGameMode(context) }

        secondaryChoiceRock.setOnClickListener {
            rockPaperScissorsViewModel.secondaryChoice(
                context,
                "Rock",
            )
        }
        secondaryChoicePaper.setOnClickListener {
            rockPaperScissorsViewModel.secondaryChoice(
                context,
                "Paper",
            )
        }
        secondaryChoiceScissors.setOnClickListener {
            rockPaperScissorsViewModel.secondaryChoice(
                context,
                "Scissors",
            )
        }
    }

    private fun initiateObservers() {
        rockPaperScissorsViewModel.modeButton.observe(viewLifecycleOwner) {
            updateMode()
        }
    }

    private fun disableListeners() {
        primaryChoiceRock.setOnClickListener(null)
        primaryChoicePaper.setOnClickListener(null)
        primaryChoiceScissors.setOnClickListener(null)
        resetButton.setOnClickListener(null)
        modeButton.setOnClickListener(null)
    }

    private fun updateMode() {
        if (!rockPaperScissorsViewModel.isTwoPlayersModeEnabled) {
            onePlayerModeGroup.visibility = VISIBLE
            twoPlayersModeGroup.visibility = INVISIBLE
            secondaryChoiceResult.run {
                scaleX = 1F
                scaleY = 1F
            }
        } else {
            onePlayerModeGroup.visibility = INVISIBLE
            twoPlayersModeGroup.visibility = VISIBLE
            secondaryChoiceResult.run {
                scaleX = -1F
                scaleY = -1F
            }
        }
    }
}
