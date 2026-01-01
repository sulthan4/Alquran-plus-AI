package com.alquranplusai.data.network.api

import com.alquranplusai.data.network.dto.UserDto
import com.alquranplusai.data.network.dto.UserProfileDto
import com.alquranplusai.data.network.dto.SubscriptionDto

/**
 * API service for user management
 */
interface UserApiService {
    
    suspend fun getUser(userId: Int): UserDto
    
    suspend fun updateUser(userId: Int, user: UserDto): UserDto
    
    suspend fun getUserProfile(userId: Int): UserProfileDto
    
    suspend fun updateUserProfile(userId: Int, profile: UserProfileDto): UserProfileDto
    
    suspend fun getSubscription(userId: Int): SubscriptionDto?
    
    suspend fun createSubscription(userId: Int, subscription: SubscriptionDto): SubscriptionDto
}
