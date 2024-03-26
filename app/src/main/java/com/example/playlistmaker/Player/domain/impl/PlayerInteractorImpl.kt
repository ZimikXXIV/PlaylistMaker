package com.example.playlistmaker.Player.domain.impl

import android.media.MediaPlayer
import com.example.playlistmaker.Player.domain.api.PlayerInteractor
import com.example.playlistmaker.Player.domain.model.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerInteractorImpl : PlayerInteractor {

    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.DEFAULT

    override fun createPlayer(previewUrl: String) {

        with(mediaPlayer) {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = PlayerState.PREPARED
            }
            setOnCompletionListener {
                playerState = PlayerState.PREPARED
            }
        }
    }

    override fun getPlayerState(): PlayerState {
        return playerState
    }

    override fun play() {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSED
    }

    override fun getPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getPositionStr(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(mediaPlayer.currentPosition)
    }

    override fun release() {
        mediaPlayer.release()
    }

}