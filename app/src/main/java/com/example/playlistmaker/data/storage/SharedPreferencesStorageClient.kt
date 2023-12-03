package com.example.playlistmaker.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.StorageClient

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"

class SharedPreferencesStorageClient(context: Context): StorageClient {
    private var sharedPreferences: SharedPreferences
    init {
        sharedPreferences = context.getSharedPreferences(
            PLAYLIST_MAKER_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    override fun setData(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    override fun getData(key: String): String {
        return sharedPreferences.getString(key,"").toString()
    }
}