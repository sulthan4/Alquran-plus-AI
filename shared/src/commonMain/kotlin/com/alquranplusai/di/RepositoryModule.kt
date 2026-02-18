package com.alquranplusai.di

import com.alquranplusai.data.repositories.*
import com.alquranplusai.domain.repositories.*
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin module for repository dependencies
 */
val repositoryModule = module {
    // Quran Repository - Working with Surah and Ayah tables
    single<QuranRepository> { QuranRepositoryImpl(get(), get()) }
    
    // Translation Repository - Working with translation data
    single<TranslationRepository> { TranslationRepositoryImpl(get(), get()) }
    
    // Tafsir Repository - Working with tafsir (commentary) data
    single<TafsirRepository> { TafsirRepositoryImpl(get(), get(), get()) }
    
    // Download Repository - Managing downloads
    single<DownloadRepository> { DownloadRepositoryImpl(get(), get()) }
    
    // Audio Repository - with Quran.com API for word timings
    single<AudioRepository> { AudioRepositoryImpl(get(), get(), get()) }
    
    // Bookmark Repository - Stubbed (tables exist but not populated)
    single<BookmarkRepository> { BookmarkRepositoryImpl(get()) }
    
    // Quiz Repository - With question generation
    single<QuizRepository> { QuizRepositoryImpl(get(), get()) }
    
    // Search Repository - Hybrid search with API + AI
    single<SearchRepository> { SearchRepositoryImpl(get(), get(), getOrNull()) }
    
    // Analytics Repository - Stubbed (tables exist but not populated)
    single<AnalyticsRepository> { AnalyticsRepositoryImpl(get()) }
    
    // User Repository - Stubbed (backend not implemented)
    single<UserRepository> { UserRepositoryImpl(get()) }
    
    // Quran Data Loader - For loading complete Quran from API
    single { com.alquranplusai.data.services.QuranDataLoader(get(), get()) }
}
