package com.wrdelmanto.papps.games.tipTacToe

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.databinding.FragmentTicTacToeBinding
import com.wrdelmanto.papps.utils.showNormalToast
import java.util.Random
import kotlin.collections.ArrayList

class TicTacToeFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentTicTacToeBinding

    private lateinit var a11Button: Button
    private lateinit var a12Button: Button
    private lateinit var a13Button: Button
    private lateinit var a21Button: Button
    private lateinit var a22Button: Button
    private lateinit var a23Button: Button
    private lateinit var a31Button: Button
    private lateinit var a32Button: Button
    private lateinit var a33Button: Button

    private lateinit var pveButton: Button
    private lateinit var pvpButton: Button

    private lateinit var resetButton: Button

    private var playerOne = ArrayList<Int>()
    private var playerTwo = ArrayList<Int>()

    private var activePlayer = 1
    private var playerMode = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTicTacToeBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        a11Button = binding.ticTacToeA11
        a12Button = binding.ticTacToeA12
        a13Button = binding.ticTacToeA13
        a21Button = binding.ticTacToeA21
        a22Button = binding.ticTacToeA22
        a23Button = binding.ticTacToeA23
        a31Button = binding.ticTacToeA31
        a32Button = binding.ticTacToeA32
        a33Button = binding.ticTacToeA33

        pveButton = binding.ticTacToePveButton
        pvpButton = binding.ticTacToePvpButton

        resetButton = binding.ticTacToeResetButton

        switchPlayerMode(binding.ticTacToePveButton)
    }

    override fun onResume() {
        super.onResume()

        initiateListeners()
    }

    override fun onPause() {
        disableListeners()

        super.onPause()
    }

    private fun initiateListeners() {
        a11Button.setOnClickListener { buttonSelected(a11Button) }
        a12Button.setOnClickListener { buttonSelected(a12Button) }
        a13Button.setOnClickListener { buttonSelected(a13Button) }
        a21Button.setOnClickListener { buttonSelected(a21Button) }
        a22Button.setOnClickListener { buttonSelected(a22Button) }
        a23Button.setOnClickListener { buttonSelected(a23Button) }
        a31Button.setOnClickListener { buttonSelected(a31Button) }
        a32Button.setOnClickListener { buttonSelected(a32Button) }
        a33Button.setOnClickListener { buttonSelected(a33Button) }

        pveButton.setOnClickListener { switchPlayerMode(pveButton) }
        pvpButton.setOnClickListener { switchPlayerMode(pvpButton) }

        resetButton.setOnClickListener { resetScore() }
    }

    private fun disableListeners() {
        a11Button.setOnClickListener(null)
        a12Button.setOnClickListener(null)
        a13Button.setOnClickListener(null)
        a21Button.setOnClickListener(null)
        a22Button.setOnClickListener(null)
        a23Button.setOnClickListener(null)
        a31Button.setOnClickListener(null)
        a32Button.setOnClickListener(null)
        a33Button.setOnClickListener(null)

        pveButton.setOnClickListener(null)
        pvpButton.setOnClickListener(null)

        resetButton.setOnClickListener(null)
    }

    @Suppress("DEPRECATION")
    private fun switchPlayerMode(playerMode: Button) {
        when (playerMode) {
            pveButton -> {
                this.playerMode = 1
                pveButton.setTextColor(resources.getColor(R.color.color_secondary))
                pvpButton.setTextColor(resources.getColor(R.color.black))
            }

            pvpButton -> {
                this.playerMode = 2
                pvpButton.setTextColor(resources.getColor(R.color.color_secondary))
                pveButton.setTextColor(resources.getColor(R.color.black))
            }
        }

        resetScore()
    }

    private fun buttonSelected(buttonSelected: Button) {
        val cellId = when (buttonSelected) {
            a11Button -> 1
            a12Button -> 2
            a13Button -> 3
            a21Button -> 4
            a22Button -> 5
            a23Button -> 6
            a31Button -> 7
            a32Button -> 8
            a33Button -> 9
            else -> 0
        }

        playGame(cellId, buttonSelected)
    }

    private fun playGame(cellId: Int, buttonSelected: Button) {
        if (activePlayer == 1) {
            buttonSelected.text = "X"
            buttonSelected.setBackgroundColor(Color.GREEN)
            playerOne.add(cellId)
            activePlayer = 2

            if (playerMode == 1) {
                try {
                    autoPlay()
                } catch (ex: Exception) {
                    showNormalToast(context, "Game Over")
                }
            }
        } else {
            buttonSelected.text = "O"
            buttonSelected.setBackgroundColor(Color.CYAN)
            playerTwo.add(cellId)
            activePlayer = 1
        }

        buttonSelected.isEnabled = false
        checkWinner()
    }

    private fun checkWinner() {
        var winner = -1

        if (checkRows() != -1) winner = checkRows()
        if (checkCollumns() != -1) winner = checkRows()
        if (checkCrosses() != -1) winner = checkCrosses()

        if (winner != -1) {
            if (winner == 1) {
                if (playerMode == 1) showNormalToast(context, "Player 1 Wins!!!")
                else showNormalToast(context, "You Won!!!")
            } else {
                if (playerMode == 1) showNormalToast(context, "Player 2 Wins!!!")
                else showNormalToast(context, "CPU Wins!!!")
            }

            disableAllButtons()
        }
    }

    private fun checkRows(): Int {
        // Row 1
        if (playerOne.contains(1) && playerOne.contains(2) && playerOne.contains(3)) return 1
        if (playerTwo.contains(1) && playerTwo.contains(2) && playerTwo.contains(3)) return 2

        // Row 2
        if (playerOne.contains(4) && playerOne.contains(5) && playerOne.contains(6)) return 1
        if (playerTwo.contains(4) && playerTwo.contains(5) && playerTwo.contains(6)) return 2

        // Row 3
        if (playerOne.contains(7) && playerOne.contains(8) && playerOne.contains(9)) return 1
        if (playerTwo.contains(7) && playerTwo.contains(8) && playerTwo.contains(9)) return 2

        return -1
    }

    private fun checkCollumns(): Int {
        // Collumn 1
        if (playerOne.contains(1) && playerOne.contains(4) && playerOne.contains(7)) return 1
        if (playerTwo.contains(1) && playerTwo.contains(4) && playerTwo.contains(7)) return 2

        // Collumn 2
        if (playerOne.contains(2) && playerOne.contains(5) && playerOne.contains(8)) return 1
        if (playerTwo.contains(2) && playerTwo.contains(5) && playerTwo.contains(8)) return 2

        // Collumn 3
        if (playerOne.contains(3) && playerOne.contains(6) && playerOne.contains(9)) return 1
        if (playerTwo.contains(3) && playerTwo.contains(6) && playerTwo.contains(9)) return 2

        return -1
    }

    private fun checkCrosses(): Int {
        // Cross 1
        if (playerOne.contains(1) && playerOne.contains(5) && playerOne.contains(9)) return 1
        if (playerTwo.contains(1) && playerTwo.contains(5) && playerTwo.contains(9)) return 2

        // Cross 2
        if (playerOne.contains(3) && playerOne.contains(5) && playerOne.contains(7)) return 1
        if (playerTwo.contains(3) && playerTwo.contains(5) && playerTwo.contains(7)) return 2

        return -1
    }

    private fun disableAllButtons() {
        a11Button.isEnabled = false
        a12Button.isEnabled = false
        a13Button.isEnabled = false
        a21Button.isEnabled = false
        a22Button.isEnabled = false
        a23Button.isEnabled = false
        a31Button.isEnabled = false
        a32Button.isEnabled = false
        a33Button.isEnabled = false
    }

    private fun enableAllButtons() {
        a11Button.isEnabled = true
        a12Button.isEnabled = true
        a13Button.isEnabled = true
        a21Button.isEnabled = true
        a22Button.isEnabled = true
        a23Button.isEnabled = true
        a31Button.isEnabled = true
        a32Button.isEnabled = true
        a33Button.isEnabled = true
    }

    private fun autoPlay() {
        val emptyCells = ArrayList<Int>()

        for (cellId in 1..9) if (!playerOne.contains(cellId) || !playerTwo.contains(cellId)) emptyCells.add(
            cellId
        )

        val r = Random()
        val randomIndex = r.nextInt(emptyCells.size - 0) + 0
        val cellId = emptyCells[randomIndex]

        val buttonSelect = when (cellId) {
            1 -> a11Button
            2 -> a12Button
            3 -> a13Button
            4 -> a21Button
            5 -> a22Button
            6 -> a23Button
            7 -> a31Button
            8 -> a32Button
            9 -> a33Button
            else -> a11Button
        }

        playGame(cellId, buttonSelect)
    }

    private fun resetScore() {
        a11Button.setBackgroundResource(android.R.drawable.btn_default)
        a12Button.setBackgroundResource(android.R.drawable.btn_default)
        a13Button.setBackgroundResource(android.R.drawable.btn_default)
        a21Button.setBackgroundResource(android.R.drawable.btn_default)
        a22Button.setBackgroundResource(android.R.drawable.btn_default)
        a23Button.setBackgroundResource(android.R.drawable.btn_default)
        a31Button.setBackgroundResource(android.R.drawable.btn_default)
        a32Button.setBackgroundResource(android.R.drawable.btn_default)
        a33Button.setBackgroundResource(android.R.drawable.btn_default)

        a11Button.text = ""
        a12Button.text = ""
        a13Button.text = ""
        a21Button.text = ""
        a22Button.text = ""
        a23Button.text = ""
        a31Button.text = ""
        a32Button.text = ""
        a33Button.text = ""

        playerOne.clear()
        playerTwo.clear()

        activePlayer = 1
        playerMode = 1

        enableAllButtons()
    }
}