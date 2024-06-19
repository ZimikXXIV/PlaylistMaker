package com.example.playlistmaker.player.domain.model

data class TrackInfo(
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val trackTimeMillis: String?,
    val artworkUrl100: String?,
    val artworkUrl512: String?,
    val previewUrl: String?
)