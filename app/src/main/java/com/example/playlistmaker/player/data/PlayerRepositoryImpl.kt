package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.model.PlayerStatus
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private var playerStatus: PlayerStatus
) : PlayerRepository {

    override fun createPlayer(previewUrl: String) {

        with(mediaPlayer) {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerStatus = PlayerStatus.PREPARED
            }
            setOnCompletionListener {
                playerStatus = PlayerStatus.PREPARED
            }
        }
    }

    override fun getPlayerStatus(): PlayerStatus {
        return playerStatus
    }

    override fun play() {
        mediaPlayer.start()
        playerStatus = PlayerStatus.PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerStatus = PlayerStatus.PAUSED
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