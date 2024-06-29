package com.example.playlistmaker.playlist.data.impl


import com.example.playlistmaker.medialibrary.domain.PlaylistCard
import com.example.playlistmaker.player.domain.model.TrackInfo
import com.example.playlistmaker.playlist.data.Entity.PlaylistEntity
import com.example.playlistmaker.playlist.data.Entity.PlaylistJoinTrackEntity
import com.example.playlistmaker.playlist.data.Entity.PlaylistWithTracks
import com.example.playlistmaker.playlist.data.PlaylistDbConvertor
import com.example.playlistmaker.playlist.domain.api.PlaylistRepository
import com.example.playlistmaker.playlist.domain.model.Playlist
import com.example.playlistmaker.root.data.db.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date

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

    private fun convertToPlaylist(playlistWithTracks: List<PlaylistWithTracks>): List<PlaylistCard> {
        return playlistWithTracks.map { playlist -> playlistDbConvertor.map(playlist) }
    }

    override suspend fun insertTrack(track: TrackInfo, playlist: PlaylistCard) {
        val trackPlaylist = playlistDbConvertor.map(track)
        trackPlaylist.dateAdd = SimpleDateFormat("yyyy/dd/M hh:mm:ss").format(Date())
        appDatabase.playlistDao().insertTracks(trackPlaylist)
        appDatabase.playlistDao().insertJoin(PlaylistJoinTrackEntity(playlist.id, track.trackId))
    }

    override fun getPlaylist(playlistId: Int): Flow<List<PlaylistCard>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylistWithTracks(playlistId)
        emit(convertToPlaylist(playlists))
    }

    override suspend fun deleteTrack(playlist: PlaylistCard, track: TrackInfo) {
        appDatabase.playlistDao()
            .deletePlaylistJoinTrack(PlaylistJoinTrackEntity(playlist.id, track.trackId))
        appDatabase.playlistDao().deleteTrack(track.trackId)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        val listTracks = appDatabase.playlistDao().getAllJoinByPlaylist(playlistId)
        listTracks.forEach { join ->
            appDatabase.playlistDao().deletePlaylistJoinTrack(join)
            appDatabase.playlistDao().deleteTrack(join.trackId)
        }
        appDatabase.playlistDao().deletePlaylist(playlistId)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        val playlistEntity = playlistDbConvertor.map(playlist)
        appDatabase.playlistDao().updatePlaylist(playlistEntity)
    }

}

