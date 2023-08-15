package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackCardBinding

class TracksAdapter(
    private val tracks: ArrayList<Track>
): RecyclerView.Adapter<TracksViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val trackCardBinding = TrackCardBinding.inflate(layoutInflater, parent, false)
        return TracksViewHolder(trackCardBinding)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

}