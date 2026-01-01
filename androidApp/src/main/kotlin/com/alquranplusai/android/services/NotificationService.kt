package com.alquranplusai.android.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.alquranplusai.android.MainActivity
import com.alquranplusai.android.R

class NotificationService : Service() {
    
    private lateinit var notificationManager: NotificationManager
    
    companion object {
        const val CHANNEL_ID_GENERAL = "general_notifications"
        const val CHANNEL_ID_REMINDERS = "reminder_notifications"
        const val CHANNEL_ID_DOWNLOADS = "download_notifications"
        
        fun showNotification(context: Context, title: String, message: String, channelId: String = CHANNEL_ID_GENERAL) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            val notification = NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
            
            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannels()
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun createNotificationChannels() {
        val generalChannel = NotificationChannel(
            CHANNEL_ID_GENERAL,
            "General Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "General app notifications"
        }
        
        val reminderChannel = NotificationChannel(
            CHANNEL_ID_REMINDERS,
            "Reading Reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Daily reading reminders"
        }
        
        val downloadChannel = NotificationChannel(
            CHANNEL_ID_DOWNLOADS,
            "Downloads",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Download progress notifications"
        }
        
        notificationManager.createNotificationChannel(generalChannel)
        notificationManager.createNotificationChannel(reminderChannel)
        notificationManager.createNotificationChannel(downloadChannel)
    }
}
