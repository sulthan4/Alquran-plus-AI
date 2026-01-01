package com.alquranplusai.platform.local

import kotlinx.coroutines.flow.Flow

/**
 * Platform-specific preferences manager
 */
expect class PreferencesManager {
    suspend fun putString(key: String, value: String)
    suspend fun getString(key: String, defaultValue: String): String
    suspend fun putInt(key: String, value: Int)
    suspend fun getInt(key: String, defaultValue: Int): Int
    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean
    suspend fun putLong(key: String, value: Long)
    suspend fun getLong(key: String, defaultValue: Long): Long
    suspend fun putFloat(key: String, value: Float)
    suspend fun getFloat(key: String, defaultValue: Float): Float
    suspend fun remove(key: String)
    suspend fun clear()
    fun observeString(key: String, defaultValue: String): Flow<String>
    fun observeBoolean(key: String, defaultValue: Boolean): Flow<Boolean>
}
