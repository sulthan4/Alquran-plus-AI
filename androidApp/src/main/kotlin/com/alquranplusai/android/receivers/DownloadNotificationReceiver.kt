package com.alquranplusai.android.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.alquranplusai.android.utils.NotificationHelper

/**
 * Broadcast receiver for download notifications
 */
class DownloadNotificationReceiver : BroadcastReceiver() {
    
    companion object {
        const val ACTION_PAUSE = "com.alquranplusai.DOWNLOAD_PAUSE"
        const val ACTION_RESUME = "com.alquranplusai.DOWNLOAD_RESUME"
        const val ACTION_CANCEL = "com.alquranplusai.DOWNLOAD_CANCEL"
        const val EXTRA_DOWNLOAD_ID = "download_id"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        val downloadId = intent.getStringExtra(EXTRA_DOWNLOAD_ID) ?: return
        
        when (intent.action) {
            ACTION_PAUSE -> {
                // Pause download
                // This would call DownloadRepository.pauseDownload(downloadId)
            }
            ACTION_RESUME -> {
                // Resume download
                // This would call DownloadRepository.resumeDownload(downloadId)
            }
            ACTION_CANCEL -> {
                // Cancel download
                // This would call DownloadRepository.cancelDownload(downloadId)
                val notificationHelper = NotificationHelper(context)
                notificationHelper.cancelDownloadNotification(downloadId)
            }
        }
    }
}
