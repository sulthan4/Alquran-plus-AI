 package com.alquranplusai.android.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.alquranplusai.android.services.AlQuranNotificationManager

/**
 * Reminder Receiver
 * Handles scheduled reading reminders
 */
class ReminderReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = AlQuranNotificationManager(context)
        
        notificationManager.showReminderNotification(
            title = "Time to Read Quran",
            message = "Continue your daily reading streak!"
        )
    }
}
