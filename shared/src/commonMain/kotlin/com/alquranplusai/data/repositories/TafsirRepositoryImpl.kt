package com.alquranplusai.data.repositories

import com.alquranplusai.data.database.AlQuranDatabaseWrapper
import com.alquranplusai.data.network.api.TafsirApiService
import com.alquranplusai.domain.models.Tafsir
import com.alquranplusai.domain.models.TafsirText
import com.alquranplusai.domain.models.TafsirMetadata
import com.alquranplusai.domain.models.TafsirDownloadStatus
import com.alquranplusai.domain.models.DownloadStatus
import com.alquranplusai.domain.repositories.TafsirRepository
import com.alquranplusai.platform.local.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class TafsirRepositoryImpl(
    private val database: AlQuranDatabaseWrapper,
    private val api: TafsirApiService,
    private val preferences: PreferencesManager
) : TafsirRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getAllTafsirs(): Flow<List<Tafsir>> = flow {
        // Check if we have the new ID format (numeric strings like "169")
        // If not, we wipe and re-seed to migrate from old ID format (like "ibn_kathir")
        val hasNewFormat = database.tafsirQueries.selectById("169").executeAsOneOrNull() != null
        
        if (!hasNewFormat) {
            // Clear old data and re-seed with valid API IDs
            database.transaction {
                database.tafsirQueries.deleteAll()
            }
            seedDefaultTafsirs()
        }
        
        val tafsirs = database.tafsirQueries.selectAll().executeAsList().map { entity ->
            Tafsir(
                id = entity.id,
                name = entity.name,
                nameArabic = entity.nameArabic,
                author = entity.author,
                authorArabic = entity.authorArabic,
                language = entity.language,
                languageCode = entity.languageCode,
                description = entity.description,
                source = entity.source,
                isDownloaded = entity.isDownloaded == 1L,
                downloadSize = entity.downloadSize,
                version = entity.version,
                lastUpdated = entity.lastUpdated
            )
        }
        emit(tafsirs)
    }

    override suspend fun getTafsirById(tafsirId: String): Flow<Tafsir?> = flow {
        val entity = database.tafsirQueries.selectById(tafsirId).executeAsOneOrNull()
        emit(entity?.let {
            Tafsir(
                id = it.id,
                name = it.name,
                nameArabic = it.nameArabic,
                author = it.author,
                authorArabic = it.authorArabic,
                language = it.language,
                languageCode = it.languageCode,
                description = it.description,
                source = it.source,
                isDownloaded = it.isDownloaded == 1L,
                downloadSize = it.downloadSize,
                version = it.version,
                lastUpdated = it.lastUpdated
            )
        })
    }

    override suspend fun getDownloadedTafsirs(): Flow<List<Tafsir>> = flow {
        val tafsirs = database.tafsirQueries.selectDownloaded().executeAsList().map { entity ->
            Tafsir(
                id = entity.id,
                name = entity.name,
                nameArabic = entity.nameArabic,
                author = entity.author,
                authorArabic = entity.authorArabic,
                language = entity.language,
                languageCode = entity.languageCode,
                description = entity.description,
                source = entity.source,
                isDownloaded = true,
                downloadSize = entity.downloadSize,
                version = entity.version,
                lastUpdated = entity.lastUpdated
            )
        }
        emit(tafsirs)
    }

    override suspend fun getTafsirForAyah(
        tafsirId: String,
        surahNumber: Int,
        ayahNumber: Int
    ): Flow<TafsirText?> = flow {
        val entity = database.tafsirQueries.selectTafsirForAyah(
            tafsirId,
            surahNumber.toLong(),
            ayahNumber.toLong()
        ).executeAsOneOrNull()
        
        emit(entity?.let {
            TafsirText(
                id = it.id,
                tafsirId = it.tafsirId,
                surahNumber = it.surahNumber.toInt(),
                ayahNumber = it.ayahNumber.toInt(),
                text = it.text,
                footnotes = it.footnotes?.let { json.decodeFromString(it) } ?: emptyList(),
                references = it.refs?.let { json.decodeFromString(it) } ?: emptyList()
            )
        })
    }

    override suspend fun getTafsirForAyahRange(
        tafsirId: String,
        surahNumber: Int,
        fromAyah: Int,
        toAyah: Int
    ): Flow<List<TafsirText>> = flow {
        val texts = database.tafsirQueries.selectTafsirForAyahRange(
            tafsirId,
            surahNumber.toLong(),
            fromAyah.toLong(),
            toAyah.toLong()
        ).executeAsList().map { entity ->
            TafsirText(
                id = entity.id,
                tafsirId = entity.tafsirId,
                surahNumber = entity.surahNumber.toInt(),
                ayahNumber = entity.ayahNumber.toInt(),
                text = entity.text,
                footnotes = entity.footnotes?.let { json.decodeFromString(it) } ?: emptyList(),
                references = entity.refs?.let { json.decodeFromString(it) } ?: emptyList()
            )
        }
        emit(texts)
    }

    override suspend fun getTafsirForSurah(
        tafsirId: String,
        surahNumber: Int
    ): Flow<List<TafsirText>> = flow {
        val texts = database.tafsirQueries.selectTafsirForSurah(
            tafsirId,
            surahNumber.toLong()
        ).executeAsList().map { entity ->
            TafsirText(
                id = entity.id,
                tafsirId = entity.tafsirId,
                surahNumber = entity.surahNumber.toInt(),
                ayahNumber = entity.ayahNumber.toInt(),
                text = entity.text,
                footnotes = entity.footnotes?.let { json.decodeFromString(it) } ?: emptyList(),
                references = entity.refs?.let { json.decodeFromString(it) } ?: emptyList()
            )
        }
        emit(texts)
    }

    override suspend fun downloadTafsir(tafsirId: String): Flow<Float> = flow {
        try {
            emit(0.0f)
            
            // Fetch tafsir data from API
            val tafsirData = api.getTafsirData(tafsirId)
            val totalAyahs = tafsirData.size
            
            // Insert data and track progress
            tafsirData.forEachIndexed { index, dto ->
                database.transaction {
                    database.tafsirQueries.insertTafsirText(
                        tafsirId = tafsirId,
                        surahNumber = dto.surahNumber.toLong(),
                        ayahNumber = dto.ayahNumber.toLong(),
                        text = dto.text,
                        footnotes = dto.footnotes?.let { json.encodeToString(it) },
                        refs = dto.references?.let { json.encodeToString(it) }
                    )
                }
                
                // Emit progress outside transaction
                val progress = (index + 1).toFloat() / totalAyahs
                emit(progress)
            }
            
            // Mark as downloaded
            database.transaction {
                database.tafsirQueries.updateDownloadStatus(
                    isDownloaded = 1,
                    lastUpdated = Clock.System.now().toEpochMilliseconds(),
                    id = tafsirId
                )
            }
            
            emit(1.0f)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(-1.0f) // Error indicator
        }
    }

    override suspend fun deleteTafsir(tafsirId: String): Boolean {
        return try {
            database.transaction {
                database.tafsirQueries.deleteTafsirTexts(tafsirId)
                database.tafsirQueries.updateDownloadStatus(
                    isDownloaded = 0,
                    lastUpdated = Clock.System.now().toEpochMilliseconds(),
                    id = tafsirId
                )
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getTafsirMetadata(tafsirId: String): Flow<TafsirMetadata?> = flow {
        val tafsir = database.tafsirQueries.selectById(tafsirId).executeAsOneOrNull()
        val downloadedCount = database.tafsirQueries.countTafsirTexts(tafsirId).executeAsOne()
        
        emit(tafsir?.let {
            TafsirMetadata(
                tafsirId = it.id,
                totalAyahs = 6236, // Total ayahs in Quran
                downloadedAyahs = downloadedCount.toInt(),
                downloadProgress = if (it.isDownloaded == 1L) 1.0f else downloadedCount.toFloat() / 6236,
                downloadStatus = when {
                    it.isDownloaded == 1L -> DownloadStatus.COMPLETED
                    downloadedCount > 0 -> DownloadStatus.DOWNLOADING
                    else -> DownloadStatus.PENDING
                },
                lastSyncDate = it.lastUpdated
            )
        })
    }

    override suspend fun searchTafsir(
        tafsirId: String,
        query: String
    ): Flow<List<TafsirText>> = flow {
        val results = database.tafsirQueries.searchTafsirText(tafsirId, query)
            .executeAsList().map { entity ->
                TafsirText(
                    id = entity.id,
                    tafsirId = entity.tafsirId,
                    surahNumber = entity.surahNumber.toInt(),
                    ayahNumber = entity.ayahNumber.toInt(),
                    text = entity.text,
                    footnotes = entity.footnotes?.let { json.decodeFromString(it) } ?: emptyList(),
                    references = entity.refs?.let { json.decodeFromString(it) } ?: emptyList()
                )
            }
        emit(results)
    }

    override suspend fun getPreferredTafsirs(): Flow<List<String>> = flow {
        val preferred = preferences.getString("preferred_taf asirs", "")
        val tafsirIds = if (preferred.isNotEmpty()) {
            json.decodeFromString<List<String>>(preferred)
        } else {
            emptyList()
        }
        emit(tafsirIds)
    }

    override suspend fun setPreferredTafsirs(tafsirIds: List<String>) {
        val jsonString = json.encodeToString(tafsirIds)
        preferences.putString("preferred_tafsirs", jsonString)
    }

    private suspend fun seedDefaultTafsirs() {
        val defaultTafsirs = listOf(
            // Arabic Tafsirs
            Tafsir(
                id = "16",
                name = "Tafsir Muyassar",
                nameArabic = "تفسير الميسر",
                author = "King Fahd Complex",
                authorArabic = "مجمع الملك فهد",
                language = "Arabic",
                languageCode = "ar",
                description = "Simple and clear tafsir",
                source = "quran.com",
                downloadSize = 25 * 1024 * 1024
            ),
            Tafsir(
                id = "14",
                name = "Tafsir Ibn Kathir",
                nameArabic = "تفسير ابن كثير",
                author = "Ibn Kathir",
                authorArabic = "ابن كثير",
                language = "Arabic",
                languageCode = "ar",
                description = "One of the most famous and authentic tafsir books",
                source = "quran.com",
                downloadSize = 50 * 1024 * 1024
            ),
             Tafsir(
                id = "15",
                name = "Tafsir at-Tabari",
                nameArabic = "تفسير الطبري",
                author = "At-Tabari",
                authorArabic = "الطبري",
                language = "Arabic",
                languageCode = "ar",
                description = "Classical comprehensive tafsir",
                source = "quran.com",
                downloadSize = 60 * 1024 * 1024
            ),
            Tafsir(
                id = "91",
                name = "Tafsir As-Sa'di",
                nameArabic = "تفسير السعدي",
                author = "As-Sa'di",
                authorArabic = "السعدي",
                language = "Arabic",
                languageCode = "ar",
                description = "Clear and easy to understand tafsir",
                source = "quran.com",
                downloadSize = 30 * 1024 * 1024
            ),
             Tafsir(
                id = "90",
                name = "Tafsir al-Qurtubi",
                nameArabic = "تفسير القرطبي",
                author = "Al-Qurtubi",
                authorArabic = "القرطبي",
                language = "Arabic",
                languageCode = "ar",
                description = "Comprehensive tafsir with legal rulings",
                source = "quran.com",
                downloadSize = 55 * 1024 * 1024
            ),
             Tafsir(
                id = "94",
                name = "Tafsir al-Baghawi",
                nameArabic = "تفسير البغوي",
                author = "Al-Baghawi",
                authorArabic = "البغوي",
                language = "Arabic",
                languageCode = "ar",
                description = "Ma'alim at-Tanzil",
                source = "quran.com",
                downloadSize = 35 * 1024 * 1024
            ),

            // English Tafsirs
            Tafsir(
                id = "169",
                name = "Tafsir Ibn Kathir (Abridged)",
                nameArabic = "تفسير ابن كثير",
                author = "Hafiz Ibn Kathir",
                authorArabic = "ابن كثير",
                language = "English",
                languageCode = "en",
                description = "English translation of the famous Ibn Kathir tafsir",
                source = "quran.com",
                downloadSize = 45 * 1024 * 1024
            ),
            Tafsir(
                id = "168",
                name = "Ma'arif al-Qur'an",
                nameArabic = "معارف القرآن",
                author = "Mufti Muhammad Shafi",
                authorArabic = "مفتي محمد شفيع",
                language = "English",
                languageCode = "en",
                description = "Comprehensive modern tafsir",
                source = "quran.com",
                downloadSize = 50 * 1024 * 1024
            ),
            Tafsir(
                id = "817",
                name = "Tazkirul Quran",
                nameArabic = "تذكير القرآن",
                author = "Maulana Wahid Uddin Khan",
                authorArabic = "وحيد الدين خان",
                language = "English",
                languageCode = "en",
                description = "Contemporary commentary",
                source = "quran.com",
                downloadSize = 40 * 1024 * 1024
            ),

            // Urdu Tafsirs
            Tafsir(
                id = "160",
                name = "Tafsir Ibn Kathir",
                nameArabic = "تفسير ابن كثير",
                author = "Hafiz Ibn Kathir",
                authorArabic = "ابن كثير",
                language = "Urdu",
                languageCode = "ur",
                description = "Urdu translation of Ibn Kathir",
                source = "quran.com",
                downloadSize = 45 * 1024 * 1024
            ),
            Tafsir(
                id = "159",
                name = "Bayan ul Quran",
                nameArabic = "بيان القرآن",
                author = "Dr. Israr Ahmad",
                authorArabic = "د. إسرار أحمد",
                language = "Urdu",
                languageCode = "ur",
                description = "Comprehensive Urdu tafsir",
                source = "quran.com",
                downloadSize = 48 * 1024 * 1024
            ),
            Tafsir(
                id = "157",
                name = "Fi Zilal al-Quran",
                nameArabic = "في ظلال القرآن",
                author = "Sayyid Qutb",
                authorArabic = "سيد قطب",
                language = "Urdu",
                languageCode = "ur",
                description = "In the Shade of the Quran",
                source = "quran.com",
                downloadSize = 50 * 1024 * 1024
            ),
            
             // Bengali Tafsirs
            Tafsir(
                id = "165",
                name = "Tafsir Ahsanul Bayaan",
                nameArabic = "تفسير أحسن البيان",
                author = "Bayaan Foundation",
                authorArabic = "مؤسسة البيان",
                language = "Bengali",
                languageCode = "bn",
                description = "Clear Bengali tafsir",
                source = "quran.com",
                downloadSize = 35 * 1024 * 1024
            ),
             Tafsir(
                id = "166",
                name = "Tafsir Abu Bakr Zakaria",
                nameArabic = "تفسير أبو بكر زكريا",
                author = "Abu Bakr Zakaria",
                authorArabic = "أبو بكر زكريا",
                language = "Bengali",
                languageCode = "bn",
                description = "Authentic Bengali tafsir",
                source = "quran.com",
                downloadSize = 38 * 1024 * 1024
            ),
            Tafsir(
                id = "381",
                name = "Tafsir Fathul Majid",
                nameArabic = "تفسير فتح المجيد",
                author = "AbdulRahman Bin Hasan",
                authorArabic = "عبد الرحمن بن حسن",
                language = "Bengali",
                languageCode = "bn",
                description = "Detailed Bengali commentary",
                source = "quran.com",
                downloadSize = 40 * 1024 * 1024
            ),
             Tafsir(
                id = "164",
                name = "Tafseer Ibn Kathir",
                nameArabic = "تفسير ابن كثير",
                author = "Tawheed Publication",
                authorArabic = "توحيد",
                language = "Bengali",
                languageCode = "bn",
                description = "Bengali translation of Ibn Kathir",
                source = "quran.com",
                downloadSize = 45 * 1024 * 1024
            ),
            
            // Kurdish Tafsir
            Tafsir(
                id = "804",
                name = "Rebar Kurdish Tafsir",
                nameArabic = "تفسير ريبار",
                author = "Rebar",
                authorArabic = "ريبار",
                language = "Kurdish",
                languageCode = "ku",
                description = "Kurdish commentary",
                source = "quran.com",
                downloadSize = 32 * 1024 * 1024
            ),
            
            // Tamil (Using Translation ID 133 - Abdul Hameed Baqavi)
            Tafsir(
                id = "trans_133",
                name = "Tamil Bayan ul-Quran",
                nameArabic = "بيان القرآن (Translation)",
                author = "Abdul Hameed Baqavi",
                authorArabic = "عبد الحميد",
                language = "Tamil",
                languageCode = "ta",
                description = "Tamil translation (No Tafsir available)",
                source = "quran.com",
                downloadSize = 35 * 1024 * 1024
            ),
            
            // French (Using Translation ID 31 - Hamidullah)
            Tafsir(
                id = "trans_31",
                name = "Tafseer Muhammad Hamidullah",
                nameArabic = "تفسير محمد حميد الله",
                author = "Muhammad Hamidullah",
                authorArabic = "محمد حميد الله",
                language = "French",
                languageCode = "fr",
                description = "French translation",
                source = "quran.com",
                downloadSize = 36 * 1024 * 1024
            ),

            // Spanish (Using Translation ID 83 - Isa Garcia)
            Tafsir(
                id = "trans_83",
                name = "Tafseer Isa Garcia",
                nameArabic = "تفسير عيسى غارسيا",
                author = "Sheikh Isa Garcia",
                authorArabic = "عيسى غارسيا",
                language = "Spanish",
                languageCode = "es",
                description = "Spanish translation",
                source = "quran.com",
                downloadSize = 34 * 1024 * 1024
            ),
            
            // German (Using Translation ID 27 - Frank Bubenheim)
            Tafsir(
                id = "trans_27",
                name = "Tafseer Bubenheim",
                nameArabic = "تفسير بوبنهايم",
                author = "Frank Bubenheim",
                authorArabic = "فرانك بوبنهايم",
                language = "German",
                languageCode = "de",
                description = "German translation",
                source = "quran.com",
                downloadSize = 35 * 1024 * 1024
            ),
            
            // Hindi (Using Translation ID 122 - Azizul Haque)
            Tafsir(
                id = "trans_122",
                name = "Tafseer Azizul Haque",
                nameArabic = "تفسير عزيز الحق",
                author = "Maulana Azizul Haque",
                authorArabic = "عزيز الحق",
                language = "Hindi",
                languageCode = "hi",
                description = "Hindi translation",
                source = "quran.com",
                downloadSize = 36 * 1024 * 1024
            ),
            
            // Malay (Using Translation ID 39 - Basmeih)
            Tafsir(
                id = "trans_39",
                name = "Tafseer Basmeih",
                nameArabic = "تفسير بسميه",
                author = "Abdullah Muhammad Basmeih",
                authorArabic = "عبد الله محمد بسميه",
                language = "Malay",
                languageCode = "ms",
                description = "Malay translation",
                source = "quran.com",
                downloadSize = 33 * 1024 * 1024
            ),
            
            // Russian Tafsir
            Tafsir(
                id = "170",
                name = "Tafsir As-Sa'di",
                nameArabic = "تفسير السعدي",
                author = "As-Sa'di",
                authorArabic = "السعدي",
                language = "Russian",
                languageCode = "ru",
                description = "Russian translation of Sa'di",
                source = "quran.com",
                downloadSize = 30 * 1024 * 1024
            )
        )

        database.transaction {
            defaultTafsirs.forEach { tafsir ->
                database.tafsirQueries.insertTafsir(
                    id = tafsir.id,
                    name = tafsir.name,
                    nameArabic = tafsir.nameArabic,
                    author = tafsir.author,
                    authorArabic = tafsir.authorArabic,
                    language = tafsir.language,
                    languageCode = tafsir.languageCode,
                    description = tafsir.description,
                    source = tafsir.source,
                    isDownloaded = 0,
                    downloadSize = tafsir.downloadSize,
                    version = tafsir.version,
                    lastUpdated = tafsir.lastUpdated
                )
            }
        }
    }
}
