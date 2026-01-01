package com.alquranplusai.di

import com.alquranplusai.utils.Logger
import org.koin.dsl.module

/**
 * Core application module
 */
val appModule = module {
    // App configuration
    single {
        object {
            val appName = "Al-Quran Plus AI"
            val version = "1.0.0"
            val apiBaseUrl = "https://api.alquran.cloud/v1"
            val enableLogging = true
        }
    }
}
