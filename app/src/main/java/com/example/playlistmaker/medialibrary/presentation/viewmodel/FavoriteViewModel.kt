package com.example.playlistmaker.medialibrary.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.api.FavoriteInteractor
import com.example.playlistmaker.medialibrary.ui.State.FavoriteState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteInteractor: FavoriteInteractor) : ViewModel() {

    private var favoriteLiveData = MutableLiveData<FavoriteState>()
    private var getFavorite: Job? = null
    fun getFavoriteLiveData(): LiveData<FavoriteState> = favoriteLiveData


    init {
        fillData()
    }

    fun fillData() {
        getFavorite?.cancel()
        getFavorite = viewModelScope.launch(Dispatchers.IO) {
            favoriteInteractor.getTracks().collect { tracks ->
                favoriteLiveData.postValue(FavoriteState.ProgressBar())
                if (tracks.isNullOrEmpty()) {
                    favoriteLiveData.postValue(FavoriteState.EmptyList())
                } else favoriteLiveData.postValue(FavoriteState.FavoriteList(tracks))
            }
        }
    }

}