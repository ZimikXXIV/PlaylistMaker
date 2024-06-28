package com.example.playlistmaker.playlist.ui.State

import com.example.playlistmaker.medialibrary.domain.PlaylistCard

sealed interface ViewPlaylistState {

    data class LoadedData(val playlistCard: PlaylistCard) : ViewPlaylistState

    data class DeletedPlaylist(val isDeleted: Boolean = true) : ViewPlaylistState
}