package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val DARK_THEME_KEY = "dark_theme"
class App: Application() {
    private var darkTheme = false
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var history: SearchHistory
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(DARK_THEME_KEY, false)
        switchTheme(darkTheme)

        history = SearchHistory(sharedPreferences)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        sharedPreferences.edit()
            .putBoolean(DARK_THEME_KEY, darkTheme)
            .apply()
    }

    fun isDarkThemeSwitchedOn(): Boolean {
        return darkTheme
    }
}