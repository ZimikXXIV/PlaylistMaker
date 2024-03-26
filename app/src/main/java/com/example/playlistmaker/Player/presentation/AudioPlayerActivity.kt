package com.example.playlistmaker.Player.presentation

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.Debounce.debounce
import com.example.playlistmaker.Player.domain.model.PlayerConst
import com.example.playlistmaker.Player.domain.model.PlayerConst.TRACK_INFO
import com.example.playlistmaker.Player.domain.model.PlayerState
import com.example.playlistmaker.Player.domain.model.TrackInfo
import com.example.playlistmaker.Player.presentation.mapper.TrackInfoMapper
import com.example.playlistmaker.R
import com.example.playlistmaker.Search.domain.model.Track
import com.example.playlistmaker.Search.presentation.TrackHolder.Companion.dpToPx


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var textViewDuration: TextView
    private lateinit var textViewDurationInfo: TextView
    private lateinit var textViewAlbumInfo: TextView
    private lateinit var textViewYearInfo: TextView
    private lateinit var textViewGenreInfo: TextView
    private lateinit var textViewCountryInfo: TextView
    private lateinit var textViewTrackName: TextView
    private lateinit var textViewArtistName: TextView
    private lateinit var imageViewCoverAlbum: ImageView
    private lateinit var playButton: ImageView

    private val playerControl = Creator.providePlayerInteractor()
    private val searchRunnable = Runnable { setCurrentPosition() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        initPlayer()

        val track = if (SDK_INT >= 33) {
            intent.getSerializableExtra(TRACK_INFO, Track::class.java)!!
        } else {
            intent.getSerializableExtra(TRACK_INFO)!! as Track
        }

        val trackInfo = TrackInfoMapper.map(track)

        showTrackInfo(trackInfo)

        preparePlayer(trackInfo)

    }

    private fun setCurrentPosition() {
        when (playerControl.getPlayerState()) {
            PlayerState.PLAYING -> {
                textViewDuration.text = playerControl.getPositionStr()
            }

            PlayerState.PREPARED -> {
                textViewDuration.text = PlayerConst.DEFAULT_DURATION
            }

            else -> {}
        }
        debounce(searchRunnable, PlayerConst.DURATION_REFRESH_DELAY_MILLIS)
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
        playButton.setImageResource(R.drawable.pause_button_icon)
        debounce(searchRunnable, PlayerConst.DURATION_REFRESH_DELAY_MILLIS)
    }

    private fun pausePlayer() {
        playerControl.pause()
        playButton.setImageResource(R.drawable.play_button_icon)
        debounce(searchRunnable, PlayerConst.DURATION_REFRESH_DELAY_MILLIS)
    }

    private fun preparePlayer(track: TrackInfo) {
        if (track.previewUrl.isNullOrEmpty()) {
            return
        }
        playButton.setOnClickListener {
            playbackControl()
        }

        playerControl.createPlayer(track.previewUrl)
    }

    private fun showTrackInfo(track: TrackInfo) {

        textViewArtistName.text = track.artistName
        textViewTrackName.text = track.trackName
        textViewCountryInfo.text = track.country
        textViewGenreInfo.text = track.primaryGenreName
        textViewYearInfo.text = track.releaseDate.toString().take(4)

        textViewAlbumInfo.text = track.collectionName

        Glide.with(this)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.placeholder_big_icon)
            .transform(RoundedCorners(dpToPx(8f)))
            .centerCrop()
            .into(imageViewCoverAlbum)

        textViewDuration.text = PlayerConst.DEFAULT_DURATION
        textViewDurationInfo.text = track.trackTimeMillis

    }

    private fun initPlayer() {
        textViewDuration = findViewById(R.id.durationTrack)
        textViewDurationInfo = findViewById(R.id.durationTrackInfo)
        textViewAlbumInfo = findViewById(R.id.albumTrackInfo)
        textViewYearInfo = findViewById(R.id.yearTrackInfo)
        textViewGenreInfo = findViewById(R.id.genreTrackInfo)
        textViewCountryInfo = findViewById(R.id.countryTrackInfo)
        textViewTrackName = findViewById(R.id.trackName)
        textViewArtistName = findViewById(R.id.artistName)
        imageViewCoverAlbum = findViewById(R.id.coverAlbum)
        playButton = findViewById(R.id.playBtn)
    }



    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerControl.release()
    }

}