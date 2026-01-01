# 🌐 AlQuran.cloud API Integration Guide

## 📋 API Overview

**Base URLs:**
- `https://api.alquran.cloud`
- `https://alquran.api.islamic.network`
- `https://alquran.api.alislam.ru` (Russia endpoint)

**Features:**
- ✅ Complete Quran text in multiple editions
- ✅ Multiple translations (100+ languages)
- ✅ Audio recitations (30+ reciters)
- ✅ Search functionality
- ✅ Juz, Surah, Ayah, Manzil, Ruku access
- ✅ Supports gzip and zstd compression

---

## 🔑 API Endpoints

### 1. Editions (GET /v1/edition)

Get available text and audio editions.

**Endpoints:**
```
GET /v1/edition
GET /v1/edition?format=audio&language=fr&type=versebyverse
GET /v1/edition/language
GET /v1/edition/language/en
GET /v1/edition/type
GET /v1/edition/type/translation
GET /v1/edition/format
GET /v1/edition/format/text
```

**Parameters:**
- `format` - 'text' or 'audio'
- `language` - 2-digit code (en, ar, ur, ta, etc.)
- `type` - 'versebyverse', 'translation', etc.

**Example Response:**
```json
{
  "code": 200,
  "status": "OK",
  "data": [
    {
      "identifier": "en.asad",
      "language": "en",
      "name": "Muhammad Asad",
      "englishName": "Muhammad Asad",
      "format": "text",
      "type": "translation"
    }
  ]
}
```

---

### 2. Complete Quran (GET /v1/quran/{edition})

Get the entire Quran in a specific edition.

**Endpoints:**
```
GET /v1/quran/en.asad          # Muhammad Asad translation
GET /v1/quran/quran-uthmani    # Arabic text
GET /v1/quran/ar.alafasy       # Mishary Alafasy audio
```

**Example Response (Text):**
```json
{
  "code": 200,
  "status": "OK",
  "data": {
    "surahs": [
      {
        "number": 1,
        "name": "سُورَةُ ٱلْفَاتِحَةِ",
        "englishName": "Al-Faatiha",
        "englishNameTranslation": "The Opening",
        "revelationType": "Meccan",
        "numberOfAyahs": 7,
        "ayahs": [
          {
            "number": 1,
            "text": "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            "numberInSurah": 1,
            "juz": 1,
            "manzil": 1,
            "page": 1,
            "ruku": 1,
            "hizbQuarter": 1,
            "sajda": false
          }
        ]
      }
    ]
  }
}
```

---

### 3. Juz (GET /v1/juz/{juzNumber}/{edition})

Get a specific Juz (1-30).

**Endpoints:**
```
GET /v1/juz/30/en.asad
GET /v1/juz/30/quran-uthmani
GET /v1/juz/1/quran-uthmani?offset=3&limit=10
```

**Parameters:**
- `offset` - Skip n ayahs
- `limit` - Limit response to n ayahs

---

### 4. Surah (GET /v1/surah/{surahNumber}/{edition})

Get a specific Surah (1-114).

**Endpoints:**
```
GET /v1/surah                  # List all surahs
GET /v1/surah/114/en.asad      # Surah An-Nas translation
GET /v1/surah/114/ar.alafasy   # Surah An-Nas audio
GET /v1/surah/114              # Surah An-Nas Arabic text
GET /v1/surah/1?offset=1&limit=3  # Verses 2-4 of Al-Fatiha
```

**Multiple Editions:**
```
GET /v1/surah/114/editions/quran-uthmani,en.asad,en.pickthall
```

**Example Response:**
```json
{
  "code": 200,
  "status": "OK",
  "data": {
    "number": 1,
    "name": "سُورَةُ ٱلْفَاتِحَةِ",
    "englishName": "Al-Faatiha",
    "englishNameTranslation": "The Opening",
    "revelationType": "Meccan",
    "numberOfAyahs": 7,
    "ayahs": [...]
  }
}
```

---

