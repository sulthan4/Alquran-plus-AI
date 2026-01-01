package com.alquranplusai.di

import com.alquranplusai.data.database.AlQuranDatabaseWrapper
import com.alquranplusai.data.database.RealAlQuranDatabaseWrapper
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin module for database dependencies
 * Note: DatabaseDriverFactory must be provided by platform-specific modules
 */
val databaseModule = module {
    // Database wrapper
    single<AlQuranDatabaseWrapper> { RealAlQuranDatabaseWrapper(get()) }
}
