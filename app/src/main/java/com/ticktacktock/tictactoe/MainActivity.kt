package com.ticktacktock.tictactoe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), Logic.TicTacToeListener2, TicTacToeView.SquarePressedListener {
//    lateinit var ticTacToe: TicTacToe
    lateinit var ticTacToe: Logic



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)
//        ticTacToe = TicTacToe()
        ticTacToe = Logic()
        ticTacToe.setTicTacToeListener(this)
        ticTacToeView.squarePressListener = this

        resetButton.setOnClickListener {
            ticTacToe.resetGame()
            resetGameUi()
            resetButton.visibility = View.GONE
        }
    }

    override fun onSquarePressed(i: Int, j: Int) {
        ticTacToe.moveAt(i, j)
    }

    override fun movedAt(x: Int, y: Int, z: Int) {
        if (z == Logic.Companion.MOVE_X)
            ticTacToeView.drawXAtPosition(x, y)
        else
            ticTacToeView.drawOAtPosition(x, y)
    }

    override fun gameEndsWithATie() {
        information.visibility = View.VISIBLE
        information.text = getString(R.string.game_ends_draw)
        resetButton.visibility = View.VISIBLE
        ticTacToeView.isEnabled = false
    }

    private fun resetGameUi() {
        ticTacToeView.reset()
        ticTacToeView.isEnabled = true
        information.visibility = View.GONE
        resetButton.visibility = View.GONE
    }

    override fun gameWonBy(boardPlayer: Logic.BoardPlayer, winCoords: Array<Logic.Companion.SquareCoordinates>?) {
        information.visibility = View.VISIBLE
        information.text = "Winner is ${if (boardPlayer.move == Logic.Companion.MOVE_X) "X" else "O"}"
        if (winCoords != null) {
            ticTacToeView.animateWin(winCoords[0].i, winCoords[0].j, winCoords[2].i, winCoords[2].j)
        }
        ticTacToeView.isEnabled = false
        resetButton.visibility = View.VISIBLE
    }

}
