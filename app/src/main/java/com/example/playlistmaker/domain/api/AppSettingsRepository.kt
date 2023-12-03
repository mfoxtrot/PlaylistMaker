package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.AppSettings

interface AppSettingsRepository {
    fun setNewSettings(newSettings: AppSettings)
    fun getCurrentSettings(): AppSettings
}