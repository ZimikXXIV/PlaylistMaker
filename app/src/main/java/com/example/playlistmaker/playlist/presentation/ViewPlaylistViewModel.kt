package com.example.playlistmaker.playlist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.PlaylistCard
import com.example.playlistmaker.player.domain.model.TrackInfo
import com.example.playlistmaker.playlist.domain.api.PlaylistInteractor
import com.example.playlistmaker.playlist.ui.State.ViewPlaylistState
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private var viewPlaylistState = MutableLiveData<ViewPlaylistState>()
    private var jobWorkWithDb: Job? = null
    private var playlistId: Int = 0
    private var savedPlaylist: PlaylistCard? = null


    fun getViewPlaylistState(): MutableLiveData<ViewPlaylistState> = viewPlaylistState

    fun getSavedPlaylistInfo(): PlaylistCard = savedPlaylist!!

    fun savePlaylistId(id: Int) {
        playlistId = id
    }


    fun fillData() {
        jobWorkWithDb?.cancel()

        jobWorkWithDb = viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylist(playlistId).collect { playlist ->
                viewPlaylistState.postValue(ViewPlaylistState.LoadedData(playlist.first()))
                savedPlaylist = playlist.first()
            }

        }
    }

    fun deletePlaylist(playlstCard: PlaylistCard) {
        jobWorkWithDb?.cancel()

        jobWorkWithDb = viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.deletePlaylist(playlstCard)
            viewPlaylistState.postValue(ViewPlaylistState.DeletedPlaylist())
        }
    }

    fun shareString(shareString: String) {
        sharingInteractor.shareString(shareString)
    }

    fun deleteTrack(playlist: PlaylistCard, track: TrackInfo) {
        jobWorkWithDb?.cancel()

        jobWorkWithDb = viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.deleteTrack(playlist, track)
            delay(500)
            fillData()
        }
    }

}