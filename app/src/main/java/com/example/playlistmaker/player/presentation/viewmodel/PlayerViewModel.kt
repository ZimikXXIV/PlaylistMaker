package com.example.playlistmaker.player.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.model.PlayerConst
import com.example.playlistmaker.player.domain.model.PlayerStatus
import com.example.playlistmaker.player.presentation.model.TrackInfo
import com.example.playlistmaker.player.presentation.state.PlayerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    track: TrackInfo, private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private var loadingLiveData = MutableLiveData<PlayerState>()
    private var playerJob: Job? = null
    fun getPlayerState(): LiveData<PlayerState> = loadingLiveData
    private fun getPlayerStatus(): PlayerStatus {
        return playerInteractor.getPlayerStatus()
    }

    private fun getTrackPositionStr(): String {
        return playerInteractor.getPositionStr()
    }

    private fun debounceSetPosition() {

        playerJob?.cancel()
        playerJob = viewModelScope.launch(Dispatchers.Main) {
            delay(PlayerConst.DURATION_REFRESH_DELAY_MILLIS)
            getCurrentPosition()
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
                getTrackPositionStr()
            )
        )
        debounceSetPosition()
    }

    private fun pausePlayer() {
        playerInteractor.pause()
        loadingLiveData.postValue(
            PlayerState.Content(
                PlayerStatus.PAUSED,
                getTrackPositionStr()
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
                debounceSetPosition()
            }

            PlayerStatus.PREPARED, PlayerStatus.PAUSED, PlayerStatus.DEFAULT -> {
                loadingLiveData.postValue(
                    PlayerState.Content(
                        playerInteractor.getPlayerStatus(),
                        getTrackPositionStr()
                    )
                )
            }

            else -> {}
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
