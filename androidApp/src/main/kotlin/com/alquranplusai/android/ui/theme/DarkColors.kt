package com.alquranplusai.android.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Dark color scheme based on locked design specification
 * Primary: Light Teal (lighter variant for dark mode)
 * Secondary: Light Purple (lighter variant for dark mode)
 * Tertiary: Light Gold (lighter variant for dark mode)
 */
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4DD0E1),  // Light teal for dark mode
    onPrimary = Color(0xFF003640),  // Dark teal
    primaryContainer = Color(0xFF004D5B),  // Medium teal
    onPrimaryContainer = Color(0xFF97F0FF),  // Very light teal
    
    secondary = Color(0xFFB39DDB),  // Light purple for dark mode
    onSecondary = Color(0xFF2E1A47),  // Dark purple
    secondaryContainer = Color(0xFF45296F),  // Medium purple
    onSecondaryContainer = Color(0xFFE8DDFF),  // Very light purple
    
    tertiary = Color(0xFFFFD54F),  // Light gold for dark mode
    onTertiary = Color(0xFF3E2D00),  // Dark brown
    tertiaryContainer = Color(0xFF5A4300),  // Medium brown
    onTertiaryContainer = Color(0xFFFFE082),  // Very light gold
    
    error = Color(0xFFFFB4AB),  // Light red for dark mode
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    
    background = DarkBackground,  // #121212
    onBackground = DarkOnBackground,  // #FFFFFF
    
    surface = DarkSurface,  // #1E1E1E
    onSurface = DarkOnSurface,  // #FFFFFF
    surfaceVariant = Color(0xFF42474E),
    onSurfaceVariant = DarkTextSecondary,  // #B0B0B0
    
    outline = Color(0xFF8C9199),
    outlineVariant = Color(0xFF42474E),
    scrim = Color(0xFF000000),
    
    inverseSurface = Color(0xFFE2E2E6),
    inverseOnSurface = Color(0xFF2F3033),
    inversePrimary = DeepTeal,  // Original deep teal for light backgrounds
)

