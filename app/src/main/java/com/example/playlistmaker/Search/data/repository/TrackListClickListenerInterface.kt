package com.example.playlistmaker.Search.data.repository

import com.example.playlistmaker.Search.domain.model.Track

interface TrackListClickListenerInterface {
    fun onClick(track: Track) {}
}
