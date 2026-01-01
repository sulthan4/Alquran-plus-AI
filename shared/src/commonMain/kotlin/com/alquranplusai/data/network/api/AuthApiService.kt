package com.alquranplusai.data.network.api

/**
 * API service for authentication
 */
interface AuthApiService {
    
    suspend fun login(email: String, password: String): Map<String, String>
    
    suspend fun register(email: String, username: String, password: String): Map<String, String>
    
    suspend fun logout(userId: Int)
    
    suspend fun refreshToken(refreshToken: String): Map<String, String>
    
    suspend fun resetPassword(email: String)
    
    suspend fun verifyEmail(token: String): Boolean
}
