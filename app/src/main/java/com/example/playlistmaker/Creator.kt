package com.example.playlistmaker

import android.content.Context
import android.media.MediaPlayer.OnCompletionListener
import com.example.playlistmaker.data.AppSettingsRepositoryImpl
import com.example.playlistmaker.data.HistoryRepositoryImpl
import com.example.playlistmaker.data.PlayerRepositoryImpl
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.storage.SharedPreferencesStorageClient
import com.example.playlistmaker.data.utils.MediaPlayerClient
import com.example.playlistmaker.domain.api.AppSettingsInteractor
import com.example.playlistmaker.domain.api.AppSettingsRepository
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.impl.AppSettingsInteractorImpl
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository{
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractorImpl(): TracksInteractor{
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getAppSettingsRepository(context: Context): AppSettingsRepository {
        return AppSettingsRepositoryImpl(SharedPreferencesStorageClient(context))
    }

    fun provideAppSettingsInteractorImpl(context: Context): AppSettingsInteractor {
        return AppSettingsInteractorImpl(getAppSettingsRepository(context))
    }

    private fun getHistoryRepository(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(SharedPreferencesStorageClient(context))
    }

    fun provideHistoryInteractorImp(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository(context))
    }

    private fun getPlayerRepository(): PlayerRepository{
        return PlayerRepositoryImpl(MediaPlayerClient())
    }

    fun providePlayerIntercatorImpl(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }
}