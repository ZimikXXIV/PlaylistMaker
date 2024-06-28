package com.example.playlistmaker.playlist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.playlistmaker.playlist.data.Entity.PlaylistEntity
import com.example.playlistmaker.playlist.data.Entity.PlaylistTracksEntity
import com.example.playlistmaker.playlist.data.Entity.PlaylistWithTracksEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(track: PlaylistTracksEntity)

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Query("DELETE FROM Playlist_tracks_table where fkPlaylistId = :playlistId and trackAppleId = :trackId")
    suspend fun deleteTrack(playlistId: Int, trackId: Int)

    @Query("DELETE FROM Playlist_tracks_table where fkPlaylistId = :playlistId")
    suspend fun deleteAllFromPlaylist(playlistId: Int)

    @Query("DELETE FROM playlist_table where playlistId = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)

    @Transaction
    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylistWithTracks(): List<PlaylistWithTracksEntity>

    @Transaction
    @Query("SELECT * FROM playlist_table where playlistId = :playlistId")
    suspend fun getPlaylistWithTracks(playlistId: Int): List<PlaylistWithTracksEntity>
}
