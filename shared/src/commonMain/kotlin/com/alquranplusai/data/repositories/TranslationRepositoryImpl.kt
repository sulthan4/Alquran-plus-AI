package com.alquranplusai.data.repositories

import com.alquranplusai.data.database.AlQuranDatabaseWrapper
import com.alquranplusai.domain.models.*
import com.alquranplusai.data.network.api.TranslationApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.alquranplusai.domain.repositories.TranslationRepository

class TranslationRepositoryImpl(
    private val database: AlQuranDatabaseWrapper,
    private val api: TranslationApiService
) : TranslationRepository {

    override fun getAllTranslations(): Flow<List<Translation>> = flow {
        // Sync if empty
        val count = database.translationQueries.selectAllTranslations().executeAsList().size
        if (count == 0) {
            try {
                val remoteTranslators = api.getAllTranslators()
                remoteTranslators.forEach {
                    database.translationQueries.insertTranslation(
                        id = it.id.toString(),
                        name = it.name,
                        author = it.name,
                        language = it.languageName,
                        languageCode = it.languageCode,
                        direction = "LTR", // Default
                        type = "TRANSLATION",
                        isDownloaded = 0,
                        downloadSize = 0,
                        version = "1.0",
                        lastUpdated = 0,
                        description = null,
                        source = null,
                        copyright = null,
                        website = null,
                        completeness = 100
                    )
                }
            } catch (e: Exception) {
                // Log error
            }
        }
        
        val translations = database.translationQueries.selectAllTranslations().executeAsList().map {
            Translation(
                id = it.id,
                name = it.name,
                author = it.author,
                language = it.language,
                languageCode = it.languageCode,
                isDownloaded = it.isDownloaded == 1L
            )
        }
        emit(translations)
    }

    override fun getTranslationById(id: String): Flow<Translation?> = flow {
        val entity = database.translationQueries.selectTranslationById(id).executeAsOneOrNull()
        emit(entity?.let {
            Translation(
                id = it.id,
                name = it.name,
                author = it.author,
                language = it.language,
                languageCode = it.languageCode,
                isDownloaded = it.isDownloaded == 1L
            )
        })
    }

    override fun getTranslationsByLanguage(languageCode: String): Flow<List<Translation>> = flow {
        val translations = database.translationQueries.selectTranslationsByLanguage(languageCode).executeAsList().map {
            Translation(
                id = it.id,
                name = it.name,
                author = it.author,
                language = it.language,
                languageCode = it.languageCode,
                isDownloaded = it.isDownloaded == 1L
            )
        }
        emit(translations)
    }

    override fun getDownloadedTranslations(): Flow<List<Translation>> = flow {
        val translations = database.translationQueries.selectDownloadedTranslations().executeAsList().map {
            Translation(
                id = it.id,
                name = it.name,
                author = it.author,
                language = it.language,
                languageCode = it.languageCode,
                isDownloaded = it.isDownloaded == 1L
            )
        }
        emit(translations)
    }

    override fun downloadTranslation(translationId: String): Flow<DownloadProgress> = flow {
        emit(DownloadProgress(0, 0, 0f, DownloadStatus.DOWNLOADING))
        try {
            // In a real app, we'd fetch all 114 surahs
            // For MVP, we fetch on demand or trigger a background sync
            // Here we mark it as downloaded for now
            database.translationQueries.updateTranslationDownloaded(1L, translationId)
            emit(DownloadProgress(100, 100, 1f, DownloadStatus.COMPLETED))
        } catch (e: Exception) {
            emit(DownloadProgress(0, 0, 0f, DownloadStatus.FAILED))
        }
    }

    override suspend fun deleteTranslation(translationId: String) {
        database.translationQueries.updateTranslationDownloaded(0L, translationId)
        database.translationQueries.deleteAyahTranslationsByTranslationId(translationId)
    }

    override fun getAyahTranslations(
        surahNumber: Int,
        ayahNumber: Int,
        translationIds: List<String>
    ): Flow<List<AyahTranslation>> = flow {
        val entities = database.translationQueries.selectAyahTranslations(
            surahNumber.toLong(),
            ayahNumber.toLong(),
            translationIds
        ).executeAsList()
        
        if (entities.isEmpty() && translationIds.isNotEmpty()) {
            // Optional: trigger on-demand sync for this ayah/surah
            syncSurahTranslations(surahNumber, translationIds)
            val fresh = database.translationQueries.selectAyahTranslations(
                surahNumber.toLong(),
                ayahNumber.toLong(),
                translationIds
            ).executeAsList()
            emit(fresh.map {
                AyahTranslation(
                    translationId = it.translationId,
                    surahNumber = it.surahNumber.toInt(),
                    ayahNumber = it.ayahNumber.toInt(),
                    text = it.text
                )
            })
        } else {
            emit(entities.map {
                AyahTranslation(
                    translationId = it.translationId,
                    surahNumber = it.surahNumber.toInt(),
                    ayahNumber = it.ayahNumber.toInt(),
                    text = it.text
                )
            })
        }
    }

    private suspend fun syncSurahTranslations(surahNumber: Int, translationIds: List<String>) {
        translationIds.forEach { translationId ->
            try {
                val remoteTranslations = api.getTranslationsBySurah(surahNumber, translationId.toInt())
                database.transaction {
                    remoteTranslations.forEachIndexed { index, dto ->
                        database.translationQueries.insertAyahTranslation(
                            translationId = translationId,
                            surahNumber = surahNumber.toLong(),
                            ayahNumber = (index + 1).toLong(),
                            text = dto.text
                        )
                    }
                }
            } catch (e: Exception) {
                // Log error
            }
        }
    }

    override fun getAyahTranslation(
        surahNumber: Int,
        ayahNumber: Int,
        translationId: String
    ): Flow<AyahTranslation?> = flow {
        val entity = database.translationQueries.selectAyahTranslation(
            surahNumber.toLong(),
            ayahNumber.toLong(),
            translationId
        ).executeAsOneOrNull()
        
        emit(entity?.let {
            AyahTranslation(
                translationId = it.translationId,
                surahNumber = it.surahNumber.toInt(),
                ayahNumber = it.ayahNumber.toInt(),
                text = it.text
            )
        })
    }

    override fun getWordTranslations(
        wordId: Long,
        translationIds: List<String>
    ): Flow<List<WordTranslation>> = flow {
        val entities = database.translationQueries.selectWordTranslations(wordId, translationIds).executeAsList()
        emit(entities.map {
            WordTranslation(
                wordId = it.wordId,
                translationId = it.translationId,
                translation = it.translation,
                transliteration = it.transliteration
            )
        })
    }


    override fun getSupportedLanguages(): Flow<List<Language>> = flow {
        // Hardcoded or fetched from unique languageCodes in translations
        emit(listOf(
            Language("en", "English", "English"),
            Language("ar", "Arabic", "العربية"),
            Language("ur", "Urdu", "اردو")
        ))
    }
}
