package com.alquranplusai.android.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class QuranDownloadWorker(context: Context, params: WorkerParameters) :
        CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val dataType = inputData.getString("data_type") ?: "quran_text"

            // Simulate Quran data download
            setProgress(workDataOf("progress" to 30, "status" to "Downloading $dataType"))
            kotlinx.coroutines.delay(1000)
            setProgress(workDataOf("progress" to 70, "status" to "Processing $dataType"))
            kotlinx.coroutines.delay(500)
            setProgress(workDataOf("progress" to 100, "status" to "Complete"))

            Result.success(workDataOf("downloaded" to dataType))
        } catch (e: Exception) {
            Result.failure(workDataOf("error" to e.message))
        }
    }
}
