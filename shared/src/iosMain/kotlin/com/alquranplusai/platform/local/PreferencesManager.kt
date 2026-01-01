package com.alquranplusai.platform.local

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.Foundation.NSUserDefaults

/** iOS implementation of PreferencesManager using NSUserDefaults */
actual class PreferencesManager {

    private val settings: Settings = NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
    private val stringFlows = mutableMapOf<String, MutableStateFlow<String>>()
    private val booleanFlows = mutableMapOf<String, MutableStateFlow<Boolean>>()

    actual suspend fun putString(key: String, value: String) {
        settings.putString(key, value)
        stringFlows[key]?.value = value
    }

    actual suspend fun getString(key: String, defaultValue: String): String {
        return settings.getString(key, defaultValue)
    }

    actual suspend fun putInt(key: String, value: Int) {
        settings.putInt(key, value)
    }

    actual suspend fun getInt(key: String, defaultValue: Int): Int {
        return settings.getInt(key, defaultValue)
    }

    actual suspend fun putBoolean(key: String, value: Boolean) {
        settings.putBoolean(key, value)
        booleanFlows[key]?.value = value
    }

    actual suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return settings.getBoolean(key, defaultValue)
    }

    actual suspend fun putLong(key: String, value: Long) {
        settings.putLong(key, value)
    }

    actual suspend fun getLong(key: String, defaultValue: Long): Long {
        return settings.getLong(key, defaultValue)
    }

    actual suspend fun putFloat(key: String, value: Float) {
        settings.putFloat(key, value)
    }

    actual suspend fun getFloat(key: String, defaultValue: Float): Float {
        return settings.getFloat(key, defaultValue)
    }

    actual suspend fun remove(key: String) {
        settings.remove(key)
    }

    actual suspend fun clear() {
        settings.clear()
    }

    actual fun observeString(key: String, defaultValue: String): Flow<String> {
        return stringFlows.getOrPut(key) { MutableStateFlow(settings.getString(key, defaultValue)) }
    }

    actual fun observeBoolean(key: String, defaultValue: Boolean): Flow<Boolean> {
        return booleanFlows.getOrPut(key) {
            MutableStateFlow(settings.getBoolean(key, defaultValue))
        }
    }
}
