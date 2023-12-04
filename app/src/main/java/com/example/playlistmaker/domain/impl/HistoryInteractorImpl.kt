package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.models.Track

const val SEARCH_HISTORY_LIST_SIZE = 10
class HistoryInteractorImpl(private val repository: HistoryRepository): HistoryInteractor {
    override fun readHistory(consumer: HistoryInteractor.ReadHistoryConsumer) {
        consumer.consume(repository.readHistory())
    }

    override fun writeHistory(
        history: ArrayList<Track>,
        consumer: HistoryInteractor.WriteHistoryConsume
    ) {
        repository.writeHistory(history)
        consumer.consume()
    }

    override fun clearHistory(consumer: HistoryInteractor.ClearHistoryConsumer) {
        repository.writeHistory(arrayListOf())
        consumer.consume()
    }

    override fun addTrack(track: Track, consumer: HistoryInteractor.AddTrackConsumer) {
        val history = repository.readHistory()
        if (history.contains(track)) {
            history.remove(track)
        }

        history.add(0, track)

        if (history.size > SEARCH_HISTORY_LIST_SIZE) {
            history.removeAt(SEARCH_HISTORY_LIST_SIZE)
        }
        repository.writeHistory(history)
        consumer.consume(history)
    }

    override fun lastTrack(consumer: HistoryInteractor.LastTrackConsumer) {
        consumer.consume(repository.lastTrack())
    }
}
