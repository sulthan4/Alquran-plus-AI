#!/bin/bash

# Fix AnalyticsWorker serialization imports
cat > androidApp/src/main/kotlin/com/alquranplusai/android/workers/AnalyticsWorker.kt << 'EOF'
package com.alquranplusai.android.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class AnalyticsWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    @Serializable
    data class AnalyticsEvent(
        val eventType: String,
        val timestamp: Long,
        val data: Map<String, String>
    )
    
    @Serializable
    data class AnalyticsReport(
        val events: List<AnalyticsEvent>,
        val deviceId: String
    )
    
    override suspend fun doWork(): Result {
        return try {
            val events = collectAnalyticsEvents()
            val report = AnalyticsReport(
                events = events,
                deviceId = getDeviceId()
            )
            
            val json = Json.encodeToString(report)
            // TODO: Send to analytics server
            
            Result.success(workDataOf("events_sent" to events.size))
        } catch (e: Exception) {
            Result.failure(workDataOf("error" to e.message))
        }
    }
    
    private fun collectAnalyticsEvents(): List<AnalyticsEvent> {
        // TODO: Collect events from local storage
        return emptyList()
    }
    
    private fun getDeviceId(): String {
        val prefs = applicationContext.getSharedPreferences("analytics", Context.MODE_PRIVATE)
        var deviceId = prefs.getString("device_id", null)
        if (deviceId == null) {
            deviceId = java.util.UUID.randomUUID().toString()
            prefs.edit().putString("device_id", deviceId).apply()
        }
        return deviceId
    }
}
EOF

echo "Fixed AnalyticsWorker.kt"
