package com.example.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import com.example.playlistmaker.player.domain.mapper.TrackInfoMapper
import com.example.playlistmaker.player.domain.model.PlayerConst
import com.example.playlistmaker.player.domain.model.PlayerStatus
import com.example.playlistmaker.player.presentation.model.TrackInfo
import com.example.playlistmaker.player.presentation.state.PlayerState
import com.example.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TrackHolder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AudioPlayerActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAudioplayerBinding.inflate(layoutInflater)
    }

    private lateinit var trackInfo: TrackInfo

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(trackInfo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        val track = if (Build.VERSION.SDK_INT >= 33) {
            intent.getSerializableExtra(PlayerConst.TRACK_INFO, Track::class.java)!!
        } else {
            intent.getSerializableExtra(PlayerConst.TRACK_INFO)!! as Track
        }
        trackInfo = TrackInfoMapper.map(track)

        showTrackInfo(trackInfo)


        /*
        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(trackInfo)
        )[PlayerViewModel::class.java]*/

        viewModel.getPlayerState().observe(this) { playerState ->
            when (playerState) {
                is PlayerState.Content -> {
                    preparePlayer(playerState.playerSatus, playerState.playerTime)
                }

                else -> {
                    preparePlayer(PlayerStatus.DEFAULT, PlayerConst.DEFAULT_DURATION)
                }
            }
        }

        binding.playBtn.setOnClickListener {
            viewModel.changeStatus()
        }

    }

    private fun preparePlayer(playerStatus: PlayerStatus, playerTime: String) {
        when (playerStatus) {
            PlayerStatus.PLAYING -> {
                binding.playBtn.setImageResource(R.drawable.pause_button_icon)
            }

            PlayerStatus.PAUSED, PlayerStatus.PREPARED, PlayerStatus.DEFAULT -> {
                binding.playBtn.setImageResource(R.drawable.play_button_icon)
            }

            else -> {}
        }

        binding.durationTrack.text = playerTime

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

        Glide.with(this)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.placeholder_big_icon)
            .transform(RoundedCorners(TrackHolder.dpToPx(8f)))
            //.fitCenter()//.centerCrop()
            .into(binding.coverAlbum)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

}