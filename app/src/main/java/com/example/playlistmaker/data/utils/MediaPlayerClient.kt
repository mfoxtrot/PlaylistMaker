package com.example.playlistmaker.data.utils

import android.media.MediaPlayer
import com.example.playlistmaker.data.PlayerClient

class MediaPlayerClient(): PlayerClient  {
    private val player = MediaPlayer()

    override fun preparePlayer(
        sourceURI: String,
        onPreparedListener: ()->Unit,
        onCompletionListener: ()->Unit
    ) {
        with(player) {
            setDataSource(sourceURI)
            prepareAsync()
            setOnPreparedListener { onPreparedListener.invoke() }
            setOnCompletionListener { onCompletionListener.invoke() }
        }
    }

    override fun startPlayer() {
        if (!player.isPlaying) player.start()
    }

    override fun pausePlayer() {
        if (player.isPlaying) player.pause()
    }

    override fun stopPlayer() {
        if (player.isPlaying) player.stop()
    }

    override fun destroyPlayer() {
        with(player) {
            if (isPlaying) stop()
            release()
        }
    }

    override fun isPlaying(): Boolean {
        return player.isPlaying
    }
}