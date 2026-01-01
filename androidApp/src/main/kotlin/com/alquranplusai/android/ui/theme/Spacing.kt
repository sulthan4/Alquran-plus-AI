package com.alquranplusai.android.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spacing scale for consistent spacing throughout the app
 * Based on the locked design specification
 */
object Spacing {
    /** No spacing */
    val none: Dp = 0.dp
    
    /** Extra small - 4dp */
    val xs: Dp = 4.dp
    
    /** Small - 8dp */
    val sm: Dp = 8.dp
    
    /** Medium - 16dp */
    val md: Dp = 16.dp
    
    /** Large - 24dp */
    val lg: Dp = 24.dp
    
    /** Extra large - 32dp */
    val xl: Dp = 32.dp
    
    // Legacy aliases for compatibility
    val extraSmall: Dp = xs
    val small: Dp = sm
    val medium: Dp = md
    val large: Dp = lg
    val extraLarge: Dp = xl
    val huge: Dp = 48.dp
    val massive: Dp = 64.dp
    
    // Specific use cases
    val cardPadding: Dp = md
    val screenPadding: Dp = md
    val itemSpacing: Dp = sm
    val sectionSpacing: Dp = lg
}

