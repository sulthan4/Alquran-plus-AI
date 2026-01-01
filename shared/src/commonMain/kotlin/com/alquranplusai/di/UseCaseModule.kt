package com.alquranplusai.di

import com.alquranplusai.domain.usecases.*
import org.koin.dsl.module

val useCaseModule = module {
    // Quran Use Cases
    factory { GetSurahWithAyahsUseCase(get()) }
    factory { GetAyahWithTranslationUseCase(get()) }
    factory { SearchQuranUseCase(get()) }
    factory { GetReadingProgressUseCase(get()) }
    
    // Bookmark Use Cases
    factory { AddBookmarkUseCase(get()) }
    factory { RemoveBookmarkUseCase(get()) }
    factory { OrganizeBookmarksUseCase(get()) }
    factory { CreateFolderUseCase(get()) }
    factory { GetBookmarksWithFoldersUseCase(get()) }
    
    // Analytics Use Cases
    factory { TrackReadingSessionUseCase(get()) }
    factory { GetReadingStatisticsUseCase(get()) }
    factory { UpdateStreakUseCase(get()) }
    factory { GetAchievementsUseCase(get()) }
    
    // Quiz Use Cases
    factory { StartQuizUseCase(get()) }
    factory { SubmitQuizAnswersUseCase(get()) }
    factory { GetQuizHistoryUseCase(get()) }
}
