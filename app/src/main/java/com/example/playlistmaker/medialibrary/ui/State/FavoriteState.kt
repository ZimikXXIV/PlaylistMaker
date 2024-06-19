package com.example.playlistmaker.medialibrary.ui.State

import com.example.playlistmaker.search.domain.model.Track

sealed interface FavoriteState {

    data class FavoriteList(val favoriteTracks: List<Track>) : FavoriteState
    data class ProgressBar(val isEmpty: Boolean = true) : FavoriteState
    data class EmptyList(val isEmpty: Boolean = true) : FavoriteState

}