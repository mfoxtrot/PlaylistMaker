package com.example.playlistmaker.data

interface PlayerClient {
    fun preparePlayer(sourceURI:String, onPreparedListener: ()->Unit, onCompletionListener: ()->Unit)
    fun startPlayer()
    fun pausePlayer()
    fun stopPlayer()
    fun destroyPlayer()
    fun isPlaying():Boolean
}