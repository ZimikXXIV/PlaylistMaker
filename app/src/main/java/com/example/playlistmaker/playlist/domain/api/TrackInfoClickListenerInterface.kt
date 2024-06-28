package com.example.playlistmaker.playlist.domain.api

import com.example.playlistmaker.player.domain.model.TrackInfo

interface TrackInfoClickListenerInterface {
    fun onClick(track: TrackInfo)
}