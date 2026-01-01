package com.alquranplusai.android.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import com.alquranplusai.android.R

/**
 * Tamil font families
 */

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// Noto Sans Tamil - Modern Tamil font
private val notoSansTamilFont = GoogleFont("Noto Sans Tamil")
val NotoSansTamilFontFamily = FontFamily(
    Font(googleFont = notoSansTamilFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = notoSansTamilFont, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = notoSansTamilFont, fontProvider = provider, weight = FontWeight.Bold)
)

val DefaultTamilFont = NotoSansTamilFontFamily
