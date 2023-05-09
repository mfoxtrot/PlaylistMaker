package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import java.net.Inet4Address

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSearch = findViewById<Button>(R.id.main_menu_search)
        btnSearch.setOnClickListener {
            intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val btnMedia = findViewById<Button>(R.id.main_menu_media)
        btnMedia.setOnClickListener {
            intent = Intent(this, MediaActivity::class.java)
            startActivity(intent)
        }

        val btnSettings = findViewById<Button>(R.id.main_menu_settings)
        btnSettings.setOnClickListener {
            intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}