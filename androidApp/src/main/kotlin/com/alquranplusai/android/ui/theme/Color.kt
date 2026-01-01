package com.alquranplusai.android.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ============================================
// PRIMARY BRAND COLORS (LOCKED)
// ============================================

/** Deep Teal - Primary brand color */
val DeepTeal = Color(0xFF006064)

/** Purple - Secondary accent */
val Purple = Color(0xFF5E35B1)

/** Gold - Highlights and achievements */
val Gold = Color(0xFFFFB300)

// ============================================
// PRIMARY COLORS
// ============================================

val Primary = DeepTeal
val PrimaryVariant = Color(0xFF00838F)  // Lighter teal for gradients
val DarkPrimary = Color(0xFF004D51)  // Darker teal for dark mode
val OnPrimary = Color(0xFFFFFFFF)

// ============================================
// SECONDARY COLORS
// ============================================

val Secondary = Purple
val SecondaryVariant = Color(0xFF7E57C2)  // Lighter purple
val OnSecondary = Color(0xFFFFFFFF)

// ============================================
// ACCENT COLORS
// ============================================

val Accent = Gold
val AccentVariant = Color(0xFFFFC107)  // Lighter gold
val OnAccent = Color(0xFF000000)

// ============================================
// NEUTRAL COLORS
// ============================================

// Background
val Background = Color(0xFFFAFAFA)
val DarkBackground = Color(0xFF121212)
val OnBackground = Color(0xFF212121)
val DarkOnBackground = Color(0xFFFFFFFF)

// Surface
val Surface = Color(0xFFFFFFFF)
val DarkSurface = Color(0xFF1E1E1E)
val OnSurface = Color(0xFF212121)
val DarkOnSurface = Color(0xFFFFFFFF)

// Text
val TextPrimary = Color(0xFF212121)
val TextSecondary = Color(0xFF757575)
val DarkTextPrimary = Color(0xFFFFFFFF)
val DarkTextSecondary = Color(0xFFB0B0B0)

// ============================================
// SEMANTIC COLORS
// ============================================

val Success = Color(0xFF2E7D32)  // Green
val Warning = Color(0xFFF57C00)  // Orange
val Error = Color(0xFFC62828)  // Red
val Info = Color(0xFF0277BD)  // Blue
val OnError = Color(0xFFFFFFFF)

// ============================================
// FEATURE-SPECIFIC COLORS
// ============================================

val BrowseQuran = Color(0xFF00838F)  // Teal
val AudioPlayer = Color(0xFF5E35B1)  // Purple
val Bookmarks = Color(0xFFFF6F00)  // Orange
val DailyQuiz = Color(0xFF2E7D32)  // Green
val Search = Color(0xFF0277BD)  // Blue
val Analytics = Color(0xFF3949AB)  // Indigo

// ============================================
// GRADIENT DEFINITIONS
// ============================================

/** Header gradient: Teal → Purple */
val HeaderGradient = Brush.horizontalGradient(
    colors = listOf(DeepTeal, Purple)
)

/** Continue Reading Card gradient: Light Teal → Deep Teal */
val ContinueReadingGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF00838F), DeepTeal)
)

/** Browse Quran gradient */
val BrowseQuranGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF00ACC1), Color(0xFF00838F))
)

/** Audio Player gradient */
val AudioPlayerGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF7E57C2), Color(0xFF5E35B1))
)

/** Bookmarks gradient */
val BookmarksGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFFF8F00), Color(0xFFFF6F00))
)

/** Daily Quiz gradient */
val DailyQuizGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF43A047), Color(0xFF2E7D32))
)

/** Search gradient */
val SearchGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF039BE5), Color(0xFF0277BD))
)

/** Analytics gradient */
val AnalyticsGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF5C6BC0), Color(0xFF3949AB))
)

// ============================================
// LEGACY COMPATIBILITY (if needed)
// ============================================

@Deprecated("Use DeepTeal instead", ReplaceWith("DeepTeal"))
val Teal = DeepTeal

