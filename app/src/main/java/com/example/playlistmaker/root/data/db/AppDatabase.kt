package com.example.playlistmaker.root.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.medialibrary.data.db.TrackDao
import com.example.playlistmaker.medialibrary.data.db.TrackEntity
import com.example.playlistmaker.playlist.data.Entity.PlaylistEntity
import com.example.playlistmaker.playlist.data.Entity.PlaylistTracksEntity
import com.example.playlistmaker.playlist.data.PlaylistDao

@Database(
    version = 3, entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        PlaylistTracksEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}