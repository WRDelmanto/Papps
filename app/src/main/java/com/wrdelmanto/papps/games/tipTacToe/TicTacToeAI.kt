package com.wrdelmanto.papps.games.tipTacToe

import com.wrdelmanto.papps.utils.logD

object TicTacToeAI {
    private var playerOne = ArrayList<Int>()
    private var playerTwo = ArrayList<Int>()

    fun smartAutoPlay(playerOneList: ArrayList<Int>, playerTwoList: ArrayList<Int>): Int {
        playerOne = playerOneList
        playerTwo = playerTwoList

        val winnableRows = checkWinnableRows()
        val winnableCollumns = checkWinnableCollumns()
        val winnableCrosses = checkWinnableCrosses()

        val counterWinnableRows = checkCounterWinnableRows()
        val counterWinnableCollumns = checkCounterWinnableCollumns()
        val counterWinnableCrosses = checkCounterWinnableCrosses()

        val cellId = when {
            (winnableRows != -1) -> winnableRows
            (winnableCollumns != -1) -> winnableCollumns
            (winnableCrosses != -1) -> winnableCrosses
            (counterWinnableRows != -1) -> counterWinnableRows
            (counterWinnableCollumns != -1) -> counterWinnableCollumns
            (counterWinnableCrosses != -1) -> counterWinnableCrosses
            else -> -1
        }

        logD { "Smart Play - cellId=$cellId" }

        return cellId
    }

    private fun checkWinnableRows(): Int {
        val winnableRowOne = checkWinnableRowOne()
        if (winnableRowOne != -1) return winnableRowOne

        val winnableRowTwo = checkWinnableRowTwo()
        if (winnableRowTwo != -1) return winnableRowTwo

        val winnableRowThree = checkWinnableRowThree()
        if (winnableRowThree != -1) return winnableRowThree

        return -1
    }

    private fun checkWinnableCollumns(): Int {
        val winnableCollumnOne = checkWinnableCollumnOne()
        if (winnableCollumnOne != -1) return winnableCollumnOne

        val winnableCollumnTwo = checkWinnableCollumnTwo()
        if (winnableCollumnTwo != -1) return winnableCollumnTwo

        val winnableCollumnThree = checkWinnableCollumnThree()
        if (winnableCollumnThree != -1) return winnableCollumnThree

        return -1
    }

    private fun checkWinnableCrosses(): Int {
        val winnableCrossOne = checkWinnableCrossOne()
        if (winnableCrossOne != -1) return winnableCrossOne

        val winnableCrossTwo = checkWinnableCrossTwo()
        if (winnableCrossTwo != -1) return winnableCrossTwo

        return -1
    }

    private fun checkCounterWinnableRows(): Int {
        val counterWinnableRowOne = checkCounterWinnableRowOne()
        if (counterWinnableRowOne != -1) return counterWinnableRowOne

        val counterWinnableRowTwo = checkCounterWinnableRowTwo()
        if (counterWinnableRowTwo != -1) return counterWinnableRowTwo

        val counterWinnableRowThree = checkCounterWinnableRowThree()
        if (counterWinnableRowThree != -1) return counterWinnableRowThree

        return -1
    }

    private fun checkCounterWinnableCollumns(): Int {
        val counterWinnableCollumnOne = checkCounterWinnableCollumnOne()
        if (counterWinnableCollumnOne != -1) return counterWinnableCollumnOne

        val counterWinnableCollumnTwo = checkCounterWinnableCollumnTwo()
        if (counterWinnableCollumnTwo != -1) return counterWinnableCollumnTwo

        val counterWinnableCollumnThree = checkCounterWinnableCollumnThree()
        if (counterWinnableCollumnThree != -1) return counterWinnableCollumnThree

        return -1
    }

    private fun checkCounterWinnableCrosses(): Int {
        val counterWinnableCrossOne = checkCounterWinnableCrossOne()
        if (counterWinnableCrossOne != -1) return counterWinnableCrossOne

        val counterWinnableCrossTwo = checkCounterWinnableCrossTwo()
        if (counterWinnableCrossTwo != -1) return counterWinnableCrossTwo

        return -1
    }

    private fun checkWinnableRowOne(): Int {
        // Cell 1
        if (playerTwo.containsAll(
                listOf(
                    2, 3
                )
            ) && !playerOne.contains(1) && !playerTwo.contains(1)
        ) return 1

        // Cell 2
        if (playerTwo.containsAll(
                listOf(
                    1, 3
                )
            ) && !playerOne.contains(2) && !playerTwo.contains(2)
        ) return 2

        // Cell 3
        if (playerTwo.containsAll(
                listOf(
                    1, 2
                )
            ) && !playerOne.contains(3) && !playerTwo.contains(3)
        ) return 3

        return -1
    }

