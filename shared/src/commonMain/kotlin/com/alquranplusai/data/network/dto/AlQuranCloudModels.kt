package com.alquranplusai.data.network.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class APIResponse<T>(
    val code: Int,
    val status: String,
    val data: T
)

@Serializable
data class AlQuranSurahDto(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val revelationType: String,
    val numberOfAyahs: Int
)

@Serializable
data class AlQuranSurahDetailDto(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val revelationType: String,
    val numberOfAyahs: Int,
    val ayahs: List<AlQuranAyahDto>
)

@Serializable
data class AlQuranAyahDto(
    val number: Int,
    val text: String,
    val numberInSurah: Int,
    val juz: Int,
    val manzil: Int,
    val page: Int,
    val ruku: Int,
    val hizbQuarter: Int,
    // val sajda: Boolean = false // Commented out to avoid type mismatch during simple fetch
)

// For simple sajda boolean handling, we might need a custom serializer if the API returns mixed types.
// But mostly for "simple" edition it might be consistent.
// However, AlQuran Cloud returns an object for sajda if it's true, or false/boolean if not.
// Let's assume for listing Surahs we don't need Ayahs yet.
