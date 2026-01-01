package com.alquranplusai.android.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object ShareUtils {
    fun shareText(context: Context, text: String, title: String = "Share") {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_TITLE, title)
        }
        context.startActivity(Intent.createChooser(intent, title))
    }
    
    fun shareVerse(context: Context, surahNumber: Int, ayahNumber: Int, text: String) {
        val shareText = "Quran $surahNumber:$ayahNumber\n\n$text"
        shareText(context, shareText, "Share Verse")
    }
}
