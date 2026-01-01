package com.alquranplusai.android.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.alquranplusai.android.R

/**
 * Helper class for managing notifications
 */
class NotificationHelper(private val context: Context) {
    
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    
    companion object {
        const val CHANNEL_DOWNLOADS = "downloads"
        const val CHANNEL_REMINDERS = "reminders"
        const val CHANNEL_GENERAL = "general"
        
        const val NOTIFICATION_DOWNLOAD = 1001
        const val NOTIFICATION_REMINDER = 1002
    }
    
    init {
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val downloadChannel = NotificationChannel(
                CHANNEL_DOWNLOADS,
                "Downloads",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Download progress notifications"
            }
            
            val reminderChannel = NotificationChannel(
                CHANNEL_REMINDERS,
                "Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Prayer and reading reminders"
            }
            
            val generalChannel = NotificationChannel(
                CHANNEL_GENERAL,
                "General",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "General notifications"
            }
            
            notificationManager.createNotificationChannels(
                listOf(downloadChannel, reminderChannel, generalChannel)
            )
        }
    }
    
    fun showDownloadProgress(
        downloadId: String,
        title: String,
        progress: Int,
        max: Int = 100
    ) {
        val notification = NotificationCompat.Builder(context, CHANNEL_DOWNLOADS)
            .setContentTitle(title)
            .setContentText("Downloading...")
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setProgress(max, progress, false)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        
        notificationManager.notify(downloadId.hashCode(), notification)
    }
    
    fun showDownloadComplete(downloadId: String, title: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_DOWNLOADS)
            .setContentTitle(title)
            .setContentText("Download complete")
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        
        notificationManager.notify(downloadId.hashCode(), notification)
    }
    
    fun showDownloadFailed(downloadId: String, title: String, error: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_DOWNLOADS)
            .setContentTitle(title)
            .setContentText("Download failed: $error")
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        
        notificationManager.notify(downloadId.hashCode(), notification)
    }
    
    fun cancelDownloadNotification(downloadId: String) {
        notificationManager.cancel(downloadId.hashCode())
    }
    
    fun showReminder(title: String, message: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_REMINDERS)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        
        notificationManager.notify(NOTIFICATION_REMINDER, notification)
    }
}
