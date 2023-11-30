package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.TrackHolder.Companion.dpToPx
import java.text.SimpleDateFormat
import java.util.Locale


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
    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT


    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { setCurrentPosition() }

    private fun setCurrentPosition() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                textViewDuration.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(mediaPlayer.currentPosition)
            }

            PlayerState.STATE_PREPARED -> {
                textViewDuration.text = DEFAULT_DURATION
            }

            else -> {}
        }
        handler.postDelayed(searchRunnable, DURATION_REFRESH_DELAY)
    }

    private fun playbackControl() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            else -> {}
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
        playButton.setImageResource(R.drawable.pause_button_icon)
        handler.postDelayed(searchRunnable, DURATION_REFRESH_DELAY)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
        playButton.setImageResource(R.drawable.play_button_icon)
        handler.removeCallbacks(searchRunnable)
    }

    private fun preparePlayer(track: Track) {
        if (track.previewUrl.isNullOrEmpty()) {
            return
        }
        playButton.setOnClickListener {
            playbackControl()
        }
        with(mediaPlayer) {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playButton.isEnabled = true
                playerState = PlayerState.STATE_PREPARED
            }
            setOnCompletionListener {
                playerState = PlayerState.STATE_PREPARED
                playButton.setImageResource(R.drawable.play_button_icon)
            }
        }

    }

    private fun showTrackInfo(track: Track) {

        textViewArtistName.text = track.artistName
        textViewTrackName.text = track.trackName
        textViewCountryInfo.text = track.country
        textViewGenreInfo.text = track.primaryGenreName
        textViewYearInfo.text = track.releaseDate.toString().take(4)

        textViewAlbumInfo.text = track.collectionName

        Glide.with(this)
            .load(track.getIamgeByResolution("512x512"))
            .placeholder(R.drawable.placeholder_big_icon)
            .transform(RoundedCorners(dpToPx(8f)))
            .centerCrop()
            .into(imageViewCoverAlbum)

        textViewDuration.text = DEFAULT_DURATION
        textViewDurationInfo.text = track.convertTimeToString("mm:ss")

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        initPlayer()

        val trackInfo = if (SDK_INT >= 33) {
            intent.getParcelableExtra(TRACK_INFO, Track::class.java)!!
        } else {
            intent.getParcelableExtra<Track>(TRACK_INFO)!!
        }

        showTrackInfo(trackInfo)

        preparePlayer(trackInfo)

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    enum class PlayerState {
        STATE_DEFAULT, STATE_PREPARED, STATE_PLAYING, STATE_PAUSED
    }

    companion object {
        const val TRACK_INFO = "track_info"
        private const val DEFAULT_DURATION = "00:00"
        private const val DURATION_REFRESH_DELAY = 400L
    }

}