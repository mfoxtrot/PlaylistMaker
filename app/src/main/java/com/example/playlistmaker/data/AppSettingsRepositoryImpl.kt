package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.AppSettingsDto
import com.example.playlistmaker.domain.api.AppSettingsRepository
import com.example.playlistmaker.domain.models.AppSettings
import com.google.gson.Gson

const val PLAYLIST_MAKER_PREFERENCES = "application_settings"
class AppSettingsRepositoryImpl(private val storageClient: StorageClient): AppSettingsRepository {
    override fun setNewSettings(newSettings: AppSettings) {
        val newSettingsDto = AppSettingsDto(
            isDarkTheme = newSettings.isDarkTheme
        )
        val json = Gson().toJson(newSettingsDto)
        storageClient.setData(PLAYLIST_MAKER_PREFERENCES, json)
    }

    override fun getCurrentSettings(): AppSettings {
        val json = storageClient.getData(PLAYLIST_MAKER_PREFERENCES)
        if (json == "") return AppSettings()
        val settings = Gson().fromJson(json, AppSettingsDto::class.java)
        return AppSettings(
            isDarkTheme = settings.isDarkTheme
        )
    }

}