package com.example.playlistmaker.domain.api

interface PlayerRepository {
    fun preparePlayer(sourceURI: String, onPreparedListener: ()->Unit, onCompletionListener: ()->Unit)
    fun startPlayer()
    fun stopPlayer()
    fun pausePlayer()
    fun destroyPlayer()
    fun isPlaying():Boolean
}