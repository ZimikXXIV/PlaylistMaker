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

    private suspend fun insertPlaylist(playlist: Playlist) {
        playlistInteractor.insertPlaylist(playlist)
    }


    fun savePlaylist(
        caption: String,
        description: String?,
        coverPlaylist: Uri?
    ) {
        jobSave?.cancel()

        jobSave = viewModelScope.launch {

            if (coverPlaylist != null) fileSaveInteractor.savePhoto(coverPlaylist.toString())

            val savedPlaylist = Playlist(
                caption = caption,
                description = description,
                coverPath = coverPlaylist?.toString()
            )
            insertPlaylist(savedPlaylist)
        }

    }


}