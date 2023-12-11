package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.AppSettingsInteractor
import com.example.playlistmaker.domain.models.AppSettings

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
class App: Application() {
    private lateinit var sharedPreferences: SharedPreferences

    private val handler = Handler(Looper.getMainLooper())
    lateinit var currentAppSettings:AppSettings
    private lateinit var appSettingsInteractorImp: AppSettingsInteractor

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

        appSettingsInteractorImp = Creator.provideAppSettingsInteractorImpl(applicationContext)
        appSettingsInteractorImp.getCurrentSettings(
            consumer = object : AppSettingsInteractor.GetSettingsConsumer {
                override fun consume(currentSettings: AppSettings) {
                    currentAppSettings = currentSettings
                    val runnable = Runnable {
                        setDarkTheme(currentSettings.isDarkTheme)
                    }
                    handler.post(runnable)
                }
            }
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        currentAppSettings.isDarkTheme = darkThemeEnabled
        appSettingsInteractorImp.setNewSettings(
            newSettings = currentAppSettings,
            consumer = object: AppSettingsInteractor.SetSettingsConsumer {
                override fun consume() {
                    val runnable = Runnable {
                        setDarkTheme(darkThemeEnabled)
                    }
                    handler.post(runnable)
                }
            }
        )
    }

    private fun setDarkTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}