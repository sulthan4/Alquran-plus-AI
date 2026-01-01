package com.alquranplusai.data.database

import com.alquranplusai.data.database.AlQuranDatabase

/**
 * Database seeder for populating initial Quran data
 * This seeds the database with sample data for testing and development
 */
class DatabaseSeeder(private val database: AlQuranDatabase) {
    
    /**
     * Seeds the database with sample Quran data
     * Includes: 5 surahs, sample ayahs, 1 translation
     */
    suspend fun seedDatabase() {
        seedSurahs()
        seedAyahs()
        seedTranslations()
    }
    
    /**
     * Check if database is already seeded
     */
    suspend fun isDatabaseSeeded(): Boolean {
        val surahCount = database.surahQueries.selectAll().executeAsList().size
        return surahCount > 0
    }
    
    /**
     * Clear all data from database (if needed)
     */
    suspend fun clearDatabase() {
        // Note: Individual table clear methods would be called here
        // For now, this is a placeholder
    }
    
    private suspend fun seedSurahs() {
        val surahs = listOf(
            // Surah 1: Al-Fatihah
            SurahData(
                number = 1,
                name = "Al-Fatihah",
                nameArabic = "الفاتحة",
                nameTransliteration = "Al-Faatiha",
                nameTranslation = "The Opening",
                revelationType = "Meccan",
                numberOfAyahs = 7,
                bismillahPre = 1,
                rukuCount = 1
            ),
            // Surah 2: Al-Baqarah (partial - first 5 ayahs for testing)
            SurahData(
                number = 2,
                name = "Al-Baqarah",
                nameArabic = "البقرة",
                nameTransliteration = "Al-Baqara",
                nameTranslation = "The Cow",
                revelationType = "Medinan",
                numberOfAyahs = 286,
                bismillahPre = 1,
                rukuCount = 40
            ),
            // Surah 112: Al-Ikhlas
            SurahData(
                number = 112,
                name = "Al-Ikhlas",
                nameArabic = "الإخلاص",
                nameTransliteration = "Al-Ikhlaas",
                nameTranslation = "The Sincerity",
                revelationType = "Meccan",
                numberOfAyahs = 4,
                bismillahPre = 1,
                rukuCount = 1
            ),
            // Surah 113: Al-Falaq
            SurahData(
                number = 113,
                name = "Al-Falaq",
                nameArabic = "الفلق",
                nameTransliteration = "Al-Falaq",
                nameTranslation = "The Daybreak",
                revelationType = "Meccan",
                numberOfAyahs = 5,
                bismillahPre = 1,
                rukuCount = 1
            ),
            // Surah 114: An-Nas
            SurahData(
                number = 114,
                name = "An-Nas",
                nameArabic = "الناس",
                nameTransliteration = "An-Naas",
                nameTranslation = "Mankind",
                revelationType = "Meccan",
                numberOfAyahs = 6,
                bismillahPre = 1,
                rukuCount = 1
            )
        )
        
        surahs.forEach { surah ->
            database.surahQueries.insert(
                number = surah.number.toLong(),
                name = surah.name,
                nameArabic = surah.nameArabic,
                nameTransliteration = surah.nameTransliteration,
                nameTranslation = surah.nameTranslation,
                revelationType = surah.revelationType,
                numberOfAyahs = surah.numberOfAyahs.toLong(),
                bismillahPre = surah.bismillahPre.toLong(),
                rukuCount = surah.rukuCount.toLong()
            )
        }
    }
    
