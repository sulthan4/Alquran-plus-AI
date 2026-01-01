package com.alquranplusai.data.network.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * DTO for Tafsir metadata from API
 */
@Serializable
data class TafsirDto(
    val id: String,
    val name: String,
    @SerialName("author_name")
    val authorName: String,
    @SerialName("language_name")
    val languageName: String,
    val slug: String? = null,
    @SerialName("translated_name")
    val translatedName: TranslatedNameDto? = null
)

/**
 * DTO for Tafsir text content
 */
@Serializable
data class TafsirTextDto(
    val tafsirId: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val text: String,
    val footnotes: List<String>? = null,
    val references: List<String>? = null
)
