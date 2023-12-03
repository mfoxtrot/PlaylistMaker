package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.AppSettingsRepositoryImpl
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.storage.SharedPreferencesStorageClient
import com.example.playlistmaker.domain.api.AppSettingsInteractor
import com.example.playlistmaker.domain.api.AppSettingsRepository
import com.example.playlistmaker.domain.impl.AppSettingsInteractorImpl
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository{
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractorImpl(): TracksInteractor{
        return TracksInteractorImpl(getTracksRepository())
    }

    fun getAppSettingsRepository(context: Context): AppSettingsRepository {
        return AppSettingsRepositoryImpl(SharedPreferencesStorageClient(context))
    }

    fun provideAppSettingsInteractorImpl(context: Context): AppSettingsInteractor {
        return AppSettingsInteractorImpl(getAppSettingsRepository(context))
    }
}