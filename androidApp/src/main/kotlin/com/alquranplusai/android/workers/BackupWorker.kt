package com.alquranplusai.android.workers

import android.content.Context
import androidx.work.*
import com.alquranplusai.android.services.BackupService
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

/**
 * Backup Worker for automated backups
 */
class BackupWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val includeAudio = inputData.getBoolean("include_audio", false)
            val uploadToCloud = inputData.getBoolean("upload_to_cloud", false)
            
            val backupService = BackupService(context)
            
            // Create backup
            val backupResult = backupService.createBackup(includeAudio)
            
            when (backupResult) {
                is BackupService.BackupResult.Success -> {
                    // Optionally upload to cloud
                    if (uploadToCloud) {
                        val uploadResult = backupService.uploadToCloud(backupResult.path)
                        when (uploadResult) {
                            is BackupService.CloudUploadResult.Success -> {
                                Result.success(workDataOf(
                                    "backup_path" to backupResult.path,
                                    "cloud_path" to uploadResult.cloudPath
                                ))
                            }
                            is BackupService.CloudUploadResult.Error -> {
                                // Backup created but upload failed
                                Result.success(workDataOf(
                                    "backup_path" to backupResult.path,
                                    "upload_error" to uploadResult.message
                                ))
                            }
                        }
                    } else {
                        Result.success(workDataOf("backup_path" to backupResult.path))
                    }
                }
                is BackupService.BackupResult.Error -> {
                    Result.failure(workDataOf("error" to backupResult.message))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
    
    companion object {
        const val WORK_NAME = "backup_work"
        
        /**
         * Schedule periodic backups
         */
        fun schedulePeriodic(
            context: Context,
            intervalDays: Long = 7,
            includeAudio: Boolean = false,
            uploadToCloud: Boolean = false
        ) {
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiresDeviceIdle(true)
                .build()
            
            val inputData = workDataOf(
                "include_audio" to includeAudio,
                "upload_to_cloud" to uploadToCloud
            )
            
            val backupRequest = PeriodicWorkRequestBuilder<BackupWorker>(
                intervalDays, TimeUnit.DAYS
            )
                .setConstraints(constraints)
                .setInputData(inputData)
                .build()
            
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                backupRequest
            )
        }
        
        /**
         * Trigger immediate backup
         */
        fun backupNow(
            context: Context,
            includeAudio: Boolean = false,
            uploadToCloud: Boolean = false
        ) {
            val inputData = workDataOf(
                "include_audio" to includeAudio,
                "upload_to_cloud" to uploadToCloud
            )
            
            val backupRequest = OneTimeWorkRequestBuilder<BackupWorker>()
                .setInputData(inputData)
                .build()
            
            WorkManager.getInstance(context).enqueue(backupRequest)
        }
        
        /**
         * Cancel scheduled backups
         */
        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
