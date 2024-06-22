package com.example.playlistmaker.playlist.data.Entity

import androidx.room.Embedded
import androidx.room.Relation


data class PlaylistWithTracksEntity(
    @Embedded
    val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "fkPlaylistId"
    )
    val playlistTrack: List<PlaylistTracksEntity>
)
