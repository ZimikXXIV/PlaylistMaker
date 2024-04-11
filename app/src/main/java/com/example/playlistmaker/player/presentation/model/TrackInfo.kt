package com.example.playlistmaker.player.presentation.model

data class TrackInfo(
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val trackTimeMillis: String?,
    val artworkUrl512: String?,
    val previewUrl: String?
)