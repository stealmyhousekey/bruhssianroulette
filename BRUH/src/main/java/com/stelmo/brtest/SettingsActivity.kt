package com.stelmo.brtest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val menuButton = findViewById(R.id.s_menu_button) as Button
        val infoString = "BRUHssian Roulette is a game revolving around the idea of funny, embarassing situations. \n \n" +
                "In this demo, you can test your luck in a single-player game by pressing the SOLO button on the home screen. \n \n" +
                "From here, you'll be presented with the game screen, where you can either spin the wheel or press your luck right away by pressing the READY button. \n \n" +
                "If none of the remaining slots feel safe, simply hit the RESET button to randomize the wheel and reset your odds to 1/6. \n \n" +
                "If you lose, a random image from your phone will be displayed on-screen for all to see. \n \n" +
                "So, you feeling lucky, bruh?"

        var infoView = findViewById(R.id.infoText) as TextView
        infoView.setText(infoString)

        menuButton.setOnClickListener{
            val intent = Intent(baseContext, HomeActivity::class.java)
            startActivity(intent)
        }

    }
}
