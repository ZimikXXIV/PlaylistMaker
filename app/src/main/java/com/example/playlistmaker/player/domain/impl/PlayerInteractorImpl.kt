package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.model.PlayerStatus

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {

    override fun createPlayer(previewUrl: String) {
        playerRepository.createPlayer(previewUrl)
    }

    override fun play() {
        playerRepository.play()
    }

    override fun pause() {
        playerRepository.pause()
    }

    override fun getPlayerStatus(): PlayerStatus {
        return playerRepository.getPlayerStatus()
    }

    override fun getPositionStr(): String {
        return playerRepository.getPositionStr()
    }

    override fun getPosition(): Int {
        return playerRepository.getPosition()
    }

    override fun release() {
        playerRepository.release()
    }
}