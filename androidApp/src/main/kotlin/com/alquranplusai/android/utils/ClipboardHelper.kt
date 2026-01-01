package com.alquranplusai.android.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

class ClipboardHelper(private val context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: ClipboardHelper? = null
        
        fun getInstance(context: Context): ClipboardHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ClipboardHelper(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    fun copyText(text: String, label: String = "Copied Text", showToast: Boolean = true) {
        val clip = ClipData.newPlainText(label, text)
        clipboardManager.setPrimaryClip(clip)
        
        if (showToast) {
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    fun copyAyah(surahNumber: Int, ayahNumber: Int, arabicText: String, translation: String? = null) {
        val text = buildString {
            append("Surah $surahNumber, Ayah $ayahNumber\n\n")
            append(arabicText)
            if (translation != null) {
                append("\n\n")
                append(translation)
            }
        }
        
        copyText(text, "Ayah", showToast = true)
    }

    fun pasteText(): String? {
        if (!clipboardManager.hasPrimaryClip()) {
            return null
        }
        
        val clip = clipboardManager.primaryClip
        if (clip != null && clip.itemCount > 0) {
            val item = clip.getItemAt(0)
            return item.text?.toString()
        }
        
        return null
    }

    fun hasClipboardData(): Boolean {
        return clipboardManager.hasPrimaryClip()
    }

    fun clearClipboard() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            clipboardManager.clearPrimaryClip()
        } else {
            val clip = ClipData.newPlainText("", "")
            clipboardManager.setPrimaryClip(clip)
        }
    }

    fun addClipboardListener(listener: ClipboardManager.OnPrimaryClipChangedListener) {
        clipboardManager.addPrimaryClipChangedListener(listener)
    }

    fun removeClipboardListener(listener: ClipboardManager.OnPrimaryClipChangedListener) {
        clipboardManager.removePrimaryClipChangedListener(listener)
    }
}
