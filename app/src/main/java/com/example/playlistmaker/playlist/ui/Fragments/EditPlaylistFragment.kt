package com.example.playlistmaker.playlist.ui.Fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.playlist.presentation.EditPlaylistViewModel
import com.example.playlistmaker.playlist.ui.State.EditPlaylistState
import com.example.playlistmaker.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : NewPlaylistFragment() {

    private val viewModel by viewModel<EditPlaylistViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = arguments?.getInt(PLAYLIST_ID)

        binding.btnBack.setOnClickListener() {
            findNavController().navigateUp()
        }

        binding.btnCreatePlaylist.text = getString(R.string.save_playlist)

        binding.inputEditTextName.doAfterTextChanged {
            if (binding.inputEditTextName.text.isNullOrEmpty()) {
                binding.btnCreatePlaylist.background.setTint(resources.getColor(R.color.YP_Grey))
                binding.btnCreatePlaylist.isEnabled = false
            } else {
                binding.btnCreatePlaylist.background.setTint(resources.getColor(R.color.YP_Blue))
                binding.btnCreatePlaylist.isEnabled = true
            }
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.saveUri(uri)
                    binding.coverPlaylist.setImageURI(uri)
                }
            }

        binding.coverPlaylist.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })

        binding.btnCreatePlaylist.setOnClickListener {
            viewModel.updateData(
                caption = binding.inputEditTextName.text.toString(),
                description = binding.inputEditTextDescription.text.toString()
            )
        }

        viewModel.getEditPlaylistState().observe(viewLifecycleOwner) { editPlaylistState ->
            when (editPlaylistState) {
                is EditPlaylistState.LoadedPlaylist -> {
                    binding.inputEditTextName.setText(editPlaylistState.playlist.caption)
                    binding.inputEditTextDescription.setText(editPlaylistState.playlist.description)

                    Glide.with(requireContext())
                        .load(editPlaylistState.playlist.coverPath)
                        .placeholder(R.drawable.new_playlist_placeholder)
                        .transform(FitCenter(), RoundedCorners(Utils.dpToPx(8f)))
                        .into(binding.coverPlaylist)
                }

                is EditPlaylistState.SavedPlaylist -> findNavController().navigateUp()
                else -> {}
            }
        }
        viewModel.fillData(playlistId!!)
    }

    companion object {
        fun newInstance() = EditPlaylistFragment()
        const val PLAYLIST_ID = "EDIT_PLAYLIST_ID"
    }

}