### 5. Ayah (GET /v1/ayah/{ayahNumber}/{edition})

Get a specific Ayah (1-6236).

**Endpoints:**
```
GET /v1/ayah/262/en.asad       # Ayat Al-Kursi translation
GET /v1/ayah/2:255/en.asad     # Same using surah:ayah format
GET /v1/ayah/262/ar.alafasy    # Ayat Al-Kursi audio
GET /v1/ayah/262               # Ayat Al-Kursi Arabic text
```

**Multiple Editions:**
```
GET /v1/ayah/262/editions/quran-uthmani,en.asad,en.pickthall
```

**Example Response:**
```json
{
  "code": 200,
  "status": "OK",
  "data": {
    "number": 262,
    "text": "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ...",
    "surah": {
      "number": 2,
      "name": "سُورَةُ البَقَرَةِ",
      "englishName": "Al-Baqara"
    },
    "numberInSurah": 255,
    "juz": 3,
    "manzil": 1,
    "page": 42,
    "ruku": 35,
    "hizbQuarter": 21,
    "sajda": false
  }
}
```

---

### 6. Search (GET /v1/search/{keyword}/{surah}/{edition})

Search the Quran text.

**Endpoints:**
```
GET /v1/search/Abraham/all/en              # All English editions
GET /v1/search/Abraham/all/en.pickthall    # Specific edition
GET /v1/search/Abraham/37/en.pickthall     # In Surah 37 only
```

**Example Response:**
```json
{
  "code": 200,
  "status": "OK",
  "data": {
    "count": 69,
    "matches": [
      {
        "number": 127,
        "text": "...",
        "surah": {...},
        "numberInSurah": 124
      }
    ]
  }
}
```

---

### 7. Manzil (GET /v1/manzil/{manzilNumber}/{edition})

Get a specific Manzil (1-7).

**Endpoints:**
```
GET /v1/manzil/7/en.asad
GET /v1/manzil/7/quran-uthmani
GET /v1/manzil/7/quran-uthmani?offset=3&limit=10
```

---

### 8. Ruku (GET /v1/ruku/{rukuNumber}/{edition})

Get a specific Ruku (1-556).

**Endpoints:**
```
GET /v1/ruku/1/en.asad
GET /v1/ruku/1/quran-uthmani
GET /v1/ruku/1/quran-uthmani?offset=3&limit=10
```

---

## 🎯 Popular Edition Identifiers

### Arabic Text
- `quran-uthmani` - Uthmani script
- `quran-simple` - Simple Arabic text
- `quran-simple-enhanced` - Enhanced simple text
- `ar.muyassar` - Al-Muyassar (simplified Arabic)

### English Translations
- `en.asad` - Muhammad Asad
- `en.sahih` - Sahih International
- `en.pickthall` - Marmaduke Pickthall
- `en.yusufali` - Yusuf Ali
- `en.hilali` - Hilali & Khan

### Urdu Translations
- `ur.jalandhry` - Fateh Muhammad Jalandhry
- `ur.ahmedali` - Ahmed Ali
- `ur.maududi` - Abul Ala Maududi

### Tamil Translations
- `ta.tamil` - Tamil translation

### Audio Reciters
- `ar.alafasy` - Mishary Rashid Alafasy
- `ar.abdulbasitmurattal` - Abdul Basit (Murattal)
- `ar.husary` - Mahmoud Khalil Al-Hussary
- `ar.minshawi` - Mohamed Siddiq El-Minshawi
- `ar.saadalghamadi` - Saad Al-Ghamdi

---

## 💻 Implementation in AlQuranPlusAI

### 1. Create API Service

