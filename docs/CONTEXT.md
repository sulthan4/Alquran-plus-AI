# AlQuranPlusAI - Project Context & Requirements

**Project Name**: AlQuran Plus AI  
**Code Name**: AlQuranPlusAI  
**Package**: com.alquranplusai  
**Platform**: Kotlin Multiplatform (Android + Shared)  
**Purpose**: Production-grade Quran application with AI-powered features

---

## 🎯 Project Vision

Build a **production-ready Quran application** that matches or exceeds leading Quran apps by offering:
- Word-by-word Quran reading with detailed grammar analysis
- Multiple translations and tafsir (commentary)
- AI-powered semantic search and voice search
- Advanced audio playback with word-level synchronization
- Comprehensive memorization and quiz tools
- Detailed analytics and progress tracking
- **100% offline-first architecture**

---

## 📱 App Identity

- **Store Name**: AlQuran Plus AI
- **Launcher Name**: AlQuran Plus AI
- **Base Package**: com.alquranplusai
- **Root Folder**: AlQuranPlusAI/

---

## 🏗️ Technical Architecture

### Technology Stack (MANDATORY)

#### Shared Module (KMP)
- **Database**: SQLDelight (offline-first, required)
- **Networking**: Ktor HTTP client
- **Serialization**: kotlinx.serialization
- **Dependency Injection**: Koin
- **Coroutines**: kotlinx.coroutines
- **DateTime**: kotlinx.datetime
- **Logging**: Napier

#### Android App
- **UI Framework**: Jetpack Compose (100% Compose, no XML layouts)
- **Architecture**: MVVM with ViewModels
- **Navigation**: Compose Navigation
- **Audio**: ExoPlayer (Media3)
- **Background Work**: WorkManager
- **AI/ML**: TensorFlow Lite
- **Preferences**: DataStore

### Module Structure (STRICT)
```
AlQuranPlusAI/
├── shared/              # KMP shared module (business logic)
│   ├── domain/          # Models + Repository interfaces
│   ├── data/            # Database, Network, Repositories
│   ├── utils/           # Utilities
│   └── di/              # Koin modules
└── androidApp/          # Android UI (Jetpack Compose)
    ├── viewmodels/      # ViewModels
    ├── ui/              # Screens + Components + Theme
    ├── services/        # Background services
    ├── workers/         # WorkManager workers
    ├── receivers/       # Broadcast receivers
    └── utils/           # Android-specific utilities
```

---

## ✨ Core Features (MUST IMPLEMENT ALL)

### 1. Quran Reading & Study
**Requirements**:
- ✅ Surah/Juz/Page/Manzil/Hizb/Ruku navigation
- ✅ Mushaf-like reading experience
- ✅ **Word-by-word view** with:
  - Translation
  - Transliteration
  - Root word
  - Grammar analysis (part of speech, derivation, mood, case, person, number, gender, etc.)
  - Occurrence count in Quran
- ✅ Color-coded Tajweed rules
- ✅ Auto-scroll
- ✅ Continue from last read position
- ✅ Reading progress tracking

**Inspiration**: Apps like Quran.com, Ayah.com, Tarteel

### 2. Audio & Memorization
**Requirements**:
- ✅ 30+ reciters with different styles (Murattal, Mujawwad, Muallim)
- ✅ Streaming + offline download
- ✅ **Word-by-word audio playback** with synchronized highlighting
- ✅ Repeat ranges (Ayah, Surah, custom range)
- ✅ Group playback for memorization
- ✅ Hifz mode (memorization helper)
- ✅ Sleep timer
- ✅ Playlists
- ✅ Background playback with media notification controls
- ✅ Playback speed control
- ✅ Equalizer

**Inspiration**: Quran.com audio, Ayah app, Muslim Pro

### 3. Translations & Tafsir
**Requirements**:
- ✅ Multiple translations shown side-by-side
- ✅ 50+ languages supported
- ✅ Tafsir (commentary) viewing:
  - Ibn Kathir
  - Tabari
  - Qurtubi
  - Jalalayn
  - Others
