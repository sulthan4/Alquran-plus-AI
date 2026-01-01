# AlQuranPlusAI - Asset Management Strategy

**Purpose**: Comprehensive plan for managing all external assets (Quran data, audio, AI models, fonts, icons)

---

## 📦 Asset Categories

### 1. Quran Text Data
### 2. Audio Files
### 3. AI/ML Models
### 4. Fonts
### 5. Icons & Images
### 6. Translations & Tafsir

---

## 📁 Asset Folder Structure

```
AlQuranPlusAI/
├── assets/                          # Development assets
│   ├── data/                        # Raw data for processing
│   │   ├── quran/
│   │   │   ├── uthmani.json        # Uthmanic script
│   │   │   ├── simple.json         # Simple text
│   │   │   ├── imlaai.json         # Imlaai script
│   │   │   ├── metadata.json       # Surah info, Juz, Page, etc.
│   │   │   └── word_by_word.json   # Grammar, roots, lemmas
│   │   ├── translations/
│   │   │   ├── en_sahih.json
│   │   │   ├── ar_tafsir_kathir.json
│   │   │   └── ... (50+ files)
│   │   └── audio_timings/
│   │       ├── abdul_basit/
│   │       │   ├── 001_timing.json
│   │       │   └── ... (114 files)
│   │       └── ... (30+ reciters)
│   │
│   ├── ai_models/                   # TensorFlow Lite models
│   │   ├── semantic_search.tflite
│   │   ├── speech_recognition_ar.tflite
│   │   ├── speech_recognition_en.tflite
│   │   ├── text_classification.tflite
│   │   └── embeddings/
│   │       ├── quran_embeddings.bin
│   │       └── translation_embeddings.bin
│   │
│   └── scripts/                     # Processing scripts
│       ├── process_quran_data.py
│       ├── generate_database.py
│       ├── convert_audio_timings.py
│       └── optimize_models.py
│
└── androidApp/
    └── src/main/
        ├── assets/                  # Bundled in APK
        │   ├── databases/
        │   │   └── alquran.db      # Pre-populated database
        │   ├── ai_models/
        │   │   └── ... (lite versions)
        │   └── fonts/
        │       └── ... (Arabic fonts)
        │
        └── res/
            ├── font/                # Font resources
            │   ├── uthmanic_hafs.ttf
            │   ├── amiri_quran.ttf
            │   ├── scheherazade_new.ttf
            │   └── roboto_regular.ttf
            ├── drawable/            # Vector icons
            │   ├── ic_quran.xml
            │   ├── ic_audio.xml
            │   └── ... (50+ icons)
            ├── mipmap-*/            # App icons
            │   └── ic_launcher.png
            └── raw/                 # Sound effects
                └── notification.mp3
```

---

## 1️⃣ Quran Text Data

