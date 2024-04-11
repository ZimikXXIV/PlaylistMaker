package com.example.playlistmaker.player.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Debounce.debounce
import com.example.playlistmaker.Debounce.removeCallbacks
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.model.PlayerConst
import com.example.playlistmaker.player.domain.model.PlayerStatus
import com.example.playlistmaker.player.presentation.state.PlayerState
import com.example.playlistmaker.search.domain.model.Track

class PlayerViewModel(
    track: Track, private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private var loadingLiveData = MutableLiveData<PlayerState>()
    private val setPositionRunnable = Runnable { getCurrentPosition() }

    fun getPlayerState(): LiveData<PlayerState> = loadingLiveData
    private fun getPlayerStatus(): PlayerStatus {
        return playerInteractor.getPlayerStatus()
    }

    private fun getTrackPositionStr(): String {
        return playerInteractor.getPositionStr()
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
        debounce(setPositionRunnable, PlayerConst.DURATION_REFRESH_DELAY_MILLIS)
    }

    private fun pausePlayer() {
        playerInteractor.pause()
        loadingLiveData.postValue(
            PlayerState.Content(
                PlayerStatus.PAUSED,
                PlayerConst.DEFAULT_DURATION
            )
        )
        removeCallbacks(setPositionRunnable)
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
                debounce(setPositionRunnable, PlayerConst.DURATION_REFRESH_DELAY_MILLIS)
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

    fun onPause() {
        pausePlayer()
    }

    fun onDestroy() {
        removeCallbacks(setPositionRunnable)
        playerInteractor.release()
    }

    companion object {
        fun getViewModelFactory(track: Track): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    PlayerViewModel(
                        track,
                        Creator.getPlayerInteractor()
                    )
                }
            }
    }

}