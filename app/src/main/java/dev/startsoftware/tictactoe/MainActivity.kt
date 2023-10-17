package dev.startsoftware.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_options)

        // Reference the buttons by their IDs
        val playWithUserButton = findViewById<Button>(R.id.playWithUserButton)
        val playWithComputerButton = findViewById<Button>(R.id.playWithComputerButton)

        playWithUserButton.setOnClickListener {
            val intent = Intent(this, UserGameActivity::class.java)
            startActivity(intent)
        }

        playWithComputerButton.setOnClickListener {
            val intent = Intent(this, ComputerGameActivity::class.java)
            startActivity(intent)
        }
    }
}
