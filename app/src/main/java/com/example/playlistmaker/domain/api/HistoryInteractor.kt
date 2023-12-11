package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface HistoryInteractor {
    fun readHistory(consumer: ReadHistoryConsumer)
    fun writeHistory(history: ArrayList<Track>, consumer: WriteHistoryConsume)
    fun clearHistory(consumer: ClearHistoryConsumer)
    fun addTrack(track: Track, consumer: AddTrackConsumer)
    fun lastTrack(consumer: LastTrackConsumer)
    interface ReadHistoryConsumer{
        fun consume(history: ArrayList<Track>)
    }
    interface WriteHistoryConsume{
        fun consume()
    }
    interface ClearHistoryConsumer{
        fun consume()
    }
    interface AddTrackConsumer{
        fun consume(history: ArrayList<Track>)
    }
    interface LastTrackConsumer{
        fun consume(lastTrack: Track?)
    }
}