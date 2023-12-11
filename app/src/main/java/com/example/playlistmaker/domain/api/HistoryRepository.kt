package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface HistoryRepository {
    fun readHistory(): ArrayList<Track>
    fun writeHistory(history: ArrayList<Track>)
    fun lastTrack(): Track?
}