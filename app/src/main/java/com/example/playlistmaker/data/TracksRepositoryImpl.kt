package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.FoundTracksInfo
import com.example.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient): TracksRepository {
    override fun findTracks(searchString: String): FoundTracksInfo {
        try {
            val response = networkClient.doRequest(TracksSearchRequest(searchString))
            if (response.resultCode == 200) {
                return FoundTracksInfo(
                    isSuccess = true,
                    data = (response as TracksSearchResponse).results.map {
                        Track(
                            trackId = it.trackId,
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTimeMillis = it.trackTimeMillis,
                            artworkUrl100 = it.artworkUrl100,
                            collectionName = it.collectionName,
                            releaseDate = it.releaseDate,
                            primaryGenreName = it.primaryGenreName,
                            country = it.country,
                            previewUrl = it.previewUrl
                        )
                    })
            } else {
                return FoundTracksInfo(
                    isSuccess = false,
                    data = emptyList()
                )
            }
        } catch (e: Exception ) {
            return FoundTracksInfo(
                isSuccess = false,
                data = emptyList()
            )
        }

    }
}