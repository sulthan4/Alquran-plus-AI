package com.alquranplusai.domain.models

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

/** Generic resource wrapper for handling loading states */
@Serializable
sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String, val code: Int? = null) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
    data object Idle : Resource<Nothing>()
}

/** Result wrapper for operations */
@Serializable
sealed class Result<out T, out E> {
    data class Success<T>(val value: T) : Result<T, Nothing>()
    data class Failure<E>(val error: E) : Result<Nothing, E>()
}

/** API response wrapper */
@Serializable
data class ApiResponse<T>(
        val success: Boolean,
        val data: T? = null,
        val message: String? = null,
        val error: ErrorModel? = null,
        val timestamp: Long = Clock.System.now().toEpochMilliseconds()
)

/** Paginated response */
@Serializable
data class PaginatedResponse<T>(
        val items: List<T>,
        val page: Int,
        val pageSize: Int,
        val totalItems: Int,
        val totalPages: Int,
        val hasNext: Boolean,
        val hasPrevious: Boolean
)

/** Download progress */
@Serializable
data class DownloadProgress(
        val downloadedBytes: Long,
        val totalBytes: Long,
        val progress: Float,
        val status: DownloadStatus
)

/** Network state */
@Serializable
data class NetworkState(
        val isConnected: Boolean,
        val connectionType: ConnectionType,
        val isMetered: Boolean = false
)

/** Loading state */
@Serializable
sealed class LoadingState {
    data object Idle : LoadingState()
    data object Loading : LoadingState()
    data class Success<T>(val data: T) : LoadingState()
    data class Error(val error: ErrorModel) : LoadingState()
}

/** Error model */
@Serializable
data class ErrorModel(
        val code: String,
        val message: String,
        val details: String? = null,
        val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
        val type: ErrorType = ErrorType.UNKNOWN
)

/** Validation result */
@Serializable
data class ValidationResult(val isValid: Boolean, val errors: List<ValidationError> = emptyList())

/** Validation error */
@Serializable
data class ValidationError(val field: String, val message: String, val code: String? = null)

/** App configuration */
@Serializable
data class AppConfig(
        val apiBaseUrl: String,
        val apiVersion: String,
        val enableAnalytics: Boolean = true,
        val enableCrashReporting: Boolean = true,
        val maxCacheSize: Long = 500 * 1024 * 1024, // 500MB
        val requestTimeout: Long = 30000,
        val features: Map<String, Boolean> = emptyMap()
)

/** Cache entry */
@Serializable
data class CacheEntry<T>(
        val key: String,
        val data: T,
        val timestamp: Long,
        val expiresAt: Long,
        val size: Long = 0
)

/** Connection type */
@Serializable
enum class ConnectionType {
    WIFI,
    CELLULAR,
    ETHERNET,
    NONE,
    UNKNOWN
}

/** Download status */
@Serializable
enum class DownloadStatus {
    IDLE,
    PENDING,
    DOWNLOADING,
    PAUSED,
    COMPLETED,
    FAILED,
    CANCELLED
}

/** Error type */
@Serializable
enum class ErrorType {
    NETWORK,
    SERVER,
    DATABASE,
    VALIDATION,
    AUTHENTICATION,
    AUTHORIZATION,
    NOT_FOUND,
    TIMEOUT,
    UNKNOWN
}

/** Sort order */
@Serializable
enum class SortOrder {
    ASCENDING,
    DESCENDING
}

/** Data source */
@Serializable
enum class DataSource {
    LOCAL,
    REMOTE,
    CACHE
}

/** Operation status */
@Serializable
enum class OperationStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    FAILED,
    CANCELLED
}

// Type aliases for convenience
typealias Folder = BookmarkFolder

typealias QuizQuestion = Question