- ✅ Download management for translation/tafsir packs
- ✅ Word-by-word translation

**Inspiration**: Quran.com translations, Ayah app

### 4. Search & AI Features
**Requirements**:
- ✅ **Exact text search** with keyword highlighting (Arabic + translations)
- ✅ **Root search** (search by Arabic root)
- ✅ **Morphology search** (advanced grammar-based search)
- ✅ **Topic-based grouping** (Salah, Zakah, Hajj, Prophets, etc.)
- ✅ **Semantic search** (AI-powered, embeddings-based)
  - Find verses by meaning, not just exact words
  - AI insights summarizing relevant verses
- ✅ **Voice search** (Arabic + English speech recognition)
- ✅ Search history
- ✅ Search favorites
- ✅ Trending queries
- ✅ Search suggestions

**Inspiration**: Quran.com semantic search, Tarteel voice features

### 5. Bookmarks, Notes & Collections
**Requirements**:
- ✅ Bookmark any Ayah
- ✅ Create folders and collections
- ✅ Add personal notes to verses
- ✅ Tags for organization
- ✅ Categories (Favorite, To Memorize, To Study, Reflection, Dua, etc.)
- ✅ Priority levels
- ✅ Color coding
- ✅ Reminders linked to bookmarks
- ✅ Export/import bookmarks
- ✅ Quick access collections

**Inspiration**: Quran.com bookmarks, Ayah app collections

### 6. Quizzes & Daily Challenge
**Requirements**:
- ✅ Quran-based quizzes with multiple categories:
  - General knowledge
  - Surah names
  - Ayah completion
  - Prophets & stories
  - Rules (Fiqh)
  - History
  - Memorization tests
  - Tajweed
  - Translation
  - Tafsir
- ✅ Difficulty levels (Beginner, Easy, Medium, Hard, Expert, Master)
- ✅ Question types (Multiple choice, True/False, Fill-in-blank, Matching, Ordering)
- ✅ Timed quizzes
- ✅ Scoring system
- ✅ Explanations for answers
- ✅ Hints
- ✅ Review mode
- ✅ **Daily challenge** with streaks
- ✅ **Leaderboards** (global, category-based)
- ✅ Achievements
- ✅ Goals (daily, weekly, monthly)

**Inspiration**: Duolingo-style engagement, Quran quiz apps

### 7. Analytics & Progress Tracking
**Requirements**:
- ✅ Track reading time
- ✅ Verses read count
- ✅ Surahs completed
- ✅ Streaks (daily reading)
- ✅ Charts and visualizations:
  - Reading time chart
  - Ayahs read chart
  - Surah completion chart
  - Streak calendar
- ✅ Weekly reports
- ✅ Monthly insights
- ✅ Yearly overview
- ✅ Goals with progress tracking
- ✅ Milestones
- ✅ Notifications for goals

**Inspiration**: Duolingo analytics, Strava-style tracking

### 8. User & Subscription
**Requirements**:
- ✅ Authentication (email/password + optional social login)
- ✅ User profiles
- ✅ Preferences (reading, audio, UI, notifications, privacy)
- ✅ **Subscription tiers**:
  - Free: Basic features, limited reciters, limited quizzes
  - Premium: All reciters, unlimited quizzes, advanced AI features, analytics
  - Lifetime: One-time purchase
- ✅ Secure token storage
- ✅ Optional biometric lock
- ✅ Cloud sync (bookmarks, progress, settings)
- ✅ Backup/restore

**Inspiration**: Standard app subscription models

### 9. Settings (Comprehensive)
**Requirements**:
- ✅ **Reading Settings**:
  - Font choice (Uthmanic Hafs, Amiri, Scheherazade, Noto Naskh)
  - Font size
  - Line spacing
  - Text type (Uthmani, Simple, Imlaai)
  - Show Tajweed colors
  - Show transliteration
  - Show word-by-word
  - Default translations
  - Reading mode (Continuous, Page-by-page, Ayah-by-ayah, Mushaf)
  
