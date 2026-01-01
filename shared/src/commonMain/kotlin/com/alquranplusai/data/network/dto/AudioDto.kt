package com.alquranplusai.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class AudioDto(
    val id: Int,
    val reciterId: Int,
    val surahNumber: Int,
    val ayahNumber: Int? = null,
    val url: String,
    val format: String = "mp3",
    val quality: String = "high",
    val fileSize: Long = 0,
    val verseTimings: List<VerseTimingDto> = emptyList()
)

@Serializable
data class VerseTimingDto(
    val verseKey: String,
    val from: Long,
    val to: Long,
    val segments: List<List<Double>>
)

@Serializable
data class ReciterDto(
    val id: Int,
    val name: String,
    val nameArabic: String? = null,
    val style: String,
    val languageCode: String = "ar",
    val bitrate: Int = 128,
    val hasWordTiming: Boolean = false
)

@Serializable
data class PlaylistDto(
    val id: Int,
    val userId: Int,
    val name: String,
    val description: String? = null,
    val isPublic: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long
)
