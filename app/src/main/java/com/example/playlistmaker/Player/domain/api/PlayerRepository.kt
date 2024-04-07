package com.example.playlistmaker.Player.domain.api

import com.example.playlistmaker.Player.domain.model.PlayerStatus

interface PlayerRepository {
    fun createPlayer(previewUrl: String)
    fun getPlayerStatus(): PlayerStatus
    fun play()
    fun pause()
    fun getPosition(): Int
    fun getPositionStr(): String
    fun release()
}