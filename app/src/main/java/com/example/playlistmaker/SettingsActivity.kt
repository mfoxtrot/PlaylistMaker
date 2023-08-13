package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnBack = findViewById<View>(R.id.back_button)
        btnBack.setOnClickListener{
            finish()
        }

        val imgShareApp = findViewById<View>(R.id.shareAppIcon)
        imgShareApp.setOnClickListener {
            shareApp()
        }

        val imgSupport = findViewById<View>(R.id.supportIcon)
        imgSupport.setOnClickListener {
            callSupport()
        }

        val imgUserAgreement = findViewById<View>(R.id.userAgreementIcon)
        imgUserAgreement.setOnClickListener{
            openUserAgreement()
        }

    }

    private fun shareApp(){
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getString(R.string.android_course_url))
        }
        startActivity(intent)
    }

    private fun callSupport() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject_text))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.support_body_text))
        }
        startActivity(intent)
    }

    private fun openUserAgreement(){
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(getString(R.string.user_agreement_url))
        }
        startActivity(intent)
    }
}