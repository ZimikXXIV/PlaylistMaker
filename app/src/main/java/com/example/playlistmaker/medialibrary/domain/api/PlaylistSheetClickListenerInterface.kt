package com.example.playlistmaker.medialibrary.domain.api

import com.example.playlistmaker.medialibrary.domain.PlaylistCard

interface PlaylistSheetClickListenerInterface {
    fun onClick(playlist: PlaylistCard)
}