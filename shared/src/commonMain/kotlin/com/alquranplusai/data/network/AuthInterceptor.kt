package com.alquranplusai.data.network

/**
 * Authentication interceptor for API requests
 */
class AuthInterceptor(
    private val tokenProvider: () -> String?
) {
    
    fun getAuthToken(): String? {
        return tokenProvider()
    }
    
    fun hasValidToken(): Boolean {
        return !getAuthToken().isNullOrEmpty()
    }
}
