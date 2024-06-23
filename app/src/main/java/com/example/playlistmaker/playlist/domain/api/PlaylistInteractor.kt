package com.example.playlistmaker.playlist.domain.api

import com.example.playlistmaker.medialibrary.domain.PlaylistCard
import com.example.playlistmaker.player.domain.model.TrackInfo
import com.example.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun insertPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<PlaylistCard>>

    suspend fun insertTrack(track: TrackInfo, playlist: PlaylistCard)
}
