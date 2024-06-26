package com.example.playlistmaker.player.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.medialibrary.domain.PlaylistCard
import com.example.playlistmaker.medialibrary.domain.api.FavoriteInteractor
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.model.PlayerConst
import com.example.playlistmaker.player.domain.model.PlayerStatus
import com.example.playlistmaker.player.domain.model.TrackInfo
import com.example.playlistmaker.player.presentation.state.PlayerState
import com.example.playlistmaker.playlist.domain.api.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: TrackInfo,
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private var loadingLiveData = MutableLiveData<PlayerState>()
    private var playerJob: Job? = null
    private var dbInteract: Job? = null
    private var isFavorite: Boolean = false


    fun getPlayerState(): LiveData<PlayerState> = loadingLiveData
    private fun getPlayerStatus(): PlayerStatus {
        return playerInteractor.getPlayerStatus()
    }

    private fun getTrackPositionStr(): String {
        return playerInteractor.getPositionStr()
    }

    private fun debounceSetPosition() {
        playerJob?.cancel()
        playerJob = viewModelScope.launch(Dispatchers.IO) {
            while (getPlayerStatus() == PlayerStatus.PLAYING) {
                delay(PlayerConst.DURATION_REFRESH_DELAY_MILLIS)
                getCurrentPosition()
            }
        }

    }
    private fun deleteTrack(track: TrackInfo) {
        dbInteract?.cancel()
        dbInteract = viewModelScope.launch(Dispatchers.IO) {
            favoriteInteractor.deleteTrack(track)
        }
    }

    private fun insertTrack(track: TrackInfo) {
        dbInteract?.cancel()
        dbInteract = viewModelScope.launch(Dispatchers.IO) {
            favoriteInteractor.insertTrack(track)
        }
    }

    fun checkIsFavorite() {
        dbInteract?.cancel()
        dbInteract = viewModelScope.launch(Dispatchers.IO) {
            favoriteInteractor.getTrack(track).collect { tracks ->
                isFavorite = !tracks.isNullOrEmpty()
                loadingLiveData.postValue(
                    PlayerState.Favorite(isFavorite)
                )
            }
        }
    }

    fun likePressed() {
        dbInteract?.cancel()
        dbInteract = viewModelScope.launch(Dispatchers.IO) {
            favoriteInteractor.getTrack(track).collect { tracks ->
                if (tracks.isNullOrEmpty()) {
                    insertTrack(track)
                    isFavorite = true
                } else {
                    deleteTrack(track)
                    isFavorite = false
                }
                loadingLiveData.postValue(
                    PlayerState.Favorite(isFavorite)
                )
            }
        }
    }

    init {
        playerInteractor.createPlayer(track.previewUrl!!)
        changeStatus()
    }

    fun changeStatus() {
        when (getPlayerStatus()) {
            PlayerStatus.PLAYING -> {
                pausePlayer()
            }

            PlayerStatus.PAUSED, PlayerStatus.PREPARED, PlayerStatus.DEFAULT -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        playerInteractor.play()
        loadingLiveData.postValue(
            PlayerState.Content(
                PlayerStatus.PLAYING,
                PlayerConst.DEFAULT_DURATION
            )
        )
        debounceSetPosition()
    }

    private fun pausePlayer() {
        playerInteractor.pause()
        loadingLiveData.postValue(
            PlayerState.Content(
                PlayerStatus.PAUSED,
                PlayerConst.DEFAULT_DURATION
            )
        )
        playerJob?.cancel()
    }

    private fun getCurrentPosition() {

        when (playerInteractor.getPlayerStatus()) {
            PlayerStatus.PLAYING -> {
                loadingLiveData.postValue(
                    PlayerState.Content(
                        PlayerStatus.PLAYING,
                        getTrackPositionStr()
                    )
                )
            }
            PlayerStatus.PREPARED, PlayerStatus.PAUSED, PlayerStatus.DEFAULT -> {
                loadingLiveData.postValue(
                    PlayerState.Content(
                        playerInteractor.getPlayerStatus(),
                        PlayerConst.DEFAULT_DURATION
                    )
                )
            }

            else -> {}
        }
    }

    fun fillPlaylist() {
        dbInteract?.cancel()
        dbInteract = viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylists().collect { playlists ->
                loadingLiveData.postValue(PlayerState.LoadPlaylist())
                if (playlists.isNullOrEmpty()) {
                    loadingLiveData.postValue(PlayerState.LoadedPlaylist(emptyList<PlaylistCard>()))
                } else loadingLiveData.postValue(PlayerState.LoadedPlaylist(playlists))
            }
        }
    }

    fun saveTrackToPlaylist(playlist: PlaylistCard) {

        if (playlist.trackList.contains(track)) {
            loadingLiveData.postValue(
                PlayerState.ErrAddToPlaylist(
                    R.string.error_add_track_to_playlist,
                    playlist.caption
                )
            )
            return
        }

        dbInteract?.cancel()
        dbInteract = viewModelScope.launch {
            playlistInteractor.insertTrack(track, playlist)
            loadingLiveData.postValue(
                PlayerState.AddToPlaylist(
                    R.string.add_track_to_playlist,
                    playlist.caption
                )
            )
        }
    }

    fun onPause() {
        pausePlayer()
    }

    fun onDestroy() {
        playerJob?.cancel()
        playerInteractor.release()
    }

}
