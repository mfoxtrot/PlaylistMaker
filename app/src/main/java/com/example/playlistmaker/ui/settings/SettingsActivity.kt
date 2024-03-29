package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.models.AppSettings

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var currentAppSettings: AppSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentAppSettings = (application as App).currentAppSettings
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.switchDarkTheme.isChecked = currentAppSettings.isDarkTheme

        binding.backButton.setOnClickListener{
            finish()
        }

        binding.switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            (application as App).switchTheme(isChecked)
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
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getString(R.string.android_course_url))
            startActivity(this)
        }
    }

    private fun callSupport() {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject_text))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.support_body_text))
            startActivity(this)
        }
    }

    private fun openUserAgreement(){
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(getString(R.string.user_agreement_url))
            startActivity(this)
        }
    }
}