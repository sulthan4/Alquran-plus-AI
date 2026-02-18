package com.alquranplusai.data.database.entity

/**
 * Entity representing a Surah row from the SQLDelight database.
 * Maps all 9 columns from Surah.sq schema.
 */
data class SurahEntity(
    val number: Int,
    val name: String,
    val nameArabic: String,
    val nameTransliteration: String,
    val nameTranslation: String,
    val revelationType: String,
    val numberOfAyahs: Int,
    val bismillahPre: Boolean,
    val rukuCount: Int
)
