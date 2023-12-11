package com.example.playlistmaker.data

import com.example.playlistmaker.domain.api.PlayerRepository

class PlayerRepositoryImpl(private val playerClient: PlayerClient): PlayerRepository {
    override fun preparePlayer(sourceURI:String, onPreparedListener:()->Unit, onCompletionListener:()->Unit) {
        playerClient.preparePlayer(sourceURI = sourceURI, onPreparedListener = onPreparedListener, onCompletionListener = onCompletionListener)
    }

    override fun startPlayer() {
        playerClient.startPlayer()
    }

    override fun stopPlayer() {
        playerClient.stopPlayer()
    }

    override fun pausePlayer() {
        playerClient.pausePlayer()
    }

    override fun destroyPlayer() {
        playerClient.destroyPlayer()
    }

    override fun isPlaying(): Boolean {
        return playerClient.isPlaying()
    }
}