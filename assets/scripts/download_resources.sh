#!/bin/bash

# Configuration
DATA_DIR="assets/data/quran"
FONTS_DIR="androidApp/src/main/assets/fonts"
AUDIO_DIR="androidApp/src/main/res/raw"

mkdir -p "$DATA_DIR"
mkdir -p "$FONTS_DIR"
mkdir -p "$AUDIO_DIR"

echo "📢 Downloading Assets from AlQuran.cloud API..."

# 1. Download Surah Metadata
echo "  - Fetching Surah Metadata..."
curl -L -o "$DATA_DIR/surahs.json" "http://api.alquran.cloud/v1/surah"

# 2. Download Quran Text (Uthmani)
echo "  - Fetching Quran Text (Uthmani)..."
curl -L -o "$DATA_DIR/quran-uthmani.json" "http://api.alquran.cloud/v1/quran/quran-uthmani"

# 3. Download English Translation (Sahih International)
echo "  - Fetching English Translation (Sahih International)..."
curl -L -o "$DATA_DIR/en-sahih.json" "http://api.alquran.cloud/v1/quran/en.sahih"

# 4. Download Fonts (Amiri - Open Source)
echo "  - Fetching Amiri Font (Quran Text)..."
curl -L -o "$FONTS_DIR/amiri_quran.ttf" "https://github.com/google/fonts/raw/main/ofl/amiri/Amiri-Regular.ttf"
curl -L -o "$FONTS_DIR/amiri_bold.ttf" "https://github.com/google/fonts/raw/main/ofl/amiri/Amiri-Bold.ttf"

# 5. Create Placeholder Audio
echo "  - Creating placeholder notification.mp3..."
echo -n -e '\xFF\xF3\x44\xC4\x00\x00\x00\x03\x48\x00\x00\x00\x00\x4C\x41\x4D\x45\x33\x2E\x39\x38\x2E\x32\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00' > "$AUDIO_DIR/notification.mp3"

echo "✅ Assets downloaded successfully!"
