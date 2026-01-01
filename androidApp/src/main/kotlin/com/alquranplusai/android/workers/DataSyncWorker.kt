package com.alquranplusai.android.workers

import android.content.Context
import androidx.work.*
import com.alquranplusai.data.sync.SyncManager
import kotlinx.coroutines.flow.collect
import java.util.concurrent.TimeUnit

/**
 * Data Sync Worker for background synchronization
 */
class DataSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    private val syncManager = SyncManager()
    
    override suspend fun doWork(): Result {
        return try {
            // Check if we have network connectivity
            if (!hasNetworkConnection()) {
                return Result.retry()
            }
            
            // Perform sync
            var syncSuccessful = true
            syncManager.syncAll().collect { progress ->
                when (progress) {
                    is SyncManager.SyncProgress.Failed -> {
                        syncSuccessful = false
                    }
                    else -> {
                        // Update progress
                        setProgress(workDataOf("status" to progress.toString()))
                    }
                }
            }
            
            if (syncSuccessful) {
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
    
    private fun hasNetworkConnection(): Boolean {
        // TODO: Implement actual network check
        return true
    }
    
    companion object {
        const val WORK_NAME = "data_sync_work"
        
        /**
         * Schedule periodic sync
         */
        fun schedulePeriodic(context: Context, intervalHours: Long = 6) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
            
            val syncRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(
                intervalHours, TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    15, TimeUnit.MINUTES
                )
                .build()
            
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                syncRequest
            )
        }
        
        /**
         * Trigger immediate sync
         */
        fun syncNow(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            
            val syncRequest = OneTimeWorkRequestBuilder<DataSyncWorker>()
                .setConstraints(constraints)
                .build()
            
            WorkManager.getInstance(context).enqueue(syncRequest)
        }
        
        /**
         * Cancel sync
         */
        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
