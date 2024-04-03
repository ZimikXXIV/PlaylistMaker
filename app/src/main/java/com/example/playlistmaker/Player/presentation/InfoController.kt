package com.example.playlistmaker.Player.presentation

import android.app.Activity
import android.os.Build
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.Debounce
import com.example.playlistmaker.Player.domain.model.PlayerConst
import com.example.playlistmaker.Player.domain.model.PlayerState
import com.example.playlistmaker.Player.presentation.mapper.TrackInfoMapper
import com.example.playlistmaker.Player.presentation.model.TrackInfo
import com.example.playlistmaker.R
import com.example.playlistmaker.Search.domain.model.Track
import com.example.playlistmaker.Search.presentation.TrackHolder
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding

class InfoController(private val activity: Activity) {
    private val binding by lazy {
        ActivityAudioplayerBinding.inflate(activity.layoutInflater)
    }

    private val playerControl = Creator.getPlayerInteractor()
    private val setPositionRunnable = Runnable { setCurrentPosition() }

    fun onCreate(savedInstanceState: Bundle?) {
        activity.setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            activity.finish()
        }

        val track = if (Build.VERSION.SDK_INT >= 33) {
            activity.intent.getSerializableExtra(PlayerConst.TRACK_INFO, Track::class.java)!!
        } else {
            activity.intent.getSerializableExtra(PlayerConst.TRACK_INFO)!! as Track
        }

        val trackInfo = TrackInfoMapper.map(track)

        showTrackInfo(trackInfo)

        preparePlayer(trackInfo)

    }

    private fun setCurrentPosition() {
        when (playerControl.getPlayerState()) {
            PlayerState.PLAYING -> {
                binding.durationTrack.text = playerControl.getPositionStr()
            }

            PlayerState.PREPARED -> {
                binding.durationTrack.text = PlayerConst.DEFAULT_DURATION
            }

            else -> {}
        }
        Debounce.debounce(setPositionRunnable, PlayerConst.DURATION_REFRESH_DELAY_MILLIS)
    }

    private fun playbackControl() {
        when (playerControl.getPlayerState()) {
            PlayerState.PLAYING -> {
                pausePlayer()
            }

            PlayerState.PREPARED, PlayerState.PAUSED -> {
                startPlayer()
            }

            else -> {}
        }
    }

    private fun startPlayer() {
        playerControl.play()
        binding.playBtn.setImageResource(R.drawable.pause_button_icon)
        Debounce.debounce(setPositionRunnable, PlayerConst.DURATION_REFRESH_DELAY_MILLIS)
    }

    private fun pausePlayer() {
        playerControl.pause()
        binding.playBtn.setImageResource(R.drawable.play_button_icon)
        Debounce.debounce(setPositionRunnable, PlayerConst.DURATION_REFRESH_DELAY_MILLIS)
    }

    private fun preparePlayer(track: TrackInfo) {
        if (track.previewUrl.isNullOrEmpty()) {
            return
        }
        binding.playBtn.setOnClickListener {
            playbackControl()
        }

        playerControl.createPlayer(track.previewUrl)
    }

    private fun showTrackInfo(track: TrackInfo) {
        binding.artistName.text = track.artistName
        binding.trackName.text = track.trackName
        binding.countryTrackInfo.text = track.country
        binding.genreTrackInfo.text = track.primaryGenreName
        binding.yearTrackInfo.text = track.releaseDate.toString().take(4)
        binding.albumTrackInfo.text = track.collectionName
        binding.durationTrack.text = PlayerConst.DEFAULT_DURATION
        binding.durationTrackInfo.text = track.trackTimeMillis

        Glide.with(activity)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.placeholder_big_icon)
            .transform(RoundedCorners(TrackHolder.dpToPx(8f)))
            .centerCrop()
            .into(binding.coverAlbum)
    }

    fun onPause() {
        pausePlayer()
    }

    fun onDestroy() {
        Debounce.removeCallbacks(setPositionRunnable)
        playerControl.release()
    }
}