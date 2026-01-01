package com.alquranplusai.android.workers

import android.content.Context
import androidx.work.*
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Cleanup Worker for automated cache and temporary file cleanup
 */
class CleanupWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            // Clean temporary files
            cleanTempFiles()
            
            // Clean old cache
            cleanOldCache()
            
            // Clean expired downloads
            cleanExpiredDownloads()
            
            // Optimize database
            optimizeDatabase()
            
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
    
    private fun cleanTempFiles() {
        val tempDir = context.cacheDir
        tempDir.listFiles()?.forEach { file ->
            if (file.isFile && isOldFile(file, days = 7)) {
                file.delete()
            }
        }
    }
    
    private fun cleanOldCache() {
        val cacheDir = context.cacheDir
        val maxCacheSize = 100 * 1024 * 1024 // 100 MB
        
        val currentSize = calculateDirectorySize(cacheDir)
        if (currentSize > maxCacheSize) {
            // Delete oldest files first
            cacheDir.listFiles()
                ?.sortedBy { it.lastModified() }
                ?.forEach { file ->
                    if (calculateDirectorySize(cacheDir) > maxCacheSize) {
                        file.deleteRecursively()
                    }
                }
        }
    }
    
    private fun cleanExpiredDownloads() {
        // TODO: Implement cleanup of expired/incomplete downloads
    }
    
    private fun optimizeDatabase() {
        // TODO: Implement database optimization (VACUUM, etc.)
    }
    
    private fun isOldFile(file: File, days: Int): Boolean {
        val ageInMillis = System.currentTimeMillis() - file.lastModified()
        val daysInMillis = days * 24 * 60 * 60 * 1000L
        return ageInMillis > daysInMillis
    }
    
    private fun calculateDirectorySize(directory: File): Long {
        var size = 0L
        directory.listFiles()?.forEach { file ->
            size += if (file.isDirectory) {
                calculateDirectorySize(file)
            } else {
                file.length()
            }
        }
        return size
    }
    
    companion object {
        const val WORK_NAME = "cleanup_work"
        
        /**
         * Schedule periodic cleanup
         */
        fun schedulePeriodic(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiresDeviceIdle(true)
                .build()
            
            val cleanupRequest = PeriodicWorkRequestBuilder<CleanupWorker>(
                1, TimeUnit.DAYS
            )
                .setConstraints(constraints)
                .setInitialDelay(1, TimeUnit.HOURS)
                .build()
            
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                cleanupRequest
            )
        }
        
        /**
         * Trigger immediate cleanup
         */
        fun cleanupNow(context: Context) {
            val cleanupRequest = OneTimeWorkRequestBuilder<CleanupWorker>()
                .build()
            
            WorkManager.getInstance(context).enqueue(cleanupRequest)
        }
        
        /**
         * Cancel cleanup
         */
        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
