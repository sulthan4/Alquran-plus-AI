package com.alquranplusai.domain.models

import kotlinx.serialization.Serializable

/**
 * Represents a Tafsir (Quranic commentary) source
 */
@Serializable
data class Tafsir(
    val id: String,
    val name: String,
    val nameArabic: String,
    val author: String,
    val authorArabic: String,
    val language: String,
    val languageCode: String,
    val description: String,
    val source: String,
    val isDownloaded: Boolean = false,
    val downloadSize: Long = 0,
    val version: String = "1.0",
    val lastUpdated: Long = 0
)

/**
 * Represents the tafsir text for a specific ayah
 */
@Serializable
data class TafsirText(
    val id: Long = 0,
    val tafsirId: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val text: String,
    val footnotes: List<String> = emptyList(),
    val references: List<String> = emptyList()
)

/**
 * Metadata for tafsir download and management
 */
@Serializable
data class TafsirMetadata(
    val tafsirId: String,
    val totalAyahs: Int,
    val downloadedAyahs: Int,
    val downloadProgress: Float,
    val downloadStatus: DownloadStatus,
    val lastSyncDate: Long = 0
)

/**
 * Tafsir download status
 */
@Serializable
enum class TafsirDownloadStatus {
    NOT_DOWNLOADED,
    DOWNLOADING,
    DOWNLOADED,
    UPDATE_AVAILABLE,
    ERROR
}

/**
 * Popular tafsir sources
 */
object TafsirSources {
    const val IBN_KATHIR = "ibn_kathir"
    const val TABARI = "tabari"
    const val QURTUBI = "qurtubi"
    const val JALALAYN = "jalalayn"
    const val SAADI = "saadi"
    const val MAUDUDI = "maududi"
    const val BAGHAWI = "baghawi"
    
    val DEFAULT_TAFASIR = listOf(
        IBN_KATHIR,
        JALALAYN,
        SAADI
    )
}
