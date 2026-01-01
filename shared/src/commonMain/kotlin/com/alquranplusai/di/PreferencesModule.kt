package com.alquranplusai.di

import com.alquranplusai.data.preferences.PreferencesManager
import com.alquranplusai.data.preferences.SettingsDataStore
import org.koin.dsl.module

/**
 * Koin module for preferences dependencies
 */
val preferencesModule = module {
    single { SettingsDataStore(get()) }
    single { PreferencesManager(get()) }
}
