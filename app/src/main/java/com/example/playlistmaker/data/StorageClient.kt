package com.example.playlistmaker.data

interface StorageClient {
    fun setData(key: String, value: String)
    fun getData(key: String): String

}