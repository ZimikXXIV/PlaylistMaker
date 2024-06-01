package com.example.playlistmaker.search.presentation.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.HistoryTrackInteractor
import com.example.playlistmaker.search.domain.api.SearchTrackInteractor
import com.example.playlistmaker.search.domain.model.SearchConst
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.presentation.model.SearchStatus
import com.example.playlistmaker.search.ui.state.SearchState
import com.example.playlistmaker.utils.clickDebounce
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackHistory: HistoryTrackInteractor,
    private val searchTrackRetrofit: SearchTrackInteractor
) : ViewModel() {

    private var searchLiveData = MutableLiveData<SearchState>()

    fun getSearchLiveData(): LiveData<SearchState> = searchLiveData

    private var searchSavedText: String? = String()
    private var searchJob: Job? = null

    private var trackSearchDebounce = clickDebounce<String>(
        SearchConst.SEARCH_DEBOUNCE_DELAY_MILLIS, viewModelScope, true
    ) { changedText ->

        searchRequest(changedText)
    }
    init {
        loadTrackHistory()
    }

    fun loadTrackHistory() {
        val trackHistoryArray = trackHistory.loadTrackHistory()
        if (trackHistoryArray.count() > 0) {
            searchLiveData.postValue(
                SearchState.History(
                    SearchStatus.SEARCH_HISTORY,
                    trackHistoryArray
                )
            )
        } else {
            searchLiveData.postValue(
                SearchState.Empty(
                    SearchStatus.NONE
                )
            )
        }
    }

    fun saveTrackHistory() {
        trackHistory.saveTrackHistory()
    }

    fun addToTrackHistoryWithSave(track: Track) {
        trackHistory.addToTrackHistoryWithSave(track)
        val trackHistoryArray = trackHistory.loadTrackHistory()
        searchLiveData.postValue(
            SearchState.History(
                SearchStatus.SEARCH_HISTORY,
                trackHistoryArray
            )
        )
    }

    fun clearHistory() {
        trackHistory.clearTrackHistory()
        searchLiveData.postValue(
            SearchState.ClearHistory(
                SearchStatus.NONE
            )
        )
    }

    fun searchDebounce(changedText: String) {
        if (searchSavedText == changedText) {
            return
        }
        searchSavedText = changedText
        trackSearchDebounce(changedText)
    }

    private fun searchRequest(expression: String) {
        searchLiveData.postValue(SearchState.Loading(SearchStatus.PROGRESS_BAR))

        viewModelScope.launch {
            searchTrackRetrofit
                .searchTrack(expression)
                .collect { pair ->
                    if (pair.first == null) {
                        searchLiveData.postValue(
                            SearchState.Error(
                                SearchStatus.ERROR_EMPTY,
                                pair.second!!
                            )
                        )
                    } else {
                        searchLiveData.postValue(
                            SearchState.Search(
                                SearchStatus.SEARCH_RESULT,
                                pair.first!!
                            )
                        )
                    }
                }
        }
    }

    fun saveSearchText(expression: String) {
        searchSavedText = expression
    }

    fun onDestroyView() {
        searchJob?.cancel()
    }
}