```kotlin
// shared/src/commonMain/kotlin/com/alquranplusai/data/network/api/AlQuranCloudApi.kt

interface AlQuranCloudApi {
    
    @GET("v1/edition")
    suspend fun getEditions(
        @Query("format") format: String? = null,
        @Query("language") language: String? = null,
        @Query("type") type: String? = null
    ): EditionsResponse
    
    @GET("v1/quran/{edition}")
    suspend fun getCompleteQuran(
        @Path("edition") edition: String = "quran-uthmani"
    ): QuranResponse
    
    @GET("v1/surah")
    suspend fun getAllSurahs(): SurahListResponse
    
    @GET("v1/surah/{number}/{edition}")
    suspend fun getSurah(
        @Path("number") surahNumber: Int,
        @Path("edition") edition: String = "quran-uthmani",
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null
    ): SurahResponse
    
    @GET("v1/ayah/{reference}/{edition}")
    suspend fun getAyah(
        @Path("reference") reference: String,
        @Path("edition") edition: String = "quran-uthmani"
    ): AyahResponse
    
    @GET("v1/juz/{number}/{edition}")
    suspend fun getJuz(
        @Path("number") juzNumber: Int,
        @Path("edition") edition: String = "quran-uthmani",
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null
    ): JuzResponse
    
    @GET("v1/search/{keyword}/{surah}/{edition}")
    suspend fun search(
        @Path("keyword") keyword: String,
        @Path("surah") surah: String = "all",
        @Path("edition") edition: String = "en"
    ): SearchResponse
}
```

### 2. Response Models

```kotlin
@Serializable
data class QuranResponse(
    val code: Int,
    val status: String,
    val data: QuranData
)

@Serializable
data class QuranData(
    val surahs: List<SurahData>,
    val edition: EditionData
)

@Serializable
data class SurahData(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val revelationType: String,
    val numberOfAyahs: Int,
    val ayahs: List<AyahData>
)

@Serializable
data class AyahData(
    val number: Int,
    val text: String,
    val numberInSurah: Int,
    val juz: Int,
    val manzil: Int,
    val page: Int,
    val ruku: Int,
    val hizbQuarter: Int,
    val sajda: Boolean
)
```

### 3. Repository Implementation

```kotlin
class QuranRepositoryImpl(
    private val api: AlQuranCloudApi,
    private val dao: QuranDao
) : QuranRepository {
    
    override suspend fun syncQuranData() {
        // Download complete Quran
        val response = api.getCompleteQuran("quran-uthmani")
        
        // Save to local database
        response.data.surahs.forEach { surah ->
            dao.insertSurah(surah.toEntity())
            surah.ayahs.forEach { ayah ->
                dao.insertAyah(ayah.toEntity())
            }
        }
    }
    
    override suspend fun downloadTranslation(edition: String) {
        val response = api.getCompleteQuran(edition)
        // Save translation to database
    }
}
```

---

## 📥 Data Download Strategy

### Initial App Setup
1. Bundle basic Quran metadata in assets
2. On first launch, download:
   - Complete Arabic text (quran-uthmani)
   - Default translation (en.sahih)
   - Surah metadata

### Optional Downloads
- Additional translations (user choice)
- Audio recitations (on-demand or pre-download)
- Tafsir data (if available)

### Caching Strategy
- Cache all text data locally (SQLDelight)
- Stream audio or download for offline
- Update data periodically (weekly/monthly)

---

## ✅ Integration Checklist

- [ ] Add Ktor HTTP client dependency
- [ ] Create AlQuranCloudApi interface
- [ ] Implement response models
- [ ] Create repository implementation
- [ ] Add data sync worker
- [ ] Implement offline-first strategy
- [ ] Add error handling
- [ ] Add retry logic
- [ ] Implement caching
- [ ] Add compression support (gzip/zstd)
- [ ] Test all endpoints
- [ ] Handle rate limiting (if any)

---

## 🎉 Benefits

✅ **Free API** - No authentication required  
✅ **Comprehensive** - Complete Quran + translations  
✅ **Multiple formats** - Text and audio  
✅ **Well documented** - Clear API structure  
✅ **Reliable** - Multiple base URLs  
✅ **Efficient** - Compression support  
✅ **Flexible** - Multiple edition support  

---

**Status:** Ready for integration  
**Priority:** HIGH  
**Estimated Time:** 2-3 days for complete integration

