package com.example.playlistmaker.playlist.domain.api

import android.net.Uri

interface FileSaveRepository {
    fun savePhoto(uri: String): Uri
}
