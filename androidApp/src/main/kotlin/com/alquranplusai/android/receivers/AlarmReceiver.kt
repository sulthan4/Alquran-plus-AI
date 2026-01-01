package com.alquranplusai.android.receivers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.alquranplusai.android.MainActivity
import com.alquranplusai.android.R
import com.alquranplusai.android.services.NotificationService
import com.alquranplusai.android.services.ReminderService

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_ALARM_ID = "extra_alarm_id"
        const val EXTRA_ALARM_TITLE = "extra_alarm_title"
        const val EXTRA_ALARM_MESSAGE = "extra_alarm_message"
        const val EXTRA_ALARM_TYPE = "extra_alarm_type"
        
        const val TYPE_READING_REMINDER = "reading_reminder"
        const val TYPE_PRAYER_TIME = "prayer_time"
        const val TYPE_DAILY_VERSE = "daily_verse"
        const val TYPE_QUIZ_REMINDER = "quiz_reminder"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getIntExtra(EXTRA_ALARM_ID, -1)
        val title = intent.getStringExtra(EXTRA_ALARM_TITLE) ?: "Reminder"
        val message = intent.getStringExtra(EXTRA_ALARM_MESSAGE) ?: ""
        val alarmType = intent.getStringExtra(EXTRA_ALARM_TYPE) ?: TYPE_READING_REMINDER
        
        if (alarmId != -1) {
            showAlarmNotification(context, alarmId, title, message, alarmType)
            saveAlarmTriggerTime(context, alarmId)
        }
    }

    private fun showAlarmNotification(
        context: Context,
        alarmId: Int,
        title: String,
        message: String,
        alarmType: String
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) 
            as NotificationManager
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("alarm_id", alarmId)
            putExtra("alarm_type", alarmType)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val snoozeIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_NOTIFICATION_ACTION
            putExtra(NotificationReceiver.EXTRA_NOTIFICATION_ID, alarmId)
            putExtra(NotificationReceiver.EXTRA_ACTION_TYPE, NotificationReceiver.ACTION_SNOOZE)
        }
        
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId + 10000,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val dismissIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_NOTIFICATION_ACTION
            putExtra(NotificationReceiver.EXTRA_NOTIFICATION_ID, alarmId)
            putExtra(NotificationReceiver.EXTRA_ACTION_TYPE, NotificationReceiver.ACTION_DISMISS)
        }
        
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId + 20000,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, NotificationService.CHANNEL_ID_REMINDERS)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_launcher_foreground,
                "Snooze",
                snoozePendingIntent
            )
            .addAction(
                R.drawable.ic_launcher_foreground,
                "Dismiss",
                dismissPendingIntent
            )
            .build()
        
        notificationManager.notify(alarmId, notification)
    }

    private fun saveAlarmTriggerTime(context: Context, alarmId: Int) {
        val prefs = context.getSharedPreferences("alarm_prefs", Context.MODE_PRIVATE)
        prefs.edit().putLong("last_trigger_$alarmId", System.currentTimeMillis()).apply()
    }
}
