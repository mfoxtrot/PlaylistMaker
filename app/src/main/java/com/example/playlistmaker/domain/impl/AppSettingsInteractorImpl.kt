package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.AppSettingsInteractor
import com.example.playlistmaker.domain.api.AppSettingsRepository
import com.example.playlistmaker.domain.models.AppSettings
import java.util.concurrent.Executors

class AppSettingsInteractorImpl(private val repository: AppSettingsRepository): AppSettingsInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun setNewSettings(
        newSettings: AppSettings,
        consumer: AppSettingsInteractor.SetSettingsConsumer
    ) {
        executor.execute {
            repository.setNewSettings(newSettings)
            consumer.consume()
        }
    }

    override fun getCurrentSettings(
        consumer: AppSettingsInteractor.GetSettingsConsumer
    ) {
        executor.execute {
            consumer.consume(repository.getCurrentSettings())
        }
    }
}