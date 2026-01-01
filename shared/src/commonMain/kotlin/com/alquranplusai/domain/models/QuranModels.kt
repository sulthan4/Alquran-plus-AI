package com.alquranplusai.domain.models

import kotlinx.serialization.Serializable

/**
 * Represents a Surah (chapter) in the Quran
 */
@Serializable
data class Surah(
    val number: Int,
    val name: String,
    val nameArabic: String,
    val nameTransliteration: String,
    val nameTranslation: String,
    val revelationType: RevelationType,
    val numberOfAyahs: Int,
    val bismillahPre: Boolean = true,
    val juzNumbers: List<Int> = emptyList(),
    val pageNumbers: List<Int> = emptyList(),
    val manzilNumber: Int? = null,
    val rukuCount: Int = 0
)

/**
 * Represents an Ayah (verse) in the Quran
 */
@Serializable
data class Ayah(
    val id: Long,
    val surahNumber: Int,
    val ayahNumber: Int,
    val text: String,
    val textUthmani: String,
    val textSimple: String,
    val juzNumber: Int,
    val hizbNumber: Int,
    val rukuNumber: Int,
    val manzilNumber: Int,
    val pageNumber: Int,
    val sajdaType: SajdaType? = null,
    val sajdaNumber: Int? = null,
    val words: List<Word> = emptyList(),
    val translations: List<AyahTranslation> = emptyList()
)

/**
 * Represents a single word in an Ayah with detailed linguistic information
 */
@Serializable
data class Word(
    val id: Long,
    val ayahId: Long,
    val position: Int,
    val text: String,
    val textUthmani: String,
    val textSimple: String,
    val translation: String? = null,
    val transliteration: String? = null,
    val root: String? = null,
    val lemma: String? = null,
    val grammar: GrammarInfo? = null,
    val occurrenceCount: Int = 0,
    val audioUrl: String? = null
)

/**
 * Grammar information for a word
 */
@Serializable
data class GrammarInfo(
    val partOfSpeech: String,
    val derivation: String? = null,
    val mood: String? = null,
    val case: String? = null,
    val person: String? = null,
    val number: String? = null,
    val gender: String? = null,
    val verbal: String? = null,
    val state: String? = null,
    val aspect: String? = null,
    val form: String? = null,
    val voice: String? = null
)

/**
 * Represents a Juz (part) of the Quran
 */
@Serializable
data class Juz(
    val number: Int,
    val startSurah: Int,
    val startAyah: Int,
    val endSurah: Int,
    val endAyah: Int,
    val ayahCount: Int
)

/**
 * Represents a page in the Mushaf
 */
@Serializable
data class Page(
    val number: Int,
    val juzNumber: Int,
    val startSurah: Int,
    val startAyah: Int,
    val endSurah: Int,
    val endAyah: Int,
    val ayahs: List<Ayah> = emptyList()
)

/**
 * Represents a Manzil (7 divisions for weekly reading)
 */
@Serializable
data class Manzil(
    val number: Int,
    val startSurah: Int,
    val startAyah: Int,
    val endSurah: Int,
    val endAyah: Int,
    val ayahCount: Int
)

/**
 * Represents a Hizb (60 divisions)
 */
@Serializable
data class Hizb(
    val number: Int,
    val quarter: Int,
    val juzNumber: Int,
    val startSurah: Int,
    val startAyah: Int,
    val endSurah: Int,
    val endAyah: Int
)

/**
 * Represents a Ruku (thematic section)
 */
@Serializable
data class Ruku(
    val number: Int,
    val surahNumber: Int,
    val startAyah: Int,
    val endAyah: Int,
    val theme: String? = null
)

/**
 * Information about a Sajda (prostration) ayah
 */
@Serializable
data class SajdaInfo(
    val number: Int,
    val surahNumber: Int,
    val ayahNumber: Int,
    val type: SajdaType,
    val recommended: Boolean = true
)

/**
 * Type of revelation
 */
@Serializable
enum class RevelationType {
    MECCAN,
    MEDINAN
}

/**
 * Type of Sajda
 */
@Serializable
enum class SajdaType {
    OBLIGATORY,
    RECOMMENDED
}

/**
 * Reading mode preference
 */
@Serializable
enum class ReadingMode {
    CONTINUOUS,
    PAGE_BY_PAGE,
    AYAH_BY_AYAH,
    MUSHAF
}

/**
 * Text display type
 */
@Serializable
enum class TextType {
    UTHMANI,
    SIMPLE,
    IMLAAI
}
