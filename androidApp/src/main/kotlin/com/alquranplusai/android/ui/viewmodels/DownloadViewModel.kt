package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.*
import com.alquranplusai.domain.repositories.DownloadRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for download management
 */
class DownloadViewModel(
    private val downloadRepository: DownloadRepository
) : ViewModel() {

    // All downloads
    private val _downloads = MutableStateFlow<List<DownloadItem>>(emptyList())
    val downloads: StateFlow<List<DownloadItem>> = _downloads.asStateFlow()

    // Download queue
    private val _queue = MutableStateFlow<DownloadQueue>(DownloadQueue())
    val queue: StateFlow<DownloadQueue> = _queue.asStateFlow()

    // Download statistics
    private val _statistics = MutableStateFlow<DownloadStatistics>(DownloadStatistics())
    val statistics: StateFlow<DownloadStatistics> = _statistics.asStateFlow()

    // Current download progress
    private val _currentProgress = MutableStateFlow<Map<String, DownloadProgress>>(emptyMap())
    val currentProgress: StateFlow<Map<String, DownloadProgress>> = _currentProgress.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadDownloads()
        loadQueue()
        loadStatistics()
    }

    /**
     * Load all downloads
     */
    fun loadDownloads() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                downloadRepository.getAllDownloads().collect { items ->
                    _downloads.value = items
                }
            } catch (e: Exception) {
                _error.value = "Failed to load downloads: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Load download queue
     */
    fun loadQueue() {
        viewModelScope.launch {
            try {
                downloadRepository.getDownloadQueue().collect { queue ->
                    _queue.value = queue
                }
            } catch (e: Exception) {
                _error.value = "Failed to load queue: ${e.message}"
            }
        }
    }

    /**
     * Load statistics
     */
    fun loadStatistics() {
        viewModelScope.launch {
            try {
                downloadRepository.getStatistics().collect { stats ->
                    _statistics.value = stats
                }
            } catch (e: Exception) {
                _error.value = "Failed to load statistics: ${e.message}"
            }
        }
    }

    /**
     * Add item to download queue
     */
    fun addToQueue(item: DownloadItem) {
        viewModelScope.launch {
            try {
                val success = downloadRepository.addToQueue(item)
                if (success) {
                    loadDownloads()
                    loadQueue()
                } else {
                    _error.value = "Failed to add to queue"
                }
            } catch (e: Exception) {
                _error.value = "Error adding to queue: ${e.message}"
            }
        }
    }

    /**
     * Start download
     */
    fun startDownload(id: String) {
        viewModelScope.launch {
            try {
                downloadRepository.startDownload(id).collect { progress ->
                    _currentProgress.value = _currentProgress.value + (id to progress)
                    if (progress.status == DownloadStatus.COMPLETED) {
                        loadDownloads()
                        loadStatistics()
                    }
                }
            } catch (e: Exception) {
                _error.value = "Download failed: ${e.message}"
            }
        }
    }

    /**
     * Pause download
     */
    fun pauseDownload(id: String) {
        viewModelScope.launch {
            try {
                val success = downloadRepository.pauseDownload(id)
                if (success) {
                    loadDownloads()
                } else {
                    _error.value = "Failed to pause download"
                }
            } catch (e: Exception) {
                _error.value = "Error pausing download: ${e.message}"
            }
        }
    }

    /**
     * Resume download
     */
    fun resumeDownload(id: String) {
        viewModelScope.launch {
            try {
                val success = downloadRepository.resumeDownload(id)
                if (success) {
                    startDownload(id)
                } else {
                    _error.value = "Failed to resume download"
                }
            } catch (e: Exception) {
                _error.value = "Error resuming download: ${e.message}"
            }
        }
    }

    /**
     * Cancel download
     */
    fun cancelDownload(id: String) {
        viewModelScope.launch {
            try {
                val success = downloadRepository.cancelDownload(id)
                if (success) {
                    loadDownloads()
                } else {
                    _error.value = "Failed to cancel download"
                }
            } catch (e: Exception) {
                _error.value = "Error cancelling download: ${e.message}"
            }
        }
    }

    /**
     * Retry failed download
     */
    fun retryDownload(id: String) {
        viewModelScope.launch {
            try {
                val success = downloadRepository.retryDownload(id)
                if (success) {
                    startDownload(id)
                } else {
                    _error.value = "Failed to retry download"
                }
            } catch (e: Exception) {
                _error.value = "Error retrying download: ${e.message}"
            }
        }
    }

    /**
     * Delete download
     */
    fun deleteDownload(id: String) {
        viewModelScope.launch {
            try {
                val success = downloadRepository.deleteDownload(id)
                if (success) {
                    loadDownloads()
                    loadStatistics()
                } else {
                    _error.value = "Failed to delete download"
                }
            } catch (e: Exception) {
                _error.value = "Error deleting download: ${e.message}"
            }
        }
    }

    /**
     * Update download constraints
     */
    fun updateConstraints(constraints: DownloadConstraints) {
        viewModelScope.launch {
            try {
                downloadRepository.updateConstraints(constraints)
                loadQueue()
            } catch (e: Exception) {
                _error.value = "Failed to update constraints: ${e.message}"
            }
        }
    }

    /**
     * Clear completed downloads
     */
    fun clearCompleted() {
        viewModelScope.launch {
            try {
                val count = downloadRepository.clearCompleted()
                loadDownloads()
                loadStatistics()
            } catch (e: Exception) {
                _error.value = "Failed to clear completed: ${e.message}"
            }
        }
    }

    /**
     * Clear failed downloads
     */
    fun clearFailed() {
        viewModelScope.launch {
            try {
                val count = downloadRepository.clearFailed()
                loadDownloads()
                loadStatistics()
            } catch (e: Exception) {
                _error.value = "Failed to clear failed: ${e.message}"
            }
        }
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _error.value = null
    }
}
