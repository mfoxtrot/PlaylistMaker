package com.example.playlistmaker

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.databinding.TrackCardBinding

class TracksViewHolder(private val trackCardBinding: TrackCardBinding): RecyclerView.ViewHolder(trackCardBinding.root) {
    fun bind(track: Track) {
        trackCardBinding.trackName.text = track.trackName
        trackCardBinding.artistName.text = track.artistName
        trackCardBinding.trackTime.text = track.trackTime

        Glide.with(trackCardBinding.root)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .into(trackCardBinding.artWorkUrl)
    }
}