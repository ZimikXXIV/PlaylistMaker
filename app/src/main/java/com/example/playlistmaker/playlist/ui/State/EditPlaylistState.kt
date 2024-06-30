package com.example.playlistmaker.playlist.ui.State

import com.example.playlistmaker.playlist.domain.model.Playlist

sealed interface EditPlaylistState {

    data class LoadedPlaylist(val playlist: Playlist) : EditPlaylistState

    data class SavedPlaylist(val isSaved: Boolean = true) : EditPlaylistState

}