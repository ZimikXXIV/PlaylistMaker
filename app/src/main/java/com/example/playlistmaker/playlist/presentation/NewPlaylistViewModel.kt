package com.example.playlistmaker.playlist.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlist.domain.api.FileSaveInteractor
import com.example.playlistmaker.playlist.domain.api.PlaylistInteractor
import com.example.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val fileSaveInteractor: FileSaveInteractor
) : ViewModel() {

    private var jobSave: Job? = null
    private var savedUri: Uri? = null

    fun saveUri(uri: Uri) {
        savedUri = uri
    }

    private suspend fun insertPlaylist(playlist: Playlist) {
        playlistInteractor.insertPlaylist(playlist)
    }


    fun savePlaylist(
        caption: String,
        description: String?
    ) {
        jobSave?.cancel()

        jobSave = viewModelScope.launch {
            var savedFile: Uri? = null
            if (savedUri != null) savedFile = fileSaveInteractor.savePhoto(savedUri.toString())

            val savedPlaylist = Playlist(
                playlistId = 0,
                caption = caption,
                description = description,
                coverPath = savedFile?.toString()
            )
            insertPlaylist(savedPlaylist)
        }

    }


}