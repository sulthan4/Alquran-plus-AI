package com.alquranplusai.domain.repositories

import com.alquranplusai.domain.models.*
import kotlinx.coroutines.flow.Flow

/** Repository for translation and tafsir operations */
interface TranslationRepository {
    // Translation management
    fun getAllTranslations(): Flow<List<Translation>>
    fun getTranslationById(id: String): Flow<Translation?>
    fun getTranslationsByLanguage(languageCode: String): Flow<List<Translation>>
    fun getDownloadedTranslations(): Flow<List<Translation>>
    fun downloadTranslation(translationId: String): Flow<DownloadProgress>
    suspend fun deleteTranslation(translationId: String)

    // Ayah translations
    fun getAyahTranslations(
            surahNumber: Int,
            ayahNumber: Int,
            translationIds: List<String>
    ): Flow<List<AyahTranslation>>
    fun getAyahTranslation(
            surahNumber: Int,
            ayahNumber: Int,
            translationId: String
    ): Flow<AyahTranslation?>

    // Word-by-word translations
    fun getWordTranslations(wordId: Long, translationIds: List<String>): Flow<List<WordTranslation>>

    // Language operations
    fun getSupportedLanguages(): Flow<List<Language>>
}
