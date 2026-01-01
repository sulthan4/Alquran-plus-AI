package com.alquranplusai.android.di

import android.content.Context
import androidx.work.WorkManager
import com.alquranplusai.android.workers.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val workerModule = module {
    
    // WorkManager instance
    single { WorkManager.getInstance(androidContext()) }
    
    // Workers are instantiated by WorkManager, not Koin
    // But we can provide factories for testing or manual instantiation
    
    // Worker factories (for testing purposes)
    factory { (context: Context, params: androidx.work.WorkerParameters) ->
        BackupWorker(context, params)
    }
    
    factory { (context: Context, params: androidx.work.WorkerParameters) ->
        AnalyticsWorker(context, params)
    }
    
    factory { (context: Context, params: androidx.work.WorkerParameters) ->
        CleanupWorker(context, params)
    }
    
    factory { (context: Context, params: androidx.work.WorkerParameters) ->
        ReminderWorker(context, params)
    }
    
    factory { (context: Context, params: androidx.work.WorkerParameters) ->
        DataSyncWorker(context, params)
    }
    
    factory { (context: Context, params: androidx.work.WorkerParameters) ->
        AudioDownloadWorker(context, params)
    }
    
    factory { (context: Context, params: androidx.work.WorkerParameters) ->
        QuranDownloadWorker(context, params)
    }
}
