package com.alquranplusai.android.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.alquranplusai.android.R
import java.util.*

/**
 * Reminder Service for daily Quran reading reminders
 */
class ReminderService {
    
    companion object {
        private const val REMINDER_REQUEST_CODE = 1001
        
        /**
         * Schedule daily reminder
         */
        fun scheduleDailyReminder(
            context: Context,
            hour: Int,
            minute: Int,
            message: String = "Time for your daily Quran reading"
        ) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, com.alquranplusai.android.receivers.ReminderReceiver::class.java).apply {
                putExtra("message", message)
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                REMINDER_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            // Set calendar to specified time
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                
                // If time has passed today, schedule for tomorrow
                if (before(Calendar.getInstance())) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            
            // Schedule repeating alarm
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
        
        /**
         * Cancel daily reminder
         */
        fun cancelDailyReminder(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, com.alquranplusai.android.receivers.ReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                REMINDER_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            alarmManager.cancel(pendingIntent)
        }
        
        /**
         * Check if reminder is scheduled
         */
        fun isReminderScheduled(context: Context): Boolean {
            val intent = Intent(context, com.alquranplusai.android.receivers.ReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                REMINDER_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            
            return pendingIntent != null
        }
    }
}

/**
 * Reminder Receiver handled in com.alquranplusai.android.receivers.ReminderReceiver
 */
