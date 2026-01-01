package com.alquranplusai.android.workers

import android.content.Context
import androidx.work.*
import com.alquranplusai.android.services.ReminderService
import java.util.concurrent.TimeUnit

/**
 * Reminder Worker for scheduling reminders
 */
class ReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val hour = inputData.getInt("hour", 9)
            val minute = inputData.getInt("minute", 0)
            val message = inputData.getString("message") ?: "Time for your daily Quran reading"
            
            // Schedule the reminder
            ReminderService.scheduleDailyReminder(context, hour, minute, message)
            
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
    
    companion object {
        const val WORK_NAME = "reminder_work"
        
        /**
         * Schedule reminder worker
         */
        fun scheduleReminder(
            context: Context,
            hour: Int,
            minute: Int,
            message: String = "Time for your daily Quran reading"
        ) {
            val inputData = workDataOf(
                "hour" to hour,
                "minute" to minute,
                "message" to message
            )
            
            val reminderRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInputData(inputData)
                .build()
            
            WorkManager.getInstance(context).enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                reminderRequest
            )
        }
        
        /**
         * Cancel reminder
         */
        fun cancelReminder(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
            ReminderService.cancelDailyReminder(context)
        }
    }
}
