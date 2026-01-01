package com.alquranplusai.android.di

import androidx.navigation.NavController
import org.koin.dsl.module

val navigationModule = module {
    
    // Navigation will be provided at runtime from the composable
    // This module can be used for navigation-related utilities
    
    // Example: Navigation helpers, deep link handlers, etc.
    // These would be singletons that help with navigation logic
    
    // For now, this is a placeholder as Navigation in Compose
    // is typically handled via NavHost and doesn't need DI
}
