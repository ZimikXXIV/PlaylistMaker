package com.example.playlistmaker.medialibrary.ui.State

import com.example.playlistmaker.medialibrary.domain.PlaylistCard

sealed interface PlaylistCardState {

    data class PlaylistCards(val playlistCards: List<PlaylistCard>) : PlaylistCardState
    data class ProgressBar(val isEmpty: Boolean = true) : PlaylistCardState
    data class EmptyCards(val isEmpty: Boolean = true) : PlaylistCardState

}