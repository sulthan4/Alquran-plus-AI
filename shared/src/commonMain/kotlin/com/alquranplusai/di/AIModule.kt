package com.alquranplusai.di

import org.koin.core.module.Module
import org.koin.dsl.module
import com.alquranplusai.data.ai.*

/**
 * AI Module for dependency injection
 * Note: AI engines require platform-specific implementations and TFLite integration
 */
val aiModule = module {
    // AI engines
    single<SemanticSearchEngine> { DefaultSemanticSearchEngine(get(), get()) }
    
    // AI utilities
    single<EmbeddingsProcessor> { BasicEmbeddingsProcessor() }
    single { VectorDatabase(get()) }
}
