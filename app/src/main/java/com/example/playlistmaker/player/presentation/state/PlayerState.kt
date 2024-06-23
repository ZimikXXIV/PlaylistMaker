package com.example.playlistmaker.player.presentation.state

import com.example.playlistmaker.medialibrary.domain.PlaylistCard
import com.example.playlistmaker.player.domain.model.PlayerStatus

sealed interface PlayerState {
    data class Content(
        val playerSatus: PlayerStatus, val playerTime: String
    ) : PlayerState

    data class Favorite(val isFavorite: Boolean) : PlayerState

    data class LoadedPlaylist(val playlist: List<PlaylistCard>) : PlayerState

    data class LoadPlaylist(val isLoaded: Boolean = true) : PlayerState

    data class ErrAddToPlaylist(val err_id: Int, val playlist_caption: String) : PlayerState
    data class AddToPlaylist(val err_id: Int, val playlist_caption: String) : PlayerState

}