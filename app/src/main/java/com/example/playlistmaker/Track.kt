package com.example.playlistmaker

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String? = null,
    val releaseDate: String? = null,
    val primaryGenreName: String,
    val country: String
) {
    fun getCoverArtworkURI() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    fun getReleaseYear() = if (releaseDate.isNullOrEmpty()) "" else releaseDate.substring(0, 4)

    fun hasAlbum() = !collectionName.isNullOrEmpty()
}
