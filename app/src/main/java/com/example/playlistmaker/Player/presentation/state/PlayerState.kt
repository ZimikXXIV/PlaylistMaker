package com.example.playlistmaker.Player.presentation.state

import com.example.playlistmaker.Player.domain.model.PlayerStatus

sealed interface PlayerState {
    data class Content(val playerSatus: PlayerStatus, val playerTime: String) : PlayerState
}