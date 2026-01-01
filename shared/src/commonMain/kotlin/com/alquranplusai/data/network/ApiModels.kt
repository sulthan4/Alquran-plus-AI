package com.alquranplusai.data.network

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val code: Int,
    val status: String,
    val data: T
)

@Serializable
data class SurahResponse(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val revelationType: String,
    val ayahs: List<AyahResponse>
)

@Serializable
data class AyahResponse(
    val number: Int,
    val text: String,
    val numberInSurah: Int,
    val juz: Int,
    val manzil: Int,
    val page: Int,
    val ruku: Int,
    val hizbQuarter: Int,
    val sajda: Boolean? = null
)

@Serializable
data class EditionResponse(
    val identifier: String,
    val language: String,
    val name: String,
    val englishName: String,
    val format: String,
    val type: String,
    val direction: String? = null
)

@Serializable
data class SearchResponse(
    val count: Int,
    val matches: List<SearchMatch>
)

@Serializable
data class SearchMatch(
    val number: Int,
    val text: String,
    val edition: EditionResponse,
    val surah: SurahInfo
)

@Serializable
data class SurahInfo(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val revelationType: String,
    val numberOfAyahs: Int
)
