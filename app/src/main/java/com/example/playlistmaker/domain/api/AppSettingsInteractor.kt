package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.AppSettings

interface AppSettingsInteractor {
    fun setNewSettings(newSettings: AppSettings, consumer: SetSettingsConsumer)
    fun getCurrentSettings(consumer: GetSettingsConsumer)
    interface SetSettingsConsumer{
        fun consume()
    }
    interface GetSettingsConsumer {
        fun consume(currentSettings: AppSettings)
    }
}
