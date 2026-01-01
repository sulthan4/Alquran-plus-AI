package com.alquranplusai.android.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ThemeManager(private val context: Context) {
    
    private val prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    
    private val _currentThemeMode = MutableStateFlow(getCurrentThemeMode())
    val currentThemeMode: StateFlow<ThemeMode> = _currentThemeMode
    
    enum class ThemeMode {
        LIGHT, DARK, SYSTEM
    }
    
    fun getCurrentThemeMode(): ThemeMode {
        val mode = prefs.getString("theme_mode", "SYSTEM") ?: "SYSTEM"
        return ThemeMode.valueOf(mode)
    }
    
    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString("theme_mode", mode.name).apply()
        _currentThemeMode.value = mode
        applyTheme(mode)
    }
    
    private fun applyTheme(mode: ThemeMode) {
        val nightMode = when (mode) {
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
    
    fun isDarkMode(): Boolean {
        return when (getCurrentThemeMode()) {
            ThemeMode.DARK -> true
            ThemeMode.LIGHT -> false
            ThemeMode.SYSTEM -> {
                val uiMode = context.resources.configuration.uiMode and 
                    android.content.res.Configuration.UI_MODE_NIGHT_MASK
                uiMode == android.content.res.Configuration.UI_MODE_NIGHT_YES
            }
        }
    }
}
