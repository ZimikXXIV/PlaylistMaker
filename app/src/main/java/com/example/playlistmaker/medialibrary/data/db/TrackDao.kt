package com.example.playlistmaker.medialibrary.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Delete
    suspend fun deleteTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM track_table ORDER by dateADD desc")
    suspend fun getTracks(): List<TrackEntity>

    @Query("Select * from track_table where id = :trackId")
    suspend fun getTrack(trackId: Int): List<TrackEntity>
}