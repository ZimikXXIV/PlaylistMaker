package com.example.playlistmaker.playlist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.playlistmaker.playlist.data.Entity.PlaylistEntity
import com.example.playlistmaker.playlist.data.Entity.PlaylistJoinTrackEntity
import com.example.playlistmaker.playlist.data.Entity.PlaylistTracksEntity
import com.example.playlistmaker.playlist.data.Entity.PlaylistWithTracks

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(track: PlaylistTracksEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJoin(join: PlaylistJoinTrackEntity)

    @Delete
    suspend fun deletePlaylistJoinTrack(join: PlaylistJoinTrackEntity)

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * from PlaylistJoinTrackEntity where playlistId = :playlistId")
    suspend fun getAllJoinByPlaylist(playlistId: Int): List<PlaylistJoinTrackEntity>


    @Query(
        "DELETE FROM Playlist_tracks_table where trackId = :trackId " +
                "AND not exists (select 1 FROM PlaylistJoinTrackEntity p1 where p1.trackId = :trackId )"
    )
    suspend fun deleteTrack(trackId: Int)

    @Query("DELETE FROM playlist_table where playlistId = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)

    @Transaction
    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylistWithTracks(): List<PlaylistWithTracks>

    @Transaction
    @Query("SELECT * FROM playlist_table where playlistId = :playlistId")
    suspend fun getPlaylistWithTracks(playlistId: Int): List<PlaylistWithTracks>
}
