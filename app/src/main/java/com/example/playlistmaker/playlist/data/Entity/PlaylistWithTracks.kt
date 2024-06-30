package com.example.playlistmaker.playlist.data.Entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class PlaylistWithTracks(
    @Embedded
    val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entity = PlaylistTracksEntity::class,
        entityColumn = "trackId",
        associateBy = Junction(
            value = PlaylistJoinTrackEntity::class,
            parentColumn = "playlistId",
            entityColumn = "trackId"
        )
    )
    val playlistTrack: List<PlaylistTracksEntity>
)
