package com.alquranplusai.android.utils

import com.alquranplusai.data.database.AlQuranDatabaseWrapper

/**
 * Helper for seeding database with initial data
 */
object DatabaseSeeder {
    fun isDatabaseSeeded(database: AlQuranDatabaseWrapper): Boolean {
        return try {
            val surahCount = database.surahQueries.count().executeAsOne()
            // ayahs queries count is not exposed directly as "count" in common SQLDelight patterns unless defined
            // Checking Ayah.sq, there is no generic count query defined, only selectAll.
            // Let's rely on Surah count for now, or add a count query if needed.
            // Actually, let's just assume if Surahs are there, we might be seeded, 
            // but since we want to force re-seed for Ayahs..
            // We can check if any Ayah exists.
            val anyAyah = database.ayahQueries.selectAll().executeAsList().isNotEmpty()
            surahCount > 0 && anyAyah
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun seedDatabase(context: android.content.Context, database: AlQuranDatabaseWrapper) {
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            try {
                // 1. Seed Surahs
                val metadata = AssetLoader.loadQuranMetadata(context)
                if (metadata != null) {
                    database.transaction {
                        metadata.surahs.forEach { dto ->
                            database.surahQueries.insert(
                                number = dto.number.toLong(),
                                name = dto.name,
                                nameArabic = dto.name, 
                                nameTransliteration = dto.transliteration,
                                nameTranslation = dto.translation,
                                revelationType = dto.type,
                                numberOfAyahs = dto.total_verses.toLong(),
                                bismillahPre = if (dto.bismillah_pre) 1 else 0,
                                rukuCount = 0
                            )
                        }
                    }
                }
                
                // 2. Seed Ayahs (Uthmani)
                val quranText = AssetLoader.loadQuranUthmani(context)
                if (quranText != null) {
                    database.transaction {
                        quranText.surahs.forEach { surahDto ->
                            surahDto.ayahs.forEach { ayahDto ->
                                try {
                                    database.ayahQueries.insert(
                                        surahNumber = surahDto.number.toLong(),
                                        ayahNumber = ayahDto.number.toLong(),
                                        text = ayahDto.text, // Mapped to text column
                                        textUthmani = ayahDto.text,
                                        textSimple = ayahDto.text, // Mapped to textSimple (using Uthmani as fallback)
                                        juzNumber = 1, 
                                        hizbNumber = 1, // Order changed in SQ
                                        rukuNumber = 1,
                                        manzilNumber = 1, 
                                        pageNumber = 1, 
                                        sajdaType = null,
                                        sajdaNumber = null
                                    )
                                } catch (e: Exception) {
                                  // Ignore duplicate or error
                                }
                            }
                        }
                    }
                }
                
                // 3. Seed English Translation
                val translation = AssetLoader.loadTranslation(context, "en_sahih.json")
                if (translation != null) {
                    database.transaction {
                        // Register Translation Edition
                        val editionId = "en_sahih"
                        database.translationQueries.insertTranslation(
                            id = editionId,
                            name = translation.metadata?.name ?: "Sahih International",
                            author = translation.metadata?.translator ?: "Unknown",
                            language = translation.metadata?.language ?: "English",
                            languageCode = "en",
                            direction = "LTR",
                            type = "TRANSLATION",
                            isDownloaded = 1,
                            downloadSize = 0,
                            version = "1.0",
                            lastUpdated = 0,
                            description = translation.metadata?.description ?: "English Translation",
                            source = "Detailed",
                            copyright = "Free",
                            website = "",
                            completeness = 100
                        )
                        
                        translation.surahs.forEach { surahDto ->
                            surahDto.ayahs.forEach { ayahDto ->
                                database.translationQueries.insertAyahTranslation(
                                    translationId = editionId,
                                    surahNumber = surahDto.surah_number.toLong(),
                                    ayahNumber = ayahDto.ayah_number.toLong(),
                                    text = ayahDto.text
                                )
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
