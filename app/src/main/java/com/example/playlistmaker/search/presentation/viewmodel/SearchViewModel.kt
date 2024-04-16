package com.example.playlistmaker.search.presentation.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.Debounce.debounce
import com.example.playlistmaker.Debounce.removeCallbacks
import com.example.playlistmaker.search.domain.api.ConsumerData
import com.example.playlistmaker.search.domain.api.HistoryTrackInteractor
import com.example.playlistmaker.search.domain.api.SearchTrackInteractor
import com.example.playlistmaker.search.domain.api.TrackConsumer
import com.example.playlistmaker.search.domain.model.SearchConst
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.presentation.model.SearchStatus
import com.example.playlistmaker.search.ui.state.SearchState

class SearchViewModel(
    private val trackHistory: HistoryTrackInteractor,
    private val searchTrackRetrofit: SearchTrackInteractor
) : ViewModel() {

    private var searchLiveData = MutableLiveData<SearchState>()

    fun getSearchLiveData(): LiveData<SearchState> = searchLiveData

    private var searchSavedText: String? = String()

    val searchRunnable = Runnable {
        val newSearchText = searchSavedText ?: ""
        searchRequest(newSearchText)
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

    private fun searchRequest(expression: String) {
        searchLiveData.postValue(SearchState.Loading(SearchStatus.PROGRESS_BAR))
        searchTrackRetrofit.searchTrack(
            expression,
            object : TrackConsumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {
                    if (data is ConsumerData.Error) {
                        searchLiveData.postValue(
                            SearchState.Error(
                                SearchStatus.ERROR_EMPTY,
                                data.errorMessage
                            )
                        )
                    } else if (data is ConsumerData.Data) {
                        searchLiveData.postValue(
                            SearchState.Search(
                                SearchStatus.SEARCH_RESULT,
                                data.data
                            )
                        )
                    }
                }

            }
        )
    }

    fun searchDebounce(expression: String) {
        searchSavedText = expression
        debounce(searchRunnable, SearchConst.SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    fun saveSearchText(expression: String) {
        searchSavedText = expression
    }

    fun onDestroy() {
        removeCallbacks(searchRunnable)
    }
}
