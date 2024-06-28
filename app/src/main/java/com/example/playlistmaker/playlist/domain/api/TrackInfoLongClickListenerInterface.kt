package com.example.playlistmaker.playlist.domain.api

import com.example.playlistmaker.player.domain.model.TrackInfo

interface TrackInfoLongClickListenerInterface {

    fun onLongClick(track: TrackInfo)

}