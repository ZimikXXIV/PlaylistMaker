package com.example.playlistmaker.playlist.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.playlist.domain.api.FileSaveRepository
import java.io.File
import java.io.FileOutputStream

class FileSaveRepositoryImpl(private val context: Context) : FileSaveRepository {

    override fun savePhoto(uri: String): Uri {
        val imgUri = Uri.parse(uri)

        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        //создаем каталог, если он не создан
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val fileName = java.util.UUID.randomUUID().toString()
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, fileName)
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = context.contentResolver.openInputStream(imgUri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.toUri()
    }
}