package com.alquranplusai.android.utils

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import java.util.Locale

class LocaleHelper {

    companion object {
        fun setLocale(context: Context, languageCode: String, countryCode: String = ""): Context {
            val locale = if (countryCode.isNotEmpty()) {
                Locale(languageCode, countryCode)
            } else {
                Locale(languageCode)
            }
            
            Locale.setDefault(locale)
            
            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)
            config.setLocales(LocaleList(locale))
            
            return context.createConfigurationContext(config)
        }

        fun getLocale(context: Context): Locale {
            return context.resources.configuration.locales[0]
        }

        fun isRTL(context: Context): Boolean {
            val locale = getLocale(context)
            return locale.language in listOf("ar", "ur", "fa", "he", "iw", "yi")
        }

        fun getLayoutDirection(context: Context): Int {
            return if (isRTL(context)) {
                android.view.View.LAYOUT_DIRECTION_RTL
            } else {
                android.view.View.LAYOUT_DIRECTION_LTR
            }
        }

        fun formatNumber(number: Int, locale: Locale = Locale.getDefault()): String {
            return java.text.NumberFormat.getInstance(locale).format(number)
        }

        fun formatDecimal(number: Double, locale: Locale = Locale.getDefault()): String {
            return java.text.DecimalFormat.getInstance(locale).format(number)
        }

        fun getDisplayLanguage(languageCode: String, displayLocale: Locale = Locale.getDefault()): String {
            val locale = Locale(languageCode)
            return locale.getDisplayLanguage(displayLocale)
        }

        fun getDisplayCountry(countryCode: String, displayLocale: Locale = Locale.getDefault()): String {
            val locale = Locale("", countryCode)
            return locale.getDisplayCountry(displayLocale)
        }
    }
}
