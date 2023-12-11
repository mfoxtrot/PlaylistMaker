package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

const val SEARCH_HISTORY_LIST_KEY = "search_history"
class HistoryRepositoryImpl(private val storageClient: StorageClient): HistoryRepository {
    override fun readHistory(): ArrayList<Track> {
        val result = arrayListOf<Track>()
        val json = storageClient.getData(SEARCH_HISTORY_LIST_KEY)
        if (json=="") return result
        val tracksDto = Gson().fromJson(json, Array<TrackDto>::class.java).toCollection(ArrayList<TrackDto>())
        for (trackDto in tracksDto) {
            result.add(
                Track(
                    trackId = trackDto.trackId,
                    trackName = trackDto.trackName,
                    artistName = trackDto.artistName,
                    trackTimeMillis = trackDto.trackTimeMillis,
                    artworkUrl100 = trackDto.artworkUrl100,
                    collectionName = trackDto.collectionName,
                    releaseDate = trackDto.releaseDate,
                    primaryGenreName = trackDto.primaryGenreName,
                    country = trackDto.country,
                    previewUrl = trackDto.previewUrl
                )
            )
        }

        return result
        }

    override fun writeHistory(history: ArrayList<Track>) {
        val tracksDto = history.map {
            TrackDto(
                trackId = it.trackId,
                trackName = it.trackName,
                artistName = it.artistName,
                trackTimeMillis = it.trackTimeMillis,
                artworkUrl100 = it.artworkUrl100,
                collectionName = it.collectionName.toString(),
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                previewUrl = it.previewUrl
            )
        }
        val json = Gson().toJson(tracksDto)
        storageClient.setData(SEARCH_HISTORY_LIST_KEY, json)

    }

    override fun lastTrack(): Track? {
        val history = readHistory()
        if (history.size==0) return null
        return history[0]
    }
}