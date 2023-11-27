package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.FoundTracksInfo

interface TracksRepository {
    fun findTracks(searchString: String): FoundTracksInfo
}