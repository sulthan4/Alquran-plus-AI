package com.alquranplusai.android.workers

import android.content.Context
import androidx.work.*
import com.alquranplusai.domain.models.DownloadItem
import com.alquranplusai.domain.repositories.DownloadRepository
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

/**
 * Worker for downloading content in background using WorkManager
 */
class DownloadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {
    
    private val downloadRepository: DownloadRepository by inject()
    
    companion object {
        const val KEY_DOWNLOAD_ID = "download_id"
        const val WORK_NAME_PREFIX = "download_"
        
        fun enqueue(context: Context, downloadId: String) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresStorageNotLow(true)
                .build()
            
            val data = workDataOf(KEY_DOWNLOAD_ID to downloadId)
            
            val request = OneTimeWorkRequestBuilder<DownloadWorker>()
                .setConstraints(constraints)
                .setInputData(data)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .addTag("downloads")
                .build()
            
            WorkManager.getInstance(context).enqueueUniqueWork(
                "$WORK_NAME_PREFIX$downloadId",
                ExistingWorkPolicy.KEEP,
                request
            )
        }
        
        fun cancel(context: Context, downloadId: String) {
            WorkManager.getInstance(context).cancelUniqueWork("$WORK_NAME_PREFIX$downloadId")
        }
    }
    
    override suspend fun doWork(): Result {
        val downloadId = inputData.getString(KEY_DOWNLOAD_ID) ?: return Result.failure()
        
        return try {
            setProgress(workDataOf("progress" to 0))
            
            var lastProgress = 0
            downloadRepository.startDownload(downloadId).collect { progress ->
                val currentProgress = (progress.progress * 100).toInt()
                if (currentProgress != lastProgress) {
                    setProgress(workDataOf("progress" to currentProgress))
                    lastProgress = currentProgress
                }
            }
            
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure(workDataOf("error" to (e.message ?: "Unknown error")))
            }
        }
    }
}
