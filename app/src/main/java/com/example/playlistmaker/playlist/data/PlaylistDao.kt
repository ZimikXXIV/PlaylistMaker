package com.example.playlistmaker.playlist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.playlist.data.Entity.PlaylistEntity
import com.example.playlistmaker.playlist.data.Entity.PlaylistTracksEntity
import com.example.playlistmaker.playlist.data.Entity.PlaylistWithTracksEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(track: PlaylistTracksEntity)
    
    @Transaction
    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylistWithTracks(): List<PlaylistWithTracksEntity>
}
