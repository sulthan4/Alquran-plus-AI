package com.alquranplusai.android.services

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.alquranplusai.android.MainActivity
import com.alquranplusai.android.R

object NotificationBuilder {
    
    fun buildReminderNotification(
        context: Context,
        title: String,
        message: String
    ): NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(context, AlQuranNotificationManager.CHANNEL_REMINDERS)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }
    
    fun buildDownloadNotification(
        context: Context,
        title: String,
        progress: Int
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, AlQuranNotificationManager.CHANNEL_DOWNLOADS)
            .setContentTitle(title)
            .setContentText("Downloading...")
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setProgress(100, progress, false)
            .setOngoing(true)
    }
    
    fun buildGeneralNotification(
        context: Context,
        title: String,
        message: String,
        actionText: String? = null,
        actionIntent: PendingIntent? = null
    ): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, AlQuranNotificationManager.CHANNEL_GENERAL)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
        
        if (actionText != null && actionIntent != null) {
            builder.addAction(0, actionText, actionIntent)
        }
        
        return builder
    }
}
