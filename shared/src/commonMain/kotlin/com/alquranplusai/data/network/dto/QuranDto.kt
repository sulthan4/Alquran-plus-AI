package com.alquranplusai.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class SurahDto(
    val id: Int,
    val number: Int,
    val name: String,
    val nameArabic: String,
    val nameTransliteration: String,
    val revelationType: String,
    val numberOfAyahs: Int,
    val bismillahPre: Boolean = true
)

@Serializable
data class AyahDto(
    val id: Int,
    val surahNumber: Int,
    val ayahNumber: Int,
    val text: String,
    val textSimple: String? = null,
    val juzNumber: Int,
    val pageNumber: Int,
    val hizbNumber: Int? = null,
    val rukuNumber: Int? = null,
    val manzilNumber: Int? = null,
    val sajdah: Boolean = false
)

@Serializable
data class WordDto(
    val id: Int,
    val ayahId: Int,
    val position: Int,
    val text: String,
    val transliteration: String? = null,
    val translation: String? = null
)
