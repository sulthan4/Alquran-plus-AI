package com.alquranplusai.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuranFoundationChaptersResponse(
    val chapters: List<QuranFoundationChapterDto>
)

@Serializable
data class QuranFoundationChapterDto(
    val id: Int,
    @SerialName("revelation_place") val revelationPlace: String,
    @SerialName("revelation_order") val revelationOrder: Int,
    @SerialName("bismillah_pre") val bismillahPre: Boolean,
    @SerialName("name_simple") val nameSimple: String,
    @SerialName("name_complex") val nameComplex: String,
    @SerialName("name_arabic") val nameArabic: String,
    @SerialName("verses_count") val versesCount: Int,
    val pages: List<Int>,
    @SerialName("translated_name") val translatedName: TranslatedNameDto
)

@Serializable
data class TranslatedNameDto(
    @SerialName("language_name") val languageName: String? = null,
    @SerialName("name") val name: String? = null
)

@Serializable
data class QuranFoundationVersesResponse(
    val verses: List<QuranFoundationVerseDto>,
    val pagination: PaginationDto? = null
)

@Serializable
data class QuranFoundationVerseDto(
    val id: Int,
    @SerialName("verse_number") val verseNumber: Int,
    @SerialName("verse_key") val verseKey: String,
    @SerialName("hizb_number") val hizbNumber: Int,
    @SerialName("rub_el_hizb_number") val rubElHizbNumber: Int,
    @SerialName("ruku_number") val rukuNumber: Int,
    @SerialName("manzil_number") val manzilNumber: Int,
    @SerialName("sajdah_number") val sajdahNumber: Int? = null,
    @SerialName("text_uthmani") val textUthmani: String? = null,
    @SerialName("page_number") val pageNumber: Int,
    @SerialName("juz_number") val juzNumber: Int,
    val words: List<QuranFoundationWordDto> = emptyList()
)

@Serializable
data class QuranFoundationWordDto(
    val id: Int? = null, // Pause marks might not have IDs in some versions
    val position: Int? = null, // Not present in search results
    @SerialName("audio_url") val audioUrl: String? = null,
    @SerialName("char_type_name") val charTypeName: String? = null, // Used in verse endpoint
    @SerialName("char_type") val charType: String? = null, // Used in search endpoint
    @SerialName("code_v1") val codeV1: String? = null,
    @SerialName("text_uthmani") val textUthmani: String? = null,
    val text: String? = null,
    val translation: TranslatedNameDto? = null,
    val transliteration: TranslatedNameDto? = null
) {
    // Helper to get the char type from whichever field is present
    val resolvedCharType: String
        get() = charTypeName ?: charType ?: "word"
}

@Serializable
data class PaginationDto(
    @SerialName("per_page") val perPage: Int,
    @SerialName("current_page") val currentPage: Int,
    @SerialName("next_page") val nextPage: Int? = null,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_records") val totalRecords: Int
)

@Serializable
data class QuranFoundationRecitersResponse(
    val recitations: List<QuranFoundationReciterDto>,
    val pagination: PaginationDto? = null
)

@Serializable
data class QuranFoundationReciterDto(
    val id: Int,
    @SerialName("reciter_name") val reciterName: String,
    @SerialName("style") val style: String? = null,
    @SerialName("translated_name") val translatedName: TranslatedNameDto? = null,
    @SerialName("has_word_timing") val hasWordTiming: Boolean = false
)

@Serializable
data class QuranFoundationChapterRecitationResponse(
    @SerialName("audio_files") val audioFiles: List<QuranFoundationAudioFileDto>,
    val pagination: PaginationDto? = null
)

@Serializable
data class QuranFoundationSingleChapterRecitationResponse(
    @SerialName("audio_file") val audioFile: QuranFoundationAudioFileDto
)

@Serializable
data class QuranFoundationAudioFileDto(
    val id: Int,
    @SerialName("chapter_id") val chapterId: Int,
    @SerialName("file_size") val fileSize: Double,
    @SerialName("format") val format: String,
    @SerialName("audio_url") val audioUrl: String,
    @SerialName("timestamps") val verseTimings: List<QuranFoundationVerseTimingDto> = emptyList()
)

@Serializable
data class QuranFoundationVerseTimingDto(
    @SerialName("verse_key") val verseKey: String,
    @SerialName("timestamp_from") val timestampFrom: Long,
    @SerialName("timestamp_to") val timestampTo: Long,
    val duration: Long,
    val segments: List<List<Double>> = emptyList() // [word_index, start_time, end_time]
)

@Serializable
data class QuranFoundationTranslationsResponse(
    val translations: List<QuranFoundationTranslationResourceDto>
)

@Serializable
data class QuranFoundationTranslationResourceDto(
    val id: Int,
    val name: String,
    @SerialName("author_name") val authorName: String,
    val slug: String? = null,
    @SerialName("language_name") val languageName: String,
    @SerialName("translated_name") val translatedName: TranslatedNameDto? = null
)

@Serializable
data class QuranFoundationTafsirsResponse(
    val tafsirs: List<QuranFoundationTafsirResourceDto>
)

@Serializable
data class QuranFoundationTafsirResourceDto(
    val id: Int,
    val name: String,
    @SerialName("author_name") val authorName: String,
    val slug: String? = null,
    @SerialName("language_name") val languageName: String,
    @SerialName("translated_name") val translatedName: TranslatedNameDto? = null
)

@Serializable
data class QuranFoundationVerseTranslationsResponse(
    val translations: List<QuranFoundationVerseTranslationDto>,
    val meta: QuranFoundationTranslationMetaDto? = null
)

@Serializable
data class QuranFoundationVerseTranslationDto(
    @SerialName("resource_id") val resourceId: Int,
    val text: String
)

@Serializable
data class QuranFoundationTranslationMetaDto(
    @SerialName("translation_name") val translationName: String? = null,
    @SerialName("author_name") val authorName: String? = null
)

@Serializable
data class QuranFoundationVerseTafsirResponse(
    val tafsir: QuranFoundationTafsirDto
)

@Serializable
data class QuranFoundationTafsirDto(
    val id: Int,
    @SerialName("resource_id") val resourceId: Int,
    val text: String,
    @SerialName("resource_name") val resourceName: String? = null
)

@Serializable
data class QuranFoundationSearchResponse(
    val search: QuranFoundationSearchDataDto
)

@Serializable
data class QuranFoundationSearchDataDto(
    val query: String,
    @SerialName("total_results") val totalResults: Int,
    @SerialName("current_page") val currentPage: Int,
    @SerialName("total_pages") val totalPages: Int,
    val results: List<QuranFoundationSearchResultDto> = emptyList()
)

@Serializable
data class QuranFoundationSearchResultDto(
    @SerialName("verse_id") val verseId: Int? = null,
    @SerialName("verse_key") val verseKey: String,
    val text: String,
    val highlighted: String? = null,
    val words: List<QuranFoundationWordDto> = emptyList(),
    val translations: List<QuranFoundationVerseTranslationDto> = emptyList()
)
