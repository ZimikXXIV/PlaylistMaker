package com.example.playlistmaker.playlist.domain.api

import android.net.Uri

interface FileSaveInteractor {
    fun savePhoto(uri: String): Uri
}