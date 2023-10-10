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
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity() {

    lateinit var textViewDuration: TextView
    lateinit var textViewDurationInfo: TextView
    lateinit var textViewAlbumInfo: TextView
    lateinit var textViewYearInfo: TextView
    lateinit var textViewGenreInfo: TextView
    lateinit var textViewCountryInfo: TextView
    lateinit var textViewTrackName: TextView
    lateinit var textViewArtistName: TextView
    lateinit var imageViewCoverAlbum: ImageView


    fun showTrackInfo(track: Track) {

        textViewArtistName.text = track.artistName
        textViewTrackName.text = track.trackName
        textViewCountryInfo.text = track.country
        textViewGenreInfo.text = track.primaryGenreName
        textViewYearInfo.text = track.releaseDate.toString().take(4)

        textViewAlbumInfo.text = track.collectionName

        Glide.with(this)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_big_icon)
            .transform(RoundedCorners(dpToPx(8f)))
            .centerCrop()
            .into(imageViewCoverAlbum)

        textViewDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis!!.toLong())
        textViewDurationInfo.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis!!.toLong())

    }

    fun initPlayer() {
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