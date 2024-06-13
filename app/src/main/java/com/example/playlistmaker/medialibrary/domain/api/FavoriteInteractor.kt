package com.example.playlistmaker.medialibrary.domain.api

import com.example.playlistmaker.player.presentation.model.TrackInfo
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {
    fun getTracks(): Flow<List<Track>>
    fun getTrack(track: TrackInfo): Flow<List<TrackInfo>>
    suspend fun insertTrack(track: TrackInfo)
    suspend fun deleteTrack(track: TrackInfo)

}
