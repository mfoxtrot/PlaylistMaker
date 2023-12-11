package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import java.util.concurrent.Executors

class PlayerInteractorImpl(private val playerRepository: PlayerRepository): PlayerInteractor {
    private val executor= Executors.newCachedThreadPool()
    override fun preparePlayer(
        sourceURI: String,
        onPreparedListener: ()->Unit,
        onCompletionListener: ()->Unit
    ) {
        playerRepository.preparePlayer(sourceURI = sourceURI, onPreparedListener = onPreparedListener, onCompletionListener = onCompletionListener)
    }

    override fun startPlayer() {
        executor.execute {
            playerRepository.startPlayer()
        }
    }

    override fun pausePlayer() {
        executor.execute {
            playerRepository.pausePlayer()
        }
    }

    override fun stopPlayer() {
        executor.execute {
            playerRepository.stopPlayer()
        }
    }

    override fun destroyPlayer() {
        executor.execute {
            playerRepository.destroyPlayer()
        }
    }

    override fun isPlaying(consumer: PlayerInteractor.IsPlayingConsumer) {
        executor.execute {
            consumer.consume(playerRepository.isPlaying())
        }
    }
}