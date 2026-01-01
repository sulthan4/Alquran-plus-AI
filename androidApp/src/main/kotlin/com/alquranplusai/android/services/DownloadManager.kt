package com.alquranplusai.android.services

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class DownloadManager(private val context: Context) {
    
    companion object {
        @Volatile
        private var INSTANCE: DownloadManager? = null
        
        fun getInstance(context: Context): DownloadManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DownloadManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    data class DownloadInfo(
        val id: String = UUID.randomUUID().toString(),
        val url: String,
        val fileName: String,
        val progress: Float = 0f,
        val status: DownloadStatus = DownloadStatus.PENDING,
        val error: String? = null
    )

    enum class DownloadStatus {
        PENDING,
        DOWNLOADING,
        COMPLETED,
        FAILED,
        PAUSED,
        CANCELLED
    }

    private val _activeDownloads = MutableStateFlow<List<DownloadInfo>>(emptyList())
    val activeDownloads: StateFlow<List<DownloadInfo>> = _activeDownloads.asStateFlow()
    
    private val _downloadQueue = MutableStateFlow<List<DownloadInfo>>(emptyList())
    val downloadQueue: StateFlow<List<DownloadInfo>> = _downloadQueue.asStateFlow()

    fun queueDownload(url: String, fileName: String): String {
        val downloadInfo = DownloadInfo(
            url = url,
            fileName = fileName,
            status = DownloadStatus.PENDING
        )
        
        val currentQueue = _downloadQueue.value.toMutableList()
        currentQueue.add(downloadInfo)
        _downloadQueue.value = currentQueue
        
        startNextDownload()
        return downloadInfo.id
    }

    fun startDownload(id: String) {
        val download = findDownload(id) ?: return
        
        if (download.status == DownloadStatus.PENDING || download.status == DownloadStatus.PAUSED) {
            updateDownloadStatus(id, DownloadStatus.DOWNLOADING)
            DownloadService.start(context, download.url, download.fileName)
        }
    }

    fun pauseDownload(id: String) {
        updateDownloadStatus(id, DownloadStatus.PAUSED)
    }

    fun cancelDownload(id: String) {
        updateDownloadStatus(id, DownloadStatus.CANCELLED)
        removeFromQueue(id)
    }

    fun retryDownload(id: String) {
        val download = findDownload(id) ?: return
        if (download.status == DownloadStatus.FAILED) {
            updateDownloadStatus(id, DownloadStatus.PENDING)
            startDownload(id)
        }
    }

    fun updateProgress(id: String, progress: Float) {
        val currentDownloads = _activeDownloads.value.toMutableList()
        val index = currentDownloads.indexOfFirst { it.id == id }
        
        if (index != -1) {
            currentDownloads[index] = currentDownloads[index].copy(progress = progress)
            _activeDownloads.value = currentDownloads
        }
    }

    fun markAsCompleted(id: String) {
        updateDownloadStatus(id, DownloadStatus.COMPLETED)
        removeFromActive(id)
        startNextDownload()
    }

    fun markAsFailed(id: String, error: String) {
        val currentDownloads = _activeDownloads.value.toMutableList()
        val index = currentDownloads.indexOfFirst { it.id == id }
        
        if (index != -1) {
            currentDownloads[index] = currentDownloads[index].copy(
                status = DownloadStatus.FAILED,
                error = error
            )
            _activeDownloads.value = currentDownloads
        }
        
        startNextDownload()
    }

    fun getDownload(id: String): DownloadInfo? = findDownload(id)

    fun clearCompleted() {
        val currentDownloads = _activeDownloads.value.filter { 
            it.status != DownloadStatus.COMPLETED 
        }
        _activeDownloads.value = currentDownloads
    }

    private fun findDownload(id: String): DownloadInfo? {
        return _activeDownloads.value.find { it.id == id }
            ?: _downloadQueue.value.find { it.id == id }
    }

    private fun updateDownloadStatus(id: String, status: DownloadStatus) {
        val currentDownloads = _activeDownloads.value.toMutableList()
        val index = currentDownloads.indexOfFirst { it.id == id }
        
        if (index != -1) {
            currentDownloads[index] = currentDownloads[index].copy(status = status)
            _activeDownloads.value = currentDownloads
        }
    }

    private fun removeFromQueue(id: String) {
        val currentQueue = _downloadQueue.value.filter { it.id != id }
        _downloadQueue.value = currentQueue
    }

    private fun removeFromActive(id: String) {
        val currentDownloads = _activeDownloads.value.filter { it.id != id }
        _activeDownloads.value = currentDownloads
    }

    private fun startNextDownload() {
        val pendingDownloads = _downloadQueue.value.filter { 
            it.status == DownloadStatus.PENDING 
        }
        
        if (pendingDownloads.isNotEmpty()) {
            val nextDownload = pendingDownloads.first()
            removeFromQueue(nextDownload.id)
            
            val currentActive = _activeDownloads.value.toMutableList()
            currentActive.add(nextDownload)
            _activeDownloads.value = currentActive
            
            startDownload(nextDownload.id)
        }
    }
}
