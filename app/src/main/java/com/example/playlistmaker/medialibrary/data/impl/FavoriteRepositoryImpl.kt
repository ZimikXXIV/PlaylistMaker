package com.example.playlistmaker.medialibrary.data.impl

import com.example.playlistmaker.medialibrary.data.db.AppDatabase
import com.example.playlistmaker.medialibrary.data.db.TrackDbConvertor
import com.example.playlistmaker.medialibrary.data.db.TrackEntity
import com.example.playlistmaker.medialibrary.domain.api.FavoriteRepository
import com.example.playlistmaker.player.presentation.model.TrackInfo
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoriteRepository {
    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTracksEntity(tracks))
    }

    override fun getTrack(track: TrackInfo): Flow<List<TrackInfo>> = flow {
        val tracks = appDatabase.trackDao().getTrack(track.trackId)
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun deleteTrack(track: TrackInfo) {
        val trackEntity = convertFromTrackInfo(track)
        appDatabase.trackDao().deleteTrack(trackEntity)
    }

    override suspend fun insertTrack(track: TrackInfo) {
        val trackEntity = convertFromTrackInfo(track)
        appDatabase.trackDao().insertTrack(trackEntity)
    }

    private fun convertFromTracksEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.mapToTrack(track) }
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<TrackInfo> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

    private fun convertFromTrackInfo(track: TrackInfo): TrackEntity {
        return trackDbConvertor.map(track)
    }

}