package com.example.playlistmaker.Player.data

import android.media.MediaPlayer
import com.example.playlistmaker.Player.domain.PlayerState
import com.example.playlistmaker.Player.domain.api.PlayerInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerInteractorImpl : PlayerInteractor {

    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT

    override fun createPlayer(previewUrl: String) {

        with(mediaPlayer) {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = PlayerState.STATE_PREPARED
            }
            setOnCompletionListener {
                playerState = PlayerState.STATE_PREPARED
            }
        }
    }

    override fun getPlayerState(): PlayerState {
        return playerState
    }

    override fun play() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
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