    private fun checkWinnableRowTwo(): Int {
        // Cell 4
        if (playerTwo.containsAll(
                listOf(
                    5, 6
                )
            ) && !playerOne.contains(4) && !playerTwo.contains(4)
        ) return 4

        // Cell 5
        if (playerTwo.containsAll(
                listOf(
                    4, 6
                )
            ) && !playerOne.contains(5) && !playerTwo.contains(5)
        ) return 5

        // Cell 6
        if (playerTwo.containsAll(
                listOf(
                    4, 5
                )
            ) && !playerOne.contains(6) && !playerTwo.contains(6)
        ) return 6

        return -1
    }

    private fun checkWinnableRowThree(): Int {
        // Cell 7
        if (playerTwo.containsAll(
                listOf(
                    8, 9
                )
            ) && !playerOne.contains(7) && !playerTwo.contains(7)
        ) return 7

        // Cell 8
        if (playerTwo.containsAll(
                listOf(
                    7, 9
                )
            ) && !playerOne.contains(8) && !playerTwo.contains(8)
        ) return 8

        // Cell 9
        if (playerTwo.containsAll(
                listOf(
                    7, 8
                )
            ) && !playerOne.contains(9) && !playerTwo.contains(9)
        ) return 9

        return -1
    }

    private fun checkWinnableCollumnOne(): Int {
        // Cell 1
        if (playerTwo.containsAll(
                listOf(
                    4, 7
                )
            ) && !playerOne.contains(1) && !playerTwo.contains(1)
        ) return 1

        // Cell 4
        if (playerTwo.containsAll(
                listOf(
                    1, 7
                )
            ) && !playerOne.contains(4) && !playerTwo.contains(4)
        ) return 4

        // Cell 7
        if (playerTwo.containsAll(
                listOf(
                    1, 4
                )
            ) && !playerOne.contains(7) && !playerTwo.contains(7)
        ) return 7

        return -1
    }

    private fun checkWinnableCollumnTwo(): Int {
        // Cell 2
        if (playerTwo.containsAll(
                listOf(
                    5, 8
                )
            ) && !playerOne.contains(2) && !playerTwo.contains(2)
        ) return 2

        // Cell 5
        if (playerTwo.containsAll(
                listOf(
                    2, 8
                )
            ) && !playerOne.contains(5) && !playerTwo.contains(5)
        ) return 5

        // Cell 8
        if (playerTwo.containsAll(
                listOf(
                    2, 5
                )
            ) && !playerOne.contains(8) && !playerTwo.contains(8)
        ) return 8

        return -1
    }

    private fun checkWinnableCollumnThree(): Int {
        // Cell 3
        if (playerTwo.containsAll(
                listOf(
                    6, 9
                )
            ) && !playerOne.contains(3) && !playerTwo.contains(3)
        ) return 3

        // Cell 6
        if (playerTwo.containsAll(
                listOf(
                    3, 9
                )
            ) && !playerOne.contains(6) && !playerTwo.contains(6)
        ) return 6

        // Cell 9
        if (playerTwo.containsAll(
                listOf(
                    3, 6
                )
            ) && !playerOne.contains(9) && !playerTwo.contains(9)
        ) return 9

        return -1
    }

    private fun checkWinnableCrossOne(): Int {
        // Cell 1
        if (playerTwo.containsAll(
                listOf(
                    5, 9
                )
            ) && !playerOne.contains(1) && !playerTwo.contains(1)
        ) return 1

        // Cell 5
        if (playerTwo.containsAll(
                listOf(
                    1, 9
                )
            ) && !playerOne.contains(5) && !playerTwo.contains(5)
        ) return 5

        // Cell 9
        if (playerTwo.containsAll(
                listOf(
                    1, 5
                )
            ) && !playerOne.contains(9) && !playerTwo.contains(9)
        ) return 9

        return -1
    }

    private fun checkWinnableCrossTwo(): Int {
        // Cell 3
        if (playerTwo.containsAll(
                listOf(
                    5, 7
                )
            ) && !playerOne.contains(3) && !playerTwo.contains(3)
        ) return 3

        // Cell 5
        if (playerTwo.containsAll(
                listOf(
                    3, 7
                )
            ) && !playerOne.contains(5) && !playerTwo.contains(5)
        ) return 5

        // Cell 7
        if (playerTwo.containsAll(
                listOf(
                    3, 5
                )
            ) && !playerOne.contains(7) && !playerTwo.contains(7)
        ) return 7

        return -1
    }

    private fun checkCounterWinnableRowOne(): Int {
        // Cell 1
        if (playerOne.containsAll(
                listOf(
                    2, 3
                )
            ) && !playerOne.contains(1) && !playerTwo.contains(1)
        ) return 1

        // Cell 2
        if (playerOne.containsAll(
                listOf(
                    1, 3
                )
            ) && !playerOne.contains(2) && !playerTwo.contains(2)
        ) return 2

        // Cell 3
        if (playerOne.containsAll(
                listOf(
                    1, 2
                )
            ) && !playerOne.contains(3) && !playerTwo.contains(3)
        ) return 3

        return -1
    }

