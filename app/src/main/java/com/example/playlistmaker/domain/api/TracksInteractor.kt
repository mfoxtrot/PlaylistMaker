package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.FoundTracksInfo
import java.util.function.Consumer

interface TracksInteractor {
    fun findTracks(searchString: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: FoundTracksInfo)
    }
}