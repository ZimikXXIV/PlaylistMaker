package com.example.playlistmaker.player.ui

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioplayerBinding
import com.example.playlistmaker.medialibrary.domain.PlaylistCard
import com.example.playlistmaker.medialibrary.domain.api.PlaylistSheetClickListenerInterface
import com.example.playlistmaker.medialibrary.ui.adapters.BottomSheetAdapter
import com.example.playlistmaker.player.domain.mapper.TrackInfoMapper
import com.example.playlistmaker.player.domain.model.PlayerConst
import com.example.playlistmaker.player.domain.model.PlayerStatus
import com.example.playlistmaker.player.domain.model.TrackInfo
import com.example.playlistmaker.player.presentation.state.PlayerState
import com.example.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TrackHolder
import com.example.playlistmaker.utils.BindingFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AudioPlayerFragment : BindingFragment<FragmentAudioplayerBinding>(),
    PlaylistSheetClickListenerInterface {



    private lateinit var trackInfo: TrackInfo
    private val playlistCardAdapter = BottomSheetAdapter(this)
    private var playlistList: List<PlaylistCard> = emptyList()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(trackInfo)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAudioplayerBinding {
        return FragmentAudioplayerBinding.inflate(inflater, container, false)
    }

    override fun onClick(playlist: PlaylistCard) {
        viewModel.saveTrackToPlaylist(playlist)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = if (SDK_INT >= 33) {
            arguments?.getSerializable(PlayerConst.TRACK_INFO, Track::class.java)!!
        } else {
            arguments?.getSerializable(PlayerConst.TRACK_INFO)!! as Track
        }

        trackInfo = TrackInfoMapper.map(track)

        showTrackInfo(trackInfo)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)


        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // newState — новое состояние BottomSheet

                binding.overlay.isVisible = newState != BottomSheetBehavior.STATE_HIDDEN

                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        // загружаем рекламный баннер
                        viewModel.fillPlaylist()
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        // загружаем рекламный баннер
                        viewModel.fillPlaylist()
                    }

                    else -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN




        viewModel.getPlayerState().observe(viewLifecycleOwner) { playerState ->
            when (playerState) {
                is PlayerState.Content -> {
                    preparePlayer(playerState.playerSatus, playerState.playerTime)
                }
                is PlayerState.Favorite -> {
                    renderLikeButton(playerState.isFavorite)
                }

                is PlayerState.LoadPlaylist -> renderBottomSheet(playerState)
                is PlayerState.LoadedPlaylist -> renderBottomSheet(playerState)
                is PlayerState.ErrAddToPlaylist -> {
                    Toast.makeText(
                        context,
                        getString(playerState.err_id).format(playerState.playlist_caption),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is PlayerState.AddToPlaylist -> {
                    Toast.makeText(
                        context,
                        getString(playerState.err_id).format(playerState.playlist_caption),
                        Toast.LENGTH_SHORT
                    ).show()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
                else -> {
                    preparePlayer(PlayerStatus.DEFAULT, PlayerConst.DEFAULT_DURATION)
                }
            }
        }

        binding.likeBtn.setOnClickListener {
            viewModel.likePressed()
        }

        binding.playBtn.setOnClickListener {
            viewModel.changeStatus()
        }

        binding.addToPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.btnCreateNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_newPlaylistFragment)
        }

        playlistCardAdapter.setPlaylistList(playlistList)
        binding.recyclerViewPlaylists.layoutManager =
            LinearLayoutManager(binding.recyclerViewPlaylists.context)
        binding.recyclerViewPlaylists.adapter = playlistCardAdapter

        viewModel.checkIsFavorite()


    }

    private fun renderBottomSheet(playerState: PlayerState) {
        binding.layoutPlaylistProgressBar.isVisible = false
        binding.recyclerViewPlaylists.isVisible = false

        when (playerState) {
            is PlayerState.LoadedPlaylist -> {
                binding.recyclerViewPlaylists.isVisible = true
                playlistCardAdapter.setPlaylistList(playerState.playlist)
            }

            is PlayerState.LoadPlaylist -> {
                binding.layoutPlaylistProgressBar.isVisible = true
                playlistCardAdapter.setPlaylistList(emptyList())
            }

            else -> {}
        }

    }

    private fun renderLikeButton(isFavorite: Boolean) {

        if (isFavorite) {
            binding.likeBtn.setImageResource(R.drawable.liked_icon)
        } else {
            binding.likeBtn.setImageResource(R.drawable.like_icon)
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
