package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

const val SEARCH_HISTORY_LIST_KEY = "search_history"
const val SEARCH_HISTORY_LIST_SIZE = 10
class SearchHistory(private val prefs: SharedPreferences) {
    private val list = arrayListOf<Track>()

    init {
        list.addAll(searchHistoryFromPrefs())
    }

    fun addTrack(track: Track) {

        if (list.contains(track)) {
            list.remove(track)
        }

        list.add(0, track)

        if (list.size > SEARCH_HISTORY_LIST_SIZE) {
            list.removeAt(SEARCH_HISTORY_LIST_SIZE)
        }

        searchHistoryToPrefs()
    }

    fun clear() {
        list.clear()
        searchHistoryToPrefs()
    }

    fun allTracks():ArrayList<Track> {
        return list
    }

    fun lastVisitingTrack(): Track {
        return list[0]
    }

    private fun searchHistoryFromPrefs():ArrayList<Track>{
        val json = prefs.getString(SEARCH_HISTORY_LIST_KEY,"")
        if (json == "") return arrayListOf()
        return Gson().fromJson(json, Array<Track>::class.java).toCollection(ArrayList<Track>())

    }

    private fun searchHistoryToPrefs() {
        val json = Gson().toJson(list)
        prefs.edit()
            .putString(SEARCH_HISTORY_LIST_KEY, json)
            .apply()
    }
}