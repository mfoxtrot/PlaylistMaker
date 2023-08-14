package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backButton.setOnClickListener{
            finish()
        }

        binding.shareAppIcon.setOnClickListener {
            shareApp()
        }

        binding.supportIcon.setOnClickListener {
            callSupport()
        }

        binding.userAgreementIcon.setOnClickListener{
            openUserAgreement()
        }

    }

    private fun shareApp(){
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getString(R.string.android_course_url))
            startActivity(this)
        }
    }

    private fun callSupport() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject_text))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.support_body_text))
            startActivity(this)
        }
    }

    private fun openUserAgreement(){
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(getString(R.string.user_agreement_url))
            startActivity(this)
        }
    }
}