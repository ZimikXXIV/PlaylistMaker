package com.example.playlistmaker.playlist.ui.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.playlist.presentation.EditPlaylistViewModel
import com.example.playlistmaker.utils.BindingFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

open class NewPlaylistFragment : BindingFragment<FragmentNewPlaylistBinding>() {

    private val viewModel by viewModel<EditPlaylistViewModel>()
    private var isSelectedImg: Boolean = false

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNewPlaylistBinding {
        return FragmentNewPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener() {
            checkIsSaved()
        }

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
                    checkIsSaved()
                }
            })

        binding.btnCreatePlaylist.setOnClickListener {
            viewModel.savePlaylist(
                caption = binding.inputEditTextName.text.toString(),
                description = binding.inputEditTextDescription.text.toString()
            )
            findNavController().navigateUp()
            Toast.makeText(
                requireContext(),
                String.format(
                    getString(R.string.new_playlist_created),
                    binding.inputEditTextName.text.toString()
                ),
                Toast.LENGTH_LONG
            ).show()

        }
    }

    private fun checkIsSaved() {
        if (isSelectedImg ||
            !binding.inputEditTextName.text.isNullOrEmpty() || !binding.inputEditTextDescription.text.isNullOrEmpty()
        ) {
            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(R.string.new_playlist_alert_title)
                .setMessage(R.string.new_playlist_alert_message)
                .setNeutralButton(R.string.new_playlist_alert_cancel) { _, _ -> }
                .setPositiveButton(R.string.new_playlist_alert_confirm) { _, _ -> findNavController().navigateUp() }
                .show()
        } else {
            findNavController().navigateUp()
        }
    }

    override fun onResume() {
        super.onResume()

    }

    companion object {
        fun newInstance() = NewPlaylistFragment()
    }

}
