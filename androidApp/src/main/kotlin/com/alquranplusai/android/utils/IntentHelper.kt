package com.alquranplusai.android.utils

/**
 * IntentHelper - Utility class for IntentHelper
 */
object IntentHelper {
    fun openBrowser(context: android.content.Context, url: String) {
        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
        context.startActivity(intent)
    }

    fun openEmail(context: android.content.Context, email: String, subject: String = "") {
        val intent = android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
            data = android.net.Uri.parse("mailto:$email")
            putExtra(android.content.Intent.EXTRA_SUBJECT, subject)
        }
        context.startActivity(intent)
    }
}
