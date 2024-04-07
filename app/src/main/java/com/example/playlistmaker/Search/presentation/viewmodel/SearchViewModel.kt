package com.example.playlistmaker.Search.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.Debounce.debounce
import com.example.playlistmaker.Debounce.removeCallbacks
import com.example.playlistmaker.Search.domain.api.ConsumerData
import com.example.playlistmaker.Search.domain.api.HistoryTrackInteractor
import com.example.playlistmaker.Search.domain.api.SearchTrackInteractor
import com.example.playlistmaker.Search.domain.api.TrackConsumer
import com.example.playlistmaker.Search.domain.model.SearchConst
import com.example.playlistmaker.Search.domain.model.Track
import com.example.playlistmaker.Search.presentation.model.SearchStatus
import com.example.playlistmaker.Search.ui.state.SearchState

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


    private var trackHistoryArray: ArrayList<Track> = ArrayList()

    fun loadTrackHistory() {
        trackHistoryArray = trackHistory.loadTrackHistory()
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
        trackHistory.saveTrackHistory(trackHistoryArray)
    }

    fun addToTrackHistoryWithSave(track: Track) {
        trackHistory.addToTrackHistoryWithSave(track, trackHistoryArray)
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

    companion object {
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(
                        Creator.getHistoryTrackInteractor(context),
                        Creator.getSearchTrackInteractor(context)
                    )
                }
            }
    }
}
