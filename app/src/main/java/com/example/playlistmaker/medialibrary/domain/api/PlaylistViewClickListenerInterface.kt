package com.example.playlistmaker.medialibrary.domain.api

import com.example.playlistmaker.medialibrary.domain.PlaylistCard

interface PlaylistViewClickListenerInterface {
    fun onClick(playlist: PlaylistCard)
}