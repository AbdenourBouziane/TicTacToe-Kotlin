package dev.startsoftware.tictactoe

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.widget.TextView

class UserGameActivity : AppCompatActivity() {
    var turn = 1
    var gameover = false
    val vibrator: Vibrator by lazy {
        getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cells = arrayOf(R.id.tx_a1, R.id.tx_a2, R.id.tx_a3, R.id.tx_a4, R.id.tx_a5, R.id.tx_a6, R.id.tx_a7, R.id.tx_a8, R.id.tx_a9)
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
            vibrate(100)
            return
        }
        if (turn == 1) {
            findViewById<TextView>(cellId).text = "X"
            findViewById<TextView>(cellId).setTextColor(Color.parseColor("#ff0000"))
        } else {
            findViewById<TextView>(cellId).text = "O"
            findViewById<TextView>(cellId).setTextColor(Color.parseColor("#00ff00"))
        }

        val win = checkWin()
        if (win) {
            findViewById<TextView>(R.id.tx_turn).text = "Congrats to Player $turn"
            gameover = true
            vibrate(300)
            return
        }

        turn = if (turn == 1) 2 else 1
        findViewById<TextView>(R.id.tx_turn).text = "Turn of Player: $turn"
    }

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

    fun checkWin(): Boolean {
        for (possible in possible_wins) {
            var seqStr = ""
            for (cellId in possible) {
                val existingValue: String = findViewById<TextView>(cellId).text.toString()
                if (existingValue.isEmpty()) break
                seqStr += existingValue
            }
            if (seqStr == "OOO" || seqStr == "XXX") {
                for (cellId in possible) {
                    findViewById<TextView>(cellId).setBackgroundColor(Color.parseColor("#FF33F3"))
                }
                return true
            }
        }

        if (isBoardFull()) {
            // It's a tie
            findViewById<TextView>(R.id.tx_turn).text = "It's a tie!"
            gameover = true
            vibrate(300) // Vibrate for a tie
        }

        return false
    }

    fun isBoardFull(): Boolean {
        val cells = arrayOf(
            R.id.tx_a1, R.id.tx_a2, R.id.tx_a3, R.id.tx_a4, R.id.tx_a5, R.id.tx_a6, R.id.tx_a7, R.id.tx_a8, R.id.tx_a9
        )
        return cells.all { findViewById<TextView>(it).text.isNotEmpty() }
    }

    fun vibrate(duration: Long) {
        vibrator.vibrate(duration)
    }
}
