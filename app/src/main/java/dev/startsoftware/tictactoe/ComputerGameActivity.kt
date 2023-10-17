package dev.startsoftware.tictactoe

import android.graphics.Color
import android.os.Bundle
import android.os.Vibrator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class ComputerGameActivity : AppCompatActivity() {
    var turn = 1
    var gameover = false
    val playerSymbol = "X"
    val computerSymbol = "O"
    val board = Array(9) { "" }
    val vibrator: Vibrator by lazy {
        getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cells = arrayOf(
            R.id.tx_a1, R.id.tx_a2, R.id.tx_a3, R.id.tx_a4, R.id.tx_a5, R.id.tx_a6, R.id.tx_a7, R.id.tx_a8, R.id.tx_a9
        )
        for (cell in cells) {
            val item = findViewById<TextView>(cell)
            item.setOnClickListener { processClickEvent(cell) }
        }
    }

    fun processClickEvent(cellId: Int) {
        if (gameover) return
        val existingValue: String = findViewById<TextView>(cellId).text.toString()
        if (existingValue.isNotEmpty()) {
            // Square is already filled, vibrate to indicate an error
            vibrate()
            return
        }

        findViewById<TextView>(cellId).text = playerSymbol
        findViewById<TextView>(cellId).setTextColor(Color.parseColor("#ff0000"))

        // Update the board
        board[cellId - R.id.tx_a1] = playerSymbol

        // Check for a win or tie
        if (checkWin(playerSymbol)) {
            findViewById<TextView>(R.id.tx_turn).text = "Congratulations, You Win!"
            gameover = true
            vibrate()
        } else if (isBoardFull()) {
            findViewById<TextView>(R.id.tx_turn).text = "It's a tie!"
            gameover = true
            vibrate()
        } else {
            // Computer's turn
            computerMove()
        }
    }

    fun computerMove() {
        val bestMove = findBestMove()
        if (bestMove != -1) {
            val cellId = R.id.tx_a1 + bestMove
            findViewById<TextView>(cellId).text = computerSymbol
            findViewById<TextView>(cellId).setTextColor(Color.parseColor("#00ff00"))
            board[bestMove] = computerSymbol

            // Check for a win or tie
            if (checkWin(computerSymbol)) {
                findViewById<TextView>(R.id.tx_turn).text = "Computer Wins!"
                gameover = true
                vibrate()
            }
        }
    }

    fun checkWin(symbol: String): Boolean {
        for (possible in possible_wins) {
            val seqStr = possible.joinToString("") { cellId ->
                findViewById<TextView>(cellId).text.toString()
            }
            if (seqStr == symbol.repeat(3)) {
                possible.forEach { cellId ->
                    findViewById<TextView>(cellId).setBackgroundColor(Color.parseColor("#FF33F3"))
                }
                return true
            }
        }
        return false
    }

    fun isBoardFull(): Boolean {
        return board.none { it.isEmpty() }
    }

    fun vibrate() {
        vibrator.vibrate(100)
    }

    fun findBestMove(): Int {
        for (i in 0 until 9) {
            if (board[i].isEmpty()) {
                board[i] = computerSymbol
                if (checkWin(computerSymbol)) {
                    board[i] = ""
                    return i
                }
                board[i] = ""
            }
        }

        for (i in 0 until 9) {
            if (board[i].isEmpty()) {
                board[i] = playerSymbol
                if (checkWin(playerSymbol)) {
                    board[i] = ""
                    return i
                }
                board[i] = ""
            }
        }

        val corners = listOf(0, 2, 6, 8)
        val randomCorners = corners.filter { board[it].isEmpty() }
        if (randomCorners.isNotEmpty()) {
            val randomIndex = Random.nextInt(randomCorners.size)
            return randomCorners[randomIndex]
        }

        if (board[4].isEmpty()) {
            return 4
        }

        val sides = listOf(1, 3, 5, 7)
        val randomSides = sides.filter { board[it].isEmpty() }
        if (randomSides.isNotEmpty()) {
            val randomIndex = Random.nextInt(randomSides.size)
            return randomSides[randomIndex]
        }

        return -1
    }

    companion object {
        val possible_wins = arrayOf(
            arrayOf(R.id.tx_a1, R.id.tx_a2, R.id.tx_a3),
            arrayOf(R.id.tx_a4, R.id.tx_a5, R.id.tx_a6),
            arrayOf(R.id.tx_a7, R.id.tx_a8, R.id.tx_a9),
            arrayOf(R.id.tx_a1, R.id.tx_a5, R.id.tx_a9),
            arrayOf(R.id.tx_a7, R.id.tx_a5, R.id.tx_a3),
            arrayOf(R.id.tx_a1, R.id.tx_a4, R.id.tx_a7),
            arrayOf(R.id.tx_a2, R.id.tx_a5, R.id.tx_a8),
            arrayOf(R.id.tx_a3, R.id.tx_a6, R.id.tx_a9)
        )
    }
}
