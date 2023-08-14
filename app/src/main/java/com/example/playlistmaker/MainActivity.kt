package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val btnSearchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        binding.mainMenuSearch.setOnClickListener(btnSearchClickListener)

        binding.mainMenuMedia.setOnClickListener {
            intent = Intent(this, MediaActivity::class.java)
            startActivity(intent)
        }

        binding.mainMenuSettings.setOnClickListener {
            intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}