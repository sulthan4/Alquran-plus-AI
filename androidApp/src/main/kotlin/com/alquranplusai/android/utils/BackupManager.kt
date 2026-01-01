package com.alquranplusai.android.utils

import android.content.Context
import android.net.Uri
import com.alquranplusai.android.services.BackupService
import com.alquranplusai.android.services.RestoreService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class BackupManager(private val context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: BackupManager? = null
        
        fun getInstance(context: Context): BackupManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: BackupManager(context.applicationContext).also { INSTANCE = it }
            }
        }
        
        private const val PREFS_NAME = "backup_manager_prefs"
        private const val KEY_AUTO_BACKUP_ENABLED = "auto_backup_enabled"
        private const val KEY_BACKUP_FREQUENCY_DAYS = "backup_frequency_days"
        private const val KEY_LAST_BACKUP_TIME = "last_backup_time"
    }

    data class BackupInfo(
        val file: File,
        val timestamp: Long,
        val size: Long,
        val type: String
    )

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    private val _isAutoBackupEnabled = MutableStateFlow(isAutoBackupEnabled())
    val isAutoBackupEnabled: StateFlow<Boolean> = _isAutoBackupEnabled
    
    private val _availableBackups = MutableStateFlow<List<BackupInfo>>(emptyList())
    val availableBackups: StateFlow<List<BackupInfo>> = _availableBackups

    fun createBackup(
        destinationUri: Uri? = null,
        includeSettings: Boolean = true,
        includeBookmarks: Boolean = true,
        includeProgress: Boolean = true
    ) {
        // Start backup service
        val intent = android.content.Intent(context, BackupService::class.java)
        context.startService(intent)
    }

    fun restoreBackup(backupUri: Uri) {
        // Start restore service
        val intent = android.content.Intent(context, RestoreService::class.java)
        context.startService(intent)
    }

    fun setAutoBackupEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_BACKUP_ENABLED, enabled).apply()
        _isAutoBackupEnabled.value = enabled
    }

    fun isAutoBackupEnabled(): Boolean {
        return prefs.getBoolean(KEY_AUTO_BACKUP_ENABLED, false)
    }

    fun setBackupFrequency(days: Int) {
        prefs.edit().putInt(KEY_BACKUP_FREQUENCY_DAYS, days).apply()
    }

    fun getBackupFrequency(): Int {
        return prefs.getInt(KEY_BACKUP_FREQUENCY_DAYS, 7)
    }

    fun getLastBackupTime(): Long {
        return prefs.getLong(KEY_LAST_BACKUP_TIME, 0)
    }

    fun shouldCreateAutoBackup(): Boolean {
        if (!isAutoBackupEnabled()) return false
        
        val lastBackupTime = getLastBackupTime()
        val frequencyMillis = getBackupFrequency() * 24 * 60 * 60 * 1000L
        val timeSinceLastBackup = System.currentTimeMillis() - lastBackupTime
        
        return timeSinceLastBackup >= frequencyMillis
    }

    fun loadAvailableBackups() {
        val backupDir = File(context.getExternalFilesDir(null), "backups")
        if (!backupDir.exists()) {
            _availableBackups.value = emptyList()
            return
        }
        
        val backups = backupDir.listFiles { file ->
            file.isFile && file.extension == "zip"
        }?.map { file ->
            BackupInfo(
                file = file,
                timestamp = file.lastModified(),
                size = file.length(),
                type = if (file.name.startsWith("auto_")) "automatic" else "manual"
            )
        }?.sortedByDescending { it.timestamp } ?: emptyList()
        
        _availableBackups.value = backups
    }

    fun deleteBackup(backupInfo: BackupInfo): Boolean {
        return backupInfo.file.delete().also {
            if (it) loadAvailableBackups()
        }
    }

    fun getBackupSize(backupInfo: BackupInfo): String {
        val sizeInMB = backupInfo.size / (1024.0 * 1024.0)
        return String.format("%.2f MB", sizeInMB)
    }
}
