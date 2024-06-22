package com.example.playlistmaker.playlist.domain.impl

import com.example.playlistmaker.medialibrary.domain.PlaylistCard
import com.example.playlistmaker.playlist.domain.api.PlaylistInteractor
import com.example.playlistmaker.playlist.domain.api.PlaylistRepository
import com.example.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {

    override suspend fun insertTrack(playlist: Playlist) {
        playlistRepository.insertTrack(playlist)
    }

    override fun getPlaylists(): Flow<List<PlaylistCard>> {
        return playlistRepository.getPlaylists()
    }

}