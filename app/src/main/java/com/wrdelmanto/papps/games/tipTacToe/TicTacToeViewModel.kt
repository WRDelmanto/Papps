package com.wrdelmanto.papps.games.tipTacToe

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wrdelmanto.papps.ONE_QUARTER_SECOND_IN_MILLIS
import com.wrdelmanto.papps.ONE_SECOND_IN_MILLIS
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.changeColorGraduallyAndRollback
import com.wrdelmanto.papps.utils.logD
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TicTacToeViewModel : ViewModel() {
    private val _modeButton = MutableLiveData<String>()
    val modeButton: LiveData<String> = _modeButton

    private val _a11 = MutableLiveData<String>()
    val a11: LiveData<String> = _a11
    private val _a11BackgroundColor = MutableLiveData<Drawable>()
    val a11BackgroundColor: LiveData<Drawable> = _a11BackgroundColor

    private val _a12 = MutableLiveData<String>()
    val a12: LiveData<String> = _a12
    private val _a12BackgroundColor = MutableLiveData<Drawable>()
    val a12BackgroundColor: LiveData<Drawable> = _a12BackgroundColor

    private val _a13 = MutableLiveData<String>()
    val a13: LiveData<String> = _a13
    private val _a13BackgroundColor = MutableLiveData<Drawable>()
    val a13BackgroundColor: LiveData<Drawable> = _a13BackgroundColor

    private val _a21 = MutableLiveData<String>()
    val a21: LiveData<String> = _a21
    private val _a21BackgroundColor = MutableLiveData<Drawable>()
    val a21BackgroundColor: LiveData<Drawable> = _a21BackgroundColor

    private val _a22 = MutableLiveData<String>()
    val a22: LiveData<String> = _a22
    private val _a22BackgroundColor = MutableLiveData<Drawable>()
    val a22BackgroundColor: LiveData<Drawable> = _a22BackgroundColor

    private val _a23 = MutableLiveData<String>()
    val a23: LiveData<String> = _a23
    private val _a23BackgroundColor = MutableLiveData<Drawable>()
    val a23BackgroundColor: LiveData<Drawable> = _a23BackgroundColor

    private val _a31 = MutableLiveData<String>()
    val a31: LiveData<String> = _a31
    private val _a31BackgroundColor = MutableLiveData<Drawable>()
    val a31BackgroundColor: LiveData<Drawable> = _a31BackgroundColor

    private val _a32 = MutableLiveData<String>()
    val a32: LiveData<String> = _a32
    private val _a32BackgroundColor = MutableLiveData<Drawable>()
    val a32BackgroundColor: LiveData<Drawable> = _a32BackgroundColor

    private val _a33 = MutableLiveData<String>()
    val a33: LiveData<String> = _a33
    private val _a33BackgroundColor = MutableLiveData<Drawable>()
    val a33BackgroundColor: LiveData<Drawable> = _a33BackgroundColor

    private val _winner = MutableLiveData<Int>()
    val winner: LiveData<Int> = _winner

    private val _playerOneScore = MutableLiveData<String>()
    val playerOneScore: LiveData<String> = _playerOneScore

    private val _playerTwoScore = MutableLiveData<String>()
    val playerTwoScore: LiveData<String> = _playerTwoScore

    private var playerOne = ArrayList<Int>()
    private var playerTwo = ArrayList<Int>()
    private var winnerButtons = listOf<Int>()

    private var activePlayer = 1
    private var isTwoPlayersModeEnabled = false

    private lateinit var initialColor: ColorDrawable
    private lateinit var highlightedColor: ColorDrawable

    private var viewModelScopeJob: Job? = null

    @RequiresApi(Build.VERSION_CODES.M)
    fun resetUi(
        context: Context,
        view: View,
        isFirstTime: Boolean = false,
        shouldResetScores: Boolean = true,
        shouldResetModeButton: Boolean = true
    ) {
        initialColor = context.resources.getColor(R.color.white, null).toDrawable()
        highlightedColor = context.resources.getColor(R.color.green, null).toDrawable()

        _a11.value = ""
        _a11BackgroundColor.value = initialColor

        _a12.value = ""
        _a12BackgroundColor.value = initialColor

        _a13.value = ""
        _a13BackgroundColor.value = initialColor

        _a21.value = ""
        _a21BackgroundColor.value = initialColor

        _a22.value = ""
        _a22BackgroundColor.value = initialColor

        _a23.value = ""
        _a23BackgroundColor.value = initialColor

        _a31.value = ""
        _a31BackgroundColor.value = initialColor

        _a32.value = ""
        _a32BackgroundColor.value = initialColor

        _a33.value = ""
        _a33BackgroundColor.value = initialColor

        playerOne.clear()
        playerTwo.clear()

        if (isTwoPlayersModeEnabled && _winner.value == 2) {
            _winner.value = -1
            activePlayer = 2
            autoPlay(context, view)
        } else {
            _winner.value = -1
            activePlayer = 1
        }

        if (isFirstTime || shouldResetScores) {
            _playerOneScore.value = "0"
            _playerTwoScore.value = "0"
        }

        if (isFirstTime || shouldResetModeButton) _modeButton.value =
            context.resources.getString(R.string.one_player_mode)

        logD { "resetUi" }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun updateGameMode(context: Context, view: View) {
        isTwoPlayersModeEnabled = !isTwoPlayersModeEnabled

        if (!isTwoPlayersModeEnabled) _modeButton.value =
            context.resources.getString(R.string.one_player_mode)
        else _modeButton.value = context.resources.getString(R.string.two_player_mode)

        resetUi(context, view, shouldResetScores = true, shouldResetModeButton = false)

        logD { "updateGameMode: isTwoPlayersModeEnabled=$isTwoPlayersModeEnabled" }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun buttonSelected(context: Context, view: View, buttonSelected: TextView) {
        if (activePlayer == 1) {
            buttonSelected.text = X_ANSWER
            playerOne.add(convertButtonTextIntoCellId(view, buttonSelected))
            playerOne.sort()
        } else {
            buttonSelected.text = O_ANSWER
            playerTwo.add(convertButtonTextIntoCellId(view, buttonSelected))
            playerTwo.sort()
        }

        buttonSelected.isEnabled = false

        checkWinner(context, view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun autoPlay(context: Context, view: View) {
        var cellId: Int

        // TODO: Better AI
        do cellId = (1..9).random()
        while (playerOne.contains(cellId) || playerTwo.contains(cellId))

        viewModelScopeJob?.cancel()
        viewModelScopeJob = viewModelScope.launch {
            convertCellIdIntoTextView(view, cellId).apply {
                delay(ONE_QUARTER_SECOND_IN_MILLIS)
                text = O_ANSWER
                isEnabled = false
            }

            playerTwo.add(cellId)
            checkWinner(context, view)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkWinner(context: Context, view: View) {
        if (checkRows() != -1) _winner.value = checkRows()
        if (checkCollumns() != -1) _winner.value = checkCollumns()
        if (checkCrosses() != -1) _winner.value = checkCrosses()

        logD { "winner=${_winner.value}" }

        if (_winner.value != -1) {
            if (_winner.value == 1) {
                logD { "Player one wins" }
                _playerOneScore.value = _playerOneScore.value?.toInt()?.plus(1).toString()
                highlightWinnerButtons(context, view)
            } else {
                logD { "Player two wins" }
                _playerTwoScore.value = _playerTwoScore.value?.toInt()?.plus(1).toString()
                highlightWinnerButtons(context, view)
            }
        } else {
            if (activePlayer == 1) {
                activePlayer = 2
                if (isTwoPlayersModeEnabled) autoPlay(context, view)
            } else activePlayer = 1
        }

        logD { "playerOne=$playerOne, playerTwo=$playerTwo, playerOneScore=${_playerOneScore.value}, playerTwoScore=${_playerTwoScore.value}, isTwoPlayersModeEnabled=$isTwoPlayersModeEnabled" }
    }

    private fun checkRows(): Int {
        // Row 1
        if (playerOne.containsAll(listOf(1, 2, 3))) {
            winnerButtons = listOf(1, 2, 3)
            return 1
        }
        if (playerTwo.containsAll(listOf(1, 2, 3))) {
            winnerButtons = listOf(1, 2, 3)
            return 2
        }

        // Row 2
        if (playerOne.containsAll(listOf(4, 5, 6))) {
            winnerButtons = listOf(4, 5, 6)
            return 1
        }
        if (playerTwo.containsAll(listOf(4, 5, 6))) {
            winnerButtons = listOf(4, 5, 6)
            return 2
        }

        // Row 3
        if (playerOne.containsAll(listOf(7, 8, 9))) {
            winnerButtons = listOf(7, 8, 9)
            return 1
        }
        if (playerTwo.containsAll(listOf(7, 8, 9))) {
            winnerButtons = listOf(7, 8, 9)
            return 2
        }

        return -1
    }

    private fun checkCollumns(): Int {
        // Collumn 1
        if (playerOne.containsAll(listOf(1, 4, 7))) {
            winnerButtons = listOf(1, 4, 7)
            return 1
        }
        if (playerTwo.containsAll(listOf(1, 4, 7))) {
            winnerButtons = listOf(1, 4, 7)
            return 2
        }

        // Collumn 2
        if (playerOne.containsAll(listOf(2, 5, 8))) {
            winnerButtons = listOf(2, 5, 8)
            return 1
        }
        if (playerTwo.containsAll(listOf(2, 5, 8))) {
            winnerButtons = listOf(2, 5, 8)
            return 2
        }

        // Collumn 3
        if (playerOne.containsAll(listOf(3, 6, 9))) {
            winnerButtons = listOf(3, 6, 9)
            return 1
        }
        if (playerTwo.containsAll(listOf(3, 6, 9))) {
            winnerButtons = listOf(3, 6, 9)
            return 2
        }

        return -1
    }

    private fun checkCrosses(): Int {
        // Cross 1
        if (playerOne.containsAll(listOf(1, 5, 9))) {
            winnerButtons = listOf(1, 5, 9)
            return 1
        }
        if (playerTwo.containsAll(listOf(1, 5, 9))) {
            winnerButtons = listOf(1, 5, 9)
            return 2
        }

        // Cross 2
        if (playerOne.containsAll(listOf(3, 5, 7))) {
            winnerButtons = listOf(3, 5, 7)
            return 1
        }
        if (playerTwo.containsAll(listOf(3, 5, 7))) {
            winnerButtons = listOf(3, 5, 7)
            return 2
        }

        return -1
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun highlightWinnerButtons(context: Context, view: View) {
        viewModelScopeJob?.cancel()
        viewModelScopeJob = viewModelScope.launch {
            delay(ONE_QUARTER_SECOND_IN_MILLIS)

            changeColorGraduallyAndRollback(
                initialColor, highlightedColor, convertCellIdIntoTextView(view, winnerButtons[0])
            )
            changeColorGraduallyAndRollback(
                initialColor, highlightedColor, convertCellIdIntoTextView(view, winnerButtons[1])
            )
            changeColorGraduallyAndRollback(
                initialColor, highlightedColor, convertCellIdIntoTextView(view, winnerButtons[2])
            )

            delay(ONE_SECOND_IN_MILLIS)

            resetUi(context, view, shouldResetScores = false, shouldResetModeButton = false)
        }
    }

    private fun convertButtonTextIntoCellId(view: View, buttonSelected: TextView): Int {
        return when (buttonSelected) {
            view.findViewById<TextView>(R.id.tic_tac_toe_a11) -> 1
            view.findViewById<TextView>(R.id.tic_tac_toe_a12) -> 2
            view.findViewById<TextView>(R.id.tic_tac_toe_a13) -> 3
            view.findViewById<TextView>(R.id.tic_tac_toe_a21) -> 4
            view.findViewById<TextView>(R.id.tic_tac_toe_a22) -> 5
            view.findViewById<TextView>(R.id.tic_tac_toe_a23) -> 6
            view.findViewById<TextView>(R.id.tic_tac_toe_a31) -> 7
            view.findViewById<TextView>(R.id.tic_tac_toe_a32) -> 8
            view.findViewById<TextView>(R.id.tic_tac_toe_a33) -> 9
            else -> 0
        }
    }

    private fun convertCellIdIntoTextView(view: View, cellId: Int): TextView {
        return when (cellId) {
            1 -> view.findViewById(R.id.tic_tac_toe_a11)
            2 -> view.findViewById(R.id.tic_tac_toe_a12)
            3 -> view.findViewById(R.id.tic_tac_toe_a13)
            4 -> view.findViewById(R.id.tic_tac_toe_a21)
            5 -> view.findViewById(R.id.tic_tac_toe_a22)
            6 -> view.findViewById(R.id.tic_tac_toe_a23)
            7 -> view.findViewById(R.id.tic_tac_toe_a31)
            8 -> view.findViewById(R.id.tic_tac_toe_a32)
            9 -> view.findViewById(R.id.tic_tac_toe_a33)
            else -> view.findViewById(R.id.tic_tac_toe_a11)
        }
    }

    private companion object {
        const val X_ANSWER = "X"
        const val O_ANSWER = "O"
    }
}