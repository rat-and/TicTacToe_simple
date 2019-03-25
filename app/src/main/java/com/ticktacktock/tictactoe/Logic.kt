package com.ticktacktock.tictactoe

import android.support.v4.util.Pair

/*
    A class that's responsible for drawing lattice
    and whole functionality for Tic Tac Toe game.
    ORIGIN = 0
 */
class Logic {
    val BOARD_ROW = 3
    val BOARD_COLUMN = 3
    private var board = Array(BOARD_ROW, {IntArray(BOARD_COLUMN)})
    private var playerToMove: BoardPlayer = BoardPlayer.PLAYER_X
    private var numberOfMoves: Int = 0
    private lateinit var ticTacToeListener: TicTacToeListener2

    interface TicTacToeListener2 {
        fun gameWonBy(boardPlayer: BoardPlayer, winPoints: Array<SquareCoordinates>?)
        fun gameEndsWithATie()
        fun movedAt(x: Int, y: Int, move: Int)
    }

    fun setTicTacToeListener(ticTacToeListener2: TicTacToeListener2) {
        ticTacToeListener = ticTacToeListener2
    }

    enum class BoardPlayer(move: Int) {
        PLAYER_X(MOVE_X),
        PLAYER_O(MOVE_O);
        var move: Int = move
    }

    companion object {
        val SPACE: Int = 0
        val MOVE_X: Int = 1
        val MOVE_O: Int = 2

        class SquareCoordinates(i: Int, j: Int) {
            var i: Int = i      //row index
            var j: Int = j      //column index

            override fun equals(o: Any?): Boolean {
                if (this === o) {
                    return true
                }
                if (o == null || javaClass != o.javaClass) {
                    return false
                }

                val that = o as SquareCoordinates?

                return if (i != that!!.i) {
                    false
                } else j == that.j
            }

            override fun hashCode(): Int {
                var result = i
                result = 31 * result + j
                return result
            }
        }
    }

    fun isValidMove(x: Int, y: Int): Boolean {
        return board[x][y] == SPACE
    }

    fun moveAt(x: Int, y: Int): Boolean {
        if (x < 0 || x > BOARD_ROW - 1 || y < 0 || y > BOARD_COLUMN - 1) {
            throw IllegalArgumentException(String.format("Coordinates %d and %d are not valid, valid set [0,1,2]", x, y))
        }
        if (!isValidMove(x, y)) {
            return false
        }

        numberOfMoves++
        if (ticTacToeListener != null) {
            ticTacToeListener.movedAt(x, y, playerToMove.move)
        }
        board[x][y] = playerToMove.move
        val won = hasWon(x, y, playerToMove)
        if (won.first!!) {
            ticTacToeListener.gameWonBy(playerToMove, won.second)
        } else if (numberOfMoves == BOARD_COLUMN * BOARD_ROW) {
            ticTacToeListener!!.gameEndsWithATie()
        }
        changeTurnToNextPlayer()
        return true
    }

    private fun hasWon(x: Int, y: Int, playerToMove: BoardPlayer): Pair<Boolean, Array<SquareCoordinates>> {
        var winCoordinates = arrayOf(SquareCoordinates(0,0), SquareCoordinates(0,0), SquareCoordinates(0,0))

        var hasWon = (checkRow(x, y, playerToMove.move, winCoordinates)
                || checkColumn(x, y, playerToMove.move, winCoordinates)
                || checkDiagonals(x, y, playerToMove.move, winCoordinates))
        return Pair.create(hasWon, winCoordinates)
    }

    private fun checkDiagonals(x: Int, y: Int, move: Int, winCoordinates: Array<SquareCoordinates>): Boolean {
        if (board[0][0] == move && board[1][1] == move && board[2][2] == move) {
            winCoordinates[0] = SquareCoordinates(0, 0)
            winCoordinates[1] = SquareCoordinates(1, 1)
            winCoordinates[2] = SquareCoordinates(2, 2)
            return true
        } else if (board[0][2] == move && board[1][1] == move && board[2][0] == move) {
            winCoordinates[0] = SquareCoordinates(0, 2)
            winCoordinates[1] = SquareCoordinates(1, 1)
            winCoordinates[2] = SquareCoordinates(2, 0)
            return true
        }
        return false
    }

    private fun checkColumn(x: Int, y: Int, movetoCheck: Int, winCoordinates: Array<SquareCoordinates>): Boolean {
        for (i in 0 until BOARD_ROW) {
            if (board[i][y] != movetoCheck) {
                return false
            }
        }
        for (i in winCoordinates.indices) {
            winCoordinates[i] = SquareCoordinates(i, y)
        }
        return true
    }

    private fun checkRow(x: Int, y: Int, movetoCheck: Int, winCoordinates: Array<SquareCoordinates>): Boolean {
        for (i in 0 until BOARD_ROW) {
            if (board[x][i] != movetoCheck) {
                return false
            }
        }
        for (i in winCoordinates.indices) {
            winCoordinates[i] = SquareCoordinates(x, i)
        }
        return true
    }

    private fun changeTurnToNextPlayer() {
        if (playerToMove == BoardPlayer.PLAYER_X) {
            playerToMove = BoardPlayer.PLAYER_O
        } else {
            playerToMove = BoardPlayer.PLAYER_X
        }
    }

    fun resetGame() {
        initGame()
    }

    private fun initGame() {
        board = Array(BOARD_ROW) { IntArray(BOARD_COLUMN) }
        playerToMove = BoardPlayer.PLAYER_X
        numberOfMoves = 0
    }
}