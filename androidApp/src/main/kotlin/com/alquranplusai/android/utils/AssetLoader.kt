package com.alquranplusai.android.utils

import android.content.Context
import com.alquranplusai.domain.models.Reciter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import com.alquranplusai.domain.models.AudioFormat
import com.alquranplusai.domain.models.RecitationStyle
import java.io.IOException

/**
 * Utility class for loading assets from the assets folder
 */
object AssetLoader {
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    /**
     * Load reciters from assets
     */
    fun loadReciters(context: Context): List<Reciter> {
        return try {
            val jsonString = context.assets.open("audio/reciters.json")
                .bufferedReader()
                .use { it.readText() }
            
            val recitersData = json.decodeFromString<RecitersData>(jsonString)
            recitersData.reciters.map { it.toDomain() }
        } catch (e: IOException) {
            emptyList()
        }
    }
    
    /**
     * Load Quran metadata from assets
     */
    fun loadQuranMetadata(context: Context): QuranMetadataData? {
        return try {
            val jsonString = context.assets.open("quran/quran_metadata.json")
                .bufferedReader()
                .use { it.readText() }
            
            json.decodeFromString<QuranMetadataData>(jsonString)
        } catch (e: IOException) {
            null
        }
    }
    
    /**
     * Load JSON file from assets
     */
    fun loadJsonFromAssets(context: Context, fileName: String): String? {
        return try {
            context.assets.open(fileName)
                .bufferedReader()
                .use { it.readText() }
        } catch (e: IOException) {
            null
        }
    }
    
    /**
     * Check if asset file exists
     */
    fun assetExists(context: Context, fileName: String): Boolean {
        return try {
            context.assets.open(fileName).close()
            true
        } catch (e: IOException) {
            false
        }
    }
    /**
     * Load Quran Uthmani text
     */
    fun loadQuranUthmani(context: Context): QuranTextData? {
        return try {
            val jsonString = context.assets.open("quran/quran_uthmani.json")
                .bufferedReader()
                .use { it.readText() }
            json.decodeFromString<QuranTextData>(jsonString)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Load Translation
     */
    fun loadTranslation(context: Context, fileName: String): TranslationData? {
        return try {
            val jsonString = context.assets.open("translations/$fileName")
                .bufferedReader()
                .use { it.readText() }
            json.decodeFromString<TranslationData>(jsonString)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}

@kotlinx.serialization.Serializable
data class RecitersData(
    val reciters: List<ReciterDto>,
    val metadata: ReciterMetadata
)


@kotlinx.serialization.Serializable
data class ReciterDto(
    val id: Int,
    val name: String,
    val name_ar: String,
    val style: String,
    val bitrate: String,
    val format: String,
    val url_pattern: String
) {
    fun toDomain() = Reciter(
        id = id.toString(),
        name = name,
        nameArabic = name_ar,
        style = try { RecitationStyle.valueOf(style.uppercase()) } catch (e: Exception) { RecitationStyle.MURATTAL },
        bitrate = bitrate.filter { it.isDigit() }.toIntOrNull() ?: 128,
        format = try { AudioFormat.valueOf(format.uppercase()) } catch (e: Exception) { AudioFormat.MP3 }
    )
}

@kotlinx.serialization.Serializable
data class ReciterMetadata(
    val total_reciters: Int,
    val default_reciter_id: Int,
    val supported_formats: List<String>,
    val supported_bitrates: List<String>
)

@kotlinx.serialization.Serializable
data class QuranMetadataData(
    val metadata: QuranStats,
    val surahs: List<SurahDto>
)

@kotlinx.serialization.Serializable
data class QuranStats(
    val total_surahs: Int,
    val total_ayahs: Int,
    val total_words: Int,
    val total_letters: Int
)

@kotlinx.serialization.Serializable
data class SurahDto(
    val number: Int,
    val name: String,
    val transliteration: String,
    val translation: String,
    val type: String,
    val total_verses: Int,
    val bismillah_pre: Boolean
)

@kotlinx.serialization.Serializable
data class QuranTextData(
    val surahs: List<SurahTextDto>
)

@kotlinx.serialization.Serializable
data class SurahTextDto(
    val number: Int,
    val ayahs: List<AyahTextDto>
)

@kotlinx.serialization.Serializable
data class AyahTextDto(
    val number: Int,
    val text: String
)

@kotlinx.serialization.Serializable
data class TranslationData(
    val metadata: TranslationMetadata? = null,
    val surahs: List<SurahTranslationDto>
)

@kotlinx.serialization.Serializable
data class TranslationMetadata(
    val translation_id: String,
    val name: String,
    val language: String,
    val translator: String? = null,
    val description: String? = null
)

@kotlinx.serialization.Serializable
data class SurahTranslationDto(
    val surah_number: Int,
    val ayahs: List<AyahTranslationDto>
)

@kotlinx.serialization.Serializable
data class AyahTranslationDto(
    val ayah_number: Int,
    val text: String
)
