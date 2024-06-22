package com.example.playlistmaker.medialibrary.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.ui.State.PlaylistCardState
import com.example.playlistmaker.playlist.domain.api.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private var playlistLiveData = MutableLiveData<PlaylistCardState>()
    private var getFavorite: Job? = null
    fun getplaylistCardLiveData(): LiveData<PlaylistCardState> = playlistLiveData


    init {
        fillData()
    }

    fun fillData() {
        getFavorite?.cancel()
        getFavorite = viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylists().collect { playlists ->
                playlistLiveData.postValue(PlaylistCardState.ProgressBar())
                if (playlists.isNullOrEmpty()) {
                    playlistLiveData.postValue(PlaylistCardState.EmptyCards())
                } else playlistLiveData.postValue(PlaylistCardState.PlaylistCards(playlists))
            }
        }
    }

}