package com.example.playlistmaker.player.presentation.state

import com.example.playlistmaker.player.domain.model.PlayerStatus

sealed interface PlayerState {
    data class Content(val playerSatus: PlayerStatus, val playerTime: String) : PlayerState
}