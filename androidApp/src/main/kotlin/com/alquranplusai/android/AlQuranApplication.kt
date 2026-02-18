package com.alquranplusai.android

import android.app.Application
import android.util.Log
import com.alquranplusai.android.di.DiModules
import com.alquranplusai.android.utils.DatabaseSeeder
import com.alquranplusai.data.database.AlQuranDatabaseWrapper
import com.alquranplusai.di.SharedKoinModules
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AlQuranApplication : Application() {
    
    private val database: AlQuranDatabaseWrapper by inject()
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AlQuranApplication)
            modules(
                // All shared modules
                SharedKoinModules.getSharedModules() +
                // Android-specific modules
                DiModules.getAllModules()
            )
        }
        
        // Create notification channels
        // NotificationHelper channels created in init
        
        // Seed database if needed
        seedDatabaseIfNeeded()
        
        Log.d(TAG, "AlQuranApplication initialized successfully")
    }
    
    private fun seedDatabaseIfNeeded() {
        GlobalScope.launch {
            try {
                if (!DatabaseSeeder.isDatabaseSeeded(database)) {
                    Log.d(TAG, "Seeding database with initial Quran data...")
                    DatabaseSeeder.seedDatabase(applicationContext, database)
                    Log.d(TAG, "Database seeding completed successfully")
                } else {
                    Log.d(TAG, "Database already seeded, skipping...")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error seeding database", e)
            }
        }
    }
    
    companion object {
        private const val TAG = "AlQuranApp"
    }
}
