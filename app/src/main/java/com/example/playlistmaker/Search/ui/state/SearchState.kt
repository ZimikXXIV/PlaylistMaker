package com.example.playlistmaker.Search.ui.state

import com.example.playlistmaker.Search.domain.model.Track
import com.example.playlistmaker.Search.presentation.model.SearchStatus

sealed interface SearchState {
    data class Loading(val status: SearchStatus) : SearchState
    data class History(val status: SearchStatus, val historyList: List<Track>) : SearchState
    data class Search(val status: SearchStatus, val searchList: List<Track>) : SearchState
    data class Error(val status: SearchStatus, val message: String) : SearchState
    data class Empty(val status: SearchStatus) : SearchState
}