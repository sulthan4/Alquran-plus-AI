package com.alquranplusai.android.integration

import android.content.Context
import com.alquranplusai.android.utils.NotificationHelper
import com.alquranplusai.android.workers.DownloadWorker
import com.alquranplusai.domain.models.DownloadItem
import com.alquranplusai.domain.models.DownloadStatus
import com.alquranplusai.domain.repositories.DownloadRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Integration layer for download notifications
 */
class DownloadNotificationIntegration(
    private val context: Context,
    private val downloadRepository: DownloadRepository,
    private val notificationHelper: NotificationHelper
) {
    
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    fun initialize() {
        // Monitor all downloads and show notifications
        scope.launch {
            downloadRepository.getAllDownloads().collect { downloads ->
                downloads.forEach { download ->
                    handleDownloadNotification(download)
                }
            }
        }
    }
    
    private fun handleDownloadNotification(download: DownloadItem) {
        when (download.status) {
            DownloadStatus.DOWNLOADING -> {
                // Calculate progress percentage
                val progress = if (download.size > 0) {
                    ((download.progress * 100).toInt())
                } else {
                    0
                }
                
                notificationHelper.showDownloadProgress(
                    downloadId = download.id,
                    title = download.name,
                    progress = progress
                )
            }
            DownloadStatus.COMPLETED -> {
                notificationHelper.showDownloadComplete(
                    downloadId = download.id,
                    title = download.name
                )
            }
            DownloadStatus.FAILED -> {
                notificationHelper.showDownloadFailed(
                    downloadId = download.id,
                    title = download.name,
                    error = "Download failed"
                )
            }
            DownloadStatus.CANCELLED -> {
                notificationHelper.cancelDownloadNotification(download.id)
            }
            else -> {
                // No notification for QUEUED or PAUSED
            }
        }
    }
    
    fun startDownload(download: DownloadItem) {
        // Enqueue download with WorkManager
        DownloadWorker.enqueue(context, download.id)
        
        // Show initial notification
        notificationHelper.showDownloadProgress(
            downloadId = download.id,
            title = download.name,
            progress = 0
        )
    }
    
    fun cancelDownload(downloadId: String) {
        // Cancel WorkManager task
        DownloadWorker.cancel(context, downloadId)
        
        // Cancel notification
        notificationHelper.cancelDownloadNotification(downloadId)
    }
}
