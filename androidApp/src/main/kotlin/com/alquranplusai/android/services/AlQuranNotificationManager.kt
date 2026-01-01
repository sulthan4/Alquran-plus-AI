package com.alquranplusai.android.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.alquranplusai.android.MainActivity
import com.alquranplusai.android.R

/**
 * Notification Manager for AlQuran Plus AI
 * Handles all app notifications
 */
class AlQuranNotificationManager(private val context: Context) {
    
    private val notificationManager = 
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    
    companion object {
        const val CHANNEL_REMINDERS = "reminders"
        const val CHANNEL_DOWNLOADS = "downloads"
        const val CHANNEL_GENERAL = "general"
        
        const val NOTIFICATION_REMINDER = 1001
        const val NOTIFICATION_DOWNLOAD = 1002
    }
    
    init {
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        // Reminders channel
        val remindersChannel = NotificationChannel(
            CHANNEL_REMINDERS,
            "Reading Reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifications for daily reading reminders"
            enableVibration(true)
        }
        
        // Downloads channel
        val downloadsChannel = NotificationChannel(
            CHANNEL_DOWNLOADS,
            "Downloads",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Notifications for audio downloads"
        }
        
        // General channel
        val generalChannel = NotificationChannel(
            CHANNEL_GENERAL,
            "General",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "General app notifications"
        }
        
        notificationManager.createNotificationChannels(
            listOf(remindersChannel, downloadsChannel, generalChannel)
        )
    }
    
    fun showReminderNotification(title: String, message: String) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_REMINDERS)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        notificationManager.notify(NOTIFICATION_REMINDER, notification)
    }
    
    fun showDownloadNotification(fileName: String, progress: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_DOWNLOADS)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle("Downloading $fileName")
            .setProgress(100, progress, false)
            .setOngoing(true)
            .build()
        
        notificationManager.notify(NOTIFICATION_DOWNLOAD, notification)
    }
    
    fun cancelDownloadNotification() {
        notificationManager.cancel(NOTIFICATION_DOWNLOAD)
    }
}
