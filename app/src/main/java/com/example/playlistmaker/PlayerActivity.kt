package com.example.playlistmaker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var currentTrack: Track

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        currentTrack = (application as App).history.lastVisitingTrack()

        binding.tvCountry.text = currentTrack.country
        binding.tvGenre.text = currentTrack.primaryGenreName
        binding.tvYear.text = currentTrack.getReleaseYear()
        binding.tvAlbum.text = currentTrack.collectionName
        binding.tvDuration.text = formatTimeMMSS(currentTrack.trackTimeMillis)
        binding.tvTimeCountdown.text = formatTimeMMSS(currentTrack.trackTimeMillis)
        binding.tvArtistName.text = currentTrack.artistName
        binding.tvTrackName.text = currentTrack.trackName

        binding.grpAlbum.isVisible = currentTrack.hasAlbum()

        Glide.with(binding.root)
            .load(currentTrack.getCoverArtworkURI())
            .placeholder(R.drawable.ic_placeholder_large)
            .centerInside()
            .into(binding.ivArtwork)

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun formatTimeMMSS(timeMillis: Long):String {
        return dateFormat.format(timeMillis)
    }

}