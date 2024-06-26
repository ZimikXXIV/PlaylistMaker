package com.example.playlistmaker.playlist.data.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int = 0,
    val playlistCaption: String,
    val playlistDescription: String?,
    val coverPath: String?
)
