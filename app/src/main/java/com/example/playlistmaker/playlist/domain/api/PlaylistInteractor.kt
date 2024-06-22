package com.example.playlistmaker.playlist.domain.api

import com.example.playlistmaker.medialibrary.domain.PlaylistCard
import com.example.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun insertTrack(playlist: Playlist)

    fun getPlaylists(): Flow<List<PlaylistCard>>

}