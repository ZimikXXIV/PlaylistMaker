package com.example.playlistmaker.medialibrary.domain

import android.net.Uri

data class PlaylistCard(
    val caption: String,
    val coverImg: Uri?,
    val countTrack: Int
)
