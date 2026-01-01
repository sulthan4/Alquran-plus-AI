package com.alquranplusai.android.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.alquranplusai.android.workers.DataSyncWorker
import java.util.concurrent.TimeUnit

/**
 * Boot Receiver
 * Reschedules alarms after device reboot
 */
class BootReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule reading reminders using WorkManager
            val workRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(
                24, TimeUnit.HOURS
            ).build()
            WorkManager.getInstance(context).enqueue(workRequest)
            
            // Restart background services if needed
            // Services will be started automatically by the system if they are persistent
        }
    }
}
