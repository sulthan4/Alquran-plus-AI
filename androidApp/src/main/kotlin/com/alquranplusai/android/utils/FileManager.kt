package com.alquranplusai.android.utils

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class FileManager(private val context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: FileManager? = null
        
        fun getInstance(context: Context): FileManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FileManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    suspend fun copyFile(source: File, destination: File): Boolean = withContext(Dispatchers.IO) {
        try {
            destination.parentFile?.mkdirs()
            FileInputStream(source).use { input ->
                FileOutputStream(destination).use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: IOException) {
            false
        }
    }

    suspend fun moveFile(source: File, destination: File): Boolean = withContext(Dispatchers.IO) {
        try {
            if (copyFile(source, destination)) {
                source.delete()
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteFile(file: File): Boolean = withContext(Dispatchers.IO) {
        try {
            file.delete()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteDirectory(directory: File): Boolean = withContext(Dispatchers.IO) {
        try {
            directory.deleteRecursively()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getFileSize(file: File): Long = withContext(Dispatchers.IO) {
        if (file.isFile) {
            file.length()
        } else if (file.isDirectory) {
            file.walkTopDown().filter { it.isFile }.map { it.length() }.sum()
        } else {
            0L
        }
    }

    fun formatFileSize(sizeInBytes: Long): String {
        val kb = 1024.0
        val mb = kb * 1024
        val gb = mb * 1024
        
        return when {
            sizeInBytes >= gb -> String.format("%.2f GB", sizeInBytes / gb)
            sizeInBytes >= mb -> String.format("%.2f MB", sizeInBytes / mb)
            sizeInBytes >= kb -> String.format("%.2f KB", sizeInBytes / kb)
            else -> "$sizeInBytes B"
        }
    }

    suspend fun createDirectory(path: String): Boolean = withContext(Dispatchers.IO) {
        try {
            File(path).mkdirs()
        } catch (e: Exception) {
            false
        }
    }

    fun getInternalStorageDir(): File {
        return context.filesDir
    }

    fun getExternalStorageDir(): File? {
        return context.getExternalFilesDir(null)
    }

    fun getCacheDir(): File {
        return context.cacheDir
    }

    fun getAudioDir(): File {
        val audioDir = File(getExternalStorageDir(), "audio")
        audioDir.mkdirs()
        return audioDir
    }

    fun getBackupDir(): File {
        val backupDir = File(getExternalStorageDir(), "backups")
        backupDir.mkdirs()
        return backupDir
    }

    fun getTempDir(): File {
        val tempDir = File(getCacheDir(), "temp")
        tempDir.mkdirs()
        return tempDir
    }

    suspend fun listFiles(directory: File): List<File> = withContext(Dispatchers.IO) {
        directory.listFiles()?.toList() ?: emptyList()
    }

    suspend fun searchFiles(directory: File, query: String): List<File> = withContext(Dispatchers.IO) {
        directory.walkTopDown()
            .filter { it.isFile && it.name.contains(query, ignoreCase = true) }
            .toList()
    }

    fun getFileExtension(file: File): String {
        return file.extension
    }

    fun getMimeType(file: File): String {
        return when (file.extension.lowercase()) {
            "mp3" -> "audio/mpeg"
            "wav" -> "audio/wav"
            "ogg" -> "audio/ogg"
            "m4a" -> "audio/mp4"
            "zip" -> "application/zip"
            "json" -> "application/json"
            "xml" -> "application/xml"
            "txt" -> "text/plain"
            "pdf" -> "application/pdf"
            else -> "application/octet-stream"
        }
    }
}
