package com.stelmo.brtest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val menuButton = findViewById(R.id.s_menu_button) as Button

        menuButton.setOnClickListener{
            val intent = Intent(baseContext, HomeActivity::class.java)
            startActivity(intent)
        }

    }
}