    private fun checkCounterWinnableRowTwo(): Int {
        // Cell 4
        if (playerOne.containsAll(
                listOf(
                    5, 6
                )
            ) && !playerOne.contains(4) && !playerTwo.contains(4)
        ) return 4

        // Cell 5
        if (playerOne.containsAll(
                listOf(
                    4, 6
                )
            ) && !playerOne.contains(5) && !playerTwo.contains(5)
        ) return 5

        // Cell 6
        if (playerOne.containsAll(
                listOf(
                    4, 5
                )
            ) && !playerOne.contains(6) && !playerTwo.contains(6)
        ) return 6

        return -1
    }

    private fun checkCounterWinnableRowThree(): Int {
        // Cell 7
        if (playerOne.containsAll(
                listOf(
                    8, 9
                )
            ) && !playerOne.contains(7) && !playerTwo.contains(7)
        ) return 7

        // Cell 8
        if (playerOne.containsAll(
                listOf(
                    7, 9
                )
            ) && !playerOne.contains(8) && !playerTwo.contains(8)
        ) return 8

        // Cell 9
        if (playerOne.containsAll(
                listOf(
                    7, 8
                )
            ) && !playerOne.contains(9) && !playerTwo.contains(9)
        ) return 9

        return -1
    }

    private fun checkCounterWinnableCollumnOne(): Int {
        // Cell 1
        if (playerOne.containsAll(
                listOf(
                    4, 7
                )
            ) && !playerOne.contains(1) && !playerTwo.contains(1)
        ) return 1

        // Cell 4
        if (playerOne.containsAll(
                listOf(
                    1, 7
                )
            ) && !playerOne.contains(4) && !playerTwo.contains(4)
        ) return 4

        // Cell 7
        if (playerOne.containsAll(
                listOf(
                    1, 4
                )
            ) && !playerOne.contains(7) && !playerTwo.contains(7)
        ) return 7

        return -1
    }

    private fun checkCounterWinnableCollumnTwo(): Int {
        // Cell 2
        if (playerOne.containsAll(
                listOf(
                    5, 8
                )
            ) && !playerOne.contains(2) && !playerTwo.contains(2)
        ) return 2

        // Cell 5
        if (playerOne.containsAll(
                listOf(
                    2, 8
                )
            ) && !playerOne.contains(5) && !playerTwo.contains(5)
        ) return 5

        // Cell 8
        if (playerOne.containsAll(
                listOf(
                    2, 5
                )
            ) && !playerOne.contains(8) && !playerTwo.contains(8)
        ) return 8

        return -1
    }

    private fun checkCounterWinnableCollumnThree(): Int {
        // Cell 3
        if (playerOne.containsAll(
                listOf(
                    6, 9
                )
            ) && !playerOne.contains(3) && !playerTwo.contains(3)
        ) return 3

        // Cell 6
        if (playerOne.containsAll(
                listOf(
                    3, 9
                )
            ) && !playerOne.contains(6) && !playerTwo.contains(6)
        ) return 6

        // Cell 9
        if (playerOne.containsAll(
                listOf(
                    3, 6
                )
            ) && !playerOne.contains(9) && !playerTwo.contains(9)
        ) return 9

        return -1
    }

    private fun checkCounterWinnableCrossOne(): Int {
        // Cell 1
        if (playerOne.containsAll(
                listOf(
                    5, 9
                )
            ) && !playerOne.contains(1) && !playerTwo.contains(1)
        ) return 1

        // Cell 5
        if (playerOne.containsAll(
                listOf(
                    1, 9
                )
            ) && !playerOne.contains(5) && !playerTwo.contains(5)
        ) return 5

        // Cell 9
        if (playerOne.containsAll(
                listOf(
                    1, 5
                )
            ) && !playerOne.contains(9) && !playerTwo.contains(9)
        ) return 9

        return -1
    }

    private fun checkCounterWinnableCrossTwo(): Int {
        // Cell 3
        if (playerOne.containsAll(
                listOf(
                    5, 7
                )
            ) && !playerOne.contains(3) && !playerTwo.contains(3)
        ) return 3

        // Cell 5
        if (playerOne.containsAll(
                listOf(
                    3, 7
                )
            ) && !playerOne.contains(5) && !playerTwo.contains(5)
        ) return 5

        // Cell 7
        if (playerOne.containsAll(
                listOf(
                    3, 5
                )
            ) && !playerOne.contains(7) && !playerTwo.contains(7)
        ) return 7

        return -1
    }
}