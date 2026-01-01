package com.alquranplusai.domain.models

import kotlinx.serialization.Serializable

/**
 * Represents a downloadable item (audio, translation, tafsir, etc.)
 */
@Serializable
data class DownloadItem(
    val id: String,
    val type: DownloadType,
    val name: String,
    val url: String,
    val size: Long,
    val status: DownloadStatus = DownloadStatus.PENDING,
    val progress: Float = 0f,
    val downloadedBytes: Long = 0,
    val priority: Int = 0,
    val metadata: Map<String, String> = emptyMap(),
    val createdAt: Long = 0,
    val updatedAt: Long = 0
)

/**
 * Type of downloadable content
 */
@Serializable
enum class DownloadType {
    AUDIO,
    TRANSLATION,
    TAFSIR,
    QURAN_TEXT,
    FONT,
    OTHER
}

/**
 * Download queue configuration
 */
@Serializable
data class DownloadQueue(
    val items: List<DownloadItem> = emptyList(),
    val maxConcurrentDownloads: Int = 3,
    val requiresWifi: Boolean = true,
    val requiresCharging: Boolean = false
)

/**
 * Download constraints
 */
@Serializable
data class DownloadConstraints(
    val requiresWifi: Boolean = true,
    val requiresCharging: Boolean = false,
    val requiresDeviceIdle: Boolean = false,
    val requiresStorageNotLow: Boolean = true
)

/**
 * Download statistics
 */
@Serializable
data class DownloadStatistics(
    val totalDownloads: Int = 0,
    val completedDownloads: Int = 0,
    val failedDownloads: Int = 0,
    val totalBytesDownloaded: Long = 0,
    val averageSpeed: Long = 0
)
