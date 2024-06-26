package com.example.playlistmaker.playlist.ui.Fragments

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentViewPlaylistBinding
import com.example.playlistmaker.medialibrary.ui.adapters.BottomSheetAdapter
import com.example.playlistmaker.player.domain.mapper.TrackInfoMapper
import com.example.playlistmaker.player.domain.model.PlayerConst
import com.example.playlistmaker.player.domain.model.TrackInfo
import com.example.playlistmaker.playlist.presentation.ViewPlaylistViewModel
import com.example.playlistmaker.playlist.ui.State.ViewPlaylistState
import com.example.playlistmaker.playlist.ui.ViewPlaylistAdapter
import com.example.playlistmaker.utils.BindingFragment
import com.example.playlistmaker.utils.Utils
import com.example.playlistmaker.utils.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class ViewPlaylistFragment : BindingFragment<FragmentViewPlaylistBinding>() {

    private val viewModel by viewModel<ViewPlaylistViewModel>()
    private val trackAdapter = ViewPlaylistAdapter(::deleteTrackWithAlert, ::openTrack)
    private val playlistCardAdapter = BottomSheetAdapter(null)
    private lateinit var bottomSheetBehaviorList: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehaviorMenu: BottomSheetBehavior<LinearLayout>


    private lateinit var openTrackDebounce: (TrackInfo) -> Unit

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentViewPlaylistBinding {
        return FragmentViewPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = if (SDK_INT >= 33) {
            arguments?.getInt(PLAYLIST_ID)!!
        } else {
            arguments?.getInt(PLAYLIST_ID)!!
        }

        viewModel.savePlaylistId(playlistId)



        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })

        binding.btnShare.setOnClickListener {
            sharePlaylist()
        }


        trackAdapter.setTrackList(emptyList())

        binding.recyclerListTracks.layoutManager =
            LinearLayoutManager(binding.recyclerListTracks.context)
        binding.recyclerListTracks.adapter = trackAdapter

        playlistCardAdapter.setPlaylistList(emptyList())

        binding.recyclerPlaylist.layoutManager =
            LinearLayoutManager(binding.recyclerPlaylist.context)
        binding.recyclerPlaylist.adapter = playlistCardAdapter

        viewModel.getViewPlaylistState().observe(viewLifecycleOwner) { viewPlaylistState ->
            when (viewPlaylistState) {
                is ViewPlaylistState.LoadedData -> {
                    val minutes =
                        TimeUnit.MILLISECONDS.toMinutes(viewPlaylistState.playlistCard.durationTrack)

                    binding.playlistCaption.text = viewPlaylistState.playlistCard.caption
                    binding.playlistDescription.text = viewPlaylistState.playlistCard.description

                    binding.playlistTrackCount.text = resources.getQuantityString(
                        R.plurals.tracks_plurals,
                        viewPlaylistState.playlistCard.countTrack,
                        viewPlaylistState.playlistCard.countTrack
                    )

                    binding.playlistDuration.text = resources.getQuantityString(
                        R.plurals.duration_plurals,
                        minutes.toInt(),
                        minutes
                    )

                    Glide.with(requireContext())
                        .load(viewPlaylistState.playlistCard.coverImg)
                        .placeholder(R.drawable.placeholder_ico)
                        .transform(FitCenter())
                        .into(binding.coverPlaylist)

                    trackAdapter.setTrackList(viewPlaylistState.playlistCard.trackList)
                    playlistCardAdapter.setPlaylistList(listOf(viewPlaylistState.playlistCard))

                }

                is ViewPlaylistState.DeletedPlaylist -> {
                    findNavController().navigateUp()
                }
            }
        }

        viewModel.fillData()

        bottomSheetBehaviorList = BottomSheetBehavior.from(binding.standardBottomSheet)

        bottomSheetBehaviorList.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset > 0) {
                    binding.overlay.isVisible = true
                    binding.overlay.alpha = slideOffset
                } else binding.overlay.isVisible = false
            }
        })

        binding.btnMenu.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.btnMenuShare.setOnClickListener {
            if (viewModel.getSavedPlaylistInfo().trackList.isEmpty()) {
                Toast.makeText(
                    context,
                    getString(R.string.empty_playlist_message_alert).format(
                        viewModel.getSavedPlaylistInfo().caption
                    ),
                    Toast.LENGTH_SHORT
                ).show()

                bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN

            } else {
                sharePlaylist()
            }


        }

        binding.btnMenuEdit.setOnClickListener {
            val args = Bundle()
            args.putInt(EditPlaylistFragment.PLAYLIST_ID, viewModel.getSavedPlaylistInfo().id)
            findNavController().navigate(
                R.id.action_viewPlaylistFragment_to_editPlaylistFragment,
                args
            )
        }

        binding.btnMenuDelete.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(R.string.empty_string)
                .setMessage(getString(R.string.delete_playlist).format(viewModel.getSavedPlaylistInfo().caption))
                .setNegativeButton(R.string.delete_track_cancel_alert) { _, _ -> }
                .setPositiveButton(R.string.delete_track_confirm_alert) { _, _ ->
                    viewModel.deletePlaylist(viewModel.getSavedPlaylistInfo())
                }
                .show()
        }

        bottomSheetBehaviorMenu = BottomSheetBehavior.from(binding.standardBottomSheetMenu)

        bottomSheetBehaviorMenu.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.overlay.isVisible = newState != BottomSheetBehavior.STATE_HIDDEN
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN

        openTrackDebounce = debounce<TrackInfo>(
            Utils.CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false
        ) { track ->
            openPlayer(track)
        }

    }

    private fun sharePlaylist() {
        if (viewModel.getSavedPlaylistInfo().countTrack == 0) {
            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(R.string.empty_string)
                .setMessage(R.string.empty_track_message_alert)
                .setPositiveButton(R.string.empty_track_confirm_alert) { _, _ -> }
                .show()
        } else {
            viewModel.shareString(getShareString())
        }
    }

    private fun getShareString(): String {
        val savedPlaylist = viewModel.getSavedPlaylistInfo()
        var msg = getString(R.string.share_playlist_name).format(savedPlaylist.caption)
        msg += getString(R.string.share_playlist_description).format(savedPlaylist.description)
        msg += resources.getQuantityString(
            R.plurals.tracks_plurals,
            savedPlaylist.countTrack,
            savedPlaylist.countTrack
        )
        var trackIdx = 0
        savedPlaylist.trackList.forEach { track ->
            msg += getString(R.string.share_playlist_tracks).format(
                trackIdx++,
                track.artistName,
                track.trackName,
                track.trackTimeMillisStr
            )
        }
        return msg
    }

    private fun deleteTrackWithAlert(track: TrackInfo) {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(R.string.delete_track_alert)
            .setMessage(R.string.empty_string)
            .setNegativeButton(R.string.delete_track_cancel_alert) { _, _ -> }
            .setPositiveButton(R.string.delete_track_confirm_alert) { _, _ ->
                viewModel.deleteTrack(
                    viewModel.getSavedPlaylistInfo(),
                    track
                )
            }
            .show()
    }

    private fun openTrack(track: TrackInfo) {
        openTrackDebounce(track)
    }

    fun openPlayer(track: TrackInfo) {
        val args = Bundle()
        args.putSerializable(PlayerConst.TRACK_INFO, TrackInfoMapper.map(track))
        findNavController().navigate(R.id.action_viewPlaylistFragment_to_audioPlayerFragment2, args)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    companion object {
        const val PLAYLIST_ID = "PLAYLIST_ID"
    }
}