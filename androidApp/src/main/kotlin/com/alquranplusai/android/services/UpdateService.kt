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

class UpdateService : Service() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    
    companion object {
        const val ACTION_CHECK_UPDATE = "com.alquranplusai.android.services.ACTION_CHECK_UPDATE"
        
        fun checkForUpdates(context: Context) {
            val intent = Intent(context, UpdateService::class.java).apply {
                action = ACTION_CHECK_UPDATE
            }
            context.startService(intent)
        }
    }

    data class UpdateInfo(
        val version: String,
        val versionCode: Int,
        val downloadUrl: String,
        val releaseNotes: String,
        val isMandatory: Boolean
    )

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_CHECK_UPDATE -> checkForUpdates()
        }
        
        return START_NOT_STICKY
    }

    private fun checkForUpdates() {
        scope.launch {
            try {
                val updateInfo = fetchUpdateInfo()
                
                if (updateInfo != null && isUpdateAvailable(updateInfo)) {
                    notifyUpdateAvailable(updateInfo)
                }
            } catch (e: Exception) {
                // Silently fail update check
            } finally {
                stopSelf()
            }
        }
    }

    private suspend fun fetchUpdateInfo(): UpdateInfo? {
        // Placeholder for Update API check
        delay(1000)
        return null
    }

    private fun isUpdateAvailable(updateInfo: UpdateInfo): Boolean {
        val currentVersionCode = try {
            packageManager.getPackageInfo(packageName, 0).versionCode
        } catch (e: Exception) {
            0
        }
        
        return updateInfo.versionCode > currentVersionCode
    }

    private fun notifyUpdateAvailable(updateInfo: UpdateInfo) {
        val message = if (updateInfo.isMandatory) {
            "A mandatory update is available"
        } else {
            "Version ${updateInfo.version} is now available"
        }
        
        NotificationService.showNotification(
            this,
            "Update Available",
            message,
            NotificationService.CHANNEL_ID_GENERAL
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