### Sources
- **Primary**: Tanzil.net API (http://tanzil.net/docs/download)
- **Alternative**: Quran.com API (https://api.quran.com)
- **Word-by-word**: Quranic Arabic Corpus (http://corpus.quran.com)

### Data Files Needed

**Quran Text** (3 versions):
```json
{
  "surah": 1,
  "ayah": 1,
  "text_uthmani": "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
  "text_simple": "بسم الله الرحمن الرحيم",
  "text_imlaai": "بسم الله الرحمن الرحيم",
  "juz": 1,
  "page": 1,
  "manzil": 1,
  "hizb": 1,
  "ruku": 1
}
```

**Word-by-Word Data**:
```json
{
  "ayah_id": 1,
  "word_position": 1,
  "text": "بِسْمِ",
  "translation": "In the name",
  "transliteration": "bismi",
  "root": "سمو",
  "lemma": "اِسْم",
  "grammar": {
    "part_of_speech": "noun",
    "case": "genitive",
    "state": "construct",
    "gender": "masculine"
  }
}
```

### Processing Strategy
1. Download raw JSON from Tanzil/Quran.com
2. Run `process_quran_data.py` to clean and structure
3. Generate SQLite database with `generate_database.py`
4. Bundle database in `androidApp/src/main/assets/databases/`

**Estimated Size**: ~50 MB (compressed database)

---

## 2️⃣ Audio Files

### Sources
- **EveryAyah.com**: http://everyayah.com/data/
- **Quran.com**: https://audio.qurancdn.com/
- **Tarteel.ai**: Custom recordings

### Reciters (30+)
```
Priority Tier 1 (Must Have):
- Abdul Basit (Mujawwad)
- Mishary Rashid Alafasy
- Saad Al-Ghamdi
- Mahmoud Khalil Al-Hussary
- Muhammad Siddiq Al-Minshawi

Priority Tier 2 (Popular):
- Maher Al-Muaiqly
- Ahmed Al-Ajmy
- Yasser Al-Dosari
- Abdur-Rahman As-Sudais
- Saud Al-Shuraim

Priority Tier 3 (Additional):
- 20+ more reciters
```

### Audio File Structure
```
CDN: https://cdn.alquranplusai.com/audio/
├── abdul_basit_mujawwad/
│   ├── 001.mp3          # Surah 1 (Al-Fatihah)
│   ├── 002.mp3          # Surah 2 (Al-Baqarah)
│   └── ... (114 files)
└── ... (30+ reciters)
```

### Word Timing Files
```json
{
  "surah": 1,
  "ayah": 1,
  "reciter": "abdul_basit",
  "timings": [
    {
      "word_position": 1,
      "start_ms": 0,
      "end_ms": 450,
      "duration_ms": 450
    },
    // ... more words
  ]
}
```

### Storage Strategy
- **Streaming**: Default mode, play from CDN
- **Download**: User can download for offline
- **Cache**: Recently played cached automatically
- **Storage Location**: `Android/data/com.alquranplusai/files/audio/`

**Estimated Size per Reciter**: 500 MB - 1 GB

---

## 3️⃣ AI/ML Models

### Models Required

#### Semantic Search Model
- **Purpose**: Find verses by meaning, not just keywords
- **Type**: Sentence embeddings (BERT-based)
- **Size**: ~50 MB (quantized)
- **Input**: User query text
- **Output**: Verse embeddings similarity scores

#### Speech Recognition - Arabic
- **Purpose**: Voice search in Arabic
- **Type**: Wav2Vec or Whisper (fine-tuned)
- **Size**: ~40 MB (quantized)
- **Input**: Audio waveform
- **Output**: Arabic text transcription

#### Speech Recognition - English
- **Purpose**: Voice search in English
- **Type**: Whisper (small model)
- **Size**: ~40 MB (quantized)
- **Input**: Audio waveform
- **Output**: English text transcription

#### Text Classification
- **Purpose**: Categorize verses by topic
- **Type**: BERT classifier
- **Size**: ~10 MB
- **Input**: Verse text
- **Output**: Topic labels (Salah, Zakah, etc.)

### Pre-computed Embeddings
```
embeddings/
├── quran_embeddings.bin         # 6,236 verse embeddings
└── translation_embeddings.bin   # Translation embeddings
```

**Total Size**: ~200 MB (all embeddings)

### Model Optimization
1. **Quantization**: Convert to INT8 (4x smaller)
2. **Pruning**: Remove unnecessary weights
3. **TFLite Conversion**: Optimize for mobile
4. **Lazy Loading**: Load only when AI features used

**Bundled in APK**: Lite versions (~30 MB total)  
**Premium Download**: Full models (~200 MB)

---

## 4️⃣ Fonts

### Arabic Fonts (Required)

**Primary Quran Font**:
- **Uthmanic Hafs** (`uthmanic_hafs.ttf`) - ~2 MB
  - Traditional Mushaf style
  - Includes all diacritics
  - Source: King Fahd Complex

**Alternative Fonts**:
- **Amiri Quran** (`amiri_quran.ttf`) - ~1 MB
- **Scheherazade New** (`scheherazade_new.ttf`) - ~1.5 MB
- **Noto Naskh Arabic** (`noto_naskh_arabic.ttf`) - ~500 KB

**Multi-language Support**:
- **Noto Sans Tamil** (`noto_sans_tamil.ttf`) - ~400 KB
- **Noto Nastaliq Urdu** (`noto_nastaliq_urdu.ttf`) - ~800 KB

**UI Font**:
- **Roboto** (system default) - Included in Android

### Font Loading Strategy
```kotlin
val UthamanicHafs = FontFamily(
    Font(R.font.uthmanic_hafs, FontWeight.Normal)
)

val AmiriQuran = FontFamily(
    Font(R.font.amiri_quran, FontWeight.Normal)
)
```

**Total Size**: ~6 MB (all fonts)

---

## 5️⃣ Icons & Images

### Vector Icons (50+)
```
res/drawable/
├── ic_quran.xml              # Quran book icon
├── ic_audio.xml              # Headphones
├── ic_bookmark.xml           # Bookmark
├── ic_search.xml             # Magnifying glass
├── ic_quiz.xml               # Trophy
├── ic_analytics.xml          # Chart
├── ic_settings.xml           # Gear
├── ic_play.xml               # Play button
├── ic_pause.xml              # Pause button
├── ic_next.xml               # Next track
├── ic_previous.xml           # Previous track
├── ic_repeat.xml             # Repeat
├── ic_shuffle.xml            # Shuffle
├── ic_download.xml           # Download arrow
├── ic_delete.xml             # Trash
├── ic_share.xml              # Share
├── ic_favorite.xml           # Heart
├── ic_folder.xml             # Folder
├── ic_tag.xml                # Tag
├── ic_note.xml               # Note
├── ic_calendar.xml           # Calendar
├── ic_streak.xml             # Flame
├── ic_achievement.xml        # Medal
└── ... (30+ more)
```

### App Icons
```
mipmap-mdpi/ic_launcher.png       (48x48)
mipmap-hdpi/ic_launcher.png       (72x72)
mipmap-xhdpi/ic_launcher.png      (96x96)
mipmap-xxhdpi/ic_launcher.png     (144x144)
mipmap-xxxhdpi/ic_launcher.png    (192x192)
```

**App Icon Design**:
- Teal background with gradient
- Stylized Quran book or Arabic calligraphy
- Modern, minimal design
- Adaptive icon support

### Reciter Images
```
Download from CDN:
https://cdn.alquranplusai.com/images/reciters/
├── abdul_basit.jpg
├── mishary_alafasy.jpg
└── ... (30+ images)
```

**Size**: 400x400 px, optimized JPEG

---

## 6️⃣ Translations & Tafsir

### Translation Packs (50+ languages)

**Priority Languages**:
1. English (5+ translations)
2. Arabic (Tafsir)
3. Urdu
4. Indonesian
5. Turkish
6. French
7. German
8. Spanish
9. Russian
10. Bengali

### Download Strategy
```
Server: https://cdn.alquranplusai.com/translations/
├── en_sahih_international.zip    (~5 MB)
├── ar_tafsir_ibn_kathir.zip      (~20 MB)
├── ur_maududi.zip                (~8 MB)
└── ... (50+ packs)
```

**ZIP Contents**:
```
en_sahih_international/
├── metadata.json
└── translations.db    # SQLite with translations
```

### Installation Flow
1. User selects translation from list
2. App downloads ZIP from CDN
3. Extracts to temp directory
4. Inserts data into main database
5. Updates `Translation` table: `isDownloaded = 1`
6. Deletes ZIP file

---

## 📥 Asset Acquisition Plan

### Phase 1: Core Data (Week 1)
- [ ] Download Quran text from Tanzil.net
- [ ] Download word-by-word from Quranic Corpus
- [ ] Process and generate SQLite database
- [ ] Test database integrity

### Phase 2: Audio Setup (Week 2)
- [ ] Download 5 priority reciters from EveryAyah
- [ ] Set up CDN (AWS S3 + CloudFront)
- [ ] Upload audio files to CDN
- [ ] Generate word timing files
- [ ] Test streaming and download

### Phase 3: Translations (Week 3)
- [ ] Download 10 priority translations
- [ ] Process into SQLite format
- [ ] Create ZIP packages
- [ ] Upload to CDN
- [ ] Test download and installation

### Phase 4: AI Models (Week 4)
- [ ] Train/fine-tune semantic search model
- [ ] Train/fine-tune speech recognition models
- [ ] Convert to TFLite
- [ ] Quantize for mobile
- [ ] Generate embeddings
- [ ] Test inference speed

### Phase 5: Fonts & Icons (Week 5)
- [ ] Acquire Arabic font licenses
- [ ] Download Noto fonts
- [ ] Create vector icons
- [ ] Design app icon
- [ ] Test font rendering

---

## 🔧 Asset Processing Scripts

### 1. Process Quran Data
```python
# assets/scripts/process_quran_data.py
import json
import sqlite3

def process_quran_text():
    # Download from Tanzil
    # Parse JSON
    # Insert into SQLite
    # Add indexes
    pass

def process_word_by_word():
    # Download from Corpus
    # Parse and structure
    # Add grammar info
    # Insert into database
    pass
```

### 2. Generate Database
```python
# assets/scripts/generate_database.py
def create_prepopulated_db():
    # Create all tables
    # Insert Quran text
    # Insert word-by-word
    # Insert metadata
    # Optimize and vacuum
    # Compress database
    pass
```

### 3. Audio Timing Converter
```python
# assets/scripts/convert_audio_timings.py
def convert_timings_to_json():
    # Parse timing files
    # Convert to JSON format
    # Validate timing accuracy
    # Save to CDN structure
    pass
```

---

## 💾 Storage Estimates

### Initial APK Size
- App code: ~10 MB
- Pre-populated database: ~50 MB
- AI models (lite): ~30 MB
- Fonts: ~6 MB
- Icons & resources: ~4 MB
**Total APK**: ~100 MB

### After User Downloads
- Translations (5 packs): ~30 MB
- Audio (1 reciter): ~800 MB
- Full AI models: ~200 MB
**Maximum Total**: ~2-3 GB

---

## 🚀 Implementation Checklist

- [ ] Set up asset folder structure
- [ ] Create processing scripts
- [ ] Download Quran data sources
- [ ] Generate pre-populated database
- [ ] Set up CDN for audio/translations
- [ ] Acquire/create AI models
- [ ] Download fonts
- [ ] Create vector icons
- [ ] Design app icon
- [ ] Test all assets in app
- [ ] Optimize file sizes
- [ ] Document asset sources and licenses

---

**Yes, I will handle ALL assets comprehensively!** This document is the complete plan. 📦✨

**Last Updated**: December 19, 2025, 12:01 AM IST
