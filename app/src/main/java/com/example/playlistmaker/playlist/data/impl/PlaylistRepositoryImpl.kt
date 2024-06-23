package com.example.playlistmaker.playlist.data.impl


import com.example.playlistmaker.medialibrary.domain.PlaylistCard
import com.example.playlistmaker.player.domain.model.TrackInfo
import com.example.playlistmaker.playlist.data.Entity.PlaylistEntity
import com.example.playlistmaker.playlist.data.Entity.PlaylistWithTracksEntity
import com.example.playlistmaker.playlist.data.PlaylistDbConvertor
import com.example.playlistmaker.playlist.domain.api.PlaylistRepository
import com.example.playlistmaker.playlist.domain.model.Playlist
import com.example.playlistmaker.root.data.db.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor
) : PlaylistRepository {
    override suspend fun insertPlaylist(playlist: Playlist) {
        val playlistEntity = convertFromTrackInfo(playlist)
        appDatabase.playlistDao().insertPlaylist(playlistEntity)
    }

    private fun convertFromTrackInfo(playlist: Playlist): PlaylistEntity {
        return playlistDbConvertor.map(playlist)
    }

    override fun getPlaylists(): Flow<List<PlaylistCard>> = flow {
        val playlists = appDatabase.playlistDao().getAllPlaylistWithTracks()
        emit(convertToPlaylist(playlists))
    }

    private fun convertToPlaylist(playlistWithTracks: List<PlaylistWithTracksEntity>): List<PlaylistCard> {
        return playlistWithTracks.map { playlist -> playlistDbConvertor.map(playlist) }
    }

    override suspend fun insertTrack(track: TrackInfo, playlist: PlaylistCard) {
        val trackPlaylist = playlistDbConvertor.map(track, playlist)
        appDatabase.playlistDao().insertTracks(trackPlaylist)
    }

}

