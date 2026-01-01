package com.alquranplusai.domain.models

import kotlinx.serialization.Serializable

/**
 * Represents a translation resource
 */
@Serializable
data class Translation(
    val id: String,
    val name: String,
    val author: String,
    val language: String,
    val languageCode: String,
    val direction: TextDirection = TextDirection.LTR,
    val type: TranslationType = TranslationType.TRANSLATION,
    val isDownloaded: Boolean = false,
    val downloadSize: Long = 0,
    val version: String = "1.0",
    val lastUpdated: Long = 0,
    val metadata: TranslationMetadata? = null
)

/**
 * Translation for a specific Ayah
 */
@Serializable
data class AyahTranslation(
    val translationId: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val text: String,
    val footnotes: List<Footnote> = emptyList()
)

/**
 * Word-by-word translation
 */
@Serializable
data class WordTranslation(
    val wordId: Long,
    val translationId: String,
    val translation: String,
    val transliteration: String? = null
)

/**
 * Metadata about a translation
 */
@Serializable
data class TranslationMetadata(
    val description: String,
    val source: String,
    val copyright: String? = null,
    val website: String? = null,
    val completeness: Int = 100,
    val tags: List<String> = emptyList()
)

/**
 * Footnote for additional context
 */
@Serializable
data class Footnote(
    val id: String,
    val text: String,
    val reference: String? = null
)

/**
 * Type of translation resource
 */
@Serializable
enum class TranslationType {
    TRANSLATION,
    TAFSIR,
    TRANSLITERATION,
    WORD_BY_WORD
}

/**
 * Text direction
 */
@Serializable
enum class TextDirection {
    LTR,
    RTL
}

/**
 * Language information
 */
@Serializable
data class Language(
    val code: String,
    val name: String,
    val nativeName: String,
    val direction: TextDirection = TextDirection.LTR,
    val isSupported: Boolean = true
)
