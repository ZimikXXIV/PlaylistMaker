package com.example.playlistmaker.playlist.ui.Fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentEditPlaylistBinding
import com.example.playlistmaker.playlist.presentation.EditPlaylistViewModel
import com.example.playlistmaker.playlist.ui.State.EditPlaylistState
import com.example.playlistmaker.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : BindingFragment<FragmentEditPlaylistBinding>() {

    private val viewModel by viewModel<EditPlaylistViewModel>()
    private var isSelectedImg: Boolean = false

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditPlaylistBinding {
        return FragmentEditPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = arguments?.getInt(PLAYLIST_ID)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.inputEditTextName.doAfterTextChanged {
            if (binding.inputEditTextName.text.isNullOrEmpty()) {
                binding.btnSavePlaylist.background.setTint(resources.getColor(R.color.YP_Grey))
                binding.btnSavePlaylist.isEnabled = false
            } else {
                binding.btnSavePlaylist.background.setTint(resources.getColor(R.color.YP_Blue))
                binding.btnSavePlaylist.isEnabled = true
            }
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.saveUri(uri)
                    isSelectedImg = true
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

        binding.btnSavePlaylist.setOnClickListener {
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
                    binding.coverPlaylist.setImageURI(Uri.parse(editPlaylistState.playlist.coverPath))
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
