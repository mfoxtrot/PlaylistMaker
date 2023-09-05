package com.example.playlistmaker

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.databinding.TrackCardBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TracksViewHolder(private val trackCardBinding: TrackCardBinding): RecyclerView.ViewHolder(trackCardBinding.root) {
    fun bind(track: Track) {
        trackCardBinding.trackName.text = track.trackName
        trackCardBinding.artistName.text = track.artistName
        trackCardBinding.trackTime.text = formatTime(track.trackTimeMillis)

        Glide.with(trackCardBinding.root)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .into(trackCardBinding.artWorkUrl)
    }

    private fun formatTime(timeMillis: Long):String {
        return SimpleDateFormat("m:ss", Locale.getDefault()).format(timeMillis)
    }
}