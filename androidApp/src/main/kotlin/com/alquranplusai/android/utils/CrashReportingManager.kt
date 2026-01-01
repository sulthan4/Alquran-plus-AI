package com.alquranplusai.android.utils

import android.content.Context
import android.util.Log

/**
 * Crash Reporting Manager for tracking and reporting crashes
 */
class CrashReportingManager(private val context: Context) {
    
    private var isEnabled = true
    
    /**
     * Initialize crash reporting
     */
    fun initialize() {
        // Set up uncaught exception handler
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            logCrash(throwable, thread)
            
            // Call original handler
            Thread.getDefaultUncaughtExceptionHandler()?.uncaughtException(thread, throwable)
        }
    }
    
    /**
     * Log a crash
     */
    fun logCrash(throwable: Throwable, thread: Thread? = null) {
        if (!isEnabled) return
        
        val crashInfo = buildString {
            appendLine("=== CRASH REPORT ===")
            appendLine("Time: ${System.currentTimeMillis()}")
            appendLine("Thread: ${thread?.name ?: "Unknown"}")
            appendLine("Exception: ${throwable.javaClass.simpleName}")
            appendLine("Message: ${throwable.message}")
            appendLine("\nStack Trace:")
            appendLine(throwable.stackTraceToString())
            
            throwable.cause?.let { cause ->
                appendLine("\nCaused by:")
                appendLine(cause.stackTraceToString())
            }
        }
        
        Log.e("CrashReport", crashInfo)
        
        // TODO: Send to crash reporting service (Firebase Crashlytics, Sentry, etc.)
        saveCrashLocally(crashInfo)
    }
    
    /**
     * Log a non-fatal exception
     */
    fun logException(throwable: Throwable, context: String? = null) {
        if (!isEnabled) return
        
        val exceptionInfo = buildString {
            appendLine("=== NON-FATAL EXCEPTION ===")
            appendLine("Time: ${System.currentTimeMillis()}")
            context?.let { appendLine("Context: $it") }
            appendLine("Exception: ${throwable.javaClass.simpleName}")
            appendLine("Message: ${throwable.message}")
            appendLine("\nStack Trace:")
            appendLine(throwable.stackTraceToString())
        }
        
        Log.w("Exception", exceptionInfo)
        
        // TODO: Send to crash reporting service
    }
    
    /**
     * Log a custom error
     */
    fun logError(message: String, details: Map<String, Any>? = null) {
        if (!isEnabled) return
        
        val errorInfo = buildString {
            appendLine("=== ERROR ===")
            appendLine("Time: ${System.currentTimeMillis()}")
            appendLine("Message: $message")
            details?.forEach { (key, value) ->
                appendLine("$key: $value")
            }
        }
        
        Log.e("Error", errorInfo)
    }
    
    /**
     * Save crash report locally
     */
    private fun saveCrashLocally(crashInfo: String) {
        try {
            val crashFile = context.getFileStreamPath("crash_${System.currentTimeMillis()}.txt")
            crashFile.writeText(crashInfo)
        } catch (e: Exception) {
            Log.e("CrashReport", "Failed to save crash report", e)
        }
    }
    
    /**
     * Enable/disable crash reporting
     */
    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
    }
    
    /**
     * Check if crash reporting is enabled
     */
    fun isEnabled(): Boolean = isEnabled
}
