package com.alquranplusai.android.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class SyncService : Service() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    
    companion object {
        const val ACTION_SYNC_NOW = "com.alquranplusai.android.services.ACTION_SYNC_NOW"
        const val ACTION_SYNC_BOOKMARKS = "com.alquranplusai.android.services.ACTION_SYNC_BOOKMARKS"
        const val ACTION_SYNC_PROGRESS = "com.alquranplusai.android.services.ACTION_SYNC_PROGRESS"
        const val ACTION_SYNC_SETTINGS = "com.alquranplusai.android.services.ACTION_SYNC_SETTINGS"
        
        fun syncNow(context: Context) {
            val intent = Intent(context, SyncService::class.java).apply {
                action = ACTION_SYNC_NOW
            }
            context.startService(intent)
        }
        
        fun syncBookmarks(context: Context) {
            val intent = Intent(context, SyncService::class.java).apply {
                action = ACTION_SYNC_BOOKMARKS
            }
            context.startService(intent)
        }
        
        fun syncProgress(context: Context) {
            val intent = Intent(context, SyncService::class.java).apply {
                action = ACTION_SYNC_PROGRESS
            }
            context.startService(intent)
        }
        
        fun syncSettings(context: Context) {
            val intent = Intent(context, SyncService::class.java).apply {
                action = ACTION_SYNC_SETTINGS
            }
            context.startService(intent)
        }
    }

    data class SyncData(
        val timestamp: Long = System.currentTimeMillis(),
        val deviceId: String,
        val dataType: String,
        val data: String
    )

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_SYNC_NOW -> performFullSync()
            ACTION_SYNC_BOOKMARKS -> syncBookmarksData()
            ACTION_SYNC_PROGRESS -> syncProgressData()
            ACTION_SYNC_SETTINGS -> syncSettingsData()
        }
        
        return START_NOT_STICKY
    }

    private fun performFullSync() {
        scope.launch {
            try {
                syncBookmarksData()
                delay(500)
                syncProgressData()
                delay(500)
                syncSettingsData()
                
                notifySyncComplete("Full sync completed successfully")
            } catch (e: Exception) {
                notifySyncFailed("Full sync failed: ${e.message}")
            } finally {
                stopSelf()
            }
        }
    }

    private fun syncBookmarksData() {
        scope.launch {
            try {
                val bookmarksData = loadLocalBookmarks()
                uploadToCloud("bookmarks", bookmarksData)
                
                val cloudBookmarks = downloadFromCloud("bookmarks")
                mergeBookmarks(cloudBookmarks)
                
                saveLastSyncTime("bookmarks")
            } catch (e: Exception) {
                notifySyncFailed("Bookmarks sync failed: ${e.message}")
            }
        }
    }

    private fun syncProgressData() {
        scope.launch {
            try {
                val progressData = loadLocalProgress()
                uploadToCloud("progress", progressData)
                
                val cloudProgress = downloadFromCloud("progress")
                mergeProgress(cloudProgress)
                
                saveLastSyncTime("progress")
            } catch (e: Exception) {
                notifySyncFailed("Progress sync failed: ${e.message}")
            }
        }
    }

    private fun syncSettingsData() {
        scope.launch {
            try {
                val settingsData = loadLocalSettings()
                uploadToCloud("settings", settingsData)
                
                val cloudSettings = downloadFromCloud("settings")
                mergeSettings(cloudSettings)
                
                saveLastSyncTime("settings")
            } catch (e: Exception) {
                notifySyncFailed("Settings sync failed: ${e.message}")
            }
        }
    }

    private fun loadLocalBookmarks(): String {
        val prefsFile = File(applicationInfo.dataDir, "shared_prefs/bookmarks.xml")
        return if (prefsFile.exists()) prefsFile.readText() else "{}"
    }

    private fun loadLocalProgress(): String {
        val prefsFile = File(applicationInfo.dataDir, "shared_prefs/progress.xml")
        return if (prefsFile.exists()) prefsFile.readText() else "{}"
    }

    private fun loadLocalSettings(): String {
        val prefsFile = File(applicationInfo.dataDir, "shared_prefs/settings.xml")
        return if (prefsFile.exists()) prefsFile.readText() else "{}"
    }

    private suspend fun uploadToCloud(dataType: String, data: String) {
        val syncData = SyncData(
            deviceId = getDeviceIdForSync(),
            dataType = dataType,
            data = data
        )
        
        // Placeholder for Cloud API upload
        delay(1000)
    }

    private suspend fun downloadFromCloud(dataType: String): String {
        // Placeholder for Cloud API download
        delay(1000)
        return "{}"
    }

    private fun mergeBookmarks(cloudData: String) {
        // Placeholder for bookmark merge logic
    }

    private fun mergeProgress(cloudData: String) {
        // Placeholder for progress merge logic
    }

    private fun mergeSettings(cloudData: String) {
        // Placeholder for settings merge logic
    }

    private fun getDeviceIdForSync(): String {
        val prefs = getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)
        var deviceId = prefs.getString("device_id", null)
        
        if (deviceId == null) {
            deviceId = java.util.UUID.randomUUID().toString()
            prefs.edit().putString("device_id", deviceId).apply()
        }
        
        return deviceId
    }

    private fun saveLastSyncTime(dataType: String) {
        val prefs = getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)
        prefs.edit().putLong("last_sync_$dataType", System.currentTimeMillis()).apply()
    }

    private fun notifySyncComplete(message: String) {
        AlQuranNotificationManager(this).showReminderNotification(
            "Sync Complete",
            message
        )
    }

    private fun notifySyncFailed(message: String) {
        AlQuranNotificationManager(this).showReminderNotification(
            "Sync Failed",
            message
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
