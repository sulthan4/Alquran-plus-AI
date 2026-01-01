package com.alquranplusai.android.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import com.alquranplusai.android.R

/**
 * Arabic font families for Quranic text
 * Using Google Fonts for easy integration
 */

// Google Fonts provider
private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// Amiri - Traditional Arabic font
private val amiriFont = GoogleFont("Amiri")
val AmiriFontFamily = FontFamily(
    Font(googleFont = amiriFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = amiriFont, fontProvider = provider, weight = FontWeight.Bold)
)

// Scheherazade - Quranic script
private val scheherazadeFont = GoogleFont("Scheherazade New")
val ScheherazadeFontFamily = FontFamily(
    Font(googleFont = scheherazadeFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = scheherazadeFont, fontProvider = provider, weight = FontWeight.Bold)
)

// Noto Naskh Arabic - Modern readable Arabic
private val notoNaskhFont = GoogleFont("Noto Naskh Arabic")
val NotoNaskhArabicFontFamily = FontFamily(
    Font(googleFont = notoNaskhFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = notoNaskhFont, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = notoNaskhFont, fontProvider = provider, weight = FontWeight.Bold)
)

// Default Arabic font for Quran text
val DefaultArabicFont = ScheherazadeFontFamily
