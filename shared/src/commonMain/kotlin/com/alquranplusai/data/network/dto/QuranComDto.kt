package com.alquranplusai.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTOs for Quran.com API responses
 */

@Serializable
data class QuranComRecitersResponse(
    val recitations: List<QuranComReciter>
)

@Serializable
data class QuranComReciter(
    val id: Int,
    @SerialName("reciter_name") val reciterName: String,
    val style: String? = null,
    @SerialName("translated_name") val translatedName: QuranComTranslatedName? = null
)

@Serializable
data class QuranComTranslatedName(
    val name: String,
    @SerialName("language_name") val languageName: String
)

@Serializable
data class QuranComVerseResponse(
    val verse: QuranComVerse
)

@Serializable
data class QuranComVerse(
    val id: Int,
    @SerialName("verse_key") val verseKey: String,
    @SerialName("verse_number") val verseNumber: Int,
    val words: List<QuranComWord>,
    val audio: QuranComAudio
)

@Serializable
data class QuranComWord(
    val id: Int,
    val position: Int,
    @SerialName("audio_url") val audioUrl: String? = null,
    @SerialName("char_type_name") val charTypeName: String,
    val text: String,
    val translation: QuranComTranslation? = null,
    val transliteration: QuranComTransliteration? = null
)

@Serializable
data class QuranComTranslation(
    val text: String,
    @SerialName("language_name") val languageName: String
)

@Serializable
data class QuranComTransliteration(
    val text: String?,
    @SerialName("language_name") val languageName: String
)

@Serializable
data class QuranComAudio(
    val url: String,
    val segments: List<List<Int>> = emptyList()
)

/**
 * Helper to convert segment arrays to WordTiming objects
 * Format: [verse_index, word_position, start_time_ms, end_time_ms]
 */
fun List<Int>.toWordTiming(ayahNumber: Int): com.alquranplusai.domain.models.WordTiming {
    return com.alquranplusai.domain.models.WordTiming(
        verseNumber = ayahNumber,
        wordPosition = this[1],
        startTime = this[2].toLong(),
        endTime = this[3].toLong(),
        duration = (this[3] - this[2]).toLong()
    )
}

/**
 * Response for fetching all verses in a chapter
 */
@Serializable
data class QuranComChapterVersesResponse(
    val verses: List<QuranComChapterVerse>,
    val pagination: QuranComPagination? = null
)

@Serializable
data class QuranComChapterVerse(
    val id: Int,
    @SerialName("verse_number") val verseNumber: Int,
    @SerialName("verse_key") val verseKey: String,
    @SerialName("hizb_number") val hizbNumber: Int,
    @SerialName("rub_el_hizb_number") val rubElHizbNumber: Int? = null,
    @SerialName("ruku_number") val rukuNumber: Int,
    @SerialName("manzil_number") val manzilNumber: Int,
    @SerialName("sajdah_number") val sajdahNumber: Int? = null,
    @SerialName("page_number") val pageNumber: Int,
    @SerialName("juz_number") val juzNumber: Int,
    @SerialName("text_uthmani") val textUthmani: String? = null
)

@Serializable
data class QuranComPagination(
    @SerialName("per_page") val perPage: Int,
    @SerialName("current_page") val currentPage: Int,
    @SerialName("next_page") val nextPage: Int? = null,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_records") val totalRecords: Int
)

/**
 * Response for Uthmani text endpoint
 */
@Serializable
data class QuranComUthmaniTextResponse(
    val verses: List<QuranComUthmaniVerse>
)

@Serializable
data class QuranComUthmaniVerse(
    val id: Int,
    @SerialName("verse_key") val verseKey: String,
    @SerialName("text_uthmani") val textUthmani: String
)
