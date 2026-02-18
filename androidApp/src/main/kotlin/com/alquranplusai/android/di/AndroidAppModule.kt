package com.alquranplusai.android.di

import com.alquranplusai.android.services.AlQuranNotificationManager
import com.alquranplusai.android.ui.viewmodels.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for Android-specific dependencies
 */
val androidAppModule = module {
    // Core ViewModels
    viewModel { HomeViewModel(get(), get()) }
    viewModel { SurahListViewModel(get()) }
    viewModel { ReadingViewModel(get(), get(), get(), get()) }
    viewModel { SurahDetailViewModel(get()) }
    viewModel { AyahDetailViewModel(get(), get(), get()) }
    viewModel { TafsirViewModel(get()) }
    
    // Audio ViewModels
    viewModel { AudioPlayerViewModel(get(), get(), get(), get()) }
    viewModel { ReciterListViewModel(get()) }
    viewModel { PlaylistViewModel(get()) }
    
    // Reading Navigation ViewModels
    viewModel { JuzListViewModel(get()) }
    viewModel { JuzViewModel(get()) }
    viewModel { PageViewModel(get()) }
    viewModel { ManzilViewModel(get()) }
    
    // Search ViewModels
    viewModel { SearchViewModel(get(), get()) }
    viewModel { VoiceSearchViewModel(get(), get()) }
    
    // Bookmark ViewModels
    viewModel { BookmarkViewModel(get()) }
    viewModel { BookmarksViewModel(get()) }
    viewModel { FoldersViewModel(get()) }
    
    // Quiz ViewModels
    viewModel { QuizViewModel(get()) }
    viewModel { QuizListViewModel(get()) }
    viewModel { QuizPlayViewModel(get()) }
    viewModel { QuizResultsViewModel(get()) }
    viewModel { DailyChallengeViewModel(get()) }
    
    // Analytics ViewModels
    viewModel { AnalyticsViewModel(get()) }
    viewModel { StreakViewModel(get()) }
    viewModel { GoalsViewModel(get()) }
    viewModel { AchievementsViewModel(get()) }
    
    // Download ViewModel
    viewModel { DownloadViewModel(get()) }
    
    // Audio ViewModel
    viewModel { AudioViewModel(get()) }
    
    // Translation ViewModel  
    viewModel { TranslationViewModel(get()) }
    
    // Translation Pack ViewModel
    viewModel { TranslationPackViewModel(get()) }
    
    // Profile & Auth ViewModels
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { AuthViewModel(get()) }
    
    // Settings ViewModels
    viewModel { SettingsViewModel(get()) }
    viewModel { ReadingPreferencesViewModel(get()) }
    viewModel { AudioSettingsViewModel(get()) }
    viewModel { NotificationSettingsViewModel(androidContext(), get()) }
    viewModel { PrivacySettingsViewModel(get()) }
    viewModel { BackupSettingsViewModel(get()) }
    viewModel { LanguageSettingsViewModel(get()) }
    viewModel { ThemeSettingsViewModel(get()) }
    viewModel { DisplaySettingsViewModel(get()) }
    
    // Onboarding & Splash ViewModels
    viewModel { SplashViewModel(androidContext()) }
    viewModel { OnboardingViewModel(androidContext()) }
    
    // Android-specific managers
    single { AlQuranNotificationManager(androidContext()) }
    single { com.alquranplusai.android.integration.DownloadNotificationIntegration(androidContext(), get(), get()) }
}
