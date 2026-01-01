# AlQuranPlusAI - Complete Project Documentation

**Version**: 1.0  
**Last Updated**: December 21, 2025  
**Status**: In Development (298/500+ files complete)

---

## Table of Contents
1. [Project Structure](#project-structure)
2. [Complete Requirements & Prompt](#complete-requirements--prompt)
3. [Implementation Status](#implementation-status)

---

# Project Structure

## Complete File Tree (500+ Files)

```
AlQuranPlusAI/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── local.properties
├── gradle/
│   ├── wrapper/
│   │   ├── gradle-wrapper.jar
│   │   └── gradle-wrapper.properties
│   └── libs.versions.toml
├── .gitignore
├── .gitattributes
├── README.md
├── LICENSE
├── CHANGELOG.md
├── CODE_OF_CONDUCT.md
├── CONTRIBUTING.md

# ================================
# SHARED MODULE (KMP)
# ================================
├── shared/
│   ├── build.gradle.kts
│   └── src/
│       ├── commonMain/
│       │   ├── kotlin/com/alquranplusai/
│       │   │   ├── domain/
│       │   │   │   ├── models/
│       │   │   │   │   ├── QuranModels.kt
│       │   │   │   │   ├── TranslationModels.kt
│       │   │   │   │   ├── AudioModels.kt
│       │   │   │   │   ├── BookmarkModels.kt
│       │   │   │   │   ├── QuizModels.kt
│       │   │   │   │   ├── SearchModels.kt
│       │   │   │   │   ├── AnalyticsModels.kt
│       │   │   │   │   ├── UserModels.kt
│       │   │   │   │   └── CommonModels.kt
│       │   │   │   └── repositories/
│       │   │   │       ├── QuranRepository.kt
│       │   │   │       ├── TranslationRepository.kt
│       │   │   │       ├── AudioRepository.kt
│       │   │   │       ├── BookmarkRepository.kt
│       │   │   │       ├── QuizRepository.kt
│       │   │   │       ├── SearchRepository.kt
│       │   │   │       ├── AnalyticsRepository.kt
│       │   │   │       └── UserRepository.kt
│       │   │   ├── data/
│       │   │   │   ├── database/
│       │   │   │   │   ├── AlQuranDatabase.kt
│       │   │   │   │   ├── DatabaseDriverFactory.kt
│       │   │   │   │   ├── DatabaseMigrations.kt
│       │   │   │   │   ├── dao/
│       │   │   │   │   │   ├── QuranDao.kt
│       │   │   │   │   │   ├── SurahDao.kt
│       │   │   │   │   │   ├── AyahDao.kt
│       │   │   │   │   │   ├── WordDao.kt
│       │   │   │   │   │   ├── TranslationDao.kt
│       │   │   │   │   │   ├── AudioDao.kt
│       │   │   │   │   │   ├── ReciterDao.kt
│       │   │   │   │   │   ├── PlaylistDao.kt
│       │   │   │   │   │   ├── PlaylistItemDao.kt
│       │   │   │   │   │   ├── BookmarkDao.kt
│       │   │   │   │   │   ├── FolderDao.kt
│       │   │   │   │   │   ├── BookmarkTagDao.kt
│       │   │   │   │   │   ├── NoteDao.kt
│       │   │   │   │   │   ├── QuizDao.kt
│       │   │   │   │   │   ├── QuestionDao.kt
│       │   │   │   │   │   ├── QuizSessionDao.kt
│       │   │   │   │   │   ├── QuizResultDao.kt
│       │   │   │   │   │   ├── UserDao.kt
│       │   │   │   │   │   ├── AnalyticsDao.kt
│       │   │   │   │   │   ├── SessionDao.kt
│       │   │   │   │   │   ├── AchievementDao.kt
│       │   │   │   │   │   ├── GoalDao.kt
│       │   │   │   │   │   └── SettingsDao.kt
│       │   │   │   │   └── entity/
│       │   │   │   │       ├── QuranEntity.kt
│       │   │   │   │       ├── SurahEntity.kt
│       │   │   │   │       ├── AyahEntity.kt
│       │   │   │   │       ├── WordEntity.kt
│       │   │   │   │       ├── TranslationEntity.kt
│       │   │   │   │       ├── AudioFileEntity.kt
│       │   │   │   │       ├── ReciterEntity.kt
│       │   │   │   │       ├── PlaylistEntity.kt
│       │   │   │   │       ├── PlaylistItemEntity.kt
│       │   │   │   │       ├── BookmarkEntity.kt
│       │   │   │   │       ├── FolderEntity.kt
│       │   │   │   │       ├── BookmarkTagEntity.kt
│       │   │   │   │       ├── NoteEntity.kt
│       │   │   │   │       ├── QuizEntity.kt
│       │   │   │   │       ├── QuestionEntity.kt
│       │   │   │   │       ├── QuizSessionEntity.kt
│       │   │   │   │       ├── QuizResultEntity.kt
│       │   │   │   │       ├── UserEntity.kt
│       │   │   │   │       ├── UserPreferencesEntity.kt
│       │   │   │   │       ├── ReadingSessionEntity.kt
│       │   │   │   │       ├── SessionInteractionEntity.kt
│       │   │   │   │       ├── AchievementEntity.kt
│       │   │   │   │       ├── GoalEntity.kt
│       │   │   │   │       └── AnalyticsEntities.kt
│       │   │   │   ├── network/
│       │   │   │   │   ├── HttpClientFactory.kt
│       │   │   │   │   ├── NetworkConfig.kt
│       │   │   │   │   ├── AuthInterceptor.kt
│       │   │   │   │   ├── ErrorHandler.kt
│       │   │   │   │   ├── api/
│       │   │   │   │   │   ├── QuranApiService.kt
│       │   │   │   │   │   ├── TranslationApiService.kt
│       │   │   │   │   │   ├── AudioApiService.kt
│       │   │   │   │   │   ├── BookmarkApiService.kt
│       │   │   │   │   │   ├── QuizApiService.kt
│       │   │   │   │   │   ├── SearchApiService.kt
│       │   │   │   │   │   ├── AnalyticsApiService.kt
│       │   │   │   │   │   ├── UserApiService.kt
│       │   │   │   │   │   └── AuthApiService.kt
│       │   │   │   │   └── dto/
│       │   │   │   │       ├── SurahDto.kt
│       │   │   │   │       ├── AyahDto.kt
│       │   │   │   │       ├── WordDto.kt
│       │   │   │   │       ├── TranslationDto.kt
│       │   │   │   │       ├── AudioDto.kt
│       │   │   │   │       ├── PlaylistDto.kt
│       │   │   │   │       ├── ReciterDto.kt
│       │   │   │   │       ├── BookmarkDto.kt
│       │   │   │   │       ├── FolderDto.kt
│       │   │   │   │       ├── BookmarkTagDto.kt
│       │   │   │   │       ├── NoteDto.kt
│       │   │   │   │       ├── QuizDto.kt
│       │   │   │   │       ├── QuestionDto.kt
│       │   │   │   │       ├── QuizResultDto.kt
│       │   │   │   │       ├── UserDto.kt
│       │   │   │   │       ├── UserProfileDto.kt
│       │   │   │   │       ├── SubscriptionDto.kt
│       │   │   │   │       ├── AnalyticsDto.kt
│       │   │   │   │       ├── SessionDto.kt
│       │   │   │   │       ├── AchievementDto.kt
│       │   │   │   │       └── GoalDto.kt
│       │   │   │   ├── ai/
│       │   │   │   │   ├── SemanticSearchEngine.kt
│       │   │   │   │   ├── SpeechRecognitionEngine.kt
│       │   │   │   │   ├── TextClassificationEngine.kt
│       │   │   │   │   ├── AIModelLoader.kt
│       │   │   │   │   ├── EmbeddingsProcessor.kt
│       │   │   │   │   ├── VectorDatabase.kt
│       │   │   │   │   └── TFLiteInterpreter.kt
│       │   │   │   ├── audio/
│       │   │   │   │   ├── AudioPlayer.kt
│       │   │   │   │   ├── AudioDownloader.kt
│       │   │   │   │   ├── AudioCache.kt
│       │   │   │   │   ├── AudioUtils.kt
│       │   │   │   │   ├── WordTimingProcessor.kt
│       │   │   │   │   └── EqualizerController.kt
│       │   │   │   ├── preferences/
│       │   │   │   │   ├── SettingsDataStore.kt
│       │   │   │   │   ├── PreferencesManager.kt
│       │   │   │   │   └── SecureStorage.kt
│       │   │   │   ├── mappers/
│       │   │   │   │   ├── QuranMapper.kt
│       │   │   │   │   ├── TranslationMapper.kt
│       │   │   │   │   ├── AudioMapper.kt
│       │   │   │   │   ├── BookmarkMapper.kt
│       │   │   │   │   ├── QuizMapper.kt
│       │   │   │   │   ├── AnalyticsMapper.kt
│       │   │   │   │   └── UserMapper.kt
│       │   │   │   └── repositories/
│       │   │   │       ├── QuranRepositoryImpl.kt
│       │   │   │       ├── TranslationRepositoryImpl.kt
│       │   │   │       ├── AudioRepositoryImpl.kt
│       │   │   │       ├── BookmarkRepositoryImpl.kt
│       │   │   │       ├── QuizRepositoryImpl.kt
│       │   │   │       ├── SearchRepositoryImpl.kt
│       │   │   │       ├── AnalyticsRepositoryImpl.kt
│       │   │   │       └── UserRepositoryImpl.kt
│       │   │   ├── utils/
│       │   │   │   ├── DateTimeFormatter.kt
│       │   │   │   ├── StringExtensions.kt
│       │   │   │   ├── FlowExtensions.kt
│       │   │   │   ├── CollectionExtensions.kt
│       │   │   │   ├── Logger.kt
│       │   │   │   ├── Constants.kt
│       │   │   │   ├── ValidationUtils.kt
│       │   │   │   ├── CryptoUtils.kt
│       │   │   │   └── JsonUtils.kt
│       │   │   └── di/
│       │   │       ├── AppModule.kt
│       │   │       ├── DatabaseModule.kt
│       │   │       ├── NetworkModule.kt
│       │   │       ├── RepositoryModule.kt
│       │   │       ├── AIModule.kt
│       │   │       ├── AudioModule.kt
│       │   │       ├── PreferencesModule.kt
│       │   │       └── UseCaseModule.kt
│       ├── commonMain/sqldelight/com/alquranplusai/database/
│       │   ├── AlQuranDatabase.sq
│       │   ├── Surah.sq
│       │   ├── Ayah.sq
│       │   ├── Word.sq
│       │   ├── Translation.sq
│       │   ├── Audio.sq
│       │   ├── Reciter.sq
│       │   ├── Playlist.sq
│       │   ├── PlaylistItem.sq
│       │   ├── Bookmark.sq
│       │   ├── Folder.sq
│       │   ├── BookmarkTag.sq
│       │   ├── Note.sq
│       │   ├── Quiz.sq
│       │   ├── Question.sq
│       │   ├── QuizSession.sq
│       │   ├── QuizResult.sq
│       │   ├── User.sq
│       │   ├── Analytics.sq
│       │   ├── Achievement.sq
│       │   ├── Goal.sq
│       │   └── Settings.sq
│       ├── androidMain/kotlin/com/alquranplusai/shared/
│       │   ├── di/AndroidPlatformModule.kt
│       │   ├── utils/AndroidUtils.kt
│       │   └── database/AndroidDatabaseDriver.kt
│       ├── commonTest/kotlin/com/alquranplusai/
│       │   ├── repositories/
│       │   │   ├── FakeQuranRepository.kt
│       │   │   ├── FakeAudioRepository.kt
│       │   │   ├── FakeBookmarkRepository.kt
│       │   │   ├── FakeQuizRepository.kt
│       │   │   ├── FakeAnalyticsRepository.kt
│       │   │   └── FakeUserRepository.kt
│       │   ├── data/
│       │   │   ├── TestData.kt
│       │   │   ├── TestSurahs.kt
│       │   │   ├── TestAyahs.kt
│       │   │   └── TestUsers.kt
│       │   └── utils/
│       │       └── TestUtils.kt
│       └── androidTest/kotlin/com/alquranplusai/
│           └── (Android-specific tests if needed)

# ================================
# ANDROID APP MODULE
# ================================
├── androidApp/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── kotlin/com/alquranplusai/android/
│       │   │   ├── MainActivity.kt
│       │   │   ├── AlQuranApplication.kt
│       │   │   ├── navigation/
│       │   │   │   ├── AppNavGraph.kt
│       │   │   │   ├── AlQuranNavHost.kt
│       │   │   │   ├── NavRoutes.kt
│       │   │   │   ├── NavArguments.kt
│       │   │   │   ├── BottomNavigation.kt
│       │   │   │   ├── DeepLinkHandler.kt
│       │   │   │   └── NavigationConstants.kt
│       │   │   ├── ui/
│       │   │   │   ├── viewmodels/
│       │   │   │   │   ├── HomeViewModel.kt
│       │   │   │   │   ├── SurahListViewModel.kt
│       │   │   │   │   ├── ReadingViewModel.kt
│       │   │   │   │   ├── JuzViewModel.kt
│       │   │   │   │   ├── PageViewModel.kt
│       │   │   │   │   ├── ManzilViewModel.kt
│       │   │   │   │   ├── AudioViewModel.kt
│       │   │   │   │   ├── PlayerViewModel.kt
│       │   │   │   │   ├── ReciterListViewModel.kt
│       │   │   │   │   ├── PlaylistViewModel.kt
│       │   │   │   │   ├── SearchViewModel.kt
│       │   │   │   │   ├── VoiceSearchViewModel.kt
│       │   │   │   │   ├── BookmarkViewModel.kt
│       │   │   │   │   ├── FoldersViewModel.kt
│       │   │   │   │   ├── QuizListViewModel.kt
│       │   │   │   │   ├── QuizPlayViewModel.kt
│       │   │   │   │   ├── QuizResultsViewModel.kt
│       │   │   │   │   ├── DailyChallengeViewModel.kt
│       │   │   │   │   ├── AnalyticsViewModel.kt
│       │   │   │   │   ├── StreakViewModel.kt
│       │   │   │   │   ├── GoalsViewModel.kt
│       │   │   │   │   ├── AchievementsViewModel.kt
│       │   │   │   │   ├── ProfileViewModel.kt
│       │   │   │   │   ├── AuthViewModel.kt
│       │   │   │   │   ├── SettingsViewModel.kt
│       │   │   │   │   ├── ReadingPreferencesViewModel.kt
│       │   │   │   │   ├── AudioSettingsViewModel.kt
│       │   │   │   │   ├── NotificationSettingsViewModel.kt
│       │   │   │   │   ├── PrivacySettingsViewModel.kt
│       │   │   │   │   ├── BackupSettingsViewModel.kt
│       │   │   │   │   ├── LanguageSettingsViewModel.kt
│       │   │   │   │   ├── ThemeSettingsViewModel.kt
│       │   │   │   │   ├── DisplaySettingsViewModel.kt
│       │   │   │   │   └── OnboardingViewModel.kt
│       │   │   │   ├── screens/
│       │   │   │   │   ├── SplashScreen.kt
│       │   │   │   │   ├── OnboardingScreen.kt
│       │   │   │   │   ├── WelcomeScreen.kt
│       │   │   │   │   ├── PermissionsScreen.kt
│       │   │   │   │   ├── SetupCompleteScreen.kt
│       │   │   │   │   ├── home/
│       │   │   │   │   │   └── HomeScreen.kt
│       │   │   │   │   ├── quran/
│       │   │   │   │   │   ├── SurahListScreen.kt
│       │   │   │   │   │   ├── SurahDetailScreen.kt
│       │   │   │   │   │   ├── ReadingScreen.kt
│       │   │   │   │   │   ├── JuzViewScreen.kt
│       │   │   │   │   │   ├── PageViewScreen.kt
│       │   │   │   │   │   ├── ManzilViewScreen.kt
│       │   │   │   │   │   └── AyahDetailScreen.kt
│       │   │   │   │   ├── audio/
│       │   │   │   │   │   ├── AudioScreen.kt
│       │   │   │   │   │   ├── AudioPlayerViewModel.kt
│       │   │   │   │   │   ├── ReciterListScreen.kt
│       │   │   │   │   │   ├── ReciterDetailScreen.kt
│       │   │   │   │   │   ├── PlaylistScreen.kt
│       │   │   │   │   │   ├── CreatePlaylistScreen.kt
│       │   │   │   │   │   └── AudioPlayerScreen.kt
│       │   │   │   │   ├── search/
│       │   │   │   │   │   ├── SearchScreen.kt
│       │   │   │   │   │   ├── SearchResultsScreen.kt
│       │   │   │   │   │   ├── VoiceSearchScreen.kt
│       │   │   │   │   │   └── AdvancedSearchScreen.kt
│       │   │   │   │   ├── bookmarks/
│       │   │   │   │   │   ├── BookmarksScreen.kt
│       │   │   │   │   │   ├── BookmarkDetailScreen.kt
│       │   │   │   │   │   ├── BookmarkFoldersScreen.kt
│       │   │   │   │   │   ├── CreateBookmarkScreen.kt
│       │   │   │   │   │   └── EditBookmarkScreen.kt
│       │   │   │   │   ├── quiz/
│       │   │   │   │   │   ├── QuizListScreen.kt
│       │   │   │   │   │   ├── QuizCategoryScreen.kt
│       │   │   │   │   │   ├── QuizDifficultyScreen.kt
│       │   │   │   │   │   ├── QuizPlayScreen.kt
│       │   │   │   │   │   ├── QuizResultsScreen.kt
│       │   │   │   │   │   ├── QuizStatisticsScreen.kt
│       │   │   │   │   │   ├── DailyChallengeScreen.kt
│       │   │   │   │   │   └── LeaderboardScreen.kt
│       │   │   │   │   ├── analytics/
│       │   │   │   │   │   ├── AnalyticsScreen.kt
│       │   │   │   │   │   ├── AnalyticsViewModel.kt
│       │   │   │   │   │   ├── StreakScreen.kt
│       │   │   │   │   │   ├── GoalsScreen.kt
│       │   │   │   │   │   ├── CreateGoalScreen.kt
│       │   │   │   │   │   ├── AchievementsScreen.kt
│       │   │   │   │   │   └── AchievementDetailScreen.kt
│       │   │   │   │   ├── profile/
│       │   │   │   │   │   ├── ProfileScreen.kt
│       │   │   │   │   │   ├── EditProfileScreen.kt
│       │   │   │   │   │   └── SubscriptionScreen.kt
│       │   │   │   │   ├── auth/
│       │   │   │   │   │   ├── AuthScreen.kt
│       │   │   │   │   │   ├── LoginScreen.kt
│       │   │   │   │   │   └── RegisterScreen.kt
│       │   │   │   │   └── settings/
│       │   │   │   │       ├── SettingsScreen.kt
│       │   │   │   │       ├── ReadingPreferencesScreen.kt
│       │   │   │   │       ├── TranslationSelectionScreen.kt
│       │   │   │   │       ├── FontSelectionScreen.kt
│       │   │   │   │       ├── AudioSettingsScreen.kt
│       │   │   │   │       ├── ReciterSelectionScreen.kt
│       │   │   │   │       ├── EqualizerScreen.kt
│       │   │   │   │       ├── NotificationSettingsScreen.kt
│       │   │   │   │       ├── ReminderSettingsScreen.kt
│       │   │   │   │       ├── PrivacySettingsScreen.kt
│       │   │   │   │       ├── SecuritySettingsScreen.kt
│       │   │   │   │       ├── BackupSettingsScreen.kt
│       │   │   │   │       ├── CloudSyncScreen.kt
│       │   │   │   │       ├── LanguageSettingsScreen.kt
│       │   │   │   │       ├── ThemeSettingsScreen.kt
│       │   │   │   │       ├── DisplaySettingsScreen.kt
│       │   │   │   │       ├── DataUsageScreen.kt
│       │   │   │   │       ├── StorageManagementScreen.kt
│       │   │   │   │       ├── CacheManagementScreen.kt
│       │   │   │   │       ├── ExportDataScreen.kt
│       │   │   │   │       ├── ImportDataScreen.kt
│       │   │   │   │       ├── AccountSettingsScreen.kt
│       │   │   │   │       ├── LinkedAccountsScreen.kt
│       │   │   │   │       ├── AboutScreen.kt
│       │   │   │   │       ├── HelpScreen.kt
│       │   │   │   │       ├── FAQScreen.kt
│       │   │   │   │       ├── FeedbackScreen.kt
│       │   │   │   │       ├── LicensesScreen.kt
│       │   │   │   │       └── ChangelogScreen.kt
│       │   │   │   ├── components/
│       │   │   │   │   ├── common/
│       │   │   │   │   │   ├── TopAppBar.kt
│       │   │   │   │   │   ├── BackButton.kt
│       │   │   │   │   │   ├── BottomSheet.kt
│       │   │   │   │   │   ├── LoadingIndicator.kt
│       │   │   │   │   │   ├── LoadingScreen.kt
│       │   │   │   │   │   ├── ErrorDialog.kt
│       │   │   │   │   │   ├── ErrorScreen.kt
│       │   │   │   │   │   ├── ConfirmationDialog.kt
│       │   │   │   │   │   ├── InfoDialog.kt
│       │   │   │   │   │   ├── ProgressCard.kt
│       │   │   │   │   │   ├── StatsCard.kt
│       │   │   │   │   │   ├── SectionHeader.kt
│       │   │   │   │   │   ├── EmptyState.kt
│       │   │   │   │   │   ├── SearchBar.kt
│       │   │   │   │   │   ├── FilterChip.kt
│       │   │   │   │   │   ├── FloatingActionButton.kt
│       │   │   │   │   │   ├── CustomButton.kt
│       │   │   │   │   │   ├── OutlinedButton.kt
│       │   │   │   │   │   ├── TextButton.kt
│       │   │   │   │   │   ├── IconButton.kt
│       │   │   │   │   │   ├── CustomTextField.kt
│       │   │   │   │   │   ├── PasswordField.kt
│       │   │   │   │   │   ├── DropdownMenu.kt
│       │   │   │   │   │   ├── DatePicker.kt
│       │   │   │   │   │   ├── TimePicker.kt
│       │   │   │   │   │   ├── SwipeableCard.kt
│       │   │   │   │   │   └── PullToRefresh.kt
│       │   │   │   │   ├── quran/
│       │   │   │   │   │   ├── SurahCard.kt
│       │   │   │   │   │   ├── SurahListItem.kt
│       │   │   │   │   │   ├── SurahHeader.kt
│       │   │   │   │   │   ├── AyahView.kt
│       │   │   │   │   │   ├── AyahCard.kt
│       │   │   │   │   │   ├── ArabicTextView.kt
│       │   │   │   │   │   ├── TranslationView.kt
│       │   │   │   │   │   ├── WordByWordView.kt
│       │   │   │   │   │   ├── TajweedTextView.kt
│       │   │   │   │   │   ├── ReadingControls.kt
│       │   │   │   │   │   ├── ReadingModeSelector.kt
│       │   │   │   │   │   ├── FontSizeControl.kt
│       │   │   │   │   │   ├── JuzCard.kt
│       │   │   │   │   │   ├── PageCard.kt
│       │   │   │   │   │   ├── ManzilCard.kt
│       │   │   │   │   │   ├── SajdaIndicator.kt
│       │   │   │   │   │   ├── VerseSeparator.kt
│       │   │   │   │   │   ├── BismillahView.kt
│       │   │   │   │   │   ├── VerseNumber.kt
│       │   │   │   │   │   └── TafsirView.kt
│       │   │   │   │   ├── audio/
│       │   │   │   │   │   ├── ReciterCard.kt
│       │   │   │   │   │   ├── ReciterListItem.kt
│       │   │   │   │   │   ├── ReciterAvatar.kt
│       │   │   │   │   │   ├── AudioControls.kt
│       │   │   │   │   │   ├── PlayerControls.kt
│       │   │   │   │   │   ├── MiniPlayer.kt
│       │   │   │   │   │   ├── FullScreenPlayer.kt
│       │   │   │   │   │   ├── ProgressSlider.kt
│       │   │   │   │   │   ├── TimeDisplay.kt
│       │   │   │   │   │   ├── PlaylistItem.kt
│       │   │   │   │   │   ├── PlaylistCard.kt
│       │   │   │   │   │   ├── EqualizerView.kt
│       │   │   │   │   │   ├── EqualizerBand.kt
│       │   │   │   │   │   ├── SpeedControl.kt
│       │   │   │   │   │   ├── RepeatModeButton.kt
│       │   │   │   │   │   ├── ShuffleButton.kt
│       │   │   │   │   │   ├── VolumeSlider.kt
│       │   │   │   │   │   ├── DownloadProgress.kt
│       │   │   │   │   │   ├── DownloadButton.kt
│       │   │   │   │   │   ├── AudioVisualization.kt
│       │   │   │   │   │   └── WaveformView.kt
│       │   │   │   │   ├── bookmarks/
│       │   │   │   │   │   ├── BookmarkCard.kt
│       │   │   │   │   │   ├── BookmarkListItem.kt
│       │   │   │   │   │   ├── BookmarkFolderCard.kt
│       │   │   │   │   │   ├── FolderListItem.kt
│       │   │   │   │   │   ├── BookmarkForm.kt
│       │   │   │   │   │   ├── ReminderSetup.kt
│       │   │   │   │   │   ├── ReminderCard.kt
│       │   │   │   │   │   ├── TagsInput.kt
│       │   │   │   │   │   ├── TagChip.kt
│       │   │   │   │   │   ├── CategoryPicker.kt
│       │   │   │   │   │   ├── CategoryChip.kt
│       │   │   │   │   │   ├── PriorityPicker.kt
│       │   │   │   │   │   ├── ColorPicker.kt
│       │   │   │   │   │   ├── BookmarkPreview.kt
│       │   │   │   │   │   └── QuickBookmarkButton.kt
│       │   │   │   │   ├── quiz/
│       │   │   │   │   │   ├── QuizCard.kt
│       │   │   │   │   │   ├── QuizListItem.kt
│       │   │   │   │   │   ├── CategoryCard.kt
│       │   │   │   │   │   ├── QuestionView.kt
│       │   │   │   │   │   ├── QuestionCard.kt
│       │   │   │   │   │   ├── AnswerOptions.kt
│       │   │   │   │   │   ├── AnswerButton.kt
│       │   │   │   │   │   ├── MultipleChoiceOptions.kt
│       │   │   │   │   │   ├── TrueFalseOptions.kt
│       │   │   │   │   │   ├── QuizProgress.kt
│       │   │   │   │   │   ├── QuizProgressBar.kt
│       │   │   │   │   │   ├── ScoreCard.kt
│       │   │   │   │   │   ├── ScoreDisplay.kt
│       │   │   │   │   │   ├── TimerView.kt
│       │   │   │   │   │   ├── CountdownTimer.kt
│       │   │   │   │   │   ├── ExplanationView.kt
│       │   │   │   │   │   ├── HintCard.kt
│       │   │   │   │   │   ├── AchievementBadge.kt
│       │   │   │   │   │   ├── DifficultyIndicator.kt
│       │   │   │   │   │   ├── QuizLeaderboard.kt
│       │   │   │   │   │   └── DailyChallengeCard.kt
│       │   │   │   │   ├── analytics/
│       │   │   │   │   │   ├── StatCard.kt
│       │   │   │   │   │   ├── StatsRow.kt
│       │   │   │   │   │   ├── ProgressChart.kt
│       │   │   │   │   │   ├── LineChart.kt
│       │   │   │   │   │   ├── BarChart.kt
│       │   │   │   │   │   ├── PieChart.kt
│       │   │   │   │   │   ├── StreakCalendar.kt
│       │   │   │   │   │   ├── CalendarDay.kt
│       │   │   │   │   │   ├── GoalCard.kt
│       │   │   │   │   │   ├── GoalProgress.kt
│       │   │   │   │   │   ├── GoalListItem.kt
│       │   │   │   │   │   ├── TrendChart.kt
│       │   │   │   │   │   ├── ReadingTimeChart.kt
│       │   │   │   │   │   ├── WeeklyReport.kt
│       │   │   │   │   │   ├── MonthlyInsights.kt
│       │   │   │   │   │   ├── YearlyOverview.kt
│       │   │   │   │   │   ├── AchievementProgress.kt
│       │   │   │   │   │   ├── MilestoneCard.kt
│       │   │   │   │   │   └── ComparisonCard.kt
│       │   │   │   │   └── search/
│       │   │   │   │       ├── SearchFilters.kt
│       │   │   │   │       ├── FilterBottomSheet.kt
│       │   │   │   │       ├── SearchResultCard.kt
│       │   │   │   │       ├── ResultListItem.kt
│       │   │   │   │       ├── HighlightedText.kt
│       │   │   │   │       ├── VoiceIndicator.kt
│       │   │   │   │       ├── VoiceWaveform.kt
│       │   │   │   │       ├── AIInsightsCard.kt
│       │   │   │   │       ├── SemanticResultCard.kt
│       │   │   │   │       ├── SearchSuggestions.kt
│       │   │   │   │       ├── SuggestionChip.kt
│       │   │   │   │       ├── RecentSearches.kt
│       │   │   │   │       ├── PopularSearches.kt
│       │   │   │   │       ├── TrendingSearches.kt
│       │   │   │   │       ├── AdvancedFilters.kt
│       │   │   │   │       └── SearchHistory.kt
│       │   │   │   └── theme/
│       │   │   │       ├── Theme.kt
│       │   │   │       ├── Color.kt
│       │   │   │       ├── DarkColors.kt
│       │   │   │       ├── LightColors.kt
│       │   │   │       ├── Typography.kt
│       │   │   │       ├── ArabicFonts.kt
│       │   │   │       ├── TamilFonts.kt
│       │   │   │       ├── UrduFonts.kt
│       │   │   │       ├── Spacing.kt
│       │   │   │       ├── Shapes.kt
│       │   │   │       ├── Dimensions.kt
│       │   │   │       └── Animations.kt
│       │   │   ├── services/
│       │   │   │   ├── AudioPlaybackService.kt
│       │   │   │   ├── MediaSessionCallback.kt
│       │   │   │   ├── NotificationService.kt
│       │   │   │   ├── NotificationBuilder.kt
│       │   │   │   ├── DownloadService.kt
│       │   │   │   ├── DownloadManager.kt
│       │   │   │   ├── BackupService.kt
│       │   │   │   ├── RestoreService.kt
│       │   │   │   ├── ReminderService.kt
│       │   │   │   ├── SyncService.kt
│       │   │   │   ├── UpdateService.kt
│       │   │   │   └── OtherServices.kt
│       │   │   ├── receivers/
│       │   │   │   ├── BootReceiver.kt
│       │   │   │   ├── NotificationReceiver.kt
│       │   │   │   ├── AlarmReceiver.kt
│       │   │   │   ├── MediaButtonReceiver.kt
│       │   │   │   ├── ConnectivityReceiver.kt
│       │   │   │   └── Receivers.kt
│       │   │   ├── workers/
│       │   │   │   ├── DataSyncWorker.kt
│       │   │   │   ├── BackupWorker.kt
│       │   │   │   ├── AnalyticsWorker.kt
│       │   │   │   ├── CleanupWorker.kt
│       │   │   │   ├── AudioDownloadWorker.kt
│       │   │   │   ├── QuranDownloadWorker.kt
│       │   │   │   ├── ReminderWorker.kt
│       │   │   │   └── OtherWorkers.kt
│       │   │   ├── utils/
│       │   │   │   ├── ThemeManager.kt
│       │   │   │   ├── LanguageManager.kt
│       │   │   │   ├── LocaleHelper.kt
│       │   │   │   ├── PermissionManager.kt
│       │   │   │   ├── AudioManager.kt
│       │   │   │   ├── ShareManager.kt
│       │   │   │   ├── BackupManager.kt
│       │   │   │   ├── FileManager.kt
│       │   │   │   ├── ImageLoader.kt
│       │   │   │   ├── DeepLinkHandler.kt
│       │   │   │   ├── IntentHelper.kt
│       │   │   │   ├── CrashReportingManager.kt
│       │   │   │   ├── AnalyticsTracker.kt
│       │   │   │   ├── PreferencesManager.kt
│       │   │   │   ├── NotificationHelper.kt
│       │   │   │   ├── NetworkMonitor.kt
│       │   │   │   ├── BiometricHelper.kt
│       │   │   │   ├── ClipboardHelper.kt
│       │   │   │   ├── VibratorHelper.kt
│       │   │   │   ├── AppUtils.kt
│       │   │   │   ├── FileUtils.kt
│       │   │   │   └── SystemUtils.kt
│       │   │   └── di/
│       │   │       ├── DiModules.kt
│       │   │       ├── AndroidAppModule.kt
│       │   │       ├── ViewModelModule.kt
│       │   │       ├── ServiceModule.kt
│       │   │       ├── UtilsModule.kt
│       │   │       ├── NavigationModule.kt
│       │   │       └── WorkerModule.kt
│       │   └── res/
│       │       └── (Android resources)
│       ├── test/kotlin/com/alquranplusai/android/
│       │   ├── viewmodels/
│       │   │   ├── HomeViewModelTest.kt
│       │   │   ├── ReadingViewModelTest.kt
│       │   │   ├── AudioViewModelTest.kt
│       │   │   ├── SearchViewModelTest.kt
│       │   │   ├── BookmarkViewModelTest.kt
│       │   │   ├── QuizViewModelTest.kt
│       │   │   └── AnalyticsViewModelTest.kt
│       │   ├── repositories/
│       │   │   ├── QuranRepositoryTest.kt
│       │   │   ├── AudioRepositoryTest.kt
│       │   │   └── BookmarkRepositoryTest.kt
│       │   ├── utils/
│       │   │   ├── DateFormatterTest.kt
│       │   │   ├── ValidationUtilsTest.kt
│       │   │   └── TestUtils.kt
│       │   └── ExampleUnitTest.kt
│       └── androidTest/kotlin/com/alquranplusai/android/
│           ├── ui/
│           │   ├── HomeScreenTest.kt
│           │   ├── ReadingScreenTest.kt
│           │   ├── AudioScreenTest.kt
│           │   ├── SearchScreenTest.kt
│           │   └── BookmarkScreenTest.kt
│           ├── database/
│           │   ├── DatabaseTest.kt
│           │   ├── DaoTest.kt
│           │   └── MigrationTest.kt
│           ├── navigation/
│           │   └── NavigationTest.kt
│           └── ExampleInstrumentedTest.kt

# ================================
# DOCUMENTATION
# ================================
└── docs/
    ├── PROJECT_STRUCTURE.md (this file)
    ├── API_DOCUMENTATION.md
    ├── ARCHITECTURE.md
    └── SETUP_GUIDE.md
```

----------------------------------------------------
===============PROMPT==============================
---------------------------------------------------
I am building a production-grade Quran application called **AlQuranPlusAI** using **Kotlin Multiplatform (KMP)** with an Android app (Jetpack Compose) and a shared business-logic module.

Your task: Generate a **complete, production-ready codebase** with **100% coverage of all features and use cases** described below, strictly following the folder structure and naming conventions. The app should be on par with or better than leading Quran apps that offer word-by-word, multi-translation, tafsir, grammar, powerful search, AI help, memorization tools, analytics, and offline audio. [web:25][web:29][web:32][web:39][web:40][web:43]

==================================================
APP IDENTITY
==================================================

- App name (store / launcher): **AlQuran Plus AI**
- Code name: **AlQuranPlusAI**
- Base package: **com.alquranplusai**
- Root folder: **AlQuranPlusAI/**

==================================================
HIGH-LEVEL TECH STACK
==================================================

- Kotlin Multiplatform: shared `:shared` module for business logic
- Android app: `:androidApp` with Jetpack Compose UI
- Database: SQLDelight (offline-first)
- Networking: Ktor (or similar KMP-capable HTTP client)
- DI: Koin for shared + Android
- Serialization: kotlinx.serialization
- Background: WorkManager on Android
- Audio: ExoPlayer wrapper for recitation, notification controls
- AI: TensorFlow Lite for semantic search & speech recognition
- Preferences: DataStore / KMP key-value for shared prefs

==================================================
FOLDER STRUCTURE (STRICT)
==================================================

**Root**
- `AlQuranPlusAI/`
  - `build.gradle.kts`
  - `settings.gradle.kts`
  - `gradle.properties`
  - `gradle/wrapper/...`
  - `gradle/libs.versions.toml`

**Shared module**
- `shared/build.gradle.kts`
- `shared/src/commonMain/kotlin/com/alquranplusai/`
  - `domain/`
    - `models/`
      - `QuranModels.kt` (Surah, Ayah, Word, Juz, Page, Manzil, Hizb, Ruku, Sajda info)
      - `TranslationModels.kt` (Translation, AyahTranslation, WordTranslation, TranslationMetadata)
      - `AudioModels.kt` (Reciter, AudioFile, Playlist, PlaylistItem, AudioSettings, EqualizerSettings, WordTiming)
      - `BookmarkModels.kt` (Bookmark, BookmarkFolder, BookmarkCategory, BookmarkReminder, BookmarkTag, Note)
      - `QuizModels.kt` (Quiz, Question, QuizAttempt, DailyChallenge, QuizSession, QuizResult, QuizStatistics, LeaderboardEntry, Achievement, Goal)
      - `SearchModels.kt` (SearchQuery, SearchResult, VoiceSearchResult, AIInsights, SearchFilters, SearchContext, SearchHistoryItem)
      - `AnalyticsModels.kt` (ReadingSession, ReadingPattern, WeeklyReport, MonthlyInsight, YearlyOverview, SessionInteraction, Milestone)
      - `UserModels.kt` (User, UserProfile, UserPreferences, UserStatistics, Subscription, Plan)
      - `CommonModels.kt` (Resource, Result, ApiResponse, DownloadProgress, NetworkState, LoadingState, ErrorModel)
    - `repositories/` (interfaces)
      - `QuranRepository.kt`
      - `TranslationRepository.kt`
      - `AudioRepository.kt`
      - `BookmarkRepository.kt`
      - `QuizRepository.kt`
      - `SearchRepository.kt`
      - `AnalyticsRepository.kt`
      - `UserRepository.kt`

  - `data/`
    - `database/`
      - `AlQuranDatabase.kt`
      - `DatabaseDriverFactory.kt`
      - `DatabaseMigrations.kt`
      - `dao/` (SQLDelight-generated interfaces + facades)
        - `QuranDao.kt`, `SurahDao.kt`, `AyahDao.kt`, `WordDao.kt`
        - `TranslationDao.kt`, `AudioDao.kt`, `ReciterDao.kt`, `PlaylistDao.kt`
        - `BookmarkDao.kt`, `FolderDao.kt`, `NoteDao.kt`
        - `QuizDao.kt`, `QuestionDao.kt`, `QuizSessionDao.kt`, `QuizResultDao.kt`
        - `UserDao.kt`, `AnalyticsDao.kt`, `SessionDao.kt`
        - `AchievementDao.kt`, `GoalDao.kt`, `SettingsDao.kt`
      - `entity/` (KMP data entities mapped to SQLDelight tables)
        - `QuranEntity.kt` (common fields)
        - `SurahEntity.kt`, `AyahEntity.kt`, `WordEntity.kt`
        - `TranslationEntity.kt`
        - `AudioFileEntity.kt`, `ReciterEntity.kt`, `PlaylistEntity.kt`, `PlaylistItemEntity.kt`
        - `BookmarkEntity.kt`, `FolderEntity.kt`, `BookmarkTagEntity.kt`, `NoteEntity.kt`
        - `QuizEntity.kt`, `QuestionEntity.kt`, `QuizSessionEntity.kt`, `QuizResultEntity.kt`
        - `UserEntity.kt`, `UserPreferencesEntity.kt`
        - `ReadingSessionEntity.kt`, `SessionInteractionEntity.kt`
        - `AchievementEntity.kt`, `GoalEntity.kt`
        - `AnalyticsEntities.kt` (aggregated metrics)
    - `network/`
      - `HttpClientFactory.kt`, `NetworkConfig.kt`, `AuthInterceptor.kt`, `ErrorHandler.kt`
      - `api/`
        - `QuranApiService.kt`, `TranslationApiService.kt`, `AudioApiService.kt`
        - `BookmarkApiService.kt`, `QuizApiService.kt`, `SearchApiService.kt`
        - `AnalyticsApiService.kt`, `UserApiService.kt`, `AuthApiService.kt`
      - `dto/`
        - `SurahDto.kt`, `AyahDto.kt`, `WordDto.kt`
        - `TranslationDto.kt`, `AudioDto.kt`, `PlaylistDto.kt`, `ReciterDto.kt`
        - `BookmarkDto.kt`, `FolderDto.kt`, `NoteDto.kt`
        - `QuizDto.kt`, `QuestionDto.kt`, `QuizResultDto.kt`
        - `UserDto.kt`, `UserProfileDto.kt`, `SubscriptionDto.kt`
        - `AnalyticsDto.kt`, `SessionDto.kt`, `AchievementDto.kt`, `GoalDto.kt`

    - `ai/`
      - `SemanticSearchEngine.kt` (embeddings-based semantic search over Quran & translations) [web:29][web:32][web:39][web:43]
      - `SpeechRecognitionEngine.kt` (Arabic + English voice search)
      - `TextClassificationEngine.kt` (intent and topic classification)
      - `AIModelLoader.kt`
      - `EmbeddingsProcessor.kt`
      - `VectorDatabase.kt`
      - `TFLiteInterpreter.kt`

    - `audio/`
      - `AudioPlayer.kt` (wrap ExoPlayer)
      - `AudioDownloader.kt`
      - `AudioCache.kt`
      - `AudioUtils.kt`
      - `WordTimingProcessor.kt` (synchronise word-by-word highlighting with audio timing) [web:32][web:39][web:41]
      - `EqualizerController.kt`

    - `preferences/`
      - `SettingsDataStore.kt`
      - `PreferencesManager.kt`
      - `SecureStorage.kt`

    - `mappers/`
      - `QuranMapper.kt`
      - `TranslationMapper.kt`
      - `AudioMapper.kt`
      - `BookmarkMapper.kt`
      - `QuizMapper.kt`
      - `AnalyticsMapper.kt`
      - `UserMapper.kt`

    - `repositories/` (implementations)
      - `QuranRepositoryImpl.kt`
      - `TranslationRepositoryImpl.kt`
      - `AudioRepositoryImpl.kt`
      - `BookmarkRepositoryImpl.kt`
      - `QuizRepositoryImpl.kt`
      - `SearchRepositoryImpl.kt`
      - `AnalyticsRepositoryImpl.kt`
      - `UserRepositoryImpl.kt`

  - `utils/`
    - `DateTimeFormatter.kt`, `StringExtensions.kt`, `FlowExtensions.kt`, `CollectionExtensions.kt`
    - `Logger.kt`, `Constants.kt`, `ValidationUtils.kt`
    - `CryptoUtils.kt`, `JsonUtils.kt`

  - `di/`
    - `AppModule.kt`, `DatabaseModule.kt`, `NetworkModule.kt`
    - `RepositoryModule.kt`, `AIModule.kt`, `AudioModule.kt`
    - `PreferencesModule.kt`, `UseCaseModule.kt` (if needed as façade over repositories)

- `shared/src/commonMain/sqldelight/com/alquranplusai/database/`
  - `AlQuranDatabase.sq` + table files:
    - `Surah.sq`, `Ayah.sq`, `Word.sq`, `Translation.sq`
    - `Audio.sq`, `Reciter.sq`, `Playlist.sq`, `Bookmark.sq`, `Folder.sq`, `Note.sq`
    - `Quiz.sq`, `Question.sq`, `QuizSession.sq`, `QuizResult.sq`
    - `User.sq`, `Analytics.sq`, `Achievement.sq`, `Goal.sq`, `Settings.sq`

**Android app module**
- `androidApp/src/main/kotlin/com/alquranplusai/android/`
  - `MainActivity.kt`
  - `AlQuranApplication.kt`
  - `navigation/` (AppNavGraph, NavRoutes, NavArguments, BottomNavigation, DeepLinkHandler, NavigationConstants)
  - `viewmodels/` (one ViewModel per screen / feature: Home, SurahList, Reading, Juz, Page, Manzil, Audio, Player, ReciterList, Playlist, Search, VoiceSearch, Bookmarks, Folders, QuizList, QuizPlay, QuizResults, DailyChallenge, Analytics, Streak, Goals, Achievements, Profile, Auth, Settings, ReadingPreferences, AudioSettings, NotificationSettings, PrivacySettings, BackupSettings, LanguageSettings, ThemeSettings, DisplaySettings, Onboarding, Subscription, etc.)
  - `ui/`
    - `screens/` (all the screens you enumerated: onboarding, auth, home, reading flows, audio, reciters, playlists, bookmarks, quiz flows, analytics screens, settings tree, profile, subscription, debug, etc.)
    - `components/common/` (TopAppBar, BackButton, BottomSheet, LoadingIndicator, ErrorScreen, dialogs, buttons, text fields, etc.)
    - `components/quran/` (SurahCard, SurahListItem, AyahView, WordByWordView, TajweedTextView, ReadingControls, etc.)
    - `components/audio/` (ReciterCard, PlayerControls, MiniPlayer, WaveformView, EqualizerView, DownloadButton, etc.)
    - `components/bookmarks/`, `components/quiz/`, `components/analytics/`, `components/search/` as previously detailed.
    - `theme/` (Theme.kt, Color.kt, DarkColors.kt, LightColors.kt, Typography.kt, ArabicFonts.kt, Spacing.kt, Shapes.kt, Dimensions.kt, Animations.kt)

  - `services/`
    - `AudioPlaybackService.kt`, `MediaSessionCallback.kt`
    - `NotificationService.kt`, `NotificationBuilder.kt`
    - `DownloadService.kt`, `BackupService.kt`, `RestoreService.kt`
    - `ReminderService.kt`, `SyncService.kt`, `UpdateService.kt`

  - `receivers/`
    - `BootReceiver.kt`, `NotificationReceiver.kt`, `AlarmReceiver.kt`, `MediaButtonReceiver.kt`, `ConnectivityReceiver.kt`

  - `workers/`
    - `DataSyncWorker.kt`, `BackupWorker.kt`, `AnalyticsWorker.kt`, `CleanupWorker.kt`, `AudioDownloadWorker.kt`, `ReminderWorker.kt`

  - `utils/`
    - `ThemeManager.kt`, `LanguageManager.kt`, `LocaleHelper.kt`, `PermissionManager.kt`
    - `AudioManager.kt`, `ShareManager.kt`, `BackupManager.kt`, `FileManager.kt`
    - `ImageLoader.kt`, `DeepLinkHandler.kt`, `IntentHelper.kt`
    - `CrashReportingManager.kt`, `AnalyticsTracker.kt`, `PreferencesManager.kt`
    - `NotificationHelper.kt`, `NetworkMonitor.kt`, `BiometricHelper.kt`, `ClipboardHelper.kt`, `VibratorHelper.kt`

  - `di/`
    - `DiModules.kt`, `AndroidModule.kt`
    - `ViewModelModule.kt`, `ServiceModule.kt`, `UtilsModule.kt`
    - `NavigationModule.kt`, `WorkerModule.kt`

==================================================
FEATURES & USE CASES (FUNCTIONAL SPEC)
==================================================

Implement **all** of the following behaviour, drawing inspiration from top Quran apps that support word-by-word, advanced search, grammar, AI assistance, and memorization: [web:25][web:29][web:32][web:35][web:39][web:41][web:43]

1. **Quran Reading & Study**
   - Surah/Juz/Page/Manzil/Hizb/Ruku navigation.
   - Mushaf-like reading experience where appropriate. [web:32][web:35]
   - Word-by-word view with translation, transliteration, root, grammar, and occurrences. [web:25][web:29][web:32][web:35]
   - Color-coded Tajweed. [web:25][web:32]
   - Auto-scroll, continue from last read, reading progress & stats.

2. **Audio & Memorization**
   - 30+ reciters, streaming + offline download. [web:29][web:32][web:39]
   - Word-by-word audio playback with synchronized highlighting. [web:32][web:39][web:41]
   - Repeat ranges, group playback, hifz mode, sleep timer, playlists. [web:32][web:39][web:41]
   - Background playback with media notification.

3. **Translations & Tafsir**
   - Multiple translations shown side-by-side for each Ayah. [web:25][web:29][web:35]
   - Tafsir viewing (e.g., Ibn Kathir, Tabari, Qurtubi, Jalalayn). [web:25][web:32][web:35]
   - Download management for translations/tafsir packs.

4. **Search & AI**
   - Exact text search with keyword highlighting across Arabic and translations. [web:29][web:32][web:39]
   - Root and morphology search for advanced users. [web:25][web:29]
   - Topic-based grouping (e.g., Salah, Zakah, Hajj). [web:32][web:35]
   - Semantic search powered by embeddings, with AI insights summarizing relevant verses. [web:37][web:43]
   - Voice search using Arabic & English speech recognition. [web:29][web:37][web:43]
   - Search history, favourites, and trending queries.

5. **Bookmarks, Notes, Collections**
   - Bookmark any Ayah, create folders and collections. [web:29][web:32]
   - Add personal notes to verses, export/import, quick access collections. [web:29]
   - Reminders linked to specific bookmarks.

6. **Quizzes & Daily Challenge**
   - Quran-based quizzes with difficulty levels and multiple categories.
   - Timed quizzes, scoring, explanations, hints, and review mode.
   - Daily challenge with streaks and leaderboards. [web:32][web:43]
   - Achievements and goals around reading and quizzes.

7. **Analytics & Progress**
   - Track reading time, verses read, Surahs completed, and streaks with charts. [web:32][web:39][web:43]
   - Weekly, monthly, yearly insight screens.
   - Goals (daily Ayah count, time or Surah goals) and notifications.

8. **User & Subscription**
   - Auth (email/password + optional social), profiles, preferences.
   - Subscription tiers that unlock advanced AI features, extra reciters, or analytics.
   - Secure token storage and optional biometric lock.

9. **Settings**
   - Reading settings (font choice, size, line spacing, themes).
   - Audio settings (default reciter, gap/loop/word-level playback).
   - Notification, privacy, backup/sync, data usage, language (UI + translations).

10. **Offline-First**
    - Quran text, metadata, word-by-word, audio (downloaded), translations stored locally with proper sync strategy. [web:25][web:29][web:32][web:39][web:40]

==================================================
IMPLEMENTATION & GENERATION RULES
==================================================

- Respect this structure and naming exactly; do not invent new top-level modules or random folders.
- Use full, compilable Kotlin code with correct package/imports.
- No dummy placeholders like TODO; provide meaningful, realistic implementations or clearly separated stubs where external services are expected.
- Generate code in **logical batches** (e.g., models → entities/DTOs → SQLDelight .sq → repositories → DI → AI/audio layer → Android ViewModels → screens → components → workers/services).
- Assume assets (Quran text, translations, audio, models) exist in appropriate `assets/` and `res/` locations and create loaders/parsers for them.
- Structure should be compatible with Android Studio’s KMP Shared Module template. [web:30][web:33][web:42]

Your output should be a **complete, production-quality AlQuranPlusAI codebase** matching all the above requirements and fully covering every described feature and use case without omissions.

---

**Document Version**: 1.0  
**Generated**: December 21, 2025  
**Maintainer**: AlQuranPlusAI Development Team
