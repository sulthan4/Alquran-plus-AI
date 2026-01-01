package com.alquranplusai.domain.repositories

import com.alquranplusai.domain.models.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing downloads
 */
interface DownloadRepository {
    
    /**
     * Get all download items
     */
    fun getAllDownloads(): Flow<List<DownloadItem>>
    
    /**
     * Get downloads by type
     */
    fun getDownloadsByType(type: DownloadType): Flow<List<DownloadItem>>
    
    /**
     * Get downloads by status
     */
    fun getDownloadsByStatus(status: DownloadStatus): Flow<List<DownloadItem>>
    
    /**
     * Get a specific download
     */
    fun getDownload(id: String): Flow<DownloadItem?>
    
    /**
     * Add item to download queue
     */
    suspend fun addToQueue(item: DownloadItem): Boolean
    
    /**
     * Start download
     */
    suspend fun startDownload(id: String): Flow<DownloadProgress>
    
    /**
     * Pause download
     */
    suspend fun pauseDownload(id: String): Boolean
    
    /**
     * Resume download
     */
    suspend fun resumeDownload(id: String): Boolean
    
    /**
     * Cancel download
     */
    suspend fun cancelDownload(id: String): Boolean
    
    /**
     * Retry failed download
     */
    suspend fun retryDownload(id: String): Boolean
    
    /**
     * Delete download
     */
    suspend fun deleteDownload(id: String): Boolean
    
    /**
     * Get download queue
     */
    fun getDownloadQueue(): Flow<DownloadQueue>
    
    /**
     * Update download constraints
     */
    suspend fun updateConstraints(constraints: DownloadConstraints)
    
    /**
     * Get download statistics
     */
    fun getStatistics(): Flow<DownloadStatistics>
    
    /**
     * Clear completed downloads
     */
    suspend fun clearCompleted(): Int
    
    /**
     * Clear failed downloads
     */
    suspend fun clearFailed(): Int
}
