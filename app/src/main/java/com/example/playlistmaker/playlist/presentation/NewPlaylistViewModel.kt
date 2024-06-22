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
        playlistInteractor.insertTrack(playlist)
    }


    fun savePlaylist(
        caption: String,
        description: String?,
        coverPlaylist: String?
    ) {
        jobSave?.cancel()

        jobSave = viewModelScope.launch {
            var imgUri: Uri? = null
            if (!coverPlaylist.isNullOrEmpty()) {
                imgUri = fileSaveInteractor.savePhoto(coverPlaylist)
            }
            val savedPlaylist = Playlist(
                caption = caption,
                description = description,
                coverPath = imgUri.toString()
            )
            insertPlaylist(savedPlaylist)
        }

    }


}