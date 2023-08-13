package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSearch = findViewById<View>(R.id.main_menu_search)
        val btnSearchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        btnSearch.setOnClickListener(btnSearchClickListener)

        val btnMedia = findViewById<View>(R.id.main_menu_media)
        btnMedia.setOnClickListener {
            intent = Intent(this, MediaActivity::class.java)
            startActivity(intent)
        }

        val btnSettings = findViewById<View>(R.id.main_menu_settings)
        btnSettings.setOnClickListener {
            intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}