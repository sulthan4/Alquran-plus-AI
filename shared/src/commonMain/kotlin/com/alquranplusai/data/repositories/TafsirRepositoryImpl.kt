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
        // Check if we have all tafsirs in database (we have 25 total)
        val count = database.tafsirQueries.count().executeAsOne()
        if (count < 25L) {
            // Clear and re-seed with all default tafsirs
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
                id = "ibn_kathir",
                name = "Tafsir Ibn Kathir",
                nameArabic = "تفسير ابن كثير",
                author = "Ibn Kathir",
                authorArabic = "ابن كثير",
                language = "Arabic",
                languageCode = "ar",
                description = "One of the most famous and authentic tafsir books",
                source = "quran.com",
                downloadSize = 50 * 1024 * 1024 // 50 MB
            ),
            Tafsir(
                id = "jalalayn",
                name = "Tafsir al-Jalalayn",
                nameArabic = "تفسير الجلالين",
                author = "Jalal ad-Din al-Mahalli & Jalal ad-Din as-Suyuti",
                authorArabic = "جلال الدين المحلي وجلال الدين السيوطي",
                language = "Arabic",
                languageCode = "ar",
                description = "Concise and widely-read tafsir",
                source = "quran.com",
                downloadSize = 20 * 1024 * 1024 // 20 MB
            ),
            Tafsir(
                id = "saadi",
                name = "Tafsir As-Sa'di",
                nameArabic = "تفسير السعدي",
                author = "Abdur-Rahman as-Sa'di",
                authorArabic = "عبد الرحمن السعدي",
                language = "Arabic",
                languageCode = "ar",
                description = "Clear and easy to understand tafsir",
                source = "quran.com",
                downloadSize = 30 * 1024 * 1024 // 30 MB
            ),
            Tafsir(
                id = "tabari",
                name = "Tafsir at-Tabari",
                nameArabic = "تفسير الطبري",
                author = "Muhammad ibn Jarir at-Tabari",
                authorArabic = "محمد بن جرير الطبري",
                language = "Arabic",
                languageCode = "ar",
                description = "Classical comprehensive tafsir",
                source = "quran.com",
                downloadSize = 60 * 1024 * 1024 // 60 MB
            ),
            Tafsir(
                id = "qurtubi",
                name = "Tafsir al-Qurtubi",
                nameArabic = "تفسير القرطبي",
                author = "Al-Qurtubi",
                authorArabic = "القرطبي",
                language = "Arabic",
                languageCode = "ar",
                description = "Comprehensive tafsir with legal rulings",
                source = "quran.com",
                downloadSize = 55 * 1024 * 1024 // 55 MB
            ),
            Tafsir(
                id = "baghawi",
                name = "Tafsir al-Baghawi",
                nameArabic = "تفسير البغوي",
                author = "Al-Baghawi",
                authorArabic = "البغوي",
                language = "Arabic",
                languageCode = "ar",
                description = "Ma'alim at-Tanzil - Clear and authentic",
                source = "quran.com",
                downloadSize = 35 * 1024 * 1024 // 35 MB
            ),
            
            // English Tafsirs
            Tafsir(
                id = "maududi",
                name = "Tafhim al-Qur'an",
                nameArabic = "تفهيم القرآن",
                author = "Abul A'la Maududi",
                authorArabic = "أبو الأعلى المودودي",
                language = "English",
                languageCode = "en",
                description = "Contemporary tafsir with modern context",
                source = "quran.com",
                downloadSize = 40 * 1024 * 1024 // 40 MB
            ),
            Tafsir(
                id = "ibn_kathir_en",
                name = "Tafsir Ibn Kathir (English)",
                nameArabic = "تفسير ابن كثير",
                author = "Ibn Kathir (Translated)",
                authorArabic = "ابن كثير",
                language = "English",
                languageCode = "en",
                description = "English translation of the famous Ibn Kathir tafsir",
                source = "quran.com",
                downloadSize = 45 * 1024 * 1024 // 45 MB
            ),
            Tafsir(
                id = "maarif",
                name = "Ma'ariful Qur'an",
                nameArabic = "معارف القرآن",
                author = "Mufti Muhammad Shafi",
                authorArabic = "مفتي محمد شفيع",
                language = "English",
                languageCode = "en",
                description = "Comprehensive modern tafsir in English",
                source = "quran.com",
                downloadSize = 50 * 1024 * 1024 // 50 MB
            ),
            Tafsir(
                id = "jalalayn_en",
                name = "Tafsir al-Jalalayn (English)",
                nameArabic = "تفسير الجلالين",
                author = "Jalalayn (Translated)",
                authorArabic = "الجلالين",
                language = "English",
                languageCode = "en",
                description = "English translation of concise Jalalayn tafsir",
                source = "quran.com",
                downloadSize = 18 * 1024 * 1024 // 18 MB
            ),
            Tafsir(
                id = "saadi_en",
                name = "Tafsir As-Sa'di (English)",
                nameArabic = "تفسير السعدي",
                author = "As-Sa'di (Translated)",
                authorArabic = "السعدي",
                language = "English",
                languageCode = "en",
                description = "English translation of clear Sa'di tafsir",
                source = "quran.com",
                downloadSize = 28 * 1024 * 1024 // 28 MB
            ),
            Tafsir(
                id = "quraish_shihab",
                name = "Tafsir Al-Misbah",
                nameArabic = "تفسير المصباح",
                author = "M. Quraish Shihab",
                authorArabic = "محمد قريش شهاب",
                language = "Indonesian",
                languageCode = "id",
                description = "Popular Indonesian tafsir",
                source = "quran.com",
                downloadSize = 42 * 1024 * 1024 // 42 MB
            ),
            
            // Tamil Tafsirs
            Tafsir(
                id = "tamil_bayan",
                name = "Tamil Bayan ul-Quran",
                nameArabic = "بيان القرآن",
                author = "Maulana Ashraf Ali Thanvi (Tamil Translation)",
                authorArabic = "أشرف علي التهانوي",
                language = "Tamil",
                languageCode = "ta",
                description = "Popular Tamil translation and commentary",
                source = "quran.com",
                downloadSize = 38 * 1024 * 1024 // 38 MB
            ),
            Tafsir(
                id = "tamil_mujahid",
                name = "Tamil Tafsir Mujahid",
                nameArabic = "تفسير مجاهد",
                author = "Mujahid Abdul Hameed",
                authorArabic = "مجاهد عبد الحميد",
                language = "Tamil",
                languageCode = "ta",
                description = "Comprehensive Tamil tafsir",
                source = "quran.com",
                downloadSize = 35 * 1024 * 1024 // 35 MB
            ),
            
            // Urdu Tafsirs
            Tafsir(
                id = "urdu_kanzul_iman",
                name = "Kanz ul-Iman",
                nameArabic = "كنز الإيمان",
                author = "Ahmed Raza Khan Barelvi",
                authorArabic = "أحمد رضا خان بريلوي",
                language = "Urdu",
                languageCode = "ur",
                description = "Popular Urdu translation with commentary",
                source = "quran.com",
                downloadSize = 32 * 1024 * 1024 // 32 MB
            ),
            Tafsir(
                id = "urdu_tafheem",
                name = "Tafheem ul-Quran (Urdu)",
                nameArabic = "تفہیم القرآن",
                author = "Abul A'la Maududi",
                authorArabic = "أبو الأعلى المودودی",
                language = "Urdu",
                languageCode = "ur",
                description = "Original Urdu version of Tafheem",
                source = "quran.com",
                downloadSize = 48 * 1024 * 1024 // 48 MB
            ),
            
            // Turkish Tafsir
            Tafsir(
                id = "turkish_diyanet",
                name = "Diyanet İşleri Tefsiri",
                nameArabic = "تفسير ديانت",
                author = "Turkish Directorate of Religious Affairs",
                authorArabic = "رئاسة الشؤون الدينية التركية",
                language = "Turkish",
                languageCode = "tr",
                description = "Official Turkish government tafsir",
                source = "quran.com",
                downloadSize = 40 * 1024 * 1024 // 40 MB
            ),
            
            // French Tafsir
            Tafsir(
                id = "french_hamidullah",
                name = "Tafsir Muhammad Hamidullah",
                nameArabic = "تفسير محمد حميد الله",
                author = "Muhammad Hamidullah",
                authorArabic = "محمد حميد الله",
                language = "French",
                languageCode = "fr",
                description = "Renowned French translation and commentary",
                source = "quran.com",
                downloadSize = 36 * 1024 * 1024 // 36 MB
            ),
            
            // Spanish Tafsir
            Tafsir(
                id = "spanish_cortes",
                name = "Tafsir Julio Cortés",
                nameArabic = "تفسير خوليو كورتيس",
                author = "Julio Cortés",
                authorArabic = "خوليو كورتيس",
                language = "Spanish",
                languageCode = "es",
                description = "Popular Spanish translation and commentary",
                source = "quran.com",
                downloadSize = 34 * 1024 * 1024 // 34 MB
            ),
            
            // Bengali Tafsir
            Tafsir(
                id = "bengali_mujibur",
                name = "Tafsir Mujibur Rahman",
                nameArabic = "تفسير مجيب الرحمن",
                author = "Maulana Mujibur Rahman",
                authorArabic = "مولانا مجيب الرحمن",
                language = "Bengali",
                languageCode = "bn",
                description = "Comprehensive Bengali tafsir",
                source = "quran.com",
                downloadSize = 37 * 1024 * 1024 // 37 MB
            ),
            
            // Malay Tafsir
            Tafsir(
                id = "malay_pimpinan",
                name = "Tafsir Pimpinan ar-Rahman",
                nameArabic = "تفسير بمبينن الرحمن",
                author = "Abdullah Abbas Nasution",
                authorArabic = "عبد الله عباس ناسوتيون",
                language = "Malay",
                languageCode = "ms",
                description = "Popular Malay tafsir",
                source = "quran.com",
                downloadSize = 33 * 1024 * 1024 // 33 MB
            ),
            
            // Persian Tafsir
            Tafsir(
                id = "persian_makarem",
                name = "Tafsir Nemooneh",
                nameArabic = "تفسير نمونه",
                author = "Ayatollah Makarem Shirazi",
                authorArabic = "آية الله مكارم شيرازي",
                language = "Persian",
                languageCode = "fa",
                description = "Contemporary Persian tafsir",
                source = "quran.com",
                downloadSize = 44 * 1024 * 1024 // 44 MB
            ),
            
            // Hindi Tafsir
            Tafsir(
                id = "hindi_farooq",
                name = "Tafsir Farooq Khan",
                nameArabic = "تفسير فاروق خان",
                author = "Maulana Farooq Khan",
                authorArabic = "مولانا فاروق خان",
                language = "Hindi",
                languageCode = "hi",
                description = "Popular Hindi translation and commentary",
                source = "quran.com",
                downloadSize = 36 * 1024 * 1024 // 36 MB
            ),
            
            // German Tafsir
            Tafsir(
                id = "german_zaidan",
                name = "Tafsir Amir Zaidan",
                nameArabic = "تفسير أمير زيدان",
                author = "Amir Zaidan",
                authorArabic = "أمير زيدان",
                language = "German",
                languageCode = "de",
                description = "Modern German tafsir",
                source = "quran.com",
                downloadSize = 35 * 1024 * 1024 // 35 MB
            ),
            
            // Russian Tafsir
            Tafsir(
                id = "russian_kuliev",
                name = "Tafsir Elmir Kuliev",
                nameArabic = "تفسير إلمير كولييف",
                author = "Elmir Kuliev",
                authorArabic = "إلمير كولييف",
                language = "Russian",
                languageCode = "ru",
                description = "Contemporary Russian tafsir",
                source = "quran.com",
                downloadSize = 39 * 1024 * 1024 // 39 MB
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
