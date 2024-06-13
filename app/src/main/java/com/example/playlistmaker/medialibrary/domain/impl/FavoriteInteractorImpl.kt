package com.example.playlistmaker.medialibrary.domain.impl

import com.example.playlistmaker.medialibrary.domain.api.FavoriteInteractor
import com.example.playlistmaker.medialibrary.domain.api.FavoriteRepository
import com.example.playlistmaker.player.presentation.model.TrackInfo
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteRepository) :
    FavoriteInteractor {
    override fun getTracks(): Flow<List<Track>> {
        return favoriteRepository.getTracks()
    }

    override fun getTrack(track: TrackInfo): Flow<List<TrackInfo>> {
        return favoriteRepository.getTrack(track)
    }

    override suspend fun insertTrack(track: TrackInfo) {
        favoriteRepository.insertTrack(track)
    }

    override suspend fun deleteTrack(track: TrackInfo) {
        favoriteRepository.deleteTrack(track)
    }
}

