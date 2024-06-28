package com.example.playlistmaker.playlist.domain.model

data class Playlist(
    val playlistId: Int,
    val caption: String,
    val description: String?,
    var coverPath: String?
)
