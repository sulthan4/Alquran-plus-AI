package com.alquranplusai.android.di

import org.koin.core.module.Module

/**
 * All DI modules for the Android app
 */
object DiModules {
    
    /**
     * Get all Android app modules
     */
    fun getAllModules(): List<Module> = listOf(
        androidAppModule,
        viewModelModule,
        serviceModule,
        utilsModule,
        navigationModule,
        workerModule
    )
}
