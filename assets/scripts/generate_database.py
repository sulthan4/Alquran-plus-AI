
import sqlite3
import os
import json
import math

# Paths
BASE_DIR = os.path.dirname(os.path.dirname(os.path.dirname(__file__)))
DB_PATH = os.path.join(BASE_DIR, 'androidApp/src/main/assets/databases/alquran.db')
DATA_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)), 'data/quran')

# Input Files
SURAHS_JSON = os.path.join(DATA_DIR, 'surahs.json')
QURAN_UTHMANI_JSON = os.path.join(DATA_DIR, 'quran-uthmani.json')
TRANSLATION_JSON = os.path.join(DATA_DIR, 'en-sahih.json')

def create_database():
    print(f"Creating database at {DB_PATH}")
    
    # Ensure directory exists
    os.makedirs(os.path.dirname(DB_PATH), exist_ok=True)
    
    # Check if files exist
    if not os.path.exists(SURAHS_JSON) or not os.path.exists(QURAN_UTHMANI_JSON) or not os.path.exists(TRANSLATION_JSON):
        print("❌ Error: Missing input JSON files. Please run download_resources.sh first.")
        return

    if os.path.exists(DB_PATH):
        os.remove(DB_PATH)
        
    conn = sqlite3.connect(DB_PATH)
    cursor = conn.cursor()
    
    # Enable foreign keys
    cursor.execute("PRAGMA foreign_keys = ON;")
    
    # --- Create Tables (matching .sq files) ---
    
    # Surah Table
    cursor.execute("""
    CREATE TABLE IF NOT EXISTS Surah (
        number INTEGER PRIMARY KEY NOT NULL,
        name TEXT NOT NULL,
        nameArabic TEXT NOT NULL,
        nameTransliteration TEXT NOT NULL,
        nameTranslation TEXT NOT NULL,
        revelationType TEXT NOT NULL,
        numberOfAyahs INTEGER NOT NULL,
        bismillahPre INTEGER NOT NULL DEFAULT 1,
        rukuCount INTEGER NOT NULL DEFAULT 0
    );
    """)

    # Ayah Table
    cursor.execute("""
    CREATE TABLE IF NOT EXISTS Ayah (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        surahNumber INTEGER NOT NULL,
        ayahNumber INTEGER NOT NULL,
        text TEXT NOT NULL,
        textUthmani TEXT NOT NULL,
        textSimple TEXT NOT NULL,
        juzNumber INTEGER NOT NULL,
        hizbNumber INTEGER NOT NULL,
        rukuNumber INTEGER NOT NULL,
        manzilNumber INTEGER NOT NULL,
        pageNumber INTEGER NOT NULL,
        sajdaType TEXT,
        sajdaNumber INTEGER,
        FOREIGN KEY (surahNumber) REFERENCES Surah(number) ON DELETE CASCADE,
        UNIQUE(surahNumber, ayahNumber)
    );
    """)

    # Translation Metadata
    cursor.execute("""
    CREATE TABLE IF NOT EXISTS Translation (
        id TEXT PRIMARY KEY NOT NULL,
        name TEXT NOT NULL,
        author TEXT NOT NULL,
        language TEXT NOT NULL,
        languageCode TEXT NOT NULL,
        direction TEXT NOT NULL DEFAULT 'LTR',
        type TEXT NOT NULL DEFAULT 'TRANSLATION',
        isDownloaded INTEGER NOT NULL DEFAULT 0,
        downloadSize INTEGER NOT NULL DEFAULT 0,
        version TEXT NOT NULL DEFAULT '1.0',
        lastUpdated INTEGER NOT NULL DEFAULT 0,
        description TEXT,
        source TEXT,
        copyright TEXT,
        website TEXT,
        completeness INTEGER NOT NULL DEFAULT 100
    );
    """)

    # AyahTranslation
    cursor.execute("""
    CREATE TABLE IF NOT EXISTS AyahTranslation (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        translationId TEXT NOT NULL,
        surahNumber INTEGER NOT NULL,
        ayahNumber INTEGER NOT NULL,
        text TEXT NOT NULL,
        FOREIGN KEY (translationId) REFERENCES Translation(id) ON DELETE CASCADE,
        UNIQUE(translationId, surahNumber, ayahNumber)
    );
    """)

    # Create Indexes
    cursor.execute("CREATE INDEX IF NOT EXISTS idx_surah_revelation_type ON Surah(revelationType);")
    cursor.execute("CREATE INDEX IF NOT EXISTS idx_surah_name ON Surah(name);")
    cursor.execute("CREATE INDEX IF NOT EXISTS idx_ayah_surah ON Ayah(surahNumber);")
    cursor.execute("CREATE INDEX IF NOT EXISTS idx_ayah_juz ON Ayah(juzNumber);")
    cursor.execute("CREATE INDEX IF NOT EXISTS idx_ayah_page ON Ayah(pageNumber);")
    cursor.execute("CREATE INDEX IF NOT EXISTS idx_ayah_translation_ref ON AyahTranslation(surahNumber, ayahNumber);")

    # --- Populate Data ---

    # 1. Populate Surahs
    print("Populating Surah table...")
    with open(SURAHS_JSON, 'r', encoding='utf-8') as f:
        surah_data = json.load(f)
        
    # Handle API response wrapper if present
    if 'data' in surah_data:
        surah_data = surah_data['data']
        
    for s in surah_data:
        # Check bismillahPre (AlQuran.cloud doesn't explicitly give it, but all surahs except 9 have it pre-appended/implied)
        # However, for simplicity, we assume 1 for all except Surah 9 (At-Tawbah)
        bismillah_pre = 0 if s['number'] == 9 else 1
        
        cursor.execute("""
        INSERT INTO Surah (number, name, nameArabic, nameTransliteration, nameTranslation, revelationType, numberOfAyahs, bismillahPre, rukuCount)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, (
            s['number'],
            s['englishName'], # Using English name as main name
            s['name'], # Arabic Name
            s['englishName'], # Transliteration (often same as englishName in API)
            s['englishNameTranslation'],
            s['revelationType'],
            s['numberOfAyahs'],
            bismillah_pre,
            0 # rukuCount not in this specific list, calculated later or left 0
        ))
    print(f"Inserted {len(surah_data)} surahs.")

    # 2. Populate Ayahs
    print("Populating Ayah table (Uthmani text)...")
    with open(QURAN_UTHMANI_JSON, 'r', encoding='utf-8') as f:
        quran_data = json.load(f)
        
    if 'data' in quran_data:
        quran_data = quran_data['data'] # Could be object with 'surahs' or list
    
    surahs_list = quran_data['surahs'] if 'surahs' in quran_data else quran_data

    total_ayahs = 0
    for surah in surahs_list:
        surah_num = surah['number']
        for ayah in surah['ayahs']:
            # Calculate hizb from quarter (API gives quarter)
            hizb_quarter = ayah.get('hizbQuarter', 1)
            hizb_num = math.ceil(hizb_quarter / 4.0)
            
            # Sajda
            sajda = ayah.get('sajda', False)
            sajda_type = 'RECOMMENDED' if sajda else None
            # Some API values are objects for sajda
            if isinstance(sajda, dict):
                 sajda_type = 'RECOMMENDED' # simplifying
            elif sajda == True:
                 sajda_type = 'RECOMMENDED'
            else:
                 sajda_type = None

            cursor.execute("""
            INSERT INTO Ayah (
                surahNumber, ayahNumber, text, textUthmani, textSimple, 
                juzNumber, hizbNumber, rukuNumber, manzilNumber, pageNumber,
                sajdaType, sajdaNumber
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """, (
                surah_num,
                ayah['numberInSurah'],
                ayah['text'], # Uthmani
                ayah['text'], # Uthmani
                ayah['text'], # Simple (using Uthmani as placeholder if simple not available separately)
                ayah.get('juz', 1),
                hizb_num,
                ayah.get('ruku', 1),
                ayah.get('manzil', 1),
                ayah.get('page', 1),
                sajda_type,
                None # sajdaNumber
            ))
            total_ayahs += 1
            
    print(f"Inserted {total_ayahs} ayahs.")

    # 3. Populate Translation (English Sahih)
    print("Populating English Translation...")
    
    # Insert Translation Metadata
    cursor.execute("""
    INSERT INTO Translation (id, name, author, language, languageCode, direction, type, isDownloaded, completeness)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """, (
        'en.sahih',
        'Sahih International',
        'Sahih International',
        'English',
        'en',
        'LTR',
        'TRANSLATION',
        1, # Pre-downloaded
        100
    ))
    
    with open(TRANSLATION_JSON, 'r', encoding='utf-8') as f:
        trans_data = json.load(f)
        
    if 'data' in trans_data:
        trans_data = trans_data['data']
        
    trans_surahs = trans_data['surahs'] if 'surahs' in trans_data else trans_data
    
    total_trans = 0
    for surah in trans_surahs:
        surah_num = surah['number']
        for ayah in surah['ayahs']:
            cursor.execute("""
            INSERT INTO AyahTranslation (translationId, surahNumber, ayahNumber, text)
            VALUES (?, ?, ?, ?)
            """, (
                'en.sahih',
                surah_num,
                ayah['numberInSurah'],
                ayah['text']
            ))
            total_trans += 1
            
    print(f"Inserted {total_trans} translation verses.")
    
    conn.commit()
    conn.close()
    print("✅ Database generation complete!")

if __name__ == "__main__":
    create_database()
