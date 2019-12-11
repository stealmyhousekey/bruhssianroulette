package com.stelmo.brtest

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //format game title
        val title = SpannableString("BRUHssian Roulette")
        title.setSpan(ForegroundColorSpan(Color.RED), 0, 4, 0)
        title.setSpan(RelativeSizeSpan(1.1f), 0, 4, 0)
        titleText.setText(title, TextView.BufferType.SPANNABLE)

        val playButton = findViewById(R.id.play_button) as Button
        val hostButton = findViewById(R.id.multi_button) as Button
        val settingsButton = findViewById(R.id.settings_button) as Button


        playButton.setOnClickListener{
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
        }

        hostButton.setOnClickListener{
            val intent = Intent(baseContext, NetworkActivity::class.java)
            startActivity(intent)
        }
//
//        clientButton.setOnClickListener{
//            val intent = Intent(baseContext, ClientActivity::class.java)
//            startActivity(intent)
//        }

        settingsButton.setOnClickListener{
            val intent = Intent(baseContext, SettingsActivity::class.java)
            startActivity(intent)
        }

    }
}
