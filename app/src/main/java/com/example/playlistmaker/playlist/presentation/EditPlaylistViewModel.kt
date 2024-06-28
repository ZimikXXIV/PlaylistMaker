package com.example.playlistmaker.playlist.presentation

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlist.domain.api.FileSaveInteractor
import com.example.playlistmaker.playlist.domain.api.PlaylistInteractor
import com.example.playlistmaker.playlist.domain.model.Playlist
import com.example.playlistmaker.playlist.ui.State.EditPlaylistState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val fileSaveInteractor: FileSaveInteractor
) : ViewModel() {

    private var jobSave: Job? = null
    private var savedUri: Uri? = null
    private var savedPlaylistId: Int? = null
    private var editPlaylistState = MutableLiveData<EditPlaylistState>()

    fun getEditPlaylistState() = editPlaylistState

    fun saveUri(uri: Uri) {
        savedUri = uri
    }

    private suspend fun updatePlaylist(playlist: Playlist) {
        playlistInteractor.updatePlaylist(playlist)
        editPlaylistState.postValue(EditPlaylistState.SavedPlaylist())
    }

    private suspend fun getPlaylist(playlistId: Int) {
        playlistInteractor.getPlaylist(playlistId).collect() { playlist ->
            val loadedPlaylist = Playlist(
                playlistId = playlist.last().id,
                caption = playlist.last().caption,
                description = playlist.last().description,
                coverPath = playlist.last().coverImg.toString()
            )
            savedPlaylistId = loadedPlaylist.playlistId
            editPlaylistState.postValue(EditPlaylistState.LoadedPlaylist(loadedPlaylist))

        }
    }

    fun fillData(playlistId: Int) {
        jobSave?.cancel()

        jobSave = viewModelScope.launch {
            getPlaylist(playlistId)
        }
    }

    fun updateData(
        caption: String,
        description: String?
    ) {
        jobSave?.cancel()

        jobSave = viewModelScope.launch {
            var savedFile: Uri? = null
            if (savedUri != null) {
                savedFile = fileSaveInteractor.savePhoto(savedUri.toString())

            }
            val playlist = Playlist(
                playlistId = savedPlaylistId!!,
                caption = caption,
                description = description,
                coverPath = savedFile.toString()
            )

            updatePlaylist(playlist)
        }

    }


}