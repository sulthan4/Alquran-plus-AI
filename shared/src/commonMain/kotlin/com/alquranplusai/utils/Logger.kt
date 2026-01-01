package com.alquranplusai.utils

import io.github.aakira.napier.Napier

/**
 * Centralized logging utility
 */
object Logger {
    private const val TAG = "AlQuranPlusAI"
    
    fun d(message: String, tag: String = TAG) {
        Napier.d(message, tag = tag)
    }
    
    fun i(message: String, tag: String = TAG) {
        Napier.i(message, tag = tag)
    }
    
    fun w(message: String, throwable: Throwable? = null, tag: String = TAG) {
        Napier.w(message, throwable, tag = tag)
    }
    
    fun e(message: String, throwable: Throwable? = null, tag: String = TAG) {
        Napier.e(message, throwable, tag = tag)
    }
    
    fun v(message: String, tag: String = TAG) {
        Napier.v(message, tag = tag)
    }
}

// Extension functions for easy logging
fun Any.logD(message: String) {
    Logger.d(message, this::class.simpleName ?: "Unknown")
}

fun Any.logI(message: String) {
    Logger.i(message, this::class.simpleName ?: "Unknown")
}

fun Any.logW(message: String, throwable: Throwable? = null) {
    Logger.w(message, throwable, this::class.simpleName ?: "Unknown")
}

fun Any.logE(message: String, throwable: Throwable? = null) {
    Logger.e(message, throwable, this::class.simpleName ?: "Unknown")
}
