package com.example.playlistmaker.playlist.data.Entity

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "trackId"])
data class PlaylistJoinTrackEntity(
    val playlistId: Int,
    val trackId: Int
)

