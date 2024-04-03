package com.example.playlistmaker.Search.domain.api

import com.example.playlistmaker.Search.domain.model.Track

interface TrackListClickListenerInterface {
    fun onClick(track: Track) {}
}
