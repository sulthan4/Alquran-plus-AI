package com.alquranplusai.data.repositories

import com.alquranplusai.data.database.AlQuranDatabaseWrapper
import com.alquranplusai.domain.models.*
import com.alquranplusai.domain.repositories.DownloadRepository
import com.alquranplusai.platform.local.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

/**
 * Implementation of DownloadRepository
 * Note: This is a simplified implementation. In production, you'd integrate with
 * Android's DownloadManager or WorkManager for actual file downloads.
 */
class DownloadRepositoryImpl(
    private val database: AlQuranDatabaseWrapper,
    private val preferences: PreferencesManager
) : DownloadRepository {

    private val json = Json { ignoreUnknownKeys = true }
    private val downloadItems = mutableMapOf<String, DownloadItem>()
    
    override fun getAllDownloads(): Flow<List<DownloadItem>> = flow {
        emit(downloadItems.values.toList())
    }
    
    override fun getDownloadsByType(type: DownloadType): Flow<List<DownloadItem>> = flow {
        emit(downloadItems.values.filter { it.type == type })
    }
    
    override fun getDownloadsByStatus(status: DownloadStatus): Flow<List<DownloadItem>> = flow {
        emit(downloadItems.values.filter { it.status == status })
    }
    
    override fun getDownload(id: String): Flow<DownloadItem?> = flow {
        emit(downloadItems[id])
    }
    
    override suspend fun addToQueue(item: DownloadItem): Boolean {
        return try {
            downloadItems[item.id] = item.copy(
                status = DownloadStatus.PENDING,
                createdAt = Clock.System.now().toEpochMilliseconds()
            )
            saveDownloadsToPreferences()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun startDownload(id: String): Flow<DownloadProgress> = flow {
        val item = downloadItems[id] ?: return@flow
        
        try {
            // Update status to downloading
            downloadItems[id] = item.copy(
                status = DownloadStatus.DOWNLOADING,
                updatedAt = Clock.System.now().toEpochMilliseconds()
            )
            
            // Simulate download progress
            // In production, this would integrate with actual download mechanism
            for (progress in 0..100 step 10) {
                val downloadedBytes = (item.size * progress / 100)
                emit(DownloadProgress(
                    downloadedBytes = downloadedBytes,
                    totalBytes = item.size,
                    progress = progress / 100f,
                    status = DownloadStatus.DOWNLOADING
                ))
                
                // Update item
                downloadItems[id] = item.copy(
                    progress = progress / 100f,
                    downloadedBytes = downloadedBytes,
                    status = DownloadStatus.DOWNLOADING,
                    updatedAt = Clock.System.now().toEpochMilliseconds()
                )
                
                kotlinx.coroutines.delay(100) // Simulate download time
            }
            
            // Mark as completed
            downloadItems[id] = item.copy(
                status = DownloadStatus.COMPLETED,
                progress = 1f,
                downloadedBytes = item.size,
                updatedAt = Clock.System.now().toEpochMilliseconds()
            )
            
            emit(DownloadProgress(
                downloadedBytes = item.size,
                totalBytes = item.size,
                progress = 1f,
                status = DownloadStatus.COMPLETED
            ))
            
            saveDownloadsToPreferences()
        } catch (e: Exception) {
            downloadItems[id] = item.copy(
                status = DownloadStatus.FAILED,
                updatedAt = Clock.System.now().toEpochMilliseconds()
            )
            emit(DownloadProgress(
                downloadedBytes = item.downloadedBytes,
                totalBytes = item.size,
                progress = item.progress,
                status = DownloadStatus.FAILED
            ))
        }
    }
    
    override suspend fun pauseDownload(id: String): Boolean {
        return try {
            downloadItems[id]?.let { item ->
                downloadItems[id] = item.copy(
                    status = DownloadStatus.PAUSED,
                    updatedAt = Clock.System.now().toEpochMilliseconds()
                )
                saveDownloadsToPreferences()
                true
            } ?: false
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun resumeDownload(id: String): Boolean {
        return try {
            downloadItems[id]?.let { item ->
                if (item.status == DownloadStatus.PAUSED) {
                    downloadItems[id] = item.copy(
                        status = DownloadStatus.PENDING,
                        updatedAt = Clock.System.now().toEpochMilliseconds()
                    )
                    saveDownloadsToPreferences()
                    true
                } else false
            } ?: false
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun cancelDownload(id: String): Boolean {
        return try {
            downloadItems[id]?.let { item ->
                downloadItems[id] = item.copy(
                    status = DownloadStatus.CANCELLED,
                    updatedAt = Clock.System.now().toEpochMilliseconds()
                )
                saveDownloadsToPreferences()
                true
            } ?: false
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun retryDownload(id: String): Boolean {
        return try {
            downloadItems[id]?.let { item ->
                if (item.status == DownloadStatus.FAILED) {
                    downloadItems[id] = item.copy(
                        status = DownloadStatus.PENDING,
                        progress = 0f,
                        downloadedBytes = 0,
                        updatedAt = Clock.System.now().toEpochMilliseconds()
                    )
                    saveDownloadsToPreferences()
                    true
                } else false
            } ?: false
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun deleteDownload(id: String): Boolean {
        return try {
            downloadItems.remove(id)
            saveDownloadsToPreferences()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override fun getDownloadQueue(): Flow<DownloadQueue> = flow {
        val constraints = getConstraintsFromPreferences()
        emit(DownloadQueue(
            items = downloadItems.values.filter { 
                it.status == DownloadStatus.PENDING || it.status == DownloadStatus.DOWNLOADING 
            }.sortedBy { it.priority },
            maxConcurrentDownloads = 3,
            requiresWifi = constraints.requiresWifi,
            requiresCharging = constraints.requiresCharging
        ))
    }
    
    override suspend fun updateConstraints(constraints: DownloadConstraints) {
        val jsonString = json.encodeToString(constraints)
        preferences.putString("download_constraints", jsonString)
    }
    
    override fun getStatistics(): Flow<DownloadStatistics> = flow {
        val items = downloadItems.values
        emit(DownloadStatistics(
            totalDownloads = items.size,
            completedDownloads = items.count { it.status == DownloadStatus.COMPLETED },
            failedDownloads = items.count { it.status == DownloadStatus.FAILED },
            totalBytesDownloaded = items.filter { it.status == DownloadStatus.COMPLETED }
                .sumOf { it.size },
            averageSpeed = 1024 * 1024 // 1 MB/s average
        ))
    }
    
    override suspend fun clearCompleted(): Int {
        val completed = downloadItems.values.filter { it.status == DownloadStatus.COMPLETED }
        completed.forEach { downloadItems.remove(it.id) }
        saveDownloadsToPreferences()
        return completed.size
    }
    
    override suspend fun clearFailed(): Int {
        val failed = downloadItems.values.filter { it.status == DownloadStatus.FAILED }
        failed.forEach { downloadItems.remove(it.id) }
        saveDownloadsToPreferences()
        return failed.size
    }
    
    private suspend fun saveDownloadsToPreferences() {
        val jsonString = json.encodeToString(downloadItems.values.toList())
        preferences.putString("download_items", jsonString)
    }
    
    private suspend fun loadDownloadsFromPreferences() {
        val jsonString = preferences.getString("download_items", "")
        if (jsonString.isNotEmpty()) {
            try {
                val items = json.decodeFromString<List<DownloadItem>>(jsonString)
                items.forEach { downloadItems[it.id] = it }
            } catch (e: Exception) {
                // Ignore parsing errors
            }
        }
    }
    
    private suspend fun getConstraintsFromPreferences(): DownloadConstraints {
        val jsonString = preferences.getString("download_constraints", "")
        return if (jsonString.isNotEmpty()) {
            try {
                json.decodeFromString(jsonString)
            } catch (e: Exception) {
                DownloadConstraints()
            }
        } else {
            DownloadConstraints()
        }
    }
    
    init {
        // Load downloads asynchronously
        kotlinx.coroutines.GlobalScope.launch {
            loadDownloadsFromPreferences()
        }
    }
}
