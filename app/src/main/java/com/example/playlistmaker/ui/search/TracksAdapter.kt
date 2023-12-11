package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackCardBinding
import com.example.playlistmaker.domain.models.Track

class TracksAdapter(
    private val tracks: ArrayList<Track>,
    private val clickOnTrackListener: (Track)-> Unit
): RecyclerView.Adapter<TracksViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val trackCardBinding = TrackCardBinding.inflate(layoutInflater, parent, false)
        return TracksViewHolder(trackCardBinding)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { clickOnTrackListener(tracks[position]) }
    }
}