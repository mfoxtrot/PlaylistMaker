package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.FoundTracksInfo
import java.util.concurrent.Executors
import java.util.function.Consumer

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun findTracks(searchString: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute{
            consumer.consume(repository.findTracks(searchString))
        }
    }
}