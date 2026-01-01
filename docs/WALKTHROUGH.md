# AlQuranPlusAI App Walkthrough

## Overview
AlQuranPlusAI is a modern, AI-powered Quran application built with Kotlin Multiplatform Mobile (KMM) and Jetpack Compose. It features advanced search capabilities, synchronized audio playback, interactive quizzes, and personalized analytics.

## Key Features

### 1. Home Screen
- **Daily Content**: Displays a daily verse, topic, and challenge to engage users.
- **Quick Access**: Shortcuts to Resume Reading, Search, and Bookmarks.
- **Analytics Summary**: A snapshot of reading streaks and progress.

### 2. Quran Reading
- **Multiple Views**: Browse by Surah, Juz, or Custom Page (Manzil).
- **Interactive Text**: Tap verses to bookmark, play audio, or view translations.
- **Rich Typography**: Uses specialized Arabic fonts (Amiri, Scheherazade, Noto Naskh) for optimal readability.
- **Translations**: Toggle translations below the Arabic text.
- **Audio Sync**: Highlights words as they are recited (for supported reciters).

### 3. Advanced Search
- **Hybrid Search**: Combines text matching (FTS) with AI-powered semantic search.
- **Semantic Queries**: Search by meaning (e.g., "patience", "inheritance laws") to find relevant verses even without matching exact keywords.
- **Root Word Search**: Explore verses sharing the same Arabic root.
- **Filtering**: Filter results by Surah, Juz, or specific topics.

### 4. Audio Player
- **Downloads for Offline**: Download complete Surahs or streaming playback.
- **Reciter Selection**: Choose from popular reciters.
- **Word-by-Word Sync**: Visualization of current word playback (requires sync data).
- **Background Play**: Listen while using other apps.

### 5. Quiz & Gamification
- **Daily Challenge**: A generated 10-question quiz every day.
- **Adaptive Difficulty**: Questions generated based on difficulty levels (Easy, Medium, Hard).
- **Categories**: Quizzes on Tajweed, History, General Knowledge, etc.
- **Leaderboards & Stats**: Track performance and earn achievements.

### 6. Analytics
- **Reading Habits**: Track time spent, verses read, and sessions.
- **Streaks**: Daily goals to maintain a reading habit.
- **Visual Charts**: Graphs showing progress over weeks and months.

## Technical Highlights
- **Architecture**: Clean Architecture with KMM Shared Module.
- **Database**: SQLDelight for local persistence.
- **Network**: Ktor for API communication.
- **DI**: Koin for dependency injection.
- **UI**: 100% Jetpack Compose implementation on Android.

## Setup Instructions
1. **Build**: Run `./gradlew :androidApp:assembleDebug` to build the APK.
2. **Fonts**: Ensure `font_certs.xml` is configured with valid Google Fonts certificates for production use.
3. **API Keys**: Configure Quran Foundation API keys in `local.properties` if required (currently uses public endpoints or placeholders).

## Next Steps
- Implement iOS UI layer (Shared module is ready).
- enhance Voice Search integration.
- Add more gamification elements (Badges, Social sharing).
