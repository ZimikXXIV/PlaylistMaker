package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface TrackListClickListenerInterface {
    fun onClick(track: Track)
}
