package com.example.playlistmaker

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.TrackHolder.Companion.dpToPx


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


    private fun showTrackInfo(track: Track) {

        textViewArtistName.text = track.artistName
        textViewTrackName.text = track.trackName
        textViewCountryInfo.text = track.country
        textViewGenreInfo.text = track.primaryGenreName
        textViewYearInfo.text = track.releaseDate.toString().take(4)

        textViewAlbumInfo.text = track.collectionName

        Glide.with(this)
            .load(track.getIamge("512"))
            .placeholder(R.drawable.placeholder_big_icon)
            .transform(RoundedCorners(dpToPx(8f)))
            .centerCrop()
            .into(imageViewCoverAlbum)

        textViewDuration.text = track.convertTimeToString("mm:ss")
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


    }

    companion object {
        const val TRACK_INFO = "track_info"
    }
}