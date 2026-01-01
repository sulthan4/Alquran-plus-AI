package com.alquranplusai.android.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.alquranplusai.android.services.NotificationService

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_NOTIFICATION_CLICKED = "com.alquranplusai.android.ACTION_NOTIFICATION_CLICKED"
        const val ACTION_NOTIFICATION_DISMISSED = "com.alquranplusai.android.ACTION_NOTIFICATION_DISMISSED"
        const val ACTION_NOTIFICATION_ACTION = "com.alquranplusai.android.ACTION_NOTIFICATION_ACTION"
        
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
        const val EXTRA_NOTIFICATION_TYPE = "extra_notification_type"
        const val EXTRA_ACTION_TYPE = "extra_action_type"
        const val EXTRA_DATA = "extra_data"
        
        const val TYPE_REMINDER = "reminder"
        const val TYPE_DOWNLOAD = "download"
        const val TYPE_SYNC = "sync"
        const val TYPE_UPDATE = "update"
        
        const val ACTION_MARK_READ = "mark_read"
        const val ACTION_SNOOZE = "snooze"
        const val ACTION_DISMISS = "dismiss"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)
        val notificationType = intent.getStringExtra(EXTRA_NOTIFICATION_TYPE)
        
        when (action) {
            ACTION_NOTIFICATION_CLICKED -> handleNotificationClicked(
                context, notificationId, notificationType
            )
            ACTION_NOTIFICATION_DISMISSED -> handleNotificationDismissed(
                context, notificationId, notificationType
            )
            ACTION_NOTIFICATION_ACTION -> {
                val actionType = intent.getStringExtra(EXTRA_ACTION_TYPE)
                handleNotificationAction(context, notificationId, notificationType, actionType)
            }
        }
    }

    private fun handleNotificationClicked(
        context: Context,
        notificationId: Int,
        notificationType: String?
    ) {
        when (notificationType) {
            TYPE_REMINDER -> {
                val prefs = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
                prefs.edit().putLong("last_reminder_click_$notificationId", System.currentTimeMillis()).apply()
            }
            TYPE_DOWNLOAD -> {
            }
            TYPE_SYNC -> {
            }
            TYPE_UPDATE -> {
            }
        }
    }

    private fun handleNotificationDismissed(
        context: Context,
        notificationId: Int,
        notificationType: String?
    ) {
        val prefs = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        prefs.edit().putLong("last_dismiss_$notificationId", System.currentTimeMillis()).apply()
    }

    private fun handleNotificationAction(
        context: Context,
        notificationId: Int,
        notificationType: String?,
        actionType: String?
    ) {
        when (actionType) {
            ACTION_MARK_READ -> {
                val prefs = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
                prefs.edit().putBoolean("read_$notificationId", true).apply()
            }
            ACTION_SNOOZE -> {
                snoozeNotification(context, notificationId, notificationType)
            }
            ACTION_DISMISS -> {
                dismissNotification(context, notificationId)
            }
        }
    }

    private fun snoozeNotification(context: Context, notificationId: Int, notificationType: String?) {
        val snoozeTime = System.currentTimeMillis() + (10 * 60 * 1000)
        
        val prefs = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        prefs.edit().putLong("snooze_until_$notificationId", snoozeTime).apply()
    }

    private fun dismissNotification(context: Context, notificationId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) 
            as android.app.NotificationManager
        notificationManager.cancel(notificationId)
    }
}
