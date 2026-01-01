package com.alquranplusai.data.network

/** Network error handler */
object ErrorHandler {

    fun handleError(exception: Throwable): String {
        return when {
            exception.message?.contains("UnknownHost", ignoreCase = true) == true ->
                    "No internet connection"
            exception.message?.contains("timeout", ignoreCase = true) == true ->
                    "Connection timeout"
            exception.message?.contains("IOException", ignoreCase = true) == true ->
                    "Network error occurred"
            else -> exception.message ?: "Unknown error occurred"
        }
    }

    fun isNetworkError(exception: Throwable): Boolean {
        val message = exception.message ?: ""
        return message.contains("UnknownHost", ignoreCase = true) ||
                message.contains("timeout", ignoreCase = true) ||
                message.contains("IOException", ignoreCase = true) ||
                exception::class.simpleName?.contains("Network") == true
    }
}
