package com.alquranplusai.di

import org.koin.core.module.Module

/**
 * Main DI module aggregator for shared module
 * Combines all Koin modules for easy initialization
 */
object SharedKoinModules {
    
    /**
     * Get all shared modules
     */
    fun getSharedModules(): List<Module> = listOf(
        platformModule,
        appModule,
        databaseModule,
        repositoryModule,
        useCaseModule,
        networkModule,
        aiModule,
        audioModule,
        preferencesModule
    )
}
