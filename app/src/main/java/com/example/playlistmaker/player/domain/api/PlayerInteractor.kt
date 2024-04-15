package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.model.PlayerStatus

interface PlayerInteractor {
    fun createPlayer(previewUrl: String)
    fun getPlayerStatus(): PlayerStatus
    fun play()
    fun pause()
    fun getPosition(): Int
    fun getPositionStr(): String
    fun release()


}