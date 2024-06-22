package com.example.playlistmaker.playlist.domain.impl

import android.net.Uri
import com.example.playlistmaker.playlist.domain.api.FileSaveInteractor
import com.example.playlistmaker.playlist.domain.api.FileSaveRepository

class FileSaveInteractorImpl(private val fileSaveRepository: FileSaveRepository) :
    FileSaveInteractor {

    override fun savePhoto(uri: String): Uri {
        return fileSaveRepository.savePhoto(uri)
    }

}