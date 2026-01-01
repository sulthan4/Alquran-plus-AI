package com.alquranplusai.domain.models

import kotlinx.serialization.Serializable

/**
 * Enhanced translation pack model for downloadable translations
 */
@Serializable
data class TranslationPack(
    val id: String,
    val translationId: String,
    val name: String,
    val language: String,
    val languageCode: String,
    val translator: String,
    val description: String,
    val downloadSize: Long,
    val isDownloaded: Boolean = false,
    val version: String = "1.0",
    val lastUpdated: Long = 0
)

/**
 * User's translation preferences
 */
@Serializable
data class TranslationPreference(
    val userId: String = "default",
    val selectedTranslationIds: List<String> = emptyList(),
    val displayMode: TranslationDisplayMode = TranslationDisplayMode.SINGLE,
    val defaultTranslationId: String? = null
)

/**
 * How translations should be displayed
 */
@Serializable
enum class TranslationDisplayMode {
    SINGLE,        // Show one translation at a time
    SIDE_BY_SIDE,  // Show multiple translations side-by-side
    STACKED        // Show multiple translations stacked vertically
}

/**
 * Download progress for translation packs
 */
@Serializable
data class TranslationDownloadProgress(
    val translationId: String,
    val downloadedAyahs: Int,
    val totalAyahs: Int,
    val progress: Float,
    val status: DownloadStatus
)
