package com.example.playlistmaker

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import retrofit2.http.DELETE
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var currentTrack: Track
    private var mediaPlayer=MediaPlayer()

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    private var handler: Handler? = null

    private var millsPlayed = 0L

    companion object {
        private const val DELAY = 100L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        handler = Handler(Looper.getMainLooper())

        currentTrack = (application as App).history.lastVisitingTrack()

        binding.tvCountry.text = currentTrack.country
        binding.tvGenre.text = currentTrack.primaryGenreName
        binding.tvYear.text = currentTrack.getReleaseYear()
        binding.tvAlbum.text = currentTrack.collectionName
        binding.tvDuration.text = formatTimeMMSS(currentTrack.trackTimeMillis)
        binding.tvArtistName.text = currentTrack.artistName
        binding.tvTrackName.text = currentTrack.trackName

        binding.grpAlbum.isVisible = currentTrack.hasAlbum()

        Glide.with(binding.root)
            .load(currentTrack.getCoverArtworkURI())
            .placeholder(R.drawable.ic_placeholder_large)
            .transform(CenterInside(),RoundedCorners(8))
            .into(binding.ivArtwork)

        binding.backButton.setOnClickListener {
            finish()
        }

        with(mediaPlayer) {
            setDataSource(currentTrack.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                onPlayerPrepared()
            }
            setOnCompletionListener {
                onPlayerComplete()
            }
        }

        binding.btnPlayPause.setOnClickListener {
            handlePlayerClick()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        binding.btnPlayPause.setImageResource(R.drawable.ic_play_button)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        mediaPlayer.release()
        handler?.removeCallbacksAndMessages(null)
    }

    private fun formatTimeMMSS(timeMillis: Long):String {
        return dateFormat.format(timeMillis)
    }

    private fun handlePlayerClick() {
        if (mediaPlayer.isPlaying) {
            binding.btnPlayPause.setImageResource(R.drawable.ic_play_button)
            handler?.post {mediaPlayer.pause()}
        } else {
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause_button)
            handler?.post { mediaPlayer.start() }
            handler?.post(checkTimer(System.currentTimeMillis()))

        }
    }

    private fun checkTimer(startTime: Long): Runnable{
        return Runnable {
            val currentTime = System.currentTimeMillis()
            millsPlayed += (currentTime - startTime)
            binding.tvTimeCountdown.text = formatTimeMMSS(millsPlayed)
            if (mediaPlayer.isPlaying) {
                handler?.postDelayed(checkTimer(currentTime), DELAY)
            }
        }
    }

    private fun onPlayerPrepared() {
        binding.tvTimeCountdown.text = formatTimeMMSS(millsPlayed)
    }

    private fun onPlayerComplete() {
        millsPlayed = 0L
        binding.tvTimeCountdown.text = formatTimeMMSS(millsPlayed)
        binding.btnPlayPause.setImageResource(R.drawable.ic_play_button)
    }

}