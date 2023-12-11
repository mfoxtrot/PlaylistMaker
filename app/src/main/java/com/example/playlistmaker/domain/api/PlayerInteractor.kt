package com.example.playlistmaker.domain.api

interface PlayerInteractor {
    fun preparePlayer(sourceURI: String, onPreparedListener:()->Unit, onCompletionListener:()->Unit)
    fun startPlayer()
    fun pausePlayer()
    fun stopPlayer()
    fun destroyPlayer()
    fun isPlaying(consumer: IsPlayingConsumer)

    interface IsPlayingConsumer{
        fun consume(isPlaying: Boolean)
    }
}