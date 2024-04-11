package com.example.playlistmaker.search.ui.state

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.presentation.model.SearchStatus

sealed interface SearchState {
    data class Loading(val status: SearchStatus) : SearchState
    data class History(val status: SearchStatus, val historyList: List<Track>) : SearchState
    data class Search(val status: SearchStatus, val searchList: List<Track>) : SearchState
    data class Error(val status: SearchStatus, val message: String) : SearchState
    data class Empty(val status: SearchStatus) : SearchState
    data class ClearHistory(val status: SearchStatus) : SearchState
}