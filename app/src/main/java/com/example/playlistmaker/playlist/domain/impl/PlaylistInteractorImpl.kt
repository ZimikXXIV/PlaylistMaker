package com.example.playlistmaker.playlist.domain.impl

import com.example.playlistmaker.medialibrary.domain.PlaylistCard
import com.example.playlistmaker.player.domain.model.TrackInfo
import com.example.playlistmaker.playlist.domain.api.PlaylistInteractor
import com.example.playlistmaker.playlist.domain.api.PlaylistRepository
import com.example.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {

    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistRepository.insertPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<PlaylistCard>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun insertTrack(track: TrackInfo, playlist: PlaylistCard) {
        playlistRepository.insertTrack(track, playlist)
    }

    override fun getPlaylist(playlistId: Int): Flow<List<PlaylistCard>> {
        return playlistRepository.getPlaylist(playlistId)
    }

    override suspend fun deleteTrack(playlist: PlaylistCard, track: TrackInfo) {
        playlistRepository.deleteTrack(playlist, track)
    }

    override suspend fun deletePlaylist(playlist: PlaylistCard) {
        playlistRepository.deletePlaylist(playlist.id)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

}