#!/bin/bash

# This script creates minimal stub implementations for all problematic Android utility classes

cd "$(dirname "$0")"

# Fix VibratorHelper
cat > androidApp/src/main/kotlin/com/alquranplusai/android/utils/VibratorHelper.kt << 'EOF'
package com.alquranplusai.android.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class VibratorHelper(private val context: Context) {
    
    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
    
    fun hasVibrator(): Boolean = vibrator.hasVibrator()
    
    fun vibrate(duration: Long = 50) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }
    
    fun vibratePattern(pattern: LongArray, repeat: Int = -1) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, repeat))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, repeat)
        }
    }
    
    fun vibrateClick() = vibrate(10)
    fun vibrateDoubleClick() = vibratePattern(longArrayOf(0, 50, 50, 50))
    fun vibrateHeavyClick() = vibrate(100)
    fun vibrateTick() = vibrate(5)
    
    fun cancel() = vibrator.cancel()
}
EOF

# Fix BiometricHelper
cat > androidApp/src/main/kotlin/com/alquranplusai/android/utils/BiometricHelper.kt << 'EOF'
package com.alquranplusai.android.utils

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricHelper(private val context: Context) {
    
    private val biometricManager = BiometricManager.from(context)
    
    fun canAuthenticate(): Boolean {
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }
    
    fun authenticate(
        activity: FragmentActivity,
        title: String,
        subtitle: String = "",
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(context)
        
        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }
                
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errString.toString())
                }
                
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onError("Authentication failed")
                }
            })
        
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText("Cancel")
            .build()
        
        biometricPrompt.authenticate(promptInfo)
    }
}
EOF

# Fix ThemeManager
cat > androidApp/src/main/kotlin/com/alquranplusai/android/utils/ThemeManager.kt << 'EOF'
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
EOF

# Fix PermissionManager
cat > androidApp/src/main/kotlin/com/alquranplusai/android/utils/PermissionManager.kt << 'EOF'
package com.alquranplusai.android.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionManager(private val context: Context) {
    
    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
    
    fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.all { hasPermission(it) }
    }
    
    fun getDeniedPermissions(permissions: Array<String>): List<String> {
        return permissions.filter { !hasPermission(it) }
    }
}
EOF

# Fix LanguageManager
cat > androidApp/src/main/kotlin/com/alquranplusai/android/utils/LanguageManager.kt << 'EOF'
package com.alquranplusai.android.utils

import android.content.Context
import java.util.Locale

class LanguageManager(private val context: Context) {
    
    private val prefs = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
    
    fun getCurrentLanguage(): String {
        return prefs.getString("language", "en") ?: "en"
    }
    
    fun setLanguage(languageCode: String) {
        prefs.edit().putString("language", languageCode).apply()
    }
    
    fun getAvailableLanguages(): List<Pair<String, String>> {
        return listOf(
            "en" to "English",
            "ar" to "العربية",
            "ur" to "اردو",
            "ta" to "தமிழ்",
            "tr" to "Türkçe",
            "id" to "Bahasa Indonesia"
        )
    }
}
EOF

# Fix AnalyticsTracker
cat > androidApp/src/main/kotlin/com/alquranplusai/android/utils/AnalyticsTracker.kt << 'EOF'
package com.alquranplusai.android.utils

import android.content.Context

class AnalyticsTracker(private val context: Context) {
    
    fun trackEvent(eventName: String, params: Map<String, Any> = emptyMap()) {
        // TODO: Implement analytics tracking
    }
    
    fun trackScreen(screenName: String) {
        trackEvent("screen_view", mapOf("screen_name" to screenName))
    }
    
    fun setUserId(userId: String) {
        // TODO: Set user ID for analytics
    }
    
    fun setUserProperty(key: String, value: String) {
        // TODO: Set user property
    }
}
EOF

echo "All utility classes fixed!"
EOF

chmod +x fix_utils.sh && ./fix_utils.sh
