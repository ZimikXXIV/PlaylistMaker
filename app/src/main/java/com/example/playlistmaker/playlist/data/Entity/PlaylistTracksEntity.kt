package com.example.playlistmaker.playlist.data.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Playlist_tracks_table")
data class PlaylistTracksEntity(
    @PrimaryKey(autoGenerate = true)
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,
    val previewUrl: String?,
    var dateAdd: String?
)
