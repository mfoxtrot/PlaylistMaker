package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

const val SEARCH_HISTORY_LIST_KEY = "search_history"
const val SEARCH_HISTORY_LIST_SIZE = 10
class SearchHistory(private val prefs: SharedPreferences) {
    var list = arrayListOf<Track>()

    init {
        searchHistoryFromPrefs()
    }

    fun addTrack(track: Track) {

        if (list.contains(track)) {
            list.remove(track)
        }

        list.add(0, track)

        if (list.size > SEARCH_HISTORY_LIST_SIZE) {
            list.dropLast(SEARCH_HISTORY_LIST_SIZE - list.size)
        }

        searchHistoryToPrefs()
    }

    fun clear() {
        list.clear()
    }

    private fun searchHistoryFromPrefs(){
        val json = prefs.getString(SEARCH_HISTORY_LIST_KEY,"")
        if (json != "") {
            list = Gson().fromJson(json, Array<Track>::class.java).toCollection(ArrayList<Track>())
        }
    }

    private fun searchHistoryToPrefs() {
        val json = Gson().toJson(list)
        prefs.edit()
            .putString(SEARCH_HISTORY_LIST_KEY, json)
            .apply()
    }
}