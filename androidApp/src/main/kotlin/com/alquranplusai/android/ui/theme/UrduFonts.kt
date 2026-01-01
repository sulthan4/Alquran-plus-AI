package com.alquranplusai.android.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import com.alquranplusai.android.R

/**
 * Urdu font families
 */

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// Noto Nastaliq Urdu - Traditional Urdu script
private val notoNastaliqFont = GoogleFont("Noto Nastaliq Urdu")
val NotoNastaliqUrduFontFamily = FontFamily(
    Font(googleFont = notoNastaliqFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = notoNastaliqFont, fontProvider = provider, weight = FontWeight.Bold)
)

val DefaultUrduFont = NotoNastaliqUrduFontFamily