    private suspend fun seedAyahs() {
        val ayahs = listOf(
            // Surah 1: Al-Fatihah (all 7 ayahs)
            AyahData(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "In the name of Allah, the Entirely Merciful, the Especially Merciful.", 1, 1, 1, 1, 1),
            AyahData(1, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "All praise is due to Allah, Lord of the worlds -", 1, 1, 1, 1, 1),
            AyahData(1, 3, "الرَّحْمَٰنِ الرَّحِيمِ", "The Entirely Merciful, the Especially Merciful,", 1, 1, 1, 1, 1),
            AyahData(1, 4, "مَالِكِ يَوْمِ الدِّينِ", "Sovereign of the Day of Recompense.", 1, 1, 1, 1, 1),
            AyahData(1, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "It is You we worship and You we ask for help.", 1, 1, 1, 1, 1),
            AyahData(1, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "Guide us to the straight path -", 1, 1, 1, 1, 1),
            AyahData(1, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "The path of those upon whom You have bestowed favor, not of those who have evoked Your anger or of those who are astray.", 1, 1, 1, 1, 2),
            
            // Surah 2: Al-Baqarah (first 5 ayahs for testing)
            AyahData(2, 1, "الم", "Alif, Lam, Meem.", 1, 1, 1, 1, 2),
            AyahData(2, 2, "ذَٰلِكَ الْكِتَابُ لَا رَيْبَ ۛ فِيهِ ۛ هُدًى لِّلْمُتَّقِينَ", "This is the Book about which there is no doubt, a guidance for those conscious of Allah -", 1, 1, 1, 1, 2),
            AyahData(2, 3, "الَّذِينَ يُؤْمِنُونَ بِالْغَيْبِ وَيُقِيمُونَ الصَّلَاةَ وَمِمَّا رَزَقْنَاهُمْ يُنفِقُونَ", "Who believe in the unseen, establish prayer, and spend out of what We have provided for them,", 1, 1, 1, 1, 2),
            AyahData(2, 4, "وَالَّذِينَ يُؤْمِنُونَ بِمَا أُنزِلَ إِلَيْكَ وَمَا أُنزِلَ مِن قَبْلِكَ وَبِالْآخِرَةِ هُمْ يُوقِنُونَ", "And who believe in what has been revealed to you, and what was revealed before you, and of the Hereafter they are certain.", 1, 1, 1, 1, 2),
            AyahData(2, 5, "أُولَٰئِكَ عَلَىٰ هُدًى مِّن رَّبِّهِمْ ۖ وَأُولَٰئِكَ هُمُ الْمُفْلِحُونَ", "Those are upon guidance from their Lord, and it is those who are the successful.", 1, 1, 1, 1, 2),
            
            // Surah 112: Al-Ikhlas (all 4 ayahs)
            AyahData(112, 1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "Say, He is Allah, the One.", 30, 60, 1, 7, 604),
            AyahData(112, 2, "اللَّهُ الصَّمَدُ", "Allah, the Eternal Refuge.", 30, 60, 1, 7, 604),
            AyahData(112, 3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "He neither begets nor is born,", 30, 60, 1, 7, 604),
            AyahData(112, 4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "Nor is there to Him any equivalent.", 30, 60, 1, 7, 604),
            
            // Surah 113: Al-Falaq (all 5 ayahs)
            AyahData(113, 1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "Say, I seek refuge in the Lord of daybreak", 30, 60, 1, 7, 604),
            AyahData(113, 2, "مِن شَرِّ مَا خَلَقَ", "From the evil of that which He created", 30, 60, 1, 7, 604),
            AyahData(113, 3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", "And from the evil of darkness when it settles", 30, 60, 1, 7, 604),
            AyahData(113, 4, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "And from the evil of the blowers in knots", 30, 60, 1, 7, 604),
            AyahData(113, 5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", "And from the evil of an envier when he envies.", 30, 60, 1, 7, 604),
            
            // Surah 114: An-Nas (all 6 ayahs)
            AyahData(114, 1, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "Say, I seek refuge in the Lord of mankind,", 30, 60, 1, 7, 604),
            AyahData(114, 2, "مَلِكِ النَّاسِ", "The Sovereign of mankind.", 30, 60, 1, 7, 604),
            AyahData(114, 3, "إِلَٰهِ النَّاسِ", "The God of mankind,", 30, 60, 1, 7, 604),
            AyahData(114, 4, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "From the evil of the retreating whisperer -", 30, 60, 1, 7, 604),
            AyahData(114, 5, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "Who whispers in the breasts of mankind -", 30, 60, 1, 7, 604),
            AyahData(114, 6, "مِنَ الْجِنَّةِ وَالنَّاسِ", "From among the jinn and mankind.", 30, 60, 1, 7, 604)
        )
        
        ayahs.forEach { ayah ->
            database.ayahQueries.insert(
                surahNumber = ayah.surahNumber.toLong(),
                ayahNumber = ayah.ayahNumber.toLong(),
                text = ayah.textArabic,
                textUthmani = ayah.textArabic,
                textSimple = ayah.textArabic,
                juzNumber = ayah.juzNumber.toLong(),
                hizbNumber = ayah.hizbNumber.toLong(),
                rukuNumber = ayah.rukuNumber.toLong(),
                manzilNumber = ayah.manzilNumber.toLong(),
                pageNumber = ayah.pageNumber.toLong(),
                sajdaType = null,
                sajdaNumber = null
            )
        }
    }
    
    private suspend fun seedTranslations() {
        // Add English translation
        database.translationQueries.insertTranslation(
            id = "en_sahih",
            name = "Saheeh International",
            author = "Saheeh International",
            language = "English",
            languageCode = "en",
            direction = "LTR",
            type = "TRANSLATION",
            isDownloaded = 1,
            downloadSize = 0,
            version = "1.0",
            lastUpdated = 0,
            description = "Clear and easy to understand modern English translation",
            source = "Saheeh International",
            copyright = "Public Domain",
            website = null,
            completeness = 100
        )
    }
    
    private suspend fun seedReciters() {
        // Note: Reciter table schema needs to be checked
        // Skipping for now until schema is confirmed
    }
    
    // Data classes for seeding
    private data class SurahData(
        val number: Int,
        val name: String,
        val nameArabic: String,
        val nameTransliteration: String,
        val nameTranslation: String,
        val revelationType: String,
        val numberOfAyahs: Int,
        val bismillahPre: Int,
        val rukuCount: Int
    )
    
    private data class AyahData(
        val surahNumber: Int,
        val ayahNumber: Int,
        val textArabic: String,
        val translation: String,
        val juzNumber: Int,
        val hizbNumber: Int,
        val rukuNumber: Int,
        val manzilNumber: Int,
        val pageNumber: Int
    )
}
