package com.alquranplusai.android.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Elevation system for consistent card and surface elevations
 * Based on Material Design 3 elevation guidelines
 */
object Elevation {
    /** No elevation */
    val none: Dp = 0.dp
    
    /** Low elevation - for subtle cards */
    val low: Dp = 2.dp
    
    /** Medium elevation - for standard cards */
    val medium: Dp = 4.dp
    
    /** High elevation - for important cards and dialogs */
    val high: Dp = 8.dp
    
    /** Highest elevation - for modals and overlays */
    val highest: Dp = 16.dp
    
    // Specific use cases
    val card: Dp = low
    val button: Dp = medium
    val dialog: Dp = high
    val modal: Dp = highest
}
