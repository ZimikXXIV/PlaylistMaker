package com.example.playlistmaker.Player.domain.api

import com.example.playlistmaker.Player.domain.PlayerState

interface PlayerInteractor {
    fun createPlayer(previewUrl: String)
    fun getPlayerState(): PlayerState
    fun play()
    fun pause()
    fun getPosition(): Int
    fun getPositionStr(): String
    fun release()


}