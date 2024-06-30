package com.example.playlistmaker.player.domain.model

import java.io.Serializable


data class TrackInfo(
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val trackTimeMillis: Long?,
    val trackTimeMillisStr: String?,
    val artworkUrl100: String?,
    val artworkUrl512: String?,
    val previewUrl: String?,
    val dateAdd: String?
) : Serializable