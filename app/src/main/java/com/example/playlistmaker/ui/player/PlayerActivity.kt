package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.App
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var currentTrack: Track
    private lateinit var historyInteractorImpl: HistoryInteractor
    private lateinit var playerInteractorImpl: PlayerInteractor

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    private val handler = Handler(Looper.getMainLooper())

    private var millsPlayed = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        historyInteractorImpl = Creator.provideHistoryInteractorImp(application as App)
        historyInteractorImpl.lastTrack(
            consumer = object : HistoryInteractor.LastTrackConsumer{
                override fun consume(lastTrack: Track?) {
                    if (lastTrack == null) finish()
                    else {
                        currentTrack = lastTrack
                    }
                }
            }
        )

        playerInteractorImpl = Creator.providePlayerIntercatorImpl()

        playerInteractorImpl.preparePlayer(
                currentTrack.previewUrl,
                ::onPlayerPrepared,
                ::onPlayerComplete
        )

        with(binding) {
            tvCountry.text = currentTrack.country
            tvGenre.text = currentTrack.primaryGenreName
            tvYear.text = currentTrack.getReleaseYear()
            tvAlbum.text = currentTrack.collectionName
            tvDuration.text = formatTimeMMSS(currentTrack.trackTimeMillis)
            tvArtistName.text = currentTrack.artistName
            tvTrackName.text = currentTrack.trackName
            grpAlbum.isVisible = currentTrack.hasAlbum()
            backButton.setOnClickListener {
                finish()
            }
            btnPlayPause.setOnClickListener {
                handlePlayerClick()
            }
        }

        Glide.with(binding.root)
            .load(currentTrack.getCoverArtworkURI())
            .placeholder(R.drawable.ic_placeholder_large)
            .transform(CenterInside(),RoundedCorners(8))
            .into(binding.ivArtwork)
    }

    override fun onStop() {
        super.onStop()
        val runnable = Runnable {
            playerInteractorImpl.isPlaying(
                consumer = object : PlayerInteractor.IsPlayingConsumer {
                    override fun consume(isPlaying: Boolean) {
                        if (isPlaying) {
                            handler.post { playerInteractorImpl.pausePlayer() }
                            binding.btnPlayPause.setImageResource(R.drawable.ic_play_button)
                        }
                    }
                }
            )
        }
        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacksAndMessages(null)
        handler.post { playerInteractorImpl.destroyPlayer() }
    }

    private fun formatTimeMMSS(timeMillis: Long):String {
        return dateFormat.format(timeMillis)
    }

    private fun handlePlayerClick() {
        val runnable = Runnable {
            playerInteractorImpl.isPlaying(
                consumer = object: PlayerInteractor.IsPlayingConsumer {
                    override fun consume(isPlaying: Boolean) {
                        if (isPlaying)  {
                            binding.btnPlayPause.setImageResource(R.drawable.ic_play_button)
                            playerInteractorImpl.pausePlayer()
                        } else {
                            binding.btnPlayPause.setImageResource(R.drawable.ic_pause_button)
                            playerInteractorImpl.startPlayer()
                            handler.post(checkTimer(System.currentTimeMillis()))
                        }
                    }
                }
            )
        }
        handler.post(runnable)
    }

    private fun checkTimer(startTime: Long): Runnable{
        return Runnable {
            val currentTime = System.currentTimeMillis()
            millsPlayed += (currentTime - startTime)
            binding.tvTimeCountdown.text = formatTimeMMSS(millsPlayed)
            val runnable = Runnable {
                playerInteractorImpl.isPlaying(
                    consumer = object : PlayerInteractor.IsPlayingConsumer {
                        override fun consume(isPlaying: Boolean) {
                            if (isPlaying) handler.postDelayed(checkTimer(currentTime), DELAY)
                        }
                    }
                )
        }
        handler.post(runnable)
    }}

    private fun onPlayerPrepared() {
        binding.tvTimeCountdown.text = formatTimeMMSS(millsPlayed)
    }

    private fun onPlayerComplete() {
        millsPlayed = 0L
        with (binding) {
            tvTimeCountdown.text = formatTimeMMSS(millsPlayed)
            btnPlayPause.setImageResource(R.drawable.ic_play_button)
        }
    }

    companion object {
        private const val DELAY = 100L
    }
}