- ✅ **Audio Settings**:
  - Default reciter
  - Playback speed
  - Auto-play
  - Gap between Ayahs
  - Gap between Surahs
  - Word-by-word audio
  - Equalizer
  
- ✅ **UI Settings**:
  - Theme (Light, Dark, System, AMOLED)
  - Language (50+ languages)
  - Animations
  - Haptic feedback
  
- ✅ **Notification Settings**:
  - Enable/disable notifications
  - Daily reminder
  - Reminder time
  - Quiz reminders
  - Achievement notifications
  
- ✅ **Privacy Settings**:
  - Share statistics
  - Show on leaderboard
  - Biometric lock
  
- ✅ **Data Settings**:
  - Auto-download audio
  - Download on WiFi only
  - Auto-backup
  - Backup frequency
  - Storage management
  - Cache management

### 10. Offline-First Architecture (CRITICAL)
**Requirements**:
- ✅ All Quran text stored locally
- ✅ Metadata (Surah info, Juz, Page, etc.) stored locally
- ✅ Word-by-word data stored locally
- ✅ Downloaded translations stored locally
- ✅ Downloaded audio stored locally
- ✅ User data (bookmarks, notes, progress) stored locally
- ✅ Sync with server when online
- ✅ App must work 100% offline after initial setup

---

## 📊 Database Schema Requirements

### Core Tables (MUST HAVE)
1. **Surah** - 114 Surahs with metadata
2. **Ayah** - 6,236 Ayahs with Juz/Page/Manzil/Hizb/Ruku info
3. **Word** - Word-by-word with grammar analysis
4. **Translation** - Translation metadata
5. **AyahTranslation** - Ayah-level translations
6. **WordTranslation** - Word-level translations
7. **Tafsir** - Tafsir metadata
8. **AyahTafsir** - Tafsir text for Ayahs
9. **Reciter** - Reciter information
10. **Audio** - Audio file metadata
11. **WordTiming** - Word-level timing for synchronized playback
12. **Playlist** - User playlists
13. **PlaylistItem** - Playlist items
14. **Bookmark** - User bookmarks
15. **Folder** - Bookmark folders
16. **BookmarkTag** - Tags for bookmarks
17. **Note** - User notes
18. **Quiz** - Quiz definitions
19. **Question** - Quiz questions
20. **QuizSession** - In-progress quiz sessions
21. **QuizResult** - Completed quiz results
22. **User** - User profiles
23. **UserStatistics** - User statistics
24. **ReadingSession** - Reading session tracking
25. **Milestone** - User milestones
26. **Achievement** - Achievement definitions
27. **Goal** - User goals
28. **Settings** - User preferences

---

## 🎨 UI/UX Requirements

### Design Principles
1. **Clean & Modern**: Material Design 3
2. **Arabic-First**: RTL support, beautiful Arabic typography
3. **Accessible**: High contrast, scalable fonts
4. **Fast**: Smooth animations, instant responses
5. **Intuitive**: Easy navigation, clear hierarchy

### Screen Requirements (60+ Screens)

#### Onboarding & Auth (8 screens)
- Splash screen
- Onboarding flow (3-4 screens)
- Welcome screen
- Permissions screen
- Login screen
- Register screen
- Setup complete screen

#### Reading Screens (8 screens)
- Home screen
- Surah list
- Surah detail
- Reading screen (main)
- Juz view
- Page view
- Manzil view
- Ayah detail (word-by-word)

#### Audio Screens (7 screens)
- Audio home
- Reciter list
- Reciter detail
- Playlist screen
- Create playlist
- Audio player (full screen)
- Mini player (persistent)

#### Search Screens (4 screens)
- Search home
- Search results
- Voice search
- Advanced search

#### Bookmark Screens (5 screens)
- Bookmarks list
- Bookmark detail
- Folders
- Create/edit bookmark
- Create/edit folder

#### Quiz Screens (8 screens)
- Quiz home
- Quiz categories
- Quiz difficulty selection
- Quiz play screen
- Quiz results
- Quiz statistics
- Daily challenge
- Leaderboard

#### Analytics Screens (6 screens)
- Analytics dashboard
- Streak calendar
- Goals
- Create goal
- Achievements
- Achievement detail

#### Profile & Settings (25+ screens)
- Profile
- Edit profile
- Subscription
- Settings hub
- Reading preferences
- Translation selection
- Font selection
- Audio settings
- Reciter selection
- Equalizer
- Notification settings
- Reminder settings
- Privacy settings
- Security settings
- Backup settings
- Cloud sync
- Language settings
- Theme settings
- Display settings
- Data usage
- Storage management
- Cache management
- Export data
- Import data
- Account settings
- About
- Help/FAQ
- Feedback
- Licenses
- Changelog

---

## 🚫 Critical Constraints (DO NOT DEVIATE)

1. **Package Structure**: MUST follow exact folder structure defined
2. **Technology Stack**: MUST use specified technologies (no substitutions)
3. **Offline-First**: MUST work offline after initial setup
4. **100% Feature Coverage**: MUST implement ALL features listed
5. **No Placeholders**: All code must be production-ready (no TODOs for core functionality)
6. **SQLDelight**: MUST use SQLDelight for database (not Room, not Realm)
7. **Jetpack Compose**: MUST use Compose for UI (no XML layouts)
8. **Koin**: MUST use Koin for DI (not Hilt, not Dagger)

---

## 📦 External Assets Required

### Data Files
- Quran text (Uthmani, Simple, Imlaai formats)
- Word-by-word data with grammar
- Translation files (50+ languages)
- Tafsir files (multiple commentaries)
- Surah metadata (names, revelation info, etc.)
- Juz/Page/Manzil/Hizb/Ruku mappings

### Audio Files
- 30+ reciters
- Surah-level audio files
- Word-level audio files (for word-by-word)
- Word timing data (JSON)

### AI Models
- Semantic search model (TFLite)
- Speech recognition model - Arabic (TFLite)
- Speech recognition model - English (TFLite)
- Text classification model (TFLite)
- Pre-computed embeddings (Quran text, translations)

### Fonts
- Uthmanic Hafs
- Amiri Quran
- Scheherazade New
- Noto Naskh Arabic
- Noto Sans Tamil
- Noto Nastaliq Urdu
- Roboto (for UI)

---

## 🎯 Success Criteria

The project is considered complete when:
1. ✅ All 10 core features are fully implemented
2. ✅ All 60+ screens are created and functional
3. ✅ All 28+ database tables are implemented
4. ✅ App works 100% offline
5. ✅ All ViewModels are connected to repositories
6. ✅ All UI components are created
7. ✅ Navigation flows work correctly
8. ✅ Audio playback works with word synchronization
9. ✅ Search (text, semantic, voice) works
10. ✅ Quizzes and analytics work
11. ✅ App builds without errors
12. ✅ No placeholder/dummy code in core features

---

## 📝 Implementation Notes

### Current Status (as of Dec 18, 2025)
- ✅ Project foundation complete
- ✅ Domain layer complete (models + repository interfaces)
- ✅ Database schema complete (20 .sq files)
- ✅ Database infrastructure complete
- ✅ Utilities complete
- ⏳ Data layer in progress (repositories, network, AI, audio)
- ⏳ Android app not started

### Next Priority
1. Complete repository implementations
2. Build network layer
3. Create Android app foundation
4. Implement ViewModels
5. Build UI components
6. Create screens
7. Add services and background workers

---

**This document is the SOURCE OF TRUTH for project requirements. Any deviation must be justified and documented.**

**Last Updated**: December 18, 2025, 11:40 PM IST
