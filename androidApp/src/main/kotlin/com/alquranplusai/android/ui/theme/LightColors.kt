package com.alquranplusai.android.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Light color scheme based on locked design specification
 * Primary: Deep Teal (#006064)
 * Secondary: Purple (#5E35B1)
 * Tertiary: Gold (#FFB300)
 */
val LightColorScheme = lightColorScheme(
    primary = DeepTeal,  // #006064
    onPrimary = Color.White,
    primaryContainer = Color(0xFF97F0FF),  // Light teal container
    onPrimaryContainer = Color(0xFF001F24),  // Dark teal
    
    secondary = Purple,  // #5E35B1
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8DDFF),  // Light purple container
    onSecondaryContainer = Color(0xFF1D0057),  // Dark purple
    
    tertiary = Gold,  // #FFB300
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFFFE082),  // Light gold container
    onTertiaryContainer = Color(0xFF2D1600),  // Dark brown
    
    error = Error,  // #C62828
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    
    background = Background,  // #FAFAFA
    onBackground = TextPrimary,  // #212121
    
    surface = Surface,  // #FFFFFF
    onSurface = OnSurface,  // #212121
    surfaceVariant = Color(0xFFDFE2EB),
    onSurfaceVariant = TextSecondary,  // #757575
    
    outline = Color(0xFF73777F),
    outlineVariant = Color(0xFFC3C7CF),
    scrim = Color(0xFF000000),
    
    inverseSurface = Color(0xFF2F3033),
    inverseOnSurface = Color(0xFFF1F0F4),
    inversePrimary = Color(0xFF4DD0E1),  // Light teal for dark backgrounds
